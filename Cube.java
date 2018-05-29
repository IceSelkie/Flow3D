
/**
 * Write a description of class Cube here.
 *
 * @author KChen
 * @version (a version number or a date)
 */
public class Cube
{
    // instance variables - replace the example below with your own
    private Path[][][] cubePaths;

    /**
     * Constructor for objects of class Cube
     */
    public Cube(int sideLength)
    {
        cubePaths = new Path[sideLength][sideLength][sideLength];
    }
    
    //public void setPath(int x, int y, int z);
    public void setPath(Path input, int x, int y, int z)
    {
        cubePaths[x][y][z] = input;
    }
    
    public Path getPath(int x, int y, int z)
    {
        return cubePaths[x][y][z];
    }
}