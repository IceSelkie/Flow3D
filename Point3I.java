import java.io.Serializable;

/**
 * class Point3I
 * <p>
 * A three dimensional point of integers to represent a location is space.
 *
 * @author Stanley S.
 * @version 1.1
 */
public class Point3I implements Serializable, Cloneable
{
  /**
   * The X coordinate of this <code>Point3I</code>.
   *
   * @serial
   */
  public int x;

  /**
   * The Y coordinate of this <code>Point3I</code>.
   *
   * @serial
   */
  public int y;

  /**
   * The Z coordinate of this <code>Point3I</code>.
   *
   * @serial
   */
  public int z;

  /**
   * Constructs and initializes a <code>Point3I</code> with
   * coordinates (0,&nbsp;,0&nbsp;0).
   *
   * @since 1.2
   */
  public Point3I()
  {
    z = y = x = 0;
  }

  /**
   * Constructs and initializes a <code>Point3I</code> with
   * the specified coordinates.
   *
   * @param x the X coordinate of the newly
   *          constructed <code>Point3I</code>
   * @param y the Y coordinate of the newly
   *          constructed <code>Point3I</code>
   * @param z the Z coordinate of the newly
   *          constructed <code>Point3I</code>
   */
  public Point3I(int x, int y, int z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  /**
   * Returns the X coordinate of this <code>Point3I</code> as
   * an <code>int</code>.
   *
   * @return the X coordinate of this <code>Point3I</code>.
   */
  public int getX()
  {
    return x;
  }

  /**
   * Returns the Y coordinate of this <code>Point3I</code> as
   * an <code>int</code>.
   *
   * @return the Y coordinate of this <code>Point3I</code>.
   */
  public int getY()
  {
    return y;
  }

  /**
   * Returns the Z coordinate of this <code>Point3I</code> as
   * an <code>int</code>.
   *
   * @return the Z coordinate of this <code>Point3I</code>.
   */
  public int getZ()
  {
    return z;
  }

  /**
   * Sets the location of this <code>Point3I</code> to the
   * specified coordinates.
   *
   * @param x the new X coordinate of this {@code Point3I}
   * @param y the new Y coordinate of this {@code Point3I}
   * @param z the new Z coordinate of this {@code Point3I}
   */
  public void setLocation(int x, int y, int z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  /**
   * Sets the location of this <code>Point3I</code> to the same
   * coordinates as the specified <code>Point3I</code> object.
   *
   * @param p the specified <code>Point3I</code> to which to set
   *          this <code>Point3I</code>
   */
  public void setLocation(Point3I p)
  {
    setLocation(p.getX(), p.getY(), p.getZ());
  }

  /**
   * Returns the square of the distance from this
   * <code>Point3I</code> to a specified <code>Point3I</code>.
   *
   * @param pt the specified point to be measured
   *           against this <code>Point3I</code>
   * @return the square of the distance between this
   * <code>Point3I</code> to a specified <code>Point3I</code>.
   */
  public double distanceSq(Point3I pt)
  {
    double dx = pt.getX() - this.getX();
    double dy = pt.getY() - this.getY();
    double dz = pt.getZ() - this.getZ();
    return (dx * dx + dy * dy + dz * dz);
  }

  /**
   * Returns the distance from this <code>Point3I</code> to a
   * specified <code>Point3I</code>.
   *
   * @param pt the specified point to be measured
   *           against this <code>Point3I</code>
   * @return the distance between this <code>Point3I</code> and
   * the specified <code>Point3I</code>.
   */
  public double distance(Point3I pt)
  {
    return Math.sqrt(distanceSq(pt));
  }

  /**
   * Returns a new <code>Point3I</code> that has the position of
   * the two <code>Point3I</code>s added together.
   *
   * @param pt the specified point to be added to this <code>Point3I</code>.
   * @return the point who's position is the sum of this <code>Point3I</code>
   * and the specified <code>Point3I</code>.
   */
  public Point3I add(Point3I pt)
  {
    return new Point3I(this.getX() + pt.getX(), this.getY() + pt.getY(), this.getZ() + pt.getZ());
  }

  /**
   * Returns a new <code>Point3I</code> that has the position of
   * the this <code>Point3I</code> added to the coordinates.
   *
   * @param x the X component to be added to this <code>Point3I</code>.
   * @param y the Y component to be added to this <code>Point3I</code>.
   * @param z the Z component to be added to this <code>Point3I</code>.
   * @return the point who's position is the sum of this <code>Point3I</code>
   * and the specified coordinates.
   */
  public Point3I add(int x, int y, int z)
  {
    return new Point3I(this.getX() + x, this.getY() + y, this.getZ() + z);
  }

  /**
   * Creates a new object of the same class and with the
   * same contents as this object.
   *
   * @return a clone of this instance.
   * @throws OutOfMemoryError if there is not enough memory.
   */
  public Object clone()
  {
    try
    {
      return super.clone();
    } catch (CloneNotSupportedException e)
    {
      // this shouldn't happen, since we are Cloneable
      throw new InternalError(e);
    }
  }

  /**
   * Determines whether or not two points are equal. Two instances of
   * <code>Point2D</code> are equal if the values of their
   * <code>x</code> and <code>y</code> member fields, representing
   * their position in the coordinate space, are the same.
   *
   * @param obj an object to be compared with this <code>Point2D</code>
   * @return <code>true</code> if the object to be compared is
   * an instance of <code>Point2D</code> and has
   * the same values; <code>false</code> otherwise.
   * @since 1.2
   */
  public boolean equals(Object obj)
  {
    if (obj instanceof Point3I)
    {
      Point3I p3i = (Point3I) obj;
      return (getX() == p3i.getX()) && (getY() == p3i.getY()) && (getZ() == p3i.getZ());
    }
    return super.equals(obj);
  }

  @Override
  public String toString()
  {
    return "Point3I{" + getX() + "," + getY() + "," + getZ() + "}";
  }
}
