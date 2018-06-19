import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.awt.*;
import java.awt.geom.Point2D;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import static java.lang.Math.*;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * class Display
 * <p>
 * Used to create a window and display stuff to the screen.
 */
public class Display
{
  /**
   * The width of the window to create.
   */
  public static final int WINDOWWIDTH = 800;
  /**
   * The height of the window to create.
   */
  public static final int WINDOWHEIGHT = 600;
  /**
   * If the window is resizeable.
   */
  public static final boolean ALLOWRESIZE = false;

  /**
   * The FPS this window will try to run at.
   */
  public static final int FPS = 60;
  /**
   * The window handle.
   * <p>
   * The pointer to the Window object that OpenGL/GLFW uses.
   */
  private static long window;
  /**
   * The size of the window, contained within a {@link WindowSize}, an inner-class of {@code Display}. If {@link Display#ALLOWRESIZE} is {@code true}, then the window's size can change, and this will update and reflect that in real time. (Just before every frame is rendered)
   */
  public static WindowSize w;
  /**
   * The {@link DisplayableWindow} that is currently being displayed in the window.
   */
  private static DisplayableWindow currentlyDisplayed;
  /**
   *
   */
  public final String windowName;

  /**
   * Keeps track of whether the mouse is currently held down. Used to call {@link DisplayableWindow#doDrag(Point)}.
   */
  private boolean mouseIsDown = false;

  /**
   * Constructor for Display
   * <p>
   * Creates a new Display Object with the given {@link DisplayableWindow} object, and will call it to render stuff.
   *
   * @param toDisplayFirst The {@link DisplayableWindow} that will be displayed first.
   */
  public Display(String windowName, DisplayableWindow toDisplayFirst)
  {
    this.windowName = windowName;
    currentlyDisplayed = toDisplayFirst;
  }

  /**
   * Sets the currently displayed {@link DisplayableWindow} to the newly specified one.
   *
   * @param display The new {@link DisplayableWindow} to use and display.
   */
  public static void setDisplay(DisplayableWindow display)
  {
    currentlyDisplayed = display;
  }

  /**
   * Opens the window, and starts it's render loop.
   */
  public void run()
  {
    init();
    loop();
    terminate();
  }

  /**
   * Sets up OpenGL and GLFW, then opens the window. Window will hang and not work, unless a display loop is running. See: {@link Display#loop()}.
   */
  private void init()
  {
    // Prevent java.awt from interfering with this thread. If any awt stuff is called prior to this, the program will crash (Ex: {@code Color c = new Color(255,0,255);}).
    System.setProperty("java.awt.headless", "true");

    // Setup an error callback. The default implementation
    // will print the error message in System.err.
    GLFWErrorCallback.createPrint(System.err).set();

    // Initialize GLFW. Most GLFW functions will not work before doing this.
    if (!glfwInit())
      throw new IllegalStateException("Unable to initialize GLFW");

    // Configure GLFW
    glfwDefaultWindowHints(); // optional, the current window hints are already the default
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
    glfwWindowHint(GLFW_RESIZABLE, ALLOWRESIZE ? GLFW_TRUE : GLFW_FALSE); // whether the window can be resized.

    // Create the window
    window = glfwCreateWindow(WINDOWWIDTH, WINDOWHEIGHT, windowName, NULL, NULL);
    if (window == NULL)
      throw new RuntimeException("Failed to create the GLFW window");

    registerCallbacks();

    // Get the thread stack and push a new frame
    try (MemoryStack stack = MemoryStack.stackPush())
    {
      IntBuffer pWidth = stack.mallocInt(1); // int*
      IntBuffer pHeight = stack.mallocInt(1); // int*

      // Get the window size passed to glfwCreateWindow
      glfwGetWindowSize(window, pWidth, pHeight);

      // Get the resolution of the primary monitor
      GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

      // Center the window
      glfwSetWindowPos(
          window,
          (vidmode.width() - pWidth.get(0)) / 2,
          (vidmode.height() - pHeight.get(0)) / 2
      );
    } // the stack frame is popped automatically

    // Make the OpenGL context current
    glfwMakeContextCurrent(window);
    // Enable v-sync
    glfwSwapInterval(1);

    // Make the window visible
    glfwShowWindow(window);
  }

