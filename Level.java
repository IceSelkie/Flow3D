import java.util.ArrayList;
import java.util.LinkedList;

/**
 * class Level
 * <p>
 * Represents a three dimensional level in the game Flow3D.
 * <p>
 * Uses a {@link Cube} object to store the information and set of {@link Path}s in the level.
 *
 * @author Peter Stratton
 * @version 1.6
 */
public class Level
{
  /**
   * The {@link Cube} that this represents and is this level.
   */
  protected Cube levelCube;

  private Level(int size)
  {
    levelCube = new Cube(size);
  }

  /**
   * Constructor for Level
   * <p>
   * Creates a new {@code Level} of a given size with a set of start point positions.
   *
   * @param size               The size of the {@link Cube} this level represents.
   * @param startPositionPairs An array of start positions to use. Each pair will be given a different {@link PathColor}, up to all colors being used.
   */
  public Level(int size, Point3I[] startPositionPairs)
  {
    this(size);

    int startPositionCount = startPositionPairs.length;

    if (startPositionCount % 2 != 0)
      throw new IllegalArgumentException("There must be an even number of end points, so that each start can be matched to an end point!");
    if (startPositionCount / 2 > PathColor.COUNT)
      throw new IllegalArgumentException("There are too many start positions!");
    for (int i = 0; i < startPositionCount; i++)
    {
      Point3I pos = startPositionPairs[i];
      if (!validLocation(pos))
        throw new IllegalArgumentException("One or more points are not in a valid position! They are outside of the Cube. Note: Positions are counted algebraically, from 0 to less than the size.");
      else
        if (levelCube.get(pos) != null)

          throw new IllegalArgumentException("Two or more start positions are in the same place!");
        else
          levelCube.addStart(pos, PathColor.get(i / 2));
    }
  }

  /**
   * Creates a new {@code Level} object set to the default "<i>Tutorial</i>" level.
   * <p>
   * This is a simple 2x2x2 cube designed to teach the player that they need to fill in all spaces with the flow to pass the level.
   * <p>
   * The start points {@link PathType#START} are hardcoded to their locations with their corresponding {@link PathColor}.
   */
  public static Level easy()
  {
    return new Level(2, new Point3I[]{
        new Point3I(0, 0, 0), // RED
        new Point3I(0, 1, 1), // RED
        new Point3I(0, 1, 0), // GREEN
        new Point3I(1, 0, 1)  // GREEN
    });
  }

  /**
   * Creates a new {@code Level} object set to the default medium difficulty level.
   * <p>
   * This is a simple 3x3x3 cube.
   * <p>
   * The start points {@link PathType#START} are hardcoded to their locations with their corresponding {@link PathColor}.
   */
  public static Level medium()
  {
    return new Level(3, new Point3I[]{
        new Point3I(1, 2, 2), // RED
        new Point3I(2, 0, 1), // RED
        new Point3I(0, 0, 0), // GREEN
        new Point3I(2, 0, 2), // GREEN
        new Point3I(1, 0, 1), // BLUE
        new Point3I(2, 2, 0)  // BLUE
    });
  }

  /**
   * Creates a new {@code Level} object set to the default "<i>hard</i>" difficulty level.
   * <p>
   * This is a simple 4x4x4 cube.
   * <p>
   * The start points {@link PathType#START} are hardcoded to their locations with their corresponding {@link PathColor}.
   */
  public static Level hard()
  {
    return new Level(4, new Point3I[]{
        new Point3I(0, 1, 2), // RED
        new Point3I(1, 1, 2), // RED
        new Point3I(0, 1, 0), // GREEN
        new Point3I(1, 3, 0), // GREEN
        new Point3I(1, 3, 1), // BLUE
        new Point3I(3, 0, 0), // BLUE
        new Point3I(2, 1, 1), // YELLOW
        new Point3I(3, 2, 1), // YELLOW
        new Point3I(2, 2, 0), // ORANGE
        new Point3I(3, 3, 3), // ORANGE
        new Point3I(0, 0, 0), // MAGENTA
        new Point3I(0, 2, 2), // MAGENTA
    });
  }

  /**
   * Gets the size of the {@code Level}.
   *
   * @return the size or side-length of this {@code Level}.
   */
  public int size()
  {
    return levelCube.size();
  }

  /**
   * Gets the {@link Path} at the specified location in the {@code Level}.
   *
   * @param x The X location in the {@code Level} of the {@link Path} to get.
   * @param y The Y location in the {@code Level} of the {@link Path} to get.
   * @param z The Z location in the {@code Level} of the {@link Path} to get.
   * @return the {@link Path} at the specified location.
   */
  public Path getPath(int x, int y, int z)
  {
    if (validLocation(x, y, z))
    {
      return levelCube.get(x, y, z);
    }
    return null;
  }

