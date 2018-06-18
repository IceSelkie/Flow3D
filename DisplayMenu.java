import org.lwjgl.glfw.GLFW;

import java.awt.*;

import static org.lwjgl.opengl.GL11.glLineWidth;

/**
 * class DisplayMenu
 * <p>
 * The window data and layout for the window of the start menu.
 *
 * @author Stanley S. & Kaushik A.
 * @version 0.0 (Not Started)
 */
public class DisplayMenu extends DisplayableWindow
{
  protected static final int displayLocations_WindowWidth = 800;
  protected static final int displayLocations_WindowHeight = 600;
  protected static final int displayLocations_WindowCenterX = displayLocations_WindowWidth / 2;
  protected static final int displayLocations_WindowCenterY = displayLocations_WindowHeight / 2;
  protected static final int displayLocations_ButtonWidth = 200;
  protected static final int displayLocations_ButtonHeight = 50;
  protected static final int displayLocations_ButtonSideLeft = displayLocations_WindowCenterX - (displayLocations_ButtonWidth / 2);
  protected static final int displayLocations_ButtonSideRight = displayLocations_WindowCenterX + (displayLocations_ButtonWidth / 2);
  protected static final int displayLocations_ButtonSideBottom = displayLocations_WindowCenterY - (displayLocations_ButtonHeight / 2);
  protected static final int displayLocations_ButtonSideTop = displayLocations_WindowCenterX + (displayLocations_ButtonHeight / 2);

  private boolean clicked = false; // Clicked window
  private DisplayTransitionHelper fade;
  private DisplayTransitionHelper textAnimation;
  private double hoverPhase = 0;

  /**
   * Constructor for Displaymenu
   */
  public DisplayMenu()
  {
    fade = new DisplayTransitionHelper(0, 60, 60, -59, InterpolationType.SINUSOID);
    textAnimation = new DisplayTransitionHelper(0, 600, 6 * Display.FPS, 0, InterpolationType.LINEAR);
  }

  /**
   * Display the menu screen
   *
   * @param window The GLFWwindow pointer that holds the window's data. Needed to do any displaying.
   */
  public void display(long window)
  {
    fade.tick();
    textAnimation.tick();
    if (fade.get() == 60)
      Display.setDisplay(new DisplaySelect(Level.easy(), Level.medium(), Level.hard()));

    if (overButton(Display.getCursorLocationOrigin(Display.w)))
      hoverPhase = Display.normalizeDouble(hoverPhase + 1 / 30, 0, 1);
    if (displayText()) ;
    displayButton();

    // Fade in
    Display.enableTransparency();
    Display.setColor4(0, 0, 31, (int) (255 * (fade.get() / 60D)));
    Display.drawRectangleOr(400, 300, 800, 600, true);
    Display.disableTransparency();
  }

  /**
   * When the user clicks a location on the window, this
   * will be called. Warning: This is asynchronous, and
   * can happen at any time, EVEN WHEN OTHER METHODS ARE
   * INPROGRESS.
   *
   * @param clickType Enumeration of which mouse button was used to do the click. See {@link GLFW#GLFW_MOUSE_BUTTON_1} through {@link GLFW#GLFW_MOUSE_BUTTON_8}
   * @param location  The point on the screen that was clicked.
   */
  public void doClick(int clickType, Point location)
  {
    if (overButton(location))
    {
      clicked = true;
      fade.setIncreasing();
    }
  }

  /**
   * Checks to see if cursor is within button limits
   *
   * @param location location of cursor
   * @return -1 false
   */
  private boolean overButton(Point location)
  {
    int x = (int) location.getX();
    int y = (int) location.getY();
    if (x >= displayLocations_ButtonSideLeft && x <= displayLocations_ButtonSideRight)
    {
      if (y >= displayLocations_ButtonSideBottom && y <= displayLocations_ButtonSideTop)
        return true;
    }
    return false;
  }

  /**
   * Action once certain point is released from contact of map
   *
   * @param clickType
   * @param location
   */
  public void doRelease(int clickType, Point location)
  {
    //not used
  }


  // **************** OTHER METHODS ************ // TODO


