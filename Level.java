import java.util.*;
/**
 * Creates levels of Flow3D.
 *
 * @author PStrat
 * @version (a version number or a date)
 */
public class Level
{
    private Cube c;
    private ArrayList<Path> draw = new ArrayList<Path>();
    private Point3I start, cur, end;
    boolean select;
    
    /**
     * Creates an easy level. Hard codes start Paths and their respective colors
     * in. Every color comes in pairs of two so they can be linked in the game.
     */
    public void easy()
    {
        c = new Cube(2);
        c.setPath(new Path(PathType.START, PathColor.RED), 0, 0, 0);
        c.setPath(new Path(PathType.START, PathColor.RED), 0, 1, 1);
        c.setPath(new Path(PathType.START, PathColor.ORANGE), 0, 1, 0);
        c.setPath(new Path(PathType.START, PathColor.ORANGE), 1, 0, 1);
    }
    
    /**
     * Creates an medium level. Hard codes start Paths and their respective colors
     * in. Every color comes in pairs of two so they can be linked in the game.
     */
    public void medium()
    {
        c = new Cube(3);
        c.setPath(new Path(PathType.START, PathColor.GREEN), 0, 0, 0);
        c.setPath(new Path(PathType.START, PathColor.BLUE), 1, 0, 1);
        c.setPath(new Path(PathType.START, PathColor.ORANGE), 1, 2, 2);
        c.setPath(new Path(PathType.START, PathColor.BLUE), 2, 0, 1);
        c.setPath(new Path(PathType.START, PathColor.ORANGE), 2, 2, 0);
        c.setPath(new Path(PathType.START, PathColor.GREEN), 2, 0, 2);
    }
    
    /**
     * Creates an hard level. Hard codes start Paths and their respective colors
     * in. Every color comes in pairs of two so they can be linked in the game.
     */
    public void hard()
    {
        c = new Cube(4);
        c.setPath(new Path(PathType.START, PathColor.MAGENTA), 0, 0, 0);
        c.setPath(new Path(PathType.START, PathColor.GREEN), 0, 1, 0);
        c.setPath(new Path(PathType.START, PathColor.RED), 0, 1, 2);
        c.setPath(new Path(PathType.START, PathColor.MAGENTA), 0, 2, 2);
        c.setPath(new Path(PathType.START, PathColor.GREEN), 1, 3, 0);
        c.setPath(new Path(PathType.START, PathColor.RED), 1, 1, 2);
        c.setPath(new Path(PathType.START, PathColor.BLUE), 1, 3, 1);
        c.setPath(new Path(PathType.START, PathColor.ORANGE), 2, 2, 0);
        c.setPath(new Path(PathType.START, PathColor.YELLOW), 2, 1, 1);
        c.setPath(new Path(PathType.START, PathColor.BLUE), 3, 0, 0);
        c.setPath(new Path(PathType.START, PathColor.YELLOW), 3, 2, 1);
        c.setPath(new Path(PathType.START, PathColor.ORANGE), 3, 3, 3);
    }
    
    /**
     * @return Returns the size of the level, which is determined by the size
     * of the cube object.
     */
    public int size()
    {
        return c.size();
    }
    
    /**
     * Checks if a position passed as an arguement is legal by checking if the 
     * point passed is inside the level.
     * @param p is the point that we want to check the legality of.
     * @return ret is the boolean result of the legal test
     */
    public boolean checkLegalPos(Point3I p)
    {
        boolean ret = false;
        
        if(p.getX() >= 0 && p.getY() >= 0 && p.getZ() >= 0 && p.getX() < c.size() && 
           p.getY() < c.size() && p.getZ() < c.size())
        {
            ret = true;
        }
        
        return ret;
    }
    
    /*
    
    public boolean select(Point3I p)
    {
        if(checkLegalPos(p) && c.getPath(p) != null && //checks if the point is a valid start location
           c.getPath(p).getType() == PathType.START)
        {
            select = true;
            start.setLocation(p);
            cur.setLocation(p);
        }
        else
        {
            select = false;
        }
        
        return select;
    }
    
    
    
    
    public int[] findEnd(PathColor color)
    {
        for(int z = 0; z < c.size(); z++)
        {
            for(int y = 0; y < c.size(); y++)
            {
                for(int x = 0; x < c.size(); x++)
                {
                    if(c.getPath(new Point3I(x,y,z)).getColor() == color && z != start.getZ()
                       && y != start.getY() && x != start.getX())
                    {
                        end = new Point3I(x,y,z);
                    }
                }
            }
        }
        int[] ret = {end.getZ(),end.getY(),end.getZ()};
        return ret;
    }
    
    
    public void draw(Point3I p)
    {
        if(checkLegalPos(p) && c.getPath(p) == null)
        {
            if((p.getX() == cur.getX() + 1 && p.getY() == cur.getY() && p.getZ() == cur.getZ()) || (p.getX() == cur.getX() - 1 && p.getY() == cur.getY() && p.getZ() == cur.getZ())
               || (p.getX() == cur.getX() && p.getY() == cur.getY() - 1 && p.getZ() == cur.getZ()) || (p.getX() == cur.getX() && p.getY() == cur.getY() + 1 && p.getX() == cur.getZ())
               || (p.getX() == cur.getX() && p.getY() == cur.getY() && p.getZ() == cur.getZ() - 1) || (p.getX() == cur.getX() && p.getY() == cur.getY() && p.getZ() == cur.getZ() + 1))
            {
                c.setPath(new Path(PathType.PATH, c.getPath(p).getColor()), p.getZ(), p.getY(), p.getX());
                cur.setLocation(p);
            }
        }
    }
    */
    
    public Path getSelectedPath()
    {
        return c.getPath(start);
    }
    
    public boolean checkConnected()
    {
        boolean ret = false;
        
        if(cur.equals(end))
        {
            ret = true;
        }
        
        return ret;
    }
    
    public boolean checkWin()
    {
        boolean ret = true; 
        
        for(int z = 0; z < c.size(); z++)
        {
            for(int y = 0; y < c.size(); y++)
            {
                for(int x = 0; x < c.size(); x++)
                {
                    if(c.getPath(new Point3I(x,y,z)) == null)
                    {
                        ret = false;
                    }
                }
            }
        }
        
        return ret;
    }
    
    public void reset(PathColor color)
    {
        for(int z = 0; z < c.size(); z++)
        {
            for(int y = 0; y < c.size(); y++)
            {
                for(int x = 0; x < c.size(); x++)
                {
                    if(c.getPath(new Point3I(x,y,z)) != null)
                    {
                        if(c.getPath(new Point3I(x,y,z)).getType() == PathType.PATH &&
                           c.getPath(new Point3I(x,y,z)).getColor() != color)
                        {
                            c.setPath(null,z,y,x);
                        }
                    }
                }
            }
        }
    }
}
    

