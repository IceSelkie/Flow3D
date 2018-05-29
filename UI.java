
/**
 * Write a description of class UI here.
 *
 * @author PStrat
 * @version (a version number or a date)
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UI extends JFrame
{   
    private JLabel label;
    private JFrame titleS, levelSelectS, easyS, mediumS, hardS;
    private JPanel titleP, levelSelectP, easyP, mediumP, hardP;
    private JButton play, easyB, mediumB, hardB;
    
    public UI()
    {
        gui();
    }
    
    private void gui()
    {
//<<<<<<< HEAD
        titleS = new JFrame("Flow 3D");        
        titleS.setVisible(true);
        titleS.setSize(800,800);
        titleS.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        levelSelectS = new JFrame("Level Select");
        levelSelectS.setVisible(false);
        levelSelectS.setSize(800,800);
        levelSelectS.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        easyS = new JFrame("Easy");
        easyS.setVisible(false);
        easyS.setSize(800,800);
        
        mediumS = new JFrame("Medium");
        mediumS.setVisible(false);
        mediumS.setSize(800,800);
        
        hardS = new JFrame("Hard");
        hardS.setVisible(false);
        hardS.setSize(800,800);
        
        titleP = new JPanel(new GridBagLayout());
        titleP.setBackground(Color.BLUE);
        
        levelSelectP = new JPanel(new GridBagLayout());
        levelSelectP.setBackground(Color.PINK);
        
        easyP = new JPanel(new GridBagLayout());
        easyP.setBackground(Color.WHITE);
        
        mediumP = new JPanel(new GridBagLayout());
        mediumP.setBackground(Color.WHITE);
        
        hardP = new JPanel(new GridBagLayout());
        hardP.setBackground(Color.WHITE);
        
        play = new JButton("Play Flow3D");
        easyB = new JButton("Easy");
        mediumB = new JButton("Medium");
        hardB = new JButton("Hard");
        
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridx = 0;
        c.gridy = 1;
        
        titleP.add(play);        
        titleS.add(titleP);
        
        levelSelectP.add(easyB);
        levelSelectP.add(mediumB);
        levelSelectP.add(hardB);
        levelSelectS.add(levelSelectP);
        
        play.addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent e)
                {
                    levelSelectS.setVisible(true);
                    titleS.dispose();
                }
            });
        
        easyB.addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent e)
                {
                    easyS.setVisible(true);
                }
            });
            
        mediumB.addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent e)
                {
                    mediumS.setVisible(true);
                }
            });
            
        hardB.addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent e)
                {
                    hardS.setVisible(true);
                }
            });
/*=======
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
>>>>>>> a2ef72ce2d96052799e2fe569fe536a598008cfa*/
    }
}
