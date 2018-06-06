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

public class Display
{

    public static final int FPS = 60;
    // The window handle
    private static long window;
    static WindowSize w;
    static DisplayableWindow currentlyDisplayed;
    boolean mouseIsDown = false;
    double scrollAmount = 0;

    public Display(DisplayableWindow toDisplayFirst)
    {
        currentlyDisplayed = toDisplayFirst;
    }

    public static void setDisplay(DisplayableWindow display)
    {
        currentlyDisplayed = display;
    }

    public void run()
    {
        //System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();
        terminate();
    }

    private void init()
    {
        // Prevent awt from interfering with this thread.
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
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will not be resizable

        // Create the window
        window = glfwCreateWindow(800, 600, "Flow: 3D", NULL, NULL);
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

    private void registerCallbacks()
    {
        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) ->
        {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        glfwSetMouseButtonCallback(window, (window, button, action, mods) ->
        {
            if (currentlyDisplayed != null)
                if (action == GLFW_PRESS)
                {
                    mouseIsDown = true;
                    currentlyDisplayed.doClick(button, getCursorLocationOrigin(w));
                }
                else if (action == GLFW_RELEASE)
                {
                    mouseIsDown = false;
                    currentlyDisplayed.doRelease(button, getCursorLocationOrigin(w));
                }
        });

        glfwSetScrollCallback(window, (window, xoffset, yoffset) ->
        {
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
            }

        });
    }

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
            if (mouseIsDown)
                currentlyDisplayed.doDrag(getCursorLocationOrigin(w));
            //input();
            update();
            render();
            finishRender();
        }
    }

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

    private void update()
    {
        w = getWindowSize();
        // clear the framebuffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        //Enables the depth buffer. Can do some weird stuff, like disable transparency.
        /*glEnable(GL_DEPTH_TEST);*/

        //glEnable(GL_BLEND);
        glDepthFunc(GL_LESS);
        glShadeModel(GL_SMOOTH);
    }

    private void render()
    {
        glPointSize(10);
        glLineWidth(5F);
        glColor3f(Color.BLUE.getRed() / 255f, Color.BLUE.getGreen() / 255f, Color.BLUE.getBlue() / 255f);
    /*
    drawLine(w, 5f, Color.ORANGE, new Point(100, 200), new Point(200, 100));

    glBegin(GL_LINE_LOOP);
    glVertex2d(0, 0);
    glVertex2d(100d / w.getWidth(), 100d / w.getHeight());
    glVertex2d(100d / w.getWidth(), 0);
    glVertex2d(-100d / w.getWidth(), 100d / w.getHeight());
    glEnd();
    */

        currentlyDisplayed.display(window);

    /*
    glBegin(GL_POINTS);
    for (int i = 0; i < clicks.size(); i++)
    {
      Point2D p = clicks.getPath(i);
      double v = o.getPath(i).getX();
      double y = o.getPath(i).getY();
      y += v;
      Color c = randomColor(p.hashCode());
      glColor3f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f);
      glVertex2d(p.getX() / w.getWidth(), (p.getY() - y) / w.getHeight());
      //p.setLocation(p.getX(), p.getY() - y);
      o.set(i, new Point2D.Double(v + .1, y));
      if (y / 2D > w.getHeight())
      {
        clicks.remove(i);
        o.remove(i--);
      }
    }
    glEnd();
    */

        //System.out.println(getWindowSize());
    }

    private void finishRender()
    {
        glfwSwapBuffers(window); // swap the color buffers

        // Poll for window events. The key callback above will only be
        // invoked during this call.
        glfwPollEvents();
    }

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



  /*public static void doPointCart(double x, double y)
  {
    doPointCart(x, y, 0);
  }*/

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

    public static void doPointCart(double x, double y)
    {
        glVertex3d(x / (w.getWidth() / 2D), y / (w.getHeight() / 2D), 0);
    }

  /*public static void doPointOr(double x, double y)
  {
    doPointCart(x - w.getWidth() / 2D, w.getHeight() / 2D - y, 0);
  }*/

    public static void doPointOr(double x, double y)
    {
        doPointCart(x - w.getWidth() / 2D, w.getHeight() / 2D - y);
    }

    public static void setColor3(Color c)
    {
        glColor3f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f);
    }

    public static void setColor3(int r, int g, int b)
    {
        glColor3f(r / 255f, g / 255f, b / 255f);
    }

    public static void setColor4(Color c)
    {
        glColor4f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
    }

    public static void setColor4(int r, int g, int b, int a)
    {
        glColor4f(r / 255f, g / 255f, b / 255f, a / 255f);
    }

    public static void drawRectangleOr(int x, int y, int width, int height, boolean fill)
    {
        glBegin(fill ? GL_POLYGON : GL_LINE_LOOP);
        doPointOr(x - width / 2, y - height / 2);
        doPointOr(x + width / 2, y - height / 2);
        doPointOr(x + width / 2, y + height / 2);
        doPointOr(x - width / 2, y + height / 2);
        glEnd();
    }

    public static void drawLineOr(Point start, Point end)
    {
        glBegin(GL_LINES);
        doPointOr(start.x, start.y);
        doPointOr(end.x, end.y);
        glEnd();
    }

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


    public void setWindow(DisplayableWindow newWindow)
    {
        currentlyDisplayed = newWindow;
    }

    public WindowSize getWindowSize()
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

    public class WindowSize
    {
        public final int w, h;

        public WindowSize(int width, int height)
        {
            w = width;
            h = height;
        }

        public int getWidth()
        {
            return w;
        }

        public int getHeight()
        {
            return h;
        }

        public String toString()
        {
            return "WindowSize[width=" + w + ",height=" + h + "]";
        }
    }
}