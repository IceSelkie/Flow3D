import java.awt.Point;
import java.util.ArrayList;
import apcslib.*;
import chn.util.*;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.opengl.GL11.*;


/**
 * class DisplaySelect
 *
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
  protected static final int displayLocations_WindowCenterX = displayLocations_WindowWidth/2;
  protected static final int displayLocations_WindowCenterY = displayLocations_WindowHeight/2;  
    
  private ArrayList<Level> levelTypes;
  /**
   * Constructor: Creates Display select object and instantiates leveTypes ArrayList as empty array. 
   */
  public DisplaySelect()
  {
      levelTypes = new ArrayList<Level>();
  }
  /**
   * <b>Summary</b> Adds a level to the expandable ArrayList of Levels.
   */
  public void addLevelType(Level input)
  {
      levelTypes.add(input);
  }
  /**
   * <b>Summary</b> Returns level stored in ArrayList levelTypes given recieved index
   * @return Level at index
   */
  public Level getLevel(int index)
  {
      return levelTypes.get(index);
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
  public void doDrag(Point location){
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
  {}
  public  void doScroll(boolean directionIsUp, Point location)
  {}
  public void display(@NativeType("GLFWwindow *") long window)
  {

  }
}
