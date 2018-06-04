/**
 * class Driver
 *
 * Has the main method to start the game of Flow3D.
 *
 * @author Kaushik A.
 * @version 1.0
 */
public class Driver
{
    public static final boolean DEBUG = true;

    /**
     * Main: Let the wonderful game of <i>Flow</i> commence.
     *
     * @param args Command line arguments passed to the program at start. Currently unused.
     */
    public static void main(String[] args)
    {
        new Display(new DisplayMenu()).run();
        //Test t = new Test();
    }
}
