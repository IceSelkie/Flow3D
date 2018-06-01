import java.awt.geom.Point2D;
import java.util.List;



/**
 * class DisplaySelect
 *
 * The window data and layout for the window of the menu to select the different layers to play.
 *
 * @author Stanley S. & Kaushik A & Kevin C.
 * @version 0.0 (Not Started)
 */
public class DisplaySelect extends DisplayableWindow
{
    public DisplaySelect()
  {}
  public void doClick(int clickType, Point2D location)
  {
    if (clickType != GLFW_MOUSE_BUTTON_1)
      return;
    if (onAnotherLevel(location))
      level = onAnotherLevel(location);
    if (onMainBoard(location))
      clickedOnSquare getSquare(cube.size(),location);
  }
  public void doDrag(Point2D location){}
  public void doRelease(int clickType, Point2D location)
  {}
  public void doScroll(boolean directionIsUp, Point2D location)
  {}
  public void display(Display.WindowSize w, long window)
  {

  }
}
