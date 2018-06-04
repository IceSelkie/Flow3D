
/**
 * Write a description of class Cube here.
 *
 * @author KChen
 * @version 1.2
 */
public class Cube
{
    // instance variables - replace the example below with your own
    private Path[][][] cubePaths;
    private int size;

    /**
     * Constructor for objects of class Cube
     */
    public Cube(int sideLength)
    {
        cubePaths = new Path[sideLength][sideLength][sideLength];
        size = sideLength;
    }

    //public void setPath(int x, int y, int z);
    public void setPath(Path input, Point3I pt)
    {
        setPath(input, pt.getX(), pt.getY(), pt.getZ());
    }

    public void setPath(Path input, int x, int y, int z)
    {
        cubePaths[x][y][z] = input;
    }

    public Path getPath(Point3I pt)
    {
        if (pt != null)
            return getPath(pt.getX(), pt.getY(), pt.getZ());
        return null;
    }

    public Path getPath(int x, int y, int z)
    {
        if (x >= 0 && y >= 0 && z >= 0)
            if (x < size() && y < size() && z < size())
                return cubePaths[x][y][z];
        return null;
    }

    public int size()
    {
        return size;
    }
}