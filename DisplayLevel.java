import org.lwjgl.glfw.GLFW;

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
   * Creates a new displayable window with the given level, starting on the top level.
   *
   * @param level The level data.
   */
  public DisplayLevel(Level level)
  {
    lvl = level;
    layer = 0;
  }

  /**
   * When the user clicks a location on the window, this
   * will be called. Warning: This is asynchronous, and
   * can happen at any time, EVEN WHEN OTHER METHODS ARE
   * INPROGRESS.
   * <p>
   * This finds where the click, and takes the necessary action.
   *
   * @param clickType Enumeration of which mouse button was used to do the click. See {@link GLFW#GLFW_MOUSE_BUTTON_1} through {@link GLFW#GLFW_MOUSE_BUTTON_8}
   * @param location  The point on the screen that was clicked.
   */
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
      Path path = lvl.get(cell);
      if (path!=null)
      {
        LinkedList<Point3I> flow = lvl.getFlow(path.getColor());
        if (flow != null && flow.contains(cell) && path.getType()!=PathType.START)
        {
          dragPath = flow;
          doDrag(location);
        }
        else
        {
          dragPath = new LinkedList<>();
          dragPath.add(getSquare(location));
        }
      }
    }
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
    if (dragPath!=null)
    {
      makeDragPermanent();
      dragPath = null;
    }
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
    System.out.println("An " + (directionIsUp?"upward":"downward")+ " scroll has been registered!");
    if (!onMainBoard(location) || lvl.isDrawable(getSquare(location).add(0,0,directionIsUp?1:-1)))
    {
      if (directionIsUp && layer + 1 < lvl.size())
        layer++;
      if (!directionIsUp && layer > 0)
        layer--;
    }

    // If the mouse is currently held down, then we are drawing a path to a new layer.
    if (dragPath != null)
      doDrag(location);
  }

  /**
   * Abstract method to be implemented by subclasses to do the actual rendering of the window.
   *
   * @param window The GLFWwindow pointer that holds the window's data. Needed to do any displaying.
   */
  public void display(long window)
  {
    Display.setColor3(new Color( 191, 191, 191));
    Display.drawRectangleOr(displayLocations_LeftBarWidth, displayLocations_WindowCenterY, 2, displayLocations_WindowHeight,true, -.9);

    for (int i = -layer; i < lvl.size() - layer; i++)
    {
      drawLayerOr(layer + i, displayLocations_LeftBar_LevelsCenterX, displayLocations_LeftBar_LevelsCenterY - displayLocations_LeftBar_LevelsBufferedSize * i, displayLocations_LeftBar_LevelsSize, 1);
      Display.setColor3(new Color(0, 0, 95));
      Display.drawRectangleOr(displayLocations_LeftBar_LevelsCenterX, displayLocations_LeftBar_LevelsCenterY - displayLocations_LeftBar_LevelsBufferedSize * i, displayLocations_LeftBar_LevelsSize, displayLocations_LeftBar_LevelsSize,true, .5);
    }

    drawLayerOr(layer, displayLocations_GameCenterX, displayLocations_GameCenterY, displayLocations_GameSize, 1);
    Display.setColor3(new Color(0, 0, 95));
    Display.drawRectangleOr(displayLocations_GameCenterX, displayLocations_GameCenterY, displayLocations_GameSize, displayLocations_GameSize,true, .5);
  }



  // **************** HELPER/ALMOSTSTATIC METHODS **************** // TODO



  private int onAnotherLevel(Point clickLocation)
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

  private boolean onMainBoard(Point clickLocation)
  {
    int x = (int) clickLocation.getX();
    int y = (int) clickLocation.getY();
    return ((x >= displayLocations_GameLeft && x <= displayLocations_GameRight) && (y >= displayLocations_GameTop && y <= displayLocations_GameBottom));
  }

  private Point3I getSquare(Point clickLocation)
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
    PathColor clr = lvl.get(dragPath.getFirst()).getColor();
    lvl.reset(clr);
    for (int i = 1; i<dragPath.size(); i++)
    {
      Point3I dragPathi = dragPath.get(i);
      Point3I dragPathiPrev = dragPath.get(i - 1);
      // Remove any paths that this path crosses paths with.
      Path pathToReplace = lvl.get(dragPathi);
      if (pathToReplace != null)
      {
        Path previousCutOff = lvl.get(lvl.getPreviousInFlow(dragPathi));
        if (previousCutOff!=null)
          previousCutOff.setDirection(null);

        LinkedList<Point3I> flow = lvl.getFlow(pathToReplace.getColor());
        if (flow != null && flow.contains(dragPathi))
        {
          while (!flow.getFirst().equals(dragPathi))
            flow.removeFirst();

          for (Point3I pt : flow)
            if (lvl.isDrawable(pt))
              lvl.setPath((Path) null, pt);
        }
      }

      if (lvl.isDrawable(dragPathi))
      {
        // lvl.setPath(dragPath.get(i-1),dragPath.get(i))
        lvl.setPath(clr, dragPathi);
        lvl.get(dragPathiPrev).setDirection(PathDirection.get(dragPathiPrev, dragPathi));
      }
      else
      {
        if (lvl.get(dragPathi).getColor() == clr)
        {
          lvl.get(dragPathiPrev).setDirection(PathDirection.get(dragPathiPrev, dragPathi));
          lvl.get(dragPathi).setDirection(null);
        }
        i = dragPath.size();
      }
    }

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
      Display.setColor3(new Color(191, 191, 191));
    }

    xPos+=width/2D;
    yPos+=width/2D;
    if (path.getType() == PathType.START)
      Display.doCircle(xPos, yPos, width / 3D, true, depth);
    if (path.getType() == PathType.PATH)
      Display.doCircle(xPos , yPos, width / 4D, true, depth);

    PathDirection nextDirection = path.getDirection();
    if (nextDirection != null)
      switch (nextDirection)
      {
        case IN:
          //vee; TODO: IF THE CONNECTION IS TO HERE, THIS WILL NOT BE DRAWN. ONLY IF FROM HERE TO THERE.
          break;
        case OUT:
          //caret; TODO: IF THE CONNECTION IS TO HERE, THIS WILL NOT BE DRAWN. ONLY IF FROM HERE TO THERE.
          break;
        case UP:
          glBegin(GL_POLYGON);
          Display.doPointOr(xPos - width / 4D, yPos - width, depth);
          Display.doPointOr(xPos - width / 4D, yPos, depth);
          Display.doPointOr(xPos + width / 4D, yPos, depth);
          Display.doPointOr(xPos + width / 4D, yPos - width, depth);
          glEnd();
          break;
        case DOWN:
          glBegin(GL_POLYGON);
          Display.doPointOr(xPos - width / 4D, yPos, depth);
          Display.doPointOr(xPos - width / 4D, yPos + width, depth);
          Display.doPointOr(xPos + width / 4D, yPos + width, depth);
          Display.doPointOr(xPos + width / 4D, yPos, depth);
          glEnd();
          break;
        case LEFT:
          glBegin(GL_POLYGON);
          Display.doPointOr(xPos - width, yPos - width / 4D, depth);
          Display.doPointOr(xPos, yPos - width / 4D, depth);
          Display.doPointOr(xPos, yPos + width / 4D, depth);
          Display.doPointOr(xPos - width, yPos + width / 4D, depth);
          glEnd();
          break;
        case RIGHT:
          glBegin(GL_POLYGON);
          Display.doPointOr(xPos, yPos - width / 4D, depth);
          Display.doPointOr(xPos + width, yPos - width / 4D, depth);
          Display.doPointOr(xPos + width, yPos + width / 4D, depth);
          Display.doPointOr(xPos, yPos + width / 4D, depth);
          glEnd();
          break;
        default:
      }
  }
}
