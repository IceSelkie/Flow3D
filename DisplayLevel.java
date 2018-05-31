import java.awt.geom.Point2D;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

/**
 * class DisplayLevel
 *
 * The window data and layout for the window of the active game, with one level displayed.
 *
 * @author Stanley S. & Kaushik A.
 * @version 0.0 (Not Started)
 */
public class DisplayLevel extends DisplayableWindow
{
  Cube cube; // The level that is currently displayed.
  int level; // Which level to display on the main display. The z-axis. 0 being top.

  /**
   * Constructor for DisplayLevel.
   * <p>
   * Creates a new displayable window with the given cube, starting on the top level.
   *
   * @param cube The level data.
   */
  public DisplayLevel(Cube cube)
  {
    level = 0;
  }
  public List<Element> getElements()
  {
    return null;
  }

  public void doClick(int clickType, Point2D location)
  {
    if (clickType != GLFW_MOUSE_BUTTON_1)
      return;
    if (onAnotherLevel(location))
      level = onAnotherLevel(location);
    if (onMainBoard(location))
      clickedOnSquare getSquare(cube.size(),location);
  }

  public void doDrag(Point2D location)
  { }

  public void doRelease(int clickType, Point2D location)
  { }

  public void doScroll(boolean directionIsUp, Point2D location)
  { }
}