  /**
   * Do the introductory animation.
   * WARNING: DOES NOT WORK!
   *
   * @return whether the animation has finished.
   */
  @Deprecated
  public boolean displayText()
  {
    double time = textAnimation.get();
    System.out.println(time);
    if (time >= 600)
      return true;


    if (time < 500)
    {
      Display.setColor3(PathColor.RED.toColor());
      // F top
      if (time > 50)
        Display.doCircle(displayLocations_WindowCenterX - 150, displayLocations_WindowCenterY - 25, 10, true);
      else
        Display.doCircle(displayLocations_WindowCenterX - 150, displayLocations_WindowCenterY - 25, 10 * time / 50, true);

      // F bottom
      if (time > 75)
        Display.doCircle(displayLocations_WindowCenterX - 200, displayLocations_WindowCenterY - -50, 10, true);
      else
        if (time > 25)
          Display.doCircle(displayLocations_WindowCenterX - 200, displayLocations_WindowCenterY - -50, 10 * (time - 25) / 50, true);

      // F bar left
      if (time > 100)
        Display.doCircle(displayLocations_WindowCenterX - 250, displayLocations_WindowCenterY - 0, 10, true);
      else
        if (time > 50)
          Display.doCircle(displayLocations_WindowCenterX - 250, displayLocations_WindowCenterY - 0, 10 * (time - 50) / 50, true);

      // F bar right
      if (time > 125)
        Display.doCircle(displayLocations_WindowCenterX - 150, displayLocations_WindowCenterY - 0, 10, true);
      else
        if (time > 75)
          Display.doCircle(displayLocations_WindowCenterX - 150, displayLocations_WindowCenterY - 0, 10 * (time - 75) / 50, true);

      glLineWidth(50F);
      Display.drawArc(displayLocations_WindowCenterX - 175, displayLocations_WindowCenterY - 25, 25, -Display.normalizeDouble(time - 200, 0, 25) / 25 * Math.PI, Display.normalizeDouble(time - 200, 0, 25) / 25 * Math.PI);
      Display.drawLineOr(new Point(displayLocations_WindowCenterX - 200, displayLocations_WindowCenterY - 25), new Point(displayLocations_WindowCenterX - 200, (int) (displayLocations_WindowCenterY - (25 - (3) * Display.normalizeDouble(time - 225, 0, 25)))));

      Display.drawLineOr(new Point(displayLocations_WindowCenterX - 250, displayLocations_WindowCenterY), new Point(displayLocations_WindowCenterX - (250 - ((150 - (int) (2 * Display.normalizeDouble(time - 250, 0, 50))))), displayLocations_WindowCenterY));

      Display.setColor3(PathColor.BLUE.toColor());
      // L top
      if (time > 150)
        Display.doCircle(displayLocations_WindowCenterX - 100, displayLocations_WindowCenterY - 50, 10, true);
      else
        if (time > 100)
          Display.doCircle(displayLocations_WindowCenterX - 100, displayLocations_WindowCenterY - 50, 10 * (time - 100) / 50, true);

      // L bottom
      if (time > 175)
        Display.doCircle(displayLocations_WindowCenterX - 100, displayLocations_WindowCenterY - -50, 10, true);
      else
        if (time > 125)
          Display.doCircle(displayLocations_WindowCenterX - 100, displayLocations_WindowCenterY - -50, 10 * (time - 125) / 50, true);
    }

    return false;
  }

  /**
   * The button to move to the level select.
   */
  public void displayButton()
  {
    Display.setColor4(0, 0, 31, 255);
    Display.drawRectangleOr(400, 300, displayLocations_ButtonWidth, displayLocations_ButtonHeight, true);
    Display.setColor4(0, 0, 31, 255);
    Display.drawRectangleOr(400, 300, displayLocations_ButtonWidth, displayLocations_ButtonHeight, false);

    if (!clicked)
      //System.out.println("Attempting: new Color(" + (191 * (1 - hoverPhase[i]) + 97 * (hoverPhase[i])) + ", " + (191 * (1 - hoverPhase[i]) + 210 * (hoverPhase[i])) + ", " + (191 * (1 - hoverPhase[i]) + 97 * (hoverPhase[i])) + ")");
      Display.setColor3(new Color((int) (191 * (1 - hoverPhase) + 97 * (hoverPhase)), (int) (191 * (1 - hoverPhase) + 210 * (hoverPhase)), (int) (191 * (1 - hoverPhase) + 97 * (hoverPhase))));
    else
      Display.setColor3(new Color(127, 127, 255));
    Display.drawRectangleOr(displayLocations_WindowCenterX, displayLocations_WindowCenterY, displayLocations_ButtonWidth, displayLocations_ButtonHeight, false);

    // Button background. depth = -.9;
    Display.setColor3(new Color(0, 0, 95));
    Display.drawRectangleOr(displayLocations_WindowCenterX, displayLocations_WindowCenterY, displayLocations_ButtonWidth, displayLocations_ButtonHeight, true);

  }
}
