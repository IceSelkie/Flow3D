/**
 * class Cube
 * <p>
 * Represents a three dimensional "cube" of flow cells within the game Flow3D.
 *
 * @author Kevin Chen
 * @version 1.2
 */
public class Cube
{
  /**
   * 3D Matrix holding the Paths that are inside the game.
   */
  private Path[][][] cubePaths;
  /**
   * Size of the cube.
   */
  private int size;

  /**
   * Constructor for Cube
   * <p>
   * Creates an empty {@code Cube} object of a given size.
   *
   * @param sideLength The side length of the {@code Cube} to create.
   */
  public Cube(int sideLength)
  {
    cubePaths = new Path[sideLength][sideLength][sideLength];
    size = sideLength;
  }

  /**
   * Creates a new start {@link Path} ({@link PathType#START}) of a given {@link PathColor} at the specified location.
   *
   * @param location the position to add the new start {@link Path} ({@link PathType#START}).
   * @param color    the color of the new start {@link Path} ({@link PathType#START}).
   */
  public void addStart(Point3I location, PathColor color)
  {
    setPath(new Path(PathType.START, color), location);
  }

  /**
   * Sets the specified location in the cube with the given {@link Path}.
   *
   * @param replacementPath The new {@link Path} object going in the {@code Cube}.
   * @param x               The X location in the {@code Cube}.
   * @param y               The Y location in the {@code Cube}.
   * @param z               The Z location in the {@code Cube}.
   */
  public void setPath(Path replacementPath, int x, int y, int z)
  {
    cubePaths[x][y][z] = replacementPath;
  }

  /**
   * Sets the specified location in the cube with the given {@link Path}.
   *
   * @param replacementPath The new {@link Path} object going in the {@code Cube}.
   * @param location        The position in the {@code Cube} to put the {@link Path}.
   */
  public void setPath(Path replacementPath, Point3I location)
  {
    if (location != null)
      setPath(replacementPath, location.getX(), location.getY(), location.getZ());
  }

  /**
   * Gets the {@link Path} at the specified location in the {@code Cube}.
   *
   * @param x The X location in the {@code Cube} of the {@link Path} to get.
   * @param y The Y location in the {@code Cube} of the {@link Path} to get.
   * @param z The Z location in the {@code Cube} of the {@link Path} to get.
   * @return the {@link Path} at the specified location.
   */
  public Path get(int x, int y, int z)
  {
    if (x >= 0 && y >= 0 && z >= 0)
      if (x < size() && y < size() && z < size())
        return cubePaths[x][y][z];
    return null;
  }

  /**
   * Gets the {@link Path} at the specified location in the {@code Cube}.
   *
   * @param location The position in the {@code Cube} of the {@link Path} to get.
   * @return the {@link Path} at the specified location.
   */
  public Path get(Point3I location)
  {
    if (location != null)
      return get(location.getX(), location.getY(), location.getZ());
    return null;
  }

  /**
   * Gets the size of this {@code Cube}.
   *
   * @return the size or side-length of this {@code Cube}.
   */
  public int size()
  {
    return size;
  }
}