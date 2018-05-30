
/**
 * Creates levels of Flow3D.
 *
 * @author PStrat
 * @version (a version number or a date)
 */
public class Level
{
    private Cube c;
    
    public void easy()
    {
        c = new Cube(2);
        c.setPath(new Path(PathType.START, PathColor.RED), 0, 0, 0);
        c.setPath(new Path(PathType.START, PathColor.RED), 1, 1, 0);
        c.setPath(new Path(PathType.START, PathColor.ORANGE), 0, 1, 0);
        c.setPath(new Path(PathType.START, PathColor.ORANGE), 0, 1, 1);
    }

    public void medium()
    {
        c = new Cube(3);
        c.setPath(new Path(PathType.START, PathColor.GREEN), 0, 0, 0);
        c.setPath(new Path(PathType.START, PathColor.BLUE), 1, 0, 1);
        c.setPath(new Path(PathType.START, PathColor.ORANGE), 2, 2, 1);
        c.setPath(new Path(PathType.START, PathColor.BLUE), 1, 0, 2);
        c.setPath(new Path(PathType.START, PathColor.ORANGE), 0, 2, 2);
        c.setPath(new Path(PathType.START, PathColor.GREEN), 2, 0, 2);
    }

    public void hard()
    {
        c = new Cube(4);
        c.setPath(new Path(PathType.START, PathColor.MAGENTA), 0, 0, 0);
        c.setPath(new Path(PathType.START, PathColor.GREEN), 0, 1, 0);
        c.setPath(new Path(PathType.START, PathColor.RED), 2, 1, 0);
        c.setPath(new Path(PathType.START, PathColor.MAGENTA), 2, 2, 0);
        c.setPath(new Path(PathType.START, PathColor.GREEN), 0, 3, 1);
        c.setPath(new Path(PathType.START, PathColor.RED), 2, 1, 1);
        c.setPath(new Path(PathType.START, PathColor.BLUE), 1, 3, 1);
        c.setPath(new Path(PathType.START, PathColor.ORANGE), 0, 2, 2);
        c.setPath(new Path(PathType.START, PathColor.YELLOW), 1, 1, 2);
        c.setPath(new Path(PathType.START, PathColor.BLUE), 0, 0, 3);
        c.setPath(new Path(PathType.START, PathColor.YELLOW), 1, 2, 3);
        c.setPath(new Path(PathType.START, PathColor.ORANGE), 3, 3, 3);
    }
    
    public void createPath(PathColor color, int x, int y, int z)
    {
        c.setPath(new Path(PathType.PATH, color), x, y, z);
    }
    
    public void deletePath(int x, int y, int z)
    {
        c.setPath(null, x, y, z);
    }
    
    public boolean isDrawable(int x, int y, int z)
    {
        boolean ret;
        
        if(c.getPath(x,y,z).getType() == PathType.EMPTY)
        {
            ret = true;
        }
        else
        {
            ret = false;
        }
        
        return ret;
    }
}
