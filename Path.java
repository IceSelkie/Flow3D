/**
 * Write a description of class Path here.
 *
 * @author Kevin Chen
 * @version (a version number or a date)
 */
public class Path
{
     
    PathType type;
    PathColor color;
    PathDirection direction;
    //six enumerations of direction to determine where the next Path object exists. (.hasConnection)
    /**
     * <b>Summary</b> Constructor for objects of class Path
     */
    public Path(PathType inputType, PathColor inputColor, PathDirection inputDirection)
    {
        type = inputType;
        color = inputColor;
        direction = inputDirection;
    }
    /**
     * <b>Summary</b> Constructor for objects of Path class for initial Path location. 
     */
    public Path(PathType inputType, PathColor inputColor)
    {
        type = inputType;
        color = inputColor;
        direction = null;
    }
    /**
     * <b>Summary</b> Method for checking if Path has connection
     * @return boolean that determines if Path has connection
     */
    public boolean hasConection()
    {
        if(direction==null)
            return false;
        return true;       
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
     * <b>Summary</b> Set Path Direction
     */
    public void setDirection(PathDirection inputDirection)
    {
        direction = inputDirection;
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
    /**
     * <b>Summary</b>Returns Path direction
     * @return direction
     */
    public PathDirection getDirection()
    {
        return direction;
    }
}