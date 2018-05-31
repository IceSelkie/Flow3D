import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.nio.*;
import java.util.Random;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Display {

  public static final int FPS = 60;
  // The window handle
  private long window;
  WindowSize w;
  DisplayableWindow currentlyDisplayed;

  public static void main(String[] args) {
    new Display().run();
  }

  public void run() {
    //System.out.println("Hello LWJGL " + Version.getVersion() + "!");

    init();
    loop();
    terminate();
  }

  private void init() {
    // Prevent awt from interfering with this thread.
    System.setProperty("java.awt.headless", "true");

    // Setup an error callback. The default implementation
    // will print the error message in System.err.
    GLFWErrorCallback.createPrint(System.err).set();

    // Initialize GLFW. Most GLFW functions will not work before doing this.
    if ( !glfwInit() )
      throw new IllegalStateException("Unable to initialize GLFW");

    // Configure GLFW
    glfwDefaultWindowHints(); // optional, the current window hints are already the default
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
    glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable

    // Create the window
    window = glfwCreateWindow(800, 600, "Flow: 3D", NULL, NULL);
    if ( window == NULL )
      throw new RuntimeException("Failed to create the GLFW window");

    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
      if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
        glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
    });

    glfwSetMouseButtonCallback(window,(window,button,action,mods)->{
      if (currentlyDisplayed!=null)
        currentlyDisplayed.doClick(button,getCursorLocation(w));
    });

    // Get the thread stack and push a new frame
    try ( MemoryStack stack = stackPush() ) {
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

  private void loop()
  {
    // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities();

    // Set the clear/background color
    glClearColor(.3f, 0.7f, 0.6f, 0.0f);


    // Run the rendering loop until the user has attempted to close
    // the window or has pressed the ESCAPE key.

    long time = System.nanoTime();
    while (!glfwWindowShouldClose(window))
    {
      time = sleep(FPS,time);
      //input();
      update();
      render();
      finishRender();
    }
  }

  private long sleep(int updates_per_second, long last_execution)
  {
    double maxSleepMS = 1000D/updates_per_second;
    double msSinceLastSleep = (System.nanoTime()-last_execution)/1000000D;
    if (maxSleepMS>msSinceLastSleep)
    {
      //System.out.printf("Sleeping for \t"+(long)(maxSleepMS-msSinceLastSleep)+" of \t"+(long)maxSleepMS+"ms.\n");
      try {Thread.sleep((long) (maxSleepMS - msSinceLastSleep));} catch (InterruptedException e)
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
    glEnable(GL_DEPTH_TEST);
    glEnable(GL_BLEND);
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

    for (Element element : currentlyDisplayed.getElements())
      displayElement(element);
    /*
    glBegin(GL_POINTS);
    for (int i = 0; i < clicks.size(); i++)
    {
      Point2D p = clicks.get(i);
      double v = o.get(i).getX();
      double y = o.get(i).getY();
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



  // **************** MY METHODS FOR HELPING ME **************** //TODO



  public void setWindow(DisplayableWindow newWindow)
  {
    currentlyDisplayed = newWindow;
  }

  public void displayElement(Element element)
  {
    //TODO
  }

  public WindowSize getWindowSize()
  {
    try ( MemoryStack stack = stackPush() )
    {
      //int* width = malloc(1);
      IntBuffer pWidth = stack.mallocInt(1); // int*
      IntBuffer pHeight = stack.mallocInt(1); // int*

      glfwGetWindowSize(window, pWidth, pHeight);
      return new WindowSize(pWidth.get(),pHeight.get());
    } catch (IllegalArgumentException e)
    {
      return new WindowSize(0,0);
    }
  }

  public Point2D getCursorLocation(WindowSize w)
  {
    try (MemoryStack stack = stackPush())
    {
      //int* width = malloc(1);
      DoubleBuffer x = stack.mallocDouble(1); // int*
      DoubleBuffer y = stack.mallocDouble(1); // int*

      glfwGetCursorPos(window, x, y);
      return new Point2D.Double(2 * x.get() - w.getWidth(), w.getHeight() - 2 * y.get());
    } catch (IllegalArgumentException e)
    {
      return null;
    }
  }

  public void drawLine(WindowSize w, float width, Color c, Point start, Point end)
  {
    glLineWidth(width);
    glColor3f(c.getRed()/255f,c.getGreen()/255f,c.getBlue()/255f);
    glBegin(GL_LINES);
    glVertex2d(start.getX()/w.getWidth(),start.getY()/w.getHeight());
    glVertex2d(end.getX()/w.getWidth(),end.getY()/w.getHeight());
    glEnd();
  }

  public Point pt(int x, int y)
  {
    return new Point(x,y);
  }

  public Color randomColor(int seed)
  {
    Random r = new Random(seed);
    return new Color(Math.abs((int)(r.nextLong()%256)),Math.abs((int)(r.nextLong()%256)),Math.abs((int)(r.nextLong()%256)));
  }

  public class WindowSize
  {
    public final int w, h;
    public WindowSize(int width, int height)
    {
      w = width;
      h = height;
    }
    public int getWidth() {return w;}
    public int getHeight() {return h;}
    public String toString() { return "WindowSize[width="+w+",height="+h+"]"; }
  }
}