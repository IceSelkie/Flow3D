import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 * class Display
 * <p>
 * Uses JFrames to be able to draw objects to the screen and have interactions and buttons.
 *
 * @author Stanley S.
 * @version 1.0
 */
public class Display extends JPanel 
{
  private List<DisplayElement> elements;
  private Graphics2D lastCanvas = null;
  private List<DisplayButton> buttons = new LinkedList<>();
  private List<DisplayButton> activelyPressedButtons = new ArrayList<>();
  private boolean mouseListener = false;
  private boolean scrollListener = false;

  /**
   * Constructor for Display.
   * <p>
   * Creates a Display of the given window size.
   *
   * @param width Width of the window in pixels. Note: there is a ~5pxl buffer on each side of the window.
   * @param height Height of the window in pixels. Note: There is a ~5pxl buffer on the bottom and ~40pxl buffer (menu bar) at the top of the window.
   */
  public Display(int width, int height)
  {
    this.elements = Collections.synchronizedList(new LinkedList<DisplayElement>());
    this.setDoubleBuffered(true);
    this.setBackground(Color.white);
    new DisplayFrame(width,height,this);
  }

  /**
   * Do not call this method. This is automatically called when the display needs to be refreshed.
   * If you need to redraw the window, use {@link #repaint()}.
   * <p>
   * This method effectively draws everything onto the screen every time it is needed.
   * <p>
   * Called by the graphics thread that is automatically generated upon creating this object.
   *
   * @param graphicsContext The graphics context to draw all of the elements onto.
   */
  public final synchronized void paintComponent(Graphics graphicsContext) {
    // To be able to use Graphics2D specific methods. (Currently none)
    Graphics2D g2d = (Graphics2D)graphicsContext;

    lastCanvas = g2d;

    // Call super method to do normal processing then draw ours on top.
    super.paintComponent(g2d);

    // This method will ALWAYS be ran on another thread from main, so when we access the element array to draw things, we need to prevent concurrent modification with a {@code synchronization}.
    // Copy the data elements to an array so they can be used and referenced without holding up the addition of new features. (Slight efficiency bump. Maybe not?)
    Object[] elementsCopy;
    synchronized(elements) {
      elementsCopy = elements.toArray();
    }
    // Now draw the elements stored within the array.
    for (Object cmd : elementsCopy)
    {
      DisplayElement command = (DisplayElement) cmd;
      command.doCommand(g2d, getWidth(), getHeight());
    }
  }

  /**
   * Adds a DisplayElement to the display Queue.
   *
   * @param newElement The new element to create.
   */
  private void add(DisplayElement newElement) { elements.add(newElement); }

  /**
   * Adds a change color call to the queue.
   * <p>
   * NOTE USES LAMBDA CHILD OF {@link DisplayElement}.
   *
   * @param color The color to change it to.
   */
  public Display setColor(Color color)
  {
    add(new DisplayElement()
    {
      public void doCommand(Graphics2D canvas, int window_width, int window_height) {canvas.setColor(color);}
    });
    return this;
  }

  /**
   * Draws a rectangle and fills it with a specified color at the specified location.
   * <p>
   * NOTE USES LAMBDA CHILD OF {@link DisplayElement}.
   *
   * @param xPos The x coordinate of the center of the rectangle.
   * @param yPos The y coordinate of the center of the rectangle.
   * @param width The width of the rectangle.
   * @param height The height of the rectangle.
   */
  public void fillRect(double xPos, double yPos, double width, double height)
  {
    add(new DisplayElement()
    {
      public void doCommand(Graphics2D canvas, int window_width, int window_height)
      {
        canvas.fillRect(round(adjustX(xPos, window_width) - width / 2.0D), round(adjustY(yPos, window_height) - height / 2.0D), round(width), round(height));
      }
    });
  }

  /**
   * Takes an {@link Image} object, and displays it centered at the specified coordinates.
   * <p>
   * NOTE USES LAMBDA CHILD OF {@link DisplayElement}.
   *
   * @param image The image to draw.
   * @param xPos The x coordinate to center the image at.
   * @param yPos The y coordinate to center the image at.
   */
  public Display drawImage(Image image, double xPos, double yPos)
  {
    if (image.isLocallyRotated()||image.isLocallyCropped()||image.isLocallyScaled())
      drawImage(image.rasterizeImage(),xPos,yPos);
    else
    add(new DisplayElement()
    {
      public void doCommand(Graphics2D canvas, int window_width, int window_height)
      {
        int x = round(xPos);
        int y = round(yPos);
        int wid = image.getWidth(), hei = image.getHeight();

        Color origColor;
        Color lastColor = origColor = canvas.getColor();
        for (int h = 0; h < hei; h++)
          for (int w = 0; w < wid; w++)
          {
            Color nw = image.getC(w, h);
            if (lastColor != nw)
              canvas.setColor(nw);
            lastColor = nw;
            canvas.fillRect((window_width - wid) / 2 + x + w, (window_height - hei) / 2 - y + h, 1, 1);
          }
        canvas.setColor(origColor);
      }
    });
    return this;
  }