  /**
   * Register the call backs from GLFW for the keyboard, mouse clicks, and scrolling.
   */
  private void registerCallbacks()
  {
    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    glfwSetKeyCallback(window, (window, key, scancode, action, mods) ->
    {
      if (currentlyDisplayed != null)
        currentlyDisplayed.keyPress(key, action);

      //if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
      //  glfwSetWindowShouldClose(window, true);
    });

    glfwSetMouseButtonCallback(window, (window, button, action, mods) ->
    {
      if (currentlyDisplayed != null)
        if (action == GLFW_PRESS)
        {
          mouseIsDown = true;
          currentlyDisplayed.doClick(button, getCursorLocationOrigin(w));
        }
        else
          if (action == GLFW_RELEASE)
          {
            mouseIsDown = false;
            currentlyDisplayed.doRelease(button, getCursorLocationOrigin(w));
          }
    });

    glfwSetScrollCallback(window, (window, xoffset, yoffset) ->
    {
      currentlyDisplayed.doScroll(yoffset > 0, getCursorLocationOrigin(w));
            /*
            scrollAmount += yoffset;
            while (scrollAmount <= -1)
            {
                scrollAmount += 1;
                currentlyDisplayed.doScroll(false, getCursorLocationOrigin(w));
            }
            while (scrollAmount >= 1)
            {
                scrollAmount -= 1;
                currentlyDisplayed.doScroll(true, getCursorLocationOrigin(w));
            }*/
    });
  }

  /**
   * The main display loop.
   * <p>
   * Loops and calls {@link DisplayableWindow#display(long)} {@link Display#FPS} times per second, until the window gets closed.
   */
  private void loop()
  {
    // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities();

    // Set the clear/background color
    glClearColor(0.0f, 0.0f, 0.25f, 1.0f); // Was that greenish: .3 .7 .6 .0


    // Run the rendering loop until the user has attempted to close
    // the window or has pressed the ESCAPE key.

    long time = System.nanoTime();
    while (!glfwWindowShouldClose(window))
    {
      time = sleep(FPS, time);

      // Poll for window events. The key callback above will only be
      // invoked during this call.
      glfwPollEvents();

      if (mouseIsDown)
        currentlyDisplayed.doDrag(getCursorLocationOrigin(w));

      update();
      render();
      finishRender();
    }
  }

  /**
   * Sleeps this thread until the next frame should be drawn.
   *
   * @param updates_per_second the FPS this is being used to calculate. Should be {@link Display#FPS}, but can be different.
   * @param last_execution     The {@link System#nanoTime()} of the last frame drawn. Used to calculate sleep time.
   * @return the current {@link System#nanoTime()}: The time this frame was drawn. Should be used as the {@code last_execution}, next time this is called.
   */
  private long sleep(int updates_per_second, long last_execution)
  {
    double maxSleepMS = 1000D / updates_per_second;
    double msSinceLastSleep = (System.nanoTime() - last_execution) / 1000000D;
    if (maxSleepMS > msSinceLastSleep)
    {
      //System.out.printf("Sleeping for \t"+(long)(maxSleepMS-msSinceLastSleep)+" of \t"+(long)maxSleepMS+"ms.\n");
      try
      {
        Thread.sleep((long) (maxSleepMS - msSinceLastSleep));
      } catch (InterruptedException e)
      {
        System.err.println("Sleep Interrupted!");
        e.printStackTrace();
      }
    }
    //else
    //  System.err.println("This thread is running behind! This is no time to rest! ("+(long)(msSinceLastSleep-maxSleepMS)+"ms behind next scheduled update time.)");
    return System.nanoTime();//last_execution+(long)(maxSleepMS*1000000);
  }

  /**
   * Updates a few things, like {@link Display#w} to the new current values. Also sets up OpenGL and GLFW for the next frame to be drawn.
   */
  private void update()
  {
    w = getWindowSize();
    // clear the framebuffer
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    //Enables the depth buffer. IF DEPTH BUFFER IS ON, THERE IS NO TRANSPARENCY!
    /*glEnable(GL_DEPTH_TEST);*/

    //glEnable(GL_BLEND); //I don't really know what this does.
    glDepthFunc(GL_LESS);
    glShadeModel(GL_SMOOTH);
  }

