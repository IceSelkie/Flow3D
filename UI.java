
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
        Display menuFrame = new Display();
        menuFrame.addButton("Play", 0, 0, 200, 150);
        menuFrame.addText("Flow 3D", 0, 250);
        
        if(menuFrame.getButton().isPressed())
        {
            menuFrame = new Display();
            menuFrame.addButton("Back", , , 80, 50); 
        }
        //menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