  /**
   * Adds a string to be drawn to the queue, in a specific size. The string will be centered. This will use the current color.
   * <p>
   * NOTE USES LAMBDA CHILD OF {@link DisplayElement}.
   *
   * @param stringToDisplay The {@link String} to display.
   * @param fontSize The size of the font to display. Value should be equal the the height of normal letters in pixels.
   * @param xPos The x coordinate to center the String at.
   * @param yPos The bottom of where the string is to be drawn. (Danglers can drop below this line.)
   */
  public Display drawString(String stringToDisplay, int fontSize, double xPos, double yPos)
  {
    add(new DisplayElement()
    {
      public void doCommand(Graphics2D canvas, int window_width, int window_height)
      {
        canvas.setFont(new Font(canvas.getFont().getFontName(),Font.PLAIN,fontSize));
        int strWidth = canvas.getFontMetrics(canvas.getFont()).stringWidth(stringToDisplay);
        canvas.drawString(stringToDisplay,(window_width-strWidth)/2+(int)xPos,window_height/2-(int)yPos);
      }
    });
    return this;
  }

  public Rectangle2D.Double stringBounds(String stringToDisplay, int fontSize, double xPos, double yPos)
  {
    if (lastCanvas==null)
      return null;
    return new Rectangle2D.Double(xPos,yPos-fontSize/2D, lastCanvas.getFontMetrics().stringWidth(stringToDisplay),fontSize);
  }

  /**
   * Adds a line to be drawn to the queue. This will use the current color.
   * <p>
   * Line is inaccurate and blocky. TODO New method to draw it better coming soon.
   * <p>
   * NOTE USES LAMBDA CHILD OF {@link DisplayElement}.
   *
   * @param x1 With {@code y1}, marks the starting location of the line.
   * @param y1 With {@code x1}, marks the starting location of the line.
   * @param x2 With {@code y2}, marks the ending location of the line.
   * @param y2 With {@code x2}, marks the ending location of the line.
   */
  @Deprecated
  public Display drawLine(double x1, double y1, double x2, double y2)
  {
    add(new DisplayElement()
    {
      public void doCommand(Graphics2D canvas, int window_width, int window_height)
      {
        canvas.draw(new Line2D.Double(adjustX(x1, window_width), adjustY(y1, window_height), adjustX(x2, window_width), adjustY(y2, window_height)));
      }
    });
    return this;
  }

  /**
   * Adds a button that can cause something to happen.
   * @param btn The button to add.
   * @param rectangle The rectangle of the trigger area for the button. Oldest button in that region is called.
   */
  public Display addButton(DisplayButton btn, Rectangle2D.Double rectangle)
  {
    enableMouseListener();
    buttons.add(btn);
    return this;
  }

  public void enableMouseListener()
  {
    mouseListener = true;
    this.addMouseListener(new MouseListener() {

      // Called when pressed then released and no movement. Called after released is called.
      public void mouseClicked(MouseEvent e)
      {
        //System.out.println("mouseClicked: ("+e.getX()+","+e.getY()+")");
      }

      // Called when mouse is beinging to be pressed.
      public void mousePressed(MouseEvent e)
      {
        //System.out.println("mousePressed: ("+e.getX()+","+e.getY()+")");
        for (DisplayButton button : buttons)
          if (isInRectangle(getLocation(e),button.getRegion()))
          {
            activelyPressedButtons.add(button);
            button.clickStart(e.getButton(), getLocation(e));
          }
      }

      // Let go of mouse click. Calls this then clicked.
      public void mouseReleased(MouseEvent e)
      {
        for (DisplayButton button : activelyPressedButtons)
          button.dragFinished(e.getButton(), getLocation(e));
        //System.out.println("mouseReleased: ("+e.getX()+","+e.getY()+")");
      }

      public void mouseEntered(MouseEvent e) { }
      public void mouseExited(MouseEvent e) { }
    });

    this.addMouseMotionListener(new MouseMotionListener() {
      // Mouse is clicked & moves.
      public void mouseDragged(MouseEvent e)
      {
        for (DisplayButton button : activelyPressedButtons)
          button.dragged(getLocation(e));
        //System.out.println("mouseDragged: ("+e.getX()+","+e.getY()+")");
      }

      // Mouse is over the JFrame and moves.
      public void mouseMoved(MouseEvent e)
      {
        //System.out.println("mouseMoved: ("+e.getX()+","+e.getY()+")");
      }
    });
  }