  /**
   * Renders the next frame.
   */
  private void render()
  {
    glPointSize(10);
    glLineWidth(5F);
    Display.setColor3(Color.BLUE);

    // Draw the stuff for this {@link DisplayableWindow}.
    currentlyDisplayed.display(window);
  }

  /**
   * Displays the rendered frame, and takes care of double buffering stuff.
   */
  private void finishRender()
  {
    glfwSwapBuffers(window); // swap the color buffers
  }

  /**
   * Closes the window and does other cleanup stuff.
   */
  private void terminate()
  {
    // Free the window callbacks and destroy the window
    glfwFreeCallbacks(window);
    glfwDestroyWindow(window);

    // Terminate GLFW and free the error callback
    glfwTerminate();
    glfwSetErrorCallback(null).free();
  }


  // **************** METHODS TO DRAW STUFF **************** //TODO


  /**
   * After running enableTransparency, transparent objects can be drawn.
   * <p>
   * Please use {@link #disableTransparency()} afterwards to disable it.
   */
  protected static void enableTransparency()
  {
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
  }

  /**
   * After running disableTransparency, transparent objects can no longer be drawn.
   * <p>
   * Use {@link #enableTransparency()} again to reenable it.
   */
  protected static void disableTransparency()
  {
    glDisable(GL_BLEND);
  }

  /**
   * Adds a point to the screen, using cartesian coordinates.
   * <p>
   * Coordinates: (0,0) maps to the center of the screen.
   * Positive x is to the right from the center.
   * Positive y is upward from the center.
   */
  public static void doPointCart(double x, double y)
  {
    glVertex2d(x / (w.getWidth() / 2D), y / (w.getHeight() / 2D));
  }

  /**
   * Adds a point to the screen, using coordinates from the origin.
   * <p>
   * Coordinates: (0,0) (origin) maps to the top left corner of the screen.
   * Positive x is to the right from the origin.
   * Positive y is downward from the origin.
   */
  public static void doPointOr(double x, double y)
  {
    doPointCart(x - w.getWidth() / 2D, w.getHeight() / 2D - y);
  }