  /**
   * Gets the {@link Path} at the specified location in the {@code Level}.
   *
   * @param location The position in the {@code Level} of the {@link Path} to get.
   * @return the {@link Path} at the specified location.
   */
  public Path getPath(Point3I location)
  {
    if (location != null)
      return getPath(location.getX(), location.getY(), location.getZ());
    return null;
  }

  /**
   * Checks to see if a specific point is overwritable or not.
   * <p>
   * If the {@link Path} at the given point is outside of the {@code Level} or is a {@link PathType#START}, then this will return {@code false}, and {@code true} otherwise.
   *
   * @param x The X location in the {@code Level} of the {@link Path} to check.
   * @param y The Y location in the {@code Level} of the {@link Path} to check.
   * @param z The Z location in the {@code Level} of the {@link Path} to check.
   * @return whether the position is overwritable or not.
   */
  public boolean isDrawable(int x, int y, int z)
  {
    if (validLocation(x, y, z))
    {
      Path path = levelCube.get(x, y, z);
      return (path == null || path.getType() != PathType.START);
    }
    return false;
  }

  /**
   * Checks to see if a specific point is overwritable or not.
   * <p>
   * If the {@link Path} at the given point is outside of the {@code Level} or is a {@link PathType#START}, then this will return {@code false}, and {@code true} otherwise.
   *
   * @param location position in the {@code Level} of the {@link Path} to check.
   * @return whether the position is overwritable or not.
   */
  public boolean isDrawable(Point3I location)
  {
    if (location != null)
      return isDrawable(location.getX(), location.getY(), location.getZ());
    return false;
  }

  /**
   * Checks to see if a position is a valid location within the {@code Level}. (Within it's indexes.)
   * <p>
   * If all of the components of the given point are greater than or equal to zero and less than the {@link Level#size()}, then this will return {@code true}, and {@code false} otherwise.
   *
   * @param x The X location in the {@code Level} of the {@link Path} to check.
   * @param y The Y location in the {@code Level} of the {@link Path} to check.
   * @param z The Z location in the {@code Level} of the {@link Path} to check.
   * @return whether the position is within the {@code Level} or not.
   */
  public boolean validLocation(int x, int y, int z)
  {
    return (x >= 0 && y >= 0 && z >= 0) &&
        (x < levelCube.size() && y < levelCube.size() && z < levelCube.size());
  }

  /**
   * Checks to see if a position is a valid location within the {@code Level}. (Within it's indexes.)
   * <p>
   * If all of the components of the given point are greater than or equal to zero and less than the {@link Level#size()}, then this will return {@code true}, and {@code false} otherwise.
   *
   * @param location position in the {@code Level} of the {@link Path} to check.
   * @return whether the position is within the {@code Level} or not.
   */
  public boolean validLocation(Point3I location)
  {
    return validLocation(location.getX(), location.getY(), location.getZ());
  }

  /**
   * Sets the given location in the {@code Level} to the passed {@link Path}.
   *
   * @param location The position in the {@code Level} to put the {@link Path}.
   * @param path     The new {@link Path} object.
   */
  public void setPath(Point3I location, Path path)
  {
    levelCube.setPath(path, location);
  }

  /**
   * Sets the given location in the {@code Level} to the specified {@link Path}.
   *
   * @param location The position in the {@code Level} to put the {@link Path}.
   * @param color    The color ({@link PathColor}) of the {@link Path}.
   */
  public void setPath(Point3I location, PathColor color)
  {
    levelCube.setPath(new Path(PathType.PATH, color), location);
  }

  /**
   * Sets the given location in the {@code Level} to the specified {@link Path}.
   *
   * @param location  The position in the {@code Level} to put the {@link Path}.
   * @param color     The color ({@link PathColor}) of the {@link Path}.
   * @param direction The direction ({@link PathDirection}) of the new {@link Path}.
   */
  public void setPath(Point3I location, PathColor color, PathDirection direction)
  {
    levelCube.setPath(new Path(PathType.PATH, color, direction), location);
  }

  /**
   * Deletes a {@link Path} at a specified point.
   *
   * @param x The X location of the {@link Path} to be deleted.
   * @param y The Y location of the {@link Path} to be deleted.
   * @param z The Z location of the {@link Path} to be deleted.
   */
  public void deletePath(int x, int y, int z)
  {
    levelCube.setPath(null, x, y, z);
  }

  /**
   * Deletes a {@link Path} at a specified point.
   *
   * @param location The location of the {@link Path} to be deleted.
   */
  public void deletePath(Point3I location)
  {
    levelCube.setPath(null, location);
  }

