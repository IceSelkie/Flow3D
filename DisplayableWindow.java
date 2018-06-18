import org.lwjgl.system.NativeType;

import java.awt.*;

/**
 * abstract class DisplayableWindow
 * <p>
 * Interface for the window data and layout for a window,
 * to display each window.
 *
 * @author Stanley S. & Kaushik A.
 * @version 1.1
 */
public abstract class DisplayableWindow
{
  public DisplayableWindow()
  {}

  /**
   * When the user clicks a location on the window, this
   * will be called. Warning: This is asynchronous, and
   * can happen at any time, EVEN WHEN OTHER METHODS ARE
   * INPROGRESS.
   *
   * @param clickType Enumeration of which mouse button was
   *                  used to do the click. See {@link org.lwjgl.glfw.GLFW#GLFW_MOUSE_BUTTON_1}
   *                  through {@link org.lwjgl.glfw.GLFW#GLFW_MOUSE_BUTTON_8}.
   * @param location  The point on the screen that was clicked.
   */
  public abstract void doClick(int clickType, Point location);

  /**
   * When the user clicks and holds, then moves the mouse,
   * this will be called. Warning: This is asynchronous, and
   * can happen at any time, EVEN WHEN OTHER METHODS ARE
   * INPROGRESS.
   *
   * @param location The point on the screen where the mouse
   *                 was moved to.
   */
  public void doDrag(Point location)
  {}

  /**
   * When the user clicks, then lets go, this will be
   * called. Warning: This is asynchronous, and can happen
   * at any time, EVEN WHEN OTHER METHODS ARE INPROGRESS.
   *
   * @param clickType Enumeration of which mouse button was
   *                  used to do the click. See {@link org.lwjgl.glfw.GLFW#GLFW_MOUSE_BUTTON_1}
   *                  through {@link org.lwjgl.glfw.GLFW#GLFW_MOUSE_BUTTON_8}.
   * @param location  The point on the screen where the
   *                  mouse is, upon being released.
   */
  public abstract void doRelease(int clickType, Point location);

  /**
   * When the user attempts to scroll, this will be
   * called. Warning: This is asynchronous, and can happen
   * at any time, EVEN WHEN OTHER METHODS ARE INPROGRESS.
   *
   * @param directionIsUp This will be {@code true}, if the
   *                      scroll is upward.
   * @param location      The point on the screen where the
   *                      mouse is, when it is scrolled.
   */
  public void doScroll(boolean directionIsUp, Point location)
  {}

  /**
   * Abstract method to be implemented by subclasses to do
   * the actual rendering of the window.
   *
   * @param window The GLFWwindow pointer that holds the
   *               window's data. Needed to do any
   *               displaying.
   */
  public abstract void display(@NativeType("GLFWwindow *") long window);

  /**
   * When the user presses a key, this will be called.
   * Warning: This is asynchronous, and can happen at any
   * time, EVEN WHEN OTHER METHODS ARE INPROGRESS.
   *
   * @param key    The key that is pressed. See {@link org.lwjgl.glfw.GLFW#GLFW_KEY_SPACE} through {@link org.lwjgl.glfw.GLFW#GLFW_KEY_WORLD_2}
   *               and {@link org.lwjgl.glfw.GLFW#GLFW_KEY_ESCAPE} through {@link org.lwjgl.glfw.GLFW#GLFW_KEY_LAST}
   *               and {@link org.lwjgl.glfw.GLFW#GLFW_MOD_SHIFT} through {@link org.lwjgl.glfw.GLFW#GLFW_MOD_NUM_LOCK}
   *               and {@link org.lwjgl.glfw.GLFW#GLFW_KEY_UNKNOWN}.
   * @param action The action taken. One of: {@link org.lwjgl.glfw.GLFW#GLFW_PRESS},
   *               {@link org.lwjgl.glfw.GLFW#GLFW_RELEASE}, or {@link org.lwjgl.glfw.GLFW#GLFW_REPEAT}.
   */
  public void keyPress(int key, int action)
  {}
}
