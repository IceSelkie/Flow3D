
/**
 * Write a description of class UI here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import javax.swing.*;

public class UI
{
    // instance variables - replace the example below with your own
    private int x;

    public static void main(String[] args)
    {
        runGame();
    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param y a sample parameter for a method
     * @return the sum of x and y
     */
    public static void runGame()
    {
        // Create display window/canvas
        // Load menu
        // wait for callback from menu to do the next thing: tutorial, game, exit, etc.
        Display display = new Display(1200,800);
        display.drawString("Flow 3D", 100,0, 250);
        display.drawString("Play", 50, 0, 0);

        display.addButton();
        // To do this, create a subclass of DisplayButton
        if(display.getButton().isPressed())
        {

            display.addButton("Back", , , 80, 50);
        }
        //menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