  public boolean isInRectangle(Point2D.Double location, Rectangle2D.Double boundingBox)
  {
    return (Math.abs(location.getX()-boundingBox.getX())<boundingBox.getWidth()/2D && Math.abs(location.getY()-boundingBox.getY())<boundingBox.getHeight()/2D);
  }

  public Point2D.Double getLocation(MouseEvent e)
  {
    return new Point2D.Double(e.getX()-getWidth()/2D,getHeight()/2-e.getY());
  }

  /**
   * Clears the screen of all elements back to solid white, and removes all buttons & listeners.
   */
  public void clearScreen()
  {
    elements.clear();
    mouseListener = false;
    scrollListener = false;
    buttons.clear();
    activelyPressedButtons.clear();
  }



  // **************** INNER-CLASSES BELOW **************** //
  /**
   * class DisplayFrame
   * <p>
   * The actual JFrame object that carries the JPanel object of {@link Display}. Only used in {@link Display}'s creation.
   *
   * @author Stanley S.
   * @version 1.0
   */
  protected static class DisplayFrame extends JFrame
  {
    /** The {@link Display} panel inside this JFrame. There is only ever one. */
    protected Display panel;
    /** The object that stores where everything is. */
    protected GridBagLayout gbLayout = new GridBagLayout();
    protected GridBagConstraints gbConstraints = new GridBagConstraints();
    /** The menubar for menu options. Currently unused. */
    protected JMenuBar menuBar = new JMenuBar();

