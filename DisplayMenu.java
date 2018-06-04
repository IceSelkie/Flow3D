import org.lwjgl.glfw.GLFW;

import java.awt.Point;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

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
    protected static final int displayLocations_WindowWidth = 800;
    protected static final int displayLocations_WindowHeight = 600;
    protected static final int displayLocations_WindowCenterX = displayLocations_WindowWidth / 2;
    protected static final int displayLocations_WindowCenterY = displayLocations_WindowHeight / 2;
    protected static final int displayLocations_ButtonWidth = 200;
    protected static final int displayLocations_ButtonHeight = 50;
    protected static final int displayLocations_ButtonBuffer = 50;
    protected static final int displayLocations_ButtonBufferedSizeY = displayLocations_ButtonHeight + displayLocations_ButtonBuffer;
    protected static final int displayLocations_ButtonSideLeft = displayLocations_WindowCenterX - (displayLocations_ButtonWidth/2);
    protected static final int displayLocations_ButtonSideRight = displayLocations_WindowCenterX + (displayLocations_ButtonWidth/2);

    private Point firstLocation; //location of initial mouse click
    private int click; //clicktype of initial mouse click
    private int clicked = -1; // Clicked window

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
     * When the user clicks a location on the window, this
     * will be called. Warning: This is asynchronous, and
     * can happen at any time, EVEN WHEN OTHER METHODS ARE
     * INPROGRESS.
     *
     * @param clickType Enumeration of which mouse button was used to do the click. See {@link GLFW#GLFW_MOUSE_BUTTON_1} through {@link GLFW#GLFW_MOUSE_BUTTON_8}
     * @param location  The point on the screen that was clicked.
     */
    public void doClick(int clickType, Point location)
    {
        int buttonOver = getOverButton(location);
        if (buttonOver!=-1)
            clicked = buttonOver;
    }
    
    /**
     *  Checks to see if cursor is within button limits
     *  
     *  @param location location of cursor
     *  @return 
     *  @return -1 false
     */
    private int getOverButton(Point location)
    {
        int x = (int)location.getX();
        int y = (int)location.getY();
        if (x >= displayLocations_ButtonSideLeft && x <= displayLocations_ButtonSideRight)
        {
//            y = (displayLocations_WindowCenterY + displayLocations_ButtonBuffer * levels.size() / 2 - displayLocations_ButtonHeight / 2) - y;
            if (y > 0 && y % displayLocations_ButtonBufferedSizeY < displayLocations_ButtonHeight)
                return y / displayLocations_ButtonBufferedSizeY;
        }
        return -1;
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
