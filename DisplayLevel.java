import java.awt.*;
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
  Level lvl; // The level that is currently displayed.
  int level; // Which level to display on the main display. The z-axis. 0 being top.

  /**
   * Constructor for DisplayLevel.
   * <p>
   * Creates a new displayable window with the given cube, starting on the top level.
   *
   * @param cube The level data.
   */
  public DisplayLevel(Level cube)
  {
    level = 0;
  }

  public void doClick(int clickType, Point2D location)
  {
    if (clickType != GLFW_MOUSE_BUTTON_1)
      return;
    if (onAnotherLevel(location)!=level)
      level = onAnotherLevel(location);
  //if (onMainBoard(location))
    //clickedOnSquare = getSquare(lvl.size(),location);
    System.out.println("Click registered at: ["+location.getX()+","+location.getY()+"].");
  }

  public void doDrag(Point2D location)
  { }

  public void doRelease(int clickType, Point2D location)
  { }

  public void doScroll(boolean directionIsUp, Point2D location)
  { }

  public void display(long window)
  {

  }


  // **************** NON-IMPLEMENTED METHODS **************** // TODO



  public int onAnotherLevel(Point2D clickLocation)
  {
    int x = (int) clickLocation.getX();
    int y = (int) clickLocation.getY();
    if (x >= 100 && x <= 250)
    {
      int offset = 725 - y;
      if (offset % 175 <= 150)
        return offset / 175 + 2;
    }
    return level;
  }

  public boolean onMainBoard(Point2D clickLocation)
  {
    int x = (int) clickLocation.getX();
    int y = (int) clickLocation.getY();
    return ((x >= 350 && x <= 750) && (y >= 50 && y <= 450));
  }

  public Point getSquare(int cubeSize, Point2D clickLocation)
  {
    int x = (int) clickLocation.getX()-350;
    int y = (int) clickLocation.getY()-50;
    if (x<0||x>=400||y<0||y>=400)
      return null;
    return new Point(cubeSize*x/400, cubeSize*y/400);
  }
}
