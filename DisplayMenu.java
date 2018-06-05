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
    protected static final int displayLocations_ButtonSideLeft = displayLocations_WindowCenterX - (displayLocations_ButtonWidth / 2);
    protected static final int displayLocations_ButtonSideRight = displayLocations_WindowCenterX + (displayLocations_ButtonWidth / 2);
    protected static final int displayLocations_ButtonSideBottom = displayLocations_WindowCenterY - (displayLocations_ButtonHeight / 2);
    protected static final int displayLocations_ButtonSideTop = displayLocations_WindowCenterX + (displayLocations_ButtonHeight / 2);
    
    private Point firstLocation; //location of initial mouse click
    private int click; //clicktype of initial mouse click
    private int clicked = -1; // Clicked window
    private int fade;
    private boolean fadeIn;

    /**
     * Constructor for Displaymenu
     */
    public DisplayMenu()
    {
        fade = 60;
        fadeIn = true;
    }

    /**
     * Display the menu screen
     * 
     * @param window The GLFWwindow pointer that holds the window's data. Needed to do any displaying.
     */
    public void display(long window)
    {
        int overButton = getOverButton(Display.getCursorLocationOrigin(Display.w));
        if (fadeIn && fade>0)
            fade--;
        if (fade==60)                  //        /--------------------
            Display.setDisplay(new DisplaySelect());//what is the parameter for the new DisplaySelect 
        if (clicked!=-1 && !fadeIn)
            fade++;
            
        //need to put in the button display
            
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
            if (y >= displayLocations_ButtonSideBottom && y <= displayLocations_ButtonSideTop)
                return 1;
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
        //not used
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
