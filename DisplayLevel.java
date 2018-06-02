import java.awt.*;
import java.util.LinkedList;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.opengl.GL11.*;

/**
 * class DisplayLevel
 * <p>
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

  // The sizes and locations of all the things that are going to be displayed.
  protected static final int displayLocations_WindowWidth = 800;
  protected static final int displayLocations_WindowHeight = 600;
  protected static final int displayLocations_WindowCenterX = displayLocations_WindowWidth/2;
  protected static final int displayLocations_WindowCenterY = displayLocations_WindowHeight/2;

  protected static final int displayLocations_MainWindowWidth = 550;
  protected static final int displayLocations_MainWindowCenterX = displayLocations_WindowWidth-(displayLocations_MainWindowWidth/2);
  protected static final int displayLocations_MainWindowCenterY = displayLocations_WindowHeight/2;

  protected static final int displayLocations_GameBufferRight = 50;
  protected static final int displayLocations_GameSize = 400;
  protected static final int displayLocations_GameBufferTop = 50;
  protected static final int displayLocations_GameCenterX = displayLocations_WindowWidth-displayLocations_GameBufferRight-(displayLocations_GameSize/2);
  protected static final int displayLocations_GameCenterY = displayLocations_GameBufferTop+(displayLocations_GameSize/2);
  protected static final int displayLocations_GameLeft = displayLocations_GameCenterX-(displayLocations_GameSize/2);
  protected static final int displayLocations_GameRight = displayLocations_WindowWidth-displayLocations_GameBufferRight;
  protected static final int displayLocations_GameTop = displayLocations_GameBufferTop;
  protected static final int displayLocations_GameBottom = displayLocations_GameTop+displayLocations_GameSize;

  protected static final int displayLocations_LeftBarWidth = displayLocations_WindowWidth-displayLocations_MainWindowWidth;
  protected static final int displayLocations_LeftBarCenterX = displayLocations_LeftBarWidth/2;
  protected static final int displayLocations_LeftBarCenterY = displayLocations_WindowHeight/2;

  protected static final int displayLocations_LeftBar_LevelsBufferLeft = 65;
  protected static final int displayLocations_LeftBar_LevelsSize = 120;
  protected static final int displayLocations_LeftBar_LevelsBuffer = 30;
  protected static final int displayLocations_LeftBar_LevelsBufferedSize = displayLocations_LeftBar_LevelsSize+displayLocations_LeftBar_LevelsBuffer;
  protected static final int displayLocations_LeftBar_LevelsCenterX = displayLocations_LeftBar_LevelsBufferLeft+(displayLocations_LeftBar_LevelsSize/2);
  protected static final int displayLocations_LeftBar_LevelsCenterY = displayLocations_WindowHeight/2;

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

    if (Driver.DEBUG) System.out.println("Click registered at: [" + location.getX() + "," + location.getY() + "].");

    layer = onAnotherLevel(location);
    //if (onMainBoard(location))
    //clickedOnSquare = getSquare(lvl.size(),location);

    Point3I cell = getSquare(location);
    if (cell!=null)
    {
      dragPath = new LinkedList<>();
      dragPath.add(getSquare(location));
    }
  }

  public void doDrag(Point location)
  {
    if (dragPath == null)
      return;

    Point3I cell = getSquare(location);

    if (cell != null && !dragPath.getLast().equals(cell))
    {
      while (dragPath.contains(cell))
        dragPath.removeLast();
      dragPath.add(cell);
      System.out.print("The drag path is: [");
      for (Point3I p : dragPath)
        System.out.print(p.toString() + ",");
      System.out.println("].");
    }
  }

  public void doRelease(int clickType, Point location)
  {
    makeDragPermanent();
    dragPath = null;
  }

  public void doScroll(boolean directionIsUp, Point location)
  {
    System.out.println("An " + (directionIsUp?"upward":"downward")+ " scroll has been registered!");
    if (directionIsUp && layer + 1 < lvl.size())
      layer++;
    if (!directionIsUp && layer > 0)
      layer--;

    // If the mouse is currently held down
    if (dragPath != null)
      doDrag(location);
  }

  public void display(long window)
  {
    Display.setColor3(new Color( 191, 191, 191));
    Display.drawRectangleOr(displayLocations_LeftBarWidth, displayLocations_WindowCenterY, 2, displayLocations_WindowHeight, -.9);

    for (int i = -layer; i < lvl.size() - layer; i++)
    {
      drawLayerOr(layer + i, displayLocations_LeftBar_LevelsCenterX, displayLocations_LeftBar_LevelsCenterY - displayLocations_LeftBar_LevelsBufferedSize * i, displayLocations_LeftBar_LevelsSize, 1);
      Display.setColor3(new Color(0, 0, 95));
      Display.drawRectangleOr(displayLocations_LeftBar_LevelsCenterX, displayLocations_LeftBar_LevelsCenterY - displayLocations_LeftBar_LevelsBufferedSize * i, displayLocations_LeftBar_LevelsSize, displayLocations_LeftBar_LevelsSize, .5);
    }

    drawLayerOr(layer, displayLocations_GameCenterX, displayLocations_GameCenterY, displayLocations_GameSize, 1);
    Display.setColor3(new Color(0, 0, 95));
    Display.drawRectangleOr(displayLocations_GameCenterX, displayLocations_GameCenterY, displayLocations_GameSize, displayLocations_GameSize, .5);
  }



  // **************** HELPER/ALMOSTSTATIC METHODS **************** // TODO



  public int onAnotherLevel(Point clickLocation)
  {
    final int quantity = lvl.size();
    final int yMid = displayLocations_WindowHeight/2;

    int x = (int) clickLocation.getX();
    int y = (int) clickLocation.getY();
    if (x >= displayLocations_LeftBar_LevelsBufferLeft && x <= displayLocations_LeftBar_LevelsBufferLeft + displayLocations_LeftBar_LevelsSize)
    {
      int offset = yMid + displayLocations_LeftBar_LevelsSize / 2 + quantity * (displayLocations_LeftBar_LevelsSize + displayLocations_LeftBar_LevelsBuffer) - y;
      if (offset % (displayLocations_LeftBar_LevelsSize + displayLocations_LeftBar_LevelsBuffer) <= displayLocations_LeftBar_LevelsSize)
      {
        int ret = layer + (offset / (displayLocations_LeftBar_LevelsSize + displayLocations_LeftBar_LevelsBuffer) - quantity);
        if (ret<lvl.size()&&ret>=0)
        {
          if (Driver.DEBUG) System.out.println("Now on layer: "+ret);
          return ret;
        }
      }
    }
    return layer;
  }

  public boolean onMainBoard(Point clickLocation)
  {
    int x = (int) clickLocation.getX();
    int y = (int) clickLocation.getY();
    return ((x >= displayLocations_GameLeft && x <= displayLocations_GameRight) && (y >= displayLocations_GameTop && y <= displayLocations_GameBottom));
  }

  public Point3I getSquare(Point clickLocation)
  {
    if (!onMainBoard(clickLocation))
      return null;
    int cubeSize = lvl.size();
    int x = (int) clickLocation.getX() - displayLocations_GameLeft;
    int y = (int) clickLocation.getY() - displayLocations_GameTop;

    //System.out.println("Click registered in square: [" + (cubeSize * x / displayLocations_GameSize) + "," + (cubeSize * y / displayLocations_GameSize) + "]");
    return new Point3I(cubeSize * x / displayLocations_GameSize, cubeSize * y / displayLocations_GameSize, layer);
  }



  // **************** METHODS THAT DO STUFF **************** // TODO



  private void makeDragPermanent()
  {
    //TODO
  }

  private void drawLayerOr(int layer, double xPos, double yPos, double width, double depth)
  {
    drawGrid(xPos, yPos, width, depth);
    xPos -= width / 2D;
    yPos -= width / 2D;
    for (int x = 0; x < lvl.size(); x++)
      for (int y = 0; y < lvl.size(); y++)
        drawPath(lvl.get(x, y, layer), xPos + x * width / lvl.size(), yPos + y * width / lvl.size(), width / lvl.size(), depth);
  }

  private void drawGrid(double xPos, double yPos, double width, double depth)
  {
    int size = lvl.size();

    xPos -= width / 2D;
    yPos -= width / 2D;

    Display.setColor3(new Color( 191, 191, 191));
    glBegin(GL_LINE_LOOP);
    Display.doPointOr(xPos, yPos, depth);
    Display.doPointOr(xPos + width, yPos, depth);
    Display.doPointOr(xPos + width, yPos + width, depth);
    Display.doPointOr(xPos, yPos + width, depth);
    glEnd();

    glBegin(GL_LINES);
    for (int r = 1; r < size; r++)
    {
      Display.doPointOr(xPos, yPos + r * width / size, depth);
      Display.doPointOr(xPos + width, yPos + r * width / size, depth);
    }
    for (int c = 1; c < size; c++)
    {
      Display.doPointOr(xPos + c * width / size, yPos, depth);
      Display.doPointOr(xPos + c * width / size, yPos + width, depth);
    }
    glEnd();
  }

  private void drawPath(Path path, double xPos, double yPos, double width, double depth)
  {
    if (path == null)
      return;

    try
    {
      Display.setColor3(path.getColor().toColor());
    } catch (NullPointerException e)
    {
      e.printStackTrace();
      Display.setColor3(new Color( 191, 191, 191));
    }

    if (path.getType() == PathType.START)
      Display.doCircle(xPos + width / 2D, yPos + width / 2D, width / 3D, true, depth);
    if (path.getType() == PathType.PATH)
      Display.doCircle(xPos + width / 2D, yPos + width / 2D, width / 4D, false, depth);


    if (path.hasConnection(Path.OUT))
    {
      //caret;
    }
    if (path.hasConnection(Path.IN))
    {
      //vee;
    }
    if (path.hasConnection(Path.RIGHT))
    {
      glBegin(GL_POLYGON);
      Display.doPointOr(xPos, yPos - width / 4D, depth);
      Display.doPointOr(xPos + width, yPos - width / 4D, depth);
      Display.doPointOr(xPos + width, yPos + width / 4D, depth);
      Display.doPointOr(xPos, yPos + width / 4D, depth);
      glEnd();
    }
    if (path.hasConnection(Path.DOWN))
    {
      glBegin(GL_POLYGON);
      Display.doPointOr(xPos - width / 4D, yPos, depth);
      Display.doPointOr(xPos - width / 4D, yPos + width, depth);
      Display.doPointOr(xPos + width / 4D, yPos + width, depth);
      Display.doPointOr(xPos + width / 4D, yPos, depth);
      glEnd();
    }
  }
}
