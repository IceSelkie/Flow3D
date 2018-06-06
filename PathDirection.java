
/**
 * Enumeration class PathDirection - write a description of the enum class here
 *
 * @author (your name here)
 * @version (version number or date here)
 */
public enum PathDirection
{
    UP, DOWN, LEFT, RIGHT, IN, OUT;

  public static final PathDirection[] DIRECTIONS = new PathDirection[]{PathDirection.LEFT, PathDirection.RIGHT, PathDirection.UP, PathDirection.DOWN, PathDirection.OUT, PathDirection.IN};


    public static PathDirection get(Point3I from, Point3I to)
    {
        Point3I change = to.add(-from.getX(),-from.getY(),-from.getZ());
        if (change.getX()==0 && change.getY()==0 && change.getZ()==0)
        {
            return null;
        }
        if (change.getX()==0 && change.getY()==0)
            return change.getZ()>0?IN:OUT;
      if (change.getX()==0 && change.getZ()==0)
        return change.getY()>0?DOWN:UP;
      if (change.getY()==0 && change.getZ()==0)
        return change.getX()>0?RIGHT:LEFT;
        return null; // TODO UNFINISHED
    }

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
