import java.awt.Point;

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

  public abstract void doClick(int clickType, Point location);
  public abstract void doDrag(Point location);
  public abstract void doRelease(int clickType, Point location);
  public abstract void doScroll(boolean directionIsUp, Point location);

  public abstract void display(long window);
}
