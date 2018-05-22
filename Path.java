
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
    /**
     * Constructor for objects of class Path
     */
    public Path(PathType inputType, PathColor inputColor)
    {
        type = inputType;
        color = inputColor;
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
