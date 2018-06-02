import java.awt.*;
import java.util.LinkedList;

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
  int layer; // Which level to display on the main display. The z-axis. 0 being top.
  LinkedList<Point3I> dragPath; // The path of which the user has dragged.

  /**
   * Constructor for DisplayLevel.
   * <p>
   * Creates a new displayable window with the given cube, starting on the top level.
   *
   * @param level The level data.
   */
  public DisplayLevel(Level level)
  {
    lvl = level;
    layer = 0;
  }

  public void doClick(int clickType, Point location)
  {
    if (clickType != GLFW_MOUSE_BUTTON_1)
      return;

    System.out.println("Click registered at: ["+location.getX()+","+location.getY()+"].");

    layer = onAnotherLevel(location);
    //if (onMainBoard(location))
      //clickedOnSquare = getSquare(lvl.size(),location);
    dragPath = new LinkedList<>();
    dragPath.add(getSquare(location));
  }

  public void doDrag(Point location)
  {
    Point3I cell = getSquare(location);
    if (cell != null && !dragPath.getLast().equals(cell))
    {
      if (dragPath.contains(cell))
        dragPath.removeLast();
      dragPath.add(cell);
    }
  }

  public void doRelease(int clickType, Point location)
  {
    makeDragPermanent();
    dragPath = null;
  }

  public void doScroll(boolean directionIsUp, Point location)
  { }

  public void display(long window)
  {
    Display.setColor3(new Color(0, 0, 50));
    Display.drawRectangleOr(124, 300, 248, 600, .5);
    Display.drawRectangleOr(526, 300, 548, 600, .5);
    Display.setColor3(new Color(255, 127, 0));

    for (int i = -3; i <= 3; i++)
      Display.drawRectangleOr(125, 300 + 150 * i, 120, 120, -1);
  }



  // **************** HELPER/ALMOSTSTATIC METHODS **************** // TODO



  public int onAnotherLevel(Point clickLocation)
  {
    final int squareSize = 120;
    final int quantity = 2;
    final int spacing = 30;
    final int xMid = 125;
    final int yMid = 300;

    int x = (int) clickLocation.getX();
    int y = (int) clickLocation.getY();
    if (x >= xMid - squareSize / 2 && x <= xMid + squareSize / 2)
    {
      int offset = yMid + squareSize / 2 + quantity * (squareSize + spacing) - y;
      if (offset % (squareSize + spacing) <= squareSize)
      {
        System.out.println("Now on layer: " + (layer + (offset / (squareSize + spacing) - quantity)));
        return layer + (offset / (squareSize + spacing) - quantity);
      }
    }
    return layer;
  }

  public boolean onMainBoard(Point clickLocation)
  {
    int x = (int) clickLocation.getX();
    int y = (int) clickLocation.getY();
    return ((x >= 350 && x <= 750) && (y >= 50 && y <= 450));
  }

  public Point3I getSquare(Point clickLocation)
  {
    int cubeSize = 4;//lvl.size();
    int x = (int) clickLocation.getX()-350;
    int y = (int) clickLocation.getY()-50;
    if (x<0||x>=400||y<0||y>=400)
      return null;
    return new Point3I(cubeSize*x/400, cubeSize*y/400,layer);
  }



  // **************** METHODS THAT DO STUFF **************** // TODO



  private void makeDragPermanent()
  {
    //TODO
  }
}