  /**
   * Sets the color to draw without transparency.
   *
   * @param c The {@link java.awt.Color} object to set the color to.
   */
  public static void setColor3(Color c)
  {
    glColor3f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f);
  }

  /**
   * Sets the color to draw without transparency.
   *
   * @param r A value between 0 and 255 for the red component of the color.
   * @param g A value between 0 and 255 for the green component of the color.
   * @param b A value between 0 and 255 for the blue component of the color.
   */
  public static void setColor3(int r, int g, int b)
  {
    glColor3f(r / 255f, g / 255f, b / 255f);
  }

  /**
   * Sets the color to draw with transparency.
   *
   * @param c The {@link java.awt.Color} object to set the color to, including the transparency component.
   */
  public static void setColor4(Color c)
  {
    glColor4f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
  }

  /**
   * Sets the color to draw with transparency.
   *
   * @param c The {@link java.awt.Color} object to set the color to.
   * @param a A value between 0 and 255 for the alpha component of the color. Transparency, with 0 being fully transparent and 255 being opaque.
   */
  public static void setColor4(Color c, int a)
  {
    glColor4f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, a / 255f);
  }

  /**
   * Sets the color to draw without transparency.
   *
   * @param r A value between 0 and 255 for the red component of the color.
   * @param g A value between 0 and 255 for the green component of the color.
   * @param b A value between 0 and 255 for the blue component of the color.
   * @param a A value between 0 and 255 for the alpha component of the color. Transparency, with 0 being fully transparent and 255 being opaque.
   */
  public static void setColor4(int r, int g, int b, int a)
  {
    glColor4f(r / 255f, g / 255f, b / 255f, a / 255f);
  }

  /**
   * Sets the color to draw without transparency.
   *
   * @param hue        A value between 0 and 359 for the hue component of the color.
   * @param saturation A value between 0 and 100 for the saturation component of the color.
   * @param brightness A value between 0 and 100 for the brightness component of the color.
   */
  public static void setColorHSB3(int hue, int saturation, int brightness)
  {
    setColorHSB4(hue, saturation, brightness, 255);
  }

  /**
   * Sets the color to draw without transparency.
   *
   * @param hue        A value between 0 and 359 for the hue component of the color.
   * @param saturation A value between 0 and 100 for the saturation component of the color.
   * @param brightness A value between 0 and 100 for the brightness component of the color.
   * @param alpha      A value between 0 and 255 for the alpha component of the color. Transparency, with 0 being fully transparent and 255 being opaque.
   */
  public static void setColorHSB4(int hue, int saturation, int brightness, int alpha)
  {
    setColor4(hsb4ToColor(hue, saturation, brightness, alpha));
  }

  /**
   * Converts the HSBA color to a Color object.
   *
   * @param hue        A value between 0 and 359 for the hue component of the color.
   * @param saturation A value between 0 and 100 for the saturation component of the color.
   * @param brightness A value between 0 and 100 for the brightness component of the color.
   * @param alpha      A value between 0 and 255 for the alpha component of the color. Transparency, with 0 being fully transparent and 255 being opaque.
   */
  public static Color hsb4ToColor(int hue, int saturation, int brightness, int alpha)
  {
    if (hue < 0)
      hue = hue % 360 + 360;
    if (hue >= 360)
      hue %= 360;
    saturation = normalizeInt(saturation, 0, 100);
    brightness = normalizeInt(brightness, 0, 100);

    saturation = saturation * 255 / 100;
    brightness = brightness * 255 / 100;

    int red = 0, green = 0, blue = 0;

    if (saturation == 0) { red = green = blue = brightness; }
    else
    {
      int t1 = brightness;
      int t2 = (255 - saturation) * brightness / 255;
      int t3 = (t1 - t2) * (hue % 60) / 60;

      if (hue < 60) { red = t1; blue = t2; green = t2 + t3; }
      else if (hue < 120) { green = t1; blue = t2; red = t1 - t3; }
      else if (hue < 180) { green = t1; red = t2; blue = t2 + t3; }
      else if (hue < 240) { blue = t1; red = t2; green = t1 - t3; }
      else if (hue < 300) { blue = t1; green = t2; red = t2 + t3; }
      else if (hue < 360) { red = t1; green = t2; blue = t1 - t3; }
      else { red = 0; green = 0; blue = 0; }
    }
    return new Color(red, green, blue, alpha);
  }

  /**
   * Draws an optionally filled rectangle, using coordinates from the origin.
   *
   * @param x      Center of the rectangle's X coordinate.
   * @param y      Center of the rectangle's Y coordinate.
   * @param width  The width of the rectangle.
   * @param height The height of the rectangle.
   * @param fill   If the rectangle should be filled.
   */
  public static void drawRectangleOr(int x, int y, int width, int height, boolean fill)
  {
    glBegin(fill ? GL_POLYGON : GL_LINE_LOOP);
    doPointOr(x - width / 2, y - height / 2);
    doPointOr(x + width / 2, y - height / 2);
    doPointOr(x + width / 2, y + height / 2);
    doPointOr(x - width / 2, y + height / 2);
    glEnd();
  }

  /**
   * Draws a line, using coordinates from the origin.
   *
   * @param start The start location of the line, using coordinates from the origin.
   * @param end   The end location of the line, using coordinates from the origin.
   */
  public static void drawLineOr(Point start, Point end)
  {
    glBegin(GL_LINES);
    doPointOr(start.getX(), start.getY());
    doPointOr(end.getX(), end.getY());
    glEnd();
  }

  /**
   * Draws a circle, using coordinates from the origin.
   *
   * @param xPos   Center of the circle's X coordinate.
   * @param yPos   Center of the circle's Y coordinate.
   * @param radius Radius of the Circle
   * @param fill   If the circle should be filled.
   */
  public static void doCircle(double xPos, double yPos, double radius, boolean fill)
  {
    int num_segments = (int) (7 * radius);
    double theta = 2 * PI / num_segments;

    double tangetial_factor = tan(theta); //calculate the tangential factor
    double radial_factor = cos(theta); //calculate the radial factor

    double x = radius; //we start at angle = 0
    double y = 0;

    glBegin(fill ? GL_POLYGON : GL_LINE_LOOP);
    for (int i = 0; i < num_segments; i++)
    {
      doPointOr(xPos + x, yPos + y);//output vertex

      //calculate the tangential vector
      //remember, the radial vector is (x, y)
      //to getPath the tangential vector we flip those coordinates and negate one of them
      double tx = -y;
      double ty = x;

      //add the tangential vector
      x += tx * tangetial_factor;
      y += ty * tangetial_factor;

      //correct using the radial factor
      x *= radial_factor;
      y *= radial_factor;
    }
    glEnd();
  }


  // **************** MY METHODS FOR HELPING ME **************** //TODO

  /**
   * Changes the currently displayed window to the specified {@link DisplayableWindow}.
   *
   * @param newWindow The new {@link DisplayableWindow} to be displayed.
   */
  public void setWindow(DisplayableWindow newWindow)
  {
    currentlyDisplayed = newWindow;
  }

  /**
   * Gets the size of the window, and returns it as a {@link WindowSize}.
   *
   * @return the size of the window as a {@link WindowSize}.
   */
  public static WindowSize getWindowSize()
  {
    try (MemoryStack stack = MemoryStack.stackPush())
    {
      //int* width = malloc(1);
      IntBuffer pWidth = stack.mallocInt(1); // int*
      IntBuffer pHeight = stack.mallocInt(1); // int*

      glfwGetWindowSize(window, pWidth, pHeight);
      return new WindowSize(pWidth.get(), pHeight.get());
    } catch (IllegalArgumentException e)
    {
      return new WindowSize(0, 0);
    }
  }

  /**
   * Gets the cursor's location, using coordinates from the origin.
   *
   * @param w the size of the window, to adjust the cursor's location from ratios to the exact values.
   * @return The cursor's location in a {@link Point2D.Double}.
   */
  public Point2D getCursorLocationCartesian(WindowSize w)
  {
    try (MemoryStack stack = MemoryStack.stackPush())
    {
      //double* x = malloc(1);
      DoubleBuffer x = stack.mallocDouble(1); // int*
      DoubleBuffer y = stack.mallocDouble(1); // int*

      glfwGetCursorPos(window, x, y);
      return new Point2D.Double(x.get() - w.getWidth() / 2D, w.getHeight() / 2D - y.get());
    } catch (IllegalArgumentException e)
    {
      return null;
    }
  }

  /**
   * Gets the cursor's location, using coordinates from the origin.
   *
   * @param w the size of the window, to adjust the cursor's location from ratios to the exact values.
   * @return The cursor's location in a {@link java.awt.Point}.
   */
  public static Point getCursorLocationOrigin(WindowSize w)
  {
    try (MemoryStack stack = MemoryStack.stackPush())
    {
      //double* x = malloc(1);
      DoubleBuffer x = stack.mallocDouble(1); // int*
      DoubleBuffer y = stack.mallocDouble(1); // int*

      glfwGetCursorPos(window, x, y);

      return new Point((int) Math.round(x.get()), (int) Math.round(y.get()));
    } catch (IllegalArgumentException e)
    {
      return null;
    }
  }

  /**
   * Restricts an integer value into a given range.
   *
   * @param value The given value to normalize.
   * @param min   The lower bound of the range.
   * @param max   The upper bound of the range.
   * @return the value normalized into the given range.
   */
  public static int normalizeInt(int value, int min, int max)
  {
    if (value <= min)
      return min;
    if (value >= max)
      return max;
    return value;
  }

  /**
   * Restricts an double value into a given range.
   *
   * @param value The given value to normalize.
   * @param min   The lower bound of the range.
   * @param max   The upper bound of the range.
   * @return the value normalized into the given range.
   */
  public static double normalizeDouble(double value, double min, double max)
  {
    if (value < min)
      return min;
    if (value > max)
      return max;
    return value;
  }

  /**
   * class WindowSize
   * <p>
   * Represents the size of a window.
   */
  public static class WindowSize
  {
    /**
     * The width of the window.
     */
    public final int w;
    /**
     * The height of the window.
     */
    public final int h;

    /**
     * Constructor for WindowSize.
     *
     * @param width  The width of the window.
     * @param height The height of the height.
     */
    public WindowSize(int width, int height)
    {
      w = width;
      h = height;
    }

    /**
     * @return the width of the window
     */
    public int getWidth()
    {
      return w;
    }

    /**
     * @return the height of the window
     */
    public int getHeight()
    {
      return h;
    }

    /**
     * Converts this {@code WindowSize} to a {@link String}.
     *
     * @return This object as a {@link String}.
     */
    public String toString()
    {
      return "WindowSize[width=" + w + ",height=" + h + "]";
    }
  }
}