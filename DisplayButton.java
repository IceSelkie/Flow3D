import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

public abstract class DisplayButton
{
  protected Display parent = null;
  protected Rectangle2D.Double region;
  protected LinkedList<Point2D> dragPath;

  public abstract void click(int mouseButton, Point2D positionClicked);
  public abstract void dragged(Point2D latestPoint);
  public abstract void dragReleased(int mouseButton, Point2D lastPoint);

  public final void clickStart(int mouseButton, Point2D location)
  {
    dragPath = new LinkedList<Point2D>();
    dragPath.add(location);
    click(mouseButton, location);
  }
  public final void drag(Point2D location)
  {
    dragPath.add(location);
    dragged(location);
  }
  public final void dragFinished(int mouseButton, Point2D location)
  {
    dragPath.add(location);
    dragReleased(mouseButton, location);
    dragPath = null;
  }

  public Display getParent()
  {
    return parent;
  }

  public Rectangle2D.Double getRegion()
  {
    return region;
  }

  public void setParent(Display parent)
  {
    this.parent = parent;
  }

  public void setRegion(Rectangle2D.Double region)
  {
    this.region = region;
  }

  public void setRegion(double x, double y, double width, double height)
  {
    this.region = new Rectangle2D.Double(x,y,width,height);
  }
}
