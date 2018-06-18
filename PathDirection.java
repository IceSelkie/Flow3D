/**
 * enumeration PathDirection
 * <p>
 * Represents the direction of a {@link Path}'s flow. Primarily used to display a level (see {@link DisplayLevel#drawPath(Point3I, double, double yPos, double)}), and finding a flow (see {@link Level#getFlowPath(PathColor)}).
 *
 * @author Kevin C. & Stanley S.
 * @version 1.1
 */
public enum PathDirection
{
  UP, DOWN, LEFT, RIGHT, IN, OUT;

  /**
   * An array of all of the different directions.
   */
  public static final PathDirection[] DIRECTIONS = new PathDirection[]{PathDirection.LEFT, PathDirection.RIGHT, PathDirection.UP, PathDirection.DOWN, PathDirection.OUT, PathDirection.IN};

  /**
   * The direction to get to {@link Point3I} {@code to} from {@link Point3I} {@code from}.
   *
   * @param from Starting position.
   * @param to   Ending position.
   * @return Direction to go between the two points, to get from the first point to the second point.
   */
  public static PathDirection getDirection(Point3I from, Point3I to)
  {
    Point3I change = to.add(-from.getX(), -from.getY(), -from.getZ());
    if (change.getX() == 0 && change.getY() == 0 && change.getZ() == 0)
    {
      return null;
    }
    if (change.getX() == 0 && change.getY() == 0)
      return change.getZ() > 0 ? IN : OUT;
    if (change.getX() == 0 && change.getZ() == 0)
      return change.getY() > 0 ? DOWN : UP;
    if (change.getY() == 0 && change.getZ() == 0)
      return change.getX() > 0 ? RIGHT : LEFT;
    return null; // TODO: What if they are not perfectly on the same axis?
  }

  /**
   * Move a {@link Point3I} one unit in this {@code PathDirection}'s direction.
   *
   * @param pointToMove Starting point.
   * @return the point that has been moved one unit in this {@code PathDirection}'s direction.
   */
  public Point3I move(Point3I pointToMove)
  {
    switch (this)
    {
      case UP:
        return pointToMove.add(0, -1, 0);
      case DOWN:
        return pointToMove.add(0, 1, 0);
      case LEFT:
        return pointToMove.add(-1, 0, 0);
      case RIGHT:
        return pointToMove.add(1, 0, 0);
      case IN:
        return pointToMove.add(0, 0, 1);
      case OUT:
        return pointToMove.add(0, 0, -1);
      default:
        return pointToMove;
    }
  }

  /**
   * Gets the {@code PathDirection} of the opposite direction.
   *
   * @return the {@code PathDirection} that is in the opposite direction of this {@code PathDirection}'s direction.
   */
  public PathDirection reverse()
  {
    switch (this)
    {
      case UP:
        return DOWN;
      case DOWN:
        return UP;
      case LEFT:
        return RIGHT;
      case RIGHT:
        return LEFT;
      case IN:
        return OUT;
      case OUT:
        return IN;
      default:
        return null;
    }
  }
}
