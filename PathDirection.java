
/**
 * Enumeration class PathDirection - write a description of the enum class here
 *
 * @author Kevin Chen
 * @version (version number or date here)
 */
public enum PathDirection
{
    UP, DOWN, LEFT, RIGHT, IN, OUT;//directions

  public static final PathDirection[] DIRECTIONS = new PathDirection[]{PathDirection.LEFT, PathDirection.RIGHT, PathDirection.UP, PathDirection.DOWN, PathDirection.OUT, PathDirection.IN};

    /**
     * Gets the PathDirection between two points. 
     * 
     * @param from is the start point to check direction
     * @param to is the end point to check direction
     * @return the direction from the from point to the to point
     */
    public static PathDirection get(Point3I from, Point3I to)
    {
        Point3I change = to.add(-from.getX(),-from.getY(),-from.getZ());
        if (change.getX()==0 && change.getY()==0 && change.getZ()==0)//checks if there is no change
        {
            return null;
        }
        //checks which direction to return based on the change
        if (change.getX()==0 && change.getY()==0)
            return change.getZ()>0?IN:OUT;
      if (change.getX()==0 && change.getZ()==0)
        return change.getY()>0?DOWN:UP;
      if (change.getY()==0 && change.getZ()==0)
        return change.getX()>0?RIGHT:LEFT;
        return null; // TODO UNFINISHED
    }
    
    /**
     * Moves a point based on its direction
     * 
     * @param pointToMove is the point that is being moved
     * @return is the moved point, has altered coordinates
     */
    public Point3I move(Point3I pointToMove)
    {
      switch (this)
      {
        //determines what to add based on direction
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
     * Reverses the direction of the path
     * 
     * @return is the direction to be reversed
     */
    public PathDirection reverse()
    {
      switch (this)
      {
        //returns the opposite of each input
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