  /**
   * Gets the flow path of a given {@link PathColor} through the {@code Level}.
   *
   * @param color The {@link PathColor} of the flow to get the flow of.
   * @return a {@link LinkedList} of {@link Point3I}s that show where a color flows from and to. Will be {@code null} if there is no flow to be found.
   */
  public LinkedList<Point3I> getFlowPath(PathColor color)
  {
    int size = size();
    Point3I found = null;
    for (int z = 0; z < size && found == null; z++)
      for (int y = 0; y < size && found == null; y++)
        for (int x = 0; x < size && found == null; x++)
        {
          Path pth = getPath(x, y, z);
          if (pth != null && pth.getColor() == color && (pth.getType() == PathType.START && pth.getDirection() != null))
            found = new Point3I(x, y, z);
        }
    if (found == null)
      return null;
    /*while (getPath(found).getType() != PathType.START) // TODO THIS CRASHES ON EASY ((V< to V> to ^>) then (^< to ^>))
    {
      found = getPreviousInFlow(found);
    }*/

    LinkedList<Point3I> flow = new LinkedList<>();
    flow.add(found);
    while (found != null && getPath(found) != null && getPath(found).getDirection() != null)
    {
      found = getPath(found).getDirection().move(found);
      flow.add(found);
    }
    return flow;
  }

  /**
   * Gets the location of the previous {@link Path} in a flow, if applicable.
   *
   * @param location The location of the {@link Path} to find the previous one of.
   * @return a {@link Point3I} of the location of the {@link Path} in the flow before this one. Will be {@code null} if there is none before it, or this isn't a valid {@link Path}.
   */
  public Point3I getPreviousInFlow(Point3I location)
  {
    if (location != null)
      if (getPath(location).getType() != PathType.START)
      {
        for (int i = 0; i < 6; i++)
          if (validLocation(location.add(PathDirection.DIRECTIONS[i].move(new Point3I()))))
          {
            Path pth = getPath(location.add(PathDirection.DIRECTIONS[i].move(new Point3I())));
            if (pth != null && pth.getDirection() == PathDirection.DIRECTIONS[i].reverse())
            {
              return location.add(PathDirection.DIRECTIONS[i].move(new Point3I()));
            }
          }
      }
    return null;
  }

  /**
   * Checks to see if this {@code Level} is complete. Makes sure each color has been linked
   * and if every location has been filled.
   *
   * @return whether this {@code Level} has been completed.
   */
  public boolean checkWin()
  {
    // Make sure every cell is filled.
    ArrayList<PathColor> colorsInLevel = new ArrayList<PathColor>(PathColor.COUNT);
    for (int z = 0; z < size(); z++)
      for (int y = 0; y < size(); y++)
        for (int x = 0; x < size(); x++)
          if (getPath(x, y, z) == null)
            return false;
          else
            if (!colorsInLevel.contains(getPath(x, y, z).getColor()))
              colorsInLevel.add(getPath(x, y, z).getColor());

    // Make sure every flow is complete.
    for (PathColor color : colorsInLevel)
    {
      LinkedList<Point3I> flow = getFlowPath(color);
      if (getPath(flow.getFirst()).getType() != PathType.START && getPath(flow.getFirst()).getType() != PathType.START)
        return false;
    }
    return true;
  }

  /**
   * Clears this {@code Level} of all non-{@link PathType#START} {@link Path}s of the given {@link PathColor}.
   *
   * @param color The {@link PathColor} of the {@link Path}s that are to be removed.
   */
  public void clearColor(PathColor color)
  {
    for (int z = 0; z < size(); z++)
      for (int y = 0; y < size(); y++)
        for (int x = 0; x < size(); x++)
        {
          Path p = levelCube.get(x, y, z);
          if (p != null && p.getColor() == color)
            if (p.getType() != PathType.START)
              levelCube.setPath(null, x, y, z);
            else
              p.setDirection(null);
        }
  }

  /**
   * Resets the level to a start level.
   *
   * @param i is the int that represents which level to be reset to: 1 for
   *          easy, 2 for medium, and 3 for hard.
   */
  public void resetLvl(int i)
  {
    for (int z = 0; z < levelCube.size(); z++)
      for (int y = 0; y < levelCube.size(); y++)
        for (int x = 0; x < levelCube.size(); x++)
          levelCube.setPath(null, x, y, z);

    if (i == 1)
      levelCube = easy().levelCube;
    else
      if (i == 2)
        levelCube = medium().levelCube;
      else
        if (i == 3)
          levelCube = hard().levelCube;
  }

  public Level clone()
  {
    Level clone = new Level(size());
    clone.levelCube = levelCube.clone();
    return clone;
  }
}