    /**
     * A PRIVATE constructor for DisplayFrame.
     * <p>
     * Creates a blank DisplayFrame object, ready to be loaded with other stuff.
     */
    private DisplayFrame() {
      this.getContentPane().setLayout(this.gbLayout);
      this.gbConstraints.weightx = 100.0D;
      this.gbConstraints.weighty = 100.0D;
      this.gbConstraints.insets = new Insets(1, 2, 1, 2);
      this.setTitle("DisplayFrame");
      this.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent evt) {
          System.exit(0);
        }
      });
    }

    /**
     * The primary constructor for DisplayFrame.
     * <p>
     * Creates a DisplayFrame object with the given size, and {@link Display} as the {@link JPanel}.
     *
     * @param width Width of the window in pixels. Note: there is a ~5pxl buffer on each side of the window.
     * @param height Height of the window in pixels. Note: There is a ~5pxl buffer on the bottom and ~40pxl buffer (menu bar) at the top of the window.
     * @param displayPanel The {@link Display} object that will be this {@link JFrame}'s {@link JPanel}.
     */
    public DisplayFrame(int width, int height, Display displayPanel) {
      this();
      this.panel = this.addPanel(displayPanel, 1, 1, 1, 1);
      this.setSize(width, height);
      this.setVisible(true);
    }

    /**
     * Changes the window to the specified size.
     *
     * @param width The new width of the window.
     * @param height The new height of the window.
     */
    public void setSize(int width, int height) {
      super.setSize(width, height);
      if (this.menuBar.getMenuCount() > 0) {
        this.setJMenuBar(this.menuBar);
      }
      this.setVisible(true);
    }

    /**
     * When a MenuItem is clicked, this is called. Extend this class to override this and add an implementation.
     *
     * @param clickedMenuItem The {@link JMenuItem} that was clicked.
     */
    public void menuItemSelected(JMenuItem clickedMenuItem) {
      System.err.println("'menuItemSelected' method required");
    }

    /**
     * Adds a MenuItem to the menu under the specified Menu drop down, and returns that object to be used with {@link #menuItemSelected(JMenuItem)} so the correct action can be taken.
     *
     * @param menuName The dropdown to put this option in.
     * @param itemName The name of the button/element
     * @return The new {@link JMenuItem} to be linked to an action.
     */
    public JMenuItem addMenuItem(String menuName, String itemName) {
      JMenu menu = null;

      int i;
      for(i = 0; i < this.menuBar.getMenuCount(); ++i) {
        menu = this.menuBar.getMenu(i);
        if (menuName.equals(menu.getText())) {
          break;
        }
      }

      if (i == this.menuBar.getMenuCount()) {
        menu = new JMenu(menuName);
        this.menuBar.add(menu);
      }

      JMenuItem menuItem = new JMenuItem(itemName);
      menuItem.addActionListener(new DisplayFrameMenuListener(this));
      menu.add(menuItem);
      return menuItem;
    }

    /**
     * Adds a panel the the JFrame to display.
     *
     * @param panel The Display/panel to add
     * @param row Where to display it (row)
     * @param col Where to display it (column)
     * @param width Width of the panel to display.
     * @param height Height of the panel to display.
     * @return The display back.
     */
    public Display addPanel(Display panel, int row, int col, int width, int height) {
      this.gbConstraints.fill = 1;
      this.gbConstraints.anchor = 18;
      this.gbConstraints.weightx = 500.0D;
      this.gbConstraints.weighty = 500.0D;
      this.gbConstraints.gridx = col - 1;
      this.gbConstraints.gridy = row - 1;
      this.gbConstraints.gridwidth = width;
      this.gbConstraints.gridheight = height;
      this.gbLayout.setConstraints(panel, this.gbConstraints);
      this.getContentPane().add(panel);
      this.gbConstraints.weightx = 100.0D;
      this.gbConstraints.weighty = 100.0D;
      return panel;
    }

    /**
     * inner-class DisplayFrameMenuListener
     * <p>
     * A listener for MenuItems or something like that.
     *
     * @author N/A (Generated)
     * @version 1.0
     */
    public static class DisplayFrameMenuListener implements ActionListener
    {
      DisplayFrame myFrame;

      public DisplayFrameMenuListener(DisplayFrame frm)
      {
        this.myFrame = frm;
      }

      public void actionPerformed(ActionEvent e)
      {
        this.myFrame.menuItemSelected((JMenuItem) e.getSource());
      }
    }
  }

  /**
   * Adjusts a cartesian x coordinate to the graphics's coordinate system from top left.
   *
   * @param x The x position to adjust.
   * @param width The width of the window.
   * @return The adjusted x coordinate.
   */
  public static double adjustX(double x, double width) {
    return x + width / 2.0D;
  }

  /**
   * Adjusts a cartesian y coordinate to the graphics's coordinate system from top left.
   *
   * @param y The y position to adjust.
   * @param height The height of the window.
   * @return The adjusted y coordinate.
   */
  public static double adjustY(double y, double height) {
    return height / 2.0D - y;
  }

  /**
   * Rounds a number to the nearest whole number.
   * <p>
   * Equivalent to {@code Math.round(val)}.
   *
   * @param val Value to round.
   * @return The rounded value.
   */
  public static int round(double val) {return (int)Math.round(val);}

  /**
   * abstract class DisplayElement
   * <p>
   * An element in the list within Display to represent a thing to display onto the display.
   * <p>
   * Used by {@link Display}.
   */
  public abstract class DisplayElement
  {
    public DisplayElement() {}

    /**
     * Executes the command and draws the object onto the {@link Display}.
     * @param canvas The {@link Graphics} context to draw the object onto.
     * @param window_width The width of this {@link Display}, so the object can be positioned correctly (relative to the middle instead of top left).
     * @param window_height The height of this {@link Display}, so the object can be positioned correctly (relative to the middle instead of top left).
     */
    public abstract void doCommand(Graphics2D canvas, int window_width, int window_height);
  }



  // **************** RETIRED METHODS BELOW THIS POINT **************** //

  /*public Display drawImage(DisplayableImage img, double xPos, double yPos)
  {
    add(new DisplayElement(new Object[]{img, xPos, yPos})
    {
      public void doCommand(Graphics2D canvas, int window_width, int window_height)
      {
        Object[] objarr = (Object[]) data;
        DisplayableImage image = (DisplayableImage) objarr[0];
        AffineTransform at = image.getAffine();
        double xPos = (double) objarr[1];
        double yPos = (double) objarr[2];

        at.setToTranslation((window_width - image.getWidth()) / 2D + xPos, (window_height - image.getHeight()) / 2D - yPos);

        canvas.drawImage(image, at, (ImageObserver) null);
      }
    });
    return this;
  }*/

  /*
  public void drawString(String str, double xPos, double yPos) {
    this.elements.add(new CommandDrawString(str, xPos, yPos));
  }

  public void drawCircle(double xPos, double yPos, double radius) {
    this.elements.add(new CommandDrawCircle(xPos, yPos, radius));
  }

  public void drawOval(double xPos, double yPos, double width, double height) {
    this.elements.add(new CommandDrawOval(xPos, yPos, width, height));
  }

  public void fillOval(double xPos, double yPos, double width, double height) {
    this.elements.add(new CommandFillOval(xPos, yPos, width, height));
  }

  public void drawRect(double xPos, double yPos, double width, double height) {
    this.elements.add(new CommandDrawRect(xPos, yPos, width, height));
  }
  */
}
