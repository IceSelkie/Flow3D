import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;


/**
 * class DisplaySelect
 * <p>
 * The window data and layout for the window of the menu to select the different layers to play.
 *
 * @author Kevin C.
 * @version 0.0 (Not Started)
 */
public class DisplaySelect extends DisplayableWindow
{
  //window size
  protected static final int displayLocations_WindowWidth = 800;
  protected static final int displayLocations_WindowHeight = 600;
  protected static final int displayLocations_WindowCenterX = displayLocations_WindowWidth / 2;
  protected static final int displayLocations_WindowCenterY = displayLocations_WindowHeight / 2;
  protected static final int displayLocations_ButtonWidth = 200;
  protected static final int displayLocations_ButtonHeight = 50;
  protected static final int displayLocations_ButtonBuffer = 50;
  protected static final int displayLocations_ButtonBufferedSizeY = displayLocations_ButtonHeight+displayLocations_ButtonBuffer;

  private ArrayList<Level> levels;

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

  }

  /**
   * When the user clicks and holds, then moves the mouse,
   * this will be called. Warning: This is asynchronous, and
   * can happen at any time, EVEN WHEN OTHER METHODS ARE
   * INPROGRESS.
   *
   * @param location The point on the screen where the mouse was moved to.
   */
  public void doDrag(Point location)
  {

  }

  /**
   * When the user clicks, then lets go, this will be
   * called. Warning: This is asynchronous, and can happen
   * at any time, EVEN WHEN OTHER METHODS ARE INPROGRESS.
   *
   * @param clickType Enumeration of which mouse button was used to do the click. See {@link GLFW#GLFW_MOUSE_BUTTON_1} through {@link GLFW#GLFW_MOUSE_BUTTON_8}
   * @param location  The point on the screen where the mouse is, upon being released.
   */
  public void doRelease(int clickType, Point location)
  {

  }

  /**
   * When the user attempts to scroll, this will be
   * called. Warning: This is asynchronous, and can happen
   * at any time, EVEN WHEN OTHER METHODS ARE INPROGRESS.
   *
   * @param directionIsUp This will be {@code true}, if the scroll is upward.
   * @param location      The point on the screen where the mouse is, when it is scrolled.
   */
  public void doScroll(boolean directionIsUp, Point location)
  {

  }

  /**
   * Abstract method to be implemented by subclasses to do the actual rendering of the window.
   *
   * @param window The GLFWwindow pointer that holds the window's data. Needed to do any displaying.
   */
  public void display(long window)
  {
    int numLevels = levels.size();
    for (int i = 0; i < numLevels; i++)
    {
      // Button outline. depth = .5;
      Display.setColor3(new Color(191, 191, 191));
      Display.drawRectangleOr(displayLocations_WindowCenterX,displayLocations_WindowCenterY+displayLocations_ButtonBuffer*i-displayLocations_ButtonBuffer*numLevels,displayLocations_ButtonWidth,displayLocations_ButtonHeight,false,.5);

      // Button background. depth = -.9;
      Display.setColor3(new Color(0, 0, 95));
      Display.drawRectangleOr(displayLocations_WindowCenterX,displayLocations_WindowCenterY+displayLocations_ButtonBuffer*i-displayLocations_ButtonBuffer*numLevels,displayLocations_ButtonWidth,displayLocations_ButtonHeight,true,-.9);

      // The contents of the button: ie: text and color, etc.
      buttonDisplay(i,levels.get(i));
    }
  }
  private void buttonDisplay(int numFromBottom, Level level)
  {
    Level compare = new Level();

    compare.easy();
    if (level.equals(compare))
    Display.setColor3(PathColor.GREEN.toColor());

    compare.medium();
    if (level.equals(compare))
    Display.setColor3(PathColor.YELLOW.toColor());

    compare.hard();
    if (level.equals(compare))
    Display.setColor3(PathColor.RED.toColor());

    Display.drawRectangleOr(displayLocations_WindowCenterX,displayLocations_WindowCenterY+displayLocations_ButtonBuffer*numFromBottom-displayLocations_ButtonBuffer*levels.size(),displayLocations_ButtonWidth,displayLocations_ButtonHeight,true, 0);

  }
}
