import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * class DisplaySelect
 * <p>
 * The window data and layout for the window of the menu to select the different layers to play.
 *
 * @author Kaushik A. & Kevin C.  
 * @version 0.0 (Not Started)
 */
public class DisplaySelect extends DisplayableWindow
{
    //window size
    protected static final int displayLocations_WindowWidth = 800;
    protected static final int displayLocations_WindowHeight = 600;
    protected static final int displayLocations_WindowCenterX = displayLocations_WindowWidth / 2;
    protected static final int displayLocations_WindowCenterY = displayLocations_WindowHeight / 2;
    protected static final int displayLocations_ButtonWidth = 200;
    protected static final int displayLocations_ButtonHeight = 50;
    protected static final int displayLocations_ButtonBuffer = 50;
    protected static final int displayLocations_ButtonBufferedSizeY = displayLocations_ButtonHeight+displayLocations_ButtonBuffer;
    protected static final int displayLocations_ButtonSideLeft = displayLocations_WindowCenterX-(displayLocations_ButtonWidth/2);
    protected static final int displayLocations_ButtonSideRight = displayLocations_WindowCenterX+(displayLocations_ButtonWidth/2);

    private ArrayList<Level> levels;

    // For fade effects on opening this window.
    private boolean fadeIn;
    private int fade;

    private int clicked = -1; // Clicked window
    private float[] hoverPhase;
   /**
   * Constructor for DisplaySelect
   * <p>
   * Instantiates ArrayList levels containing Level objects. 
   *
   * @param levels ArrayList or singular Level Object 
   */
    public DisplaySelect(Level... levels)
    {
        this(new ArrayList<Level>(Arrays.asList(levels)));
    }
    /**
     * Constructor for Display Select
     * <p>
     * Instantiates levels with parameter levels. Sets int fade to 60. Sets boolean fadeIn to true. float array 
     * hoverPhase recieves new float array of same size as levels ArrayList.
     * 
     * @param levels the recieved ArrayList of Level objects. 
     */
    public DisplaySelect(ArrayList<Level> levels)
    {
        this.levels = levels;
        fade = 60;
        fadeIn = true;
        hoverPhase = new float[levels.size()];
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
     * When the user clicks and holds, then moves the mouse,
     * this will be called. Warning: This is asynchronous, and
     * can happen at any time, EVEN WHEN OTHER METHODS ARE
     * INPROGRESS.
     * Not used within DisplaySelect
     *
     * @param location The point on the screen where the mouse was moved to.
     */
    public void doDrag(Point location)
    {
        //not essential but method signature required because exists in abstract class
    }

    /**
     * When the user clicks, then lets go, this will be
     * called. Warning: This is asynchronous, and can happen
     * at any time, EVEN WHEN OTHER METHODS ARE INPROGRESS.
     *
     * @param clickType Enumeration of which mouse button was used to do the click. See {@link GLFW#GLFW_MOUSE_BUTTON_1} through {@link GLFW#GLFW_MOUSE_BUTTON_8}
     * @param location  The point on the screen where the mouse is, upon being released.
     */
    public void doRelease(int clickType, Point location)
    {
        if (clicked!=-1)
        {
            int buttonOver = getOverButton(location);
            if (buttonOver == clicked)
                fadeIn=false;
        }
    }

    /**
     * Scroll is not used in DisplaySelect.
     * Inherited from {@link DisplayableWindow}.
     */
    public void doScroll(boolean directionIsUp, Point location)
    {
        return;
    }

    /**
     * Abstract method to be implemented by subclasses to do the actual rendering of the window.
     *
     * @param window The GLFWwindow pointer that holds the window's data. Needed to do any displaying.
     */
    public void display(long window)
    {
        int overButton = getOverButton(Display.getCursorLocationOrigin(Display.w));
        if (fadeIn && fade>0)
            fade--;
        if (fade==60)
            Display.setDisplay(new DisplayLevel(levels.get(clicked)));
        if (clicked!=-1 && !fadeIn)
            fade++;
            

        int numLevels = levels.size();
        for (int i = 0; i < numLevels; i++)
        {
            if (overButton==i)
                hoverPhase[i]=Math.min(1,hoverPhase[i]+1f/30);
            else
                hoverPhase[i]=Math.max(0,hoverPhase[i]-1f/30);

            // Button outline. depth = .5; Changes when clicked. Cuz that will look cool.
            if (clicked != i)
            //System.out.println("Attempting: new Color(" + (191 * (1 - hoverPhase[i]) + 97 * (hoverPhase[i])) + ", " + (191 * (1 - hoverPhase[i]) + 210 * (hoverPhase[i])) + ", " + (191 * (1 - hoverPhase[i]) + 97 * (hoverPhase[i])) + ")");
                Display.setColor3(new Color((int) (191 * (1 - hoverPhase[i]) + 97 * (hoverPhase[i])), (int) (191 * (1 - hoverPhase[i]) + 210 * (hoverPhase[i])), (int) (191 * (1 - hoverPhase[i]) + 97 * (hoverPhase[i]))));
            else
                Display.setColor3(new Color(127, 127, 255));
            Display.drawRectangleOr(displayLocations_WindowCenterX, displayLocations_WindowCenterY + displayLocations_ButtonBufferedSizeY * i - displayLocations_ButtonBuffer * numLevels, displayLocations_ButtonWidth, displayLocations_ButtonHeight, false);

            // Button background. depth = -.9;
            Display.setColor3(new Color(0, 0, 95));
            Display.drawRectangleOr(displayLocations_WindowCenterX, displayLocations_WindowCenterY + displayLocations_ButtonBufferedSizeY * i - displayLocations_ButtonBuffer * numLevels, displayLocations_ButtonWidth, displayLocations_ButtonHeight, true);

            // The contents of the button: ie: text and color, etc.
            buttonDisplay(i, levels.get(i));
        }

        // Fade in and out
        Display.enableTransparency();
        Display.setColor4(0,0,31,(int)(255*(fade/60D)));
        Display.drawRectangleOr(400,300,800,600,true);
        Display.disableTransparency();
    }
    /*Private Method. 
       Displays buttons (Easy Medium Hard)
       Called within display method.
       */
    private void buttonDisplay(int numFromBottom, Level level)
    {
        switch (numFromBottom)
        {
            case 0:
            Display.setColor3(PathColor.GREEN.toColor());
            break;
            case 1:
            Display.setColor3(PathColor.YELLOW.toColor());
            break;
            case 2:
            Display.setColor3(PathColor.RED.toColor());
            break;
            //default:
        }
        Display.drawRectangleOr(displayLocations_WindowCenterX,displayLocations_WindowCenterY+displayLocations_ButtonBufferedSizeY*numFromBottom-displayLocations_ButtonBuffer*levels.size(),(displayLocations_ButtonWidth)/2,(displayLocations_ButtonHeight)/2,true);
    }
    /* Private Method.
     * Determines hitboxes for buttons (Easy Medium Hard)
     * Called within display method. 
     */
    private int getOverButton(Point location)
    {
      int x = (int) location.getX();
      int y = (int) location.getY();
      if (x >= displayLocations_ButtonSideLeft && x <= displayLocations_ButtonSideRight)
      {
        y = (displayLocations_WindowCenterY + displayLocations_ButtonBuffer * levels.size() / 2) - y;
        if (y > 0 && y % displayLocations_ButtonBufferedSizeY < displayLocations_ButtonHeight)
          return levels.size() - (y / displayLocations_ButtonBufferedSizeY) - 1;
      }
      return -1;
    }
}
