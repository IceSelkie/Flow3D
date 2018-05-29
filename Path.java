
/**
 * Write a description of class Path here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Path
{
    PathType type;
    PathColor color;
    Path next;
    Path previous;
    /**
     * <b>Summary</b> Constructor for objects of class Path
     */
    public Path(PathType inputType, PathColor inputColor, Path inputNext, Path inputPrevious)
    {
        type = inputType;
        color = inputColor;
        next = inputNext;
        previous = inputPrevious;
    }
    /**
     * <b>Summary</b> Constructor for objects of Path class for initial Path location. 
     */
    public Path(PathType inputType, PathColor inputColor)
    {
        type = inputType;
        color = inputColor;
        next = previous = null;
    }
    /**
     * <b>Summary</b> Method for inserting new Path.
     */
    public void insert(Path input, PathType inputType, PathColor inputColor)
    {
        
    }
    /**
     * <b>Summary</b> Set Path Type
     */
    public void setType(PathType inputType)
    {
        type = inputType;
    }
    /**
     * <b>Summary</b> Set Path Color
     */
    public void setColor(PathColor inputColor)
    {
        color = inputColor;
    }
    /**
     * <b>Summary</b>Returns Path Type
     * @return type
     */
    public PathType getType()
    {
        return type;
    }
    /**
     * <b>Summary</b>Returns Path color
     * @return type
     */
    public PathColor getColor()
    {
        return color;
    }
}