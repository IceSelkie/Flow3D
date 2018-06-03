import java.awt.Point;

/**
 * class DisplayMenu
 *
 * The window data and layout for the window of the start menu.
 *
 * @author Stanley S. & Kaushik A.
 * @version 0.0 (Not Started)
 */
public class DisplayMenu extends DisplayableWindow
{
    Point firstLocation; //location of initial mouse click
    int click; //clicktype of initial mouse click
    
    /**
     * Constructor for Displaymenu
     */
    public DisplayMenu()
    {
    }

    /**
     * Display the menu screen
     * 
     * @param window window address
     */
    public void display(long window)
    {
        
    }

    /**
     *  Action once the mouse clicks certain point
     *  
     *  @param clickType
     *  @param location
     */
    public void doClick(int clickType, Point location)
    {
        //if (clickType != GLFW_MOUSE_BUTTON_1)
        //    return;
        
        click = clickType;
        firstLocation = location;   
    }

    /**
     *  Action once a certain point thats on the map is dragged
     *  
     *  @param location  
     */
    public void doDrag(Point location)
    {  
        //not used
    }

    /**
     *  Action once certain point is released from contact of map
     *  
     *  @param clickType
     *  @param location  
     */
    public void doRelease(int clickType, Point location)
    {
        if(location.equals(firstLocation) && clickType == click)
        {}
    }

    /**
     *  Action once mouse scroll action has been done
     *  
     *  @param directionIsUp
     *  @param location  
     */
    public void doScroll(boolean directionIsUp, Point location)
    {
        //not used
    }
}
