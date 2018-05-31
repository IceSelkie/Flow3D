import java.awt.*;

/**
 * abstract class DisplayableWindow
 *
 * Interface for the window data and layout for a window, to display each window.
 *
 * @author Stanley S. & Kaushik A.
 * @version 0.0 (Not Started)
 */
public abstract class DisplayableWindow
{
  public DisplayableWindow()
  {}

  public abstract List<Elements> getElements();
  public abstract void doClick(int clickType, Point location);
  public abstract void doDrag(Point location);
  public abstract void doRelease(int clickType, Point location);
  public abstract void doScroll(boolean directionIsUp, Point location);

}
