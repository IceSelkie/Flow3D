
/**
 * Enumeration class PathDirection - write a description of the enum class here
 *
 * @author (your name here)
 * @version (version number or date here)
 */
public enum PathDirection
{
    UP, DOWN, LEFT, RIGHT, IN, OUT;

    /**
     *
     */
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
}
