import java.awt.geom.Point2D;
import java.util.List;

/**
 * abstract class DisplayableWindow
 *
 * Interface for the window data and layout for a window, to display each window.
 *
 * @author Stanley S. & Kaushik A.
 * @version 0.1 (WIP)
 */
public abstract class DisplayableWindow
{
  public DisplayableWindow()
  {}

  public abstract void doClick(int clickType, Point2D location);
  public abstract void doDrag(Point2D location);
  public abstract void doRelease(int clickType, Point2D location);
  public abstract void doScroll(boolean directionIsUp, Point2D location);

  public void display(Display.WindowSize w, long window)
  {

  }
}
