/**
 * class Driver
 * <p>
 * Has the main method to start the game of Flow3D.
 *
 * @author Kaushik A. & Stanley S.
 * @version 1.0
 */
public class Driver
{
  /**
   * Whether debug output in other classes should be enabled or not.
   * <p>
   * Default: {@code false}.
   */
  public static final boolean DEBUG = false;

  /**
   * Main: Let the wonderful game of <i>Flow</i> commence.
   * <p>
   * Creates a {@link Display} object that is initiated to the main menu ({@link DisplayMenu}), and immediately started ({@link Display#run()}).
   *
   * @param args Command line arguments passed to the program at start. Currently unused.
   */
  public static void main(String[] args)
  {
    new Display("Flow 3D", new DisplaySelect(Level.easy(), Level.medium(), Level.hard())).run();
  }
}