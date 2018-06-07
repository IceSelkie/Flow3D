/**
 * class Driver
 * <p>
 * Has the main method to start the game of Flow3D.
 *
 * @author Kaushik Arunkumar
 * @version 1.0
 */
public class Driver
{
  public static final boolean DEBUG = true;

  /**
   * Main: Let the wonderful game of <i>Flow</i> commence.
   * <p>
   * Creates a {@link Display} object that is initiated to the main menu ({@link DisplayMenu}), and immediately started ({@link Display#run()}).
   *
   * @param args Command line arguments passed to the program at start. Currently unused.
   */
  public static void main(String[] args)
  {
    new Display(new DisplaySelect(Level.hard(),Level.medium(),Level.easy())).run();
  }
}