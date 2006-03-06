/*
 * Main.java
 *
 * Created on 9 February 2005, 19:57I
 */

package manager;
import com.rochester.budget.gui.BudgetManagerFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;


/**
 *
 * @author Cameron
 */
public class Main
{
    private static void createAndShowGUI() 
    {
        String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
        try
        {
            UIManager.setLookAndFeel(lookAndFeel);
        }
        catch (Exception e) { }
        
        //Create and set up the main frame.
        BudgetManagerFrame bmf = new BudgetManagerFrame();       
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        try
        { 
            // Load the database instance 
            Class.forName("com.mysql.jdbc.Driver");            
        }
        catch ( Exception e )
        {
            JOptionPane.showMessageDialog( null, "Error connecting to database " + e.getMessage() );
        }
        
        javax.swing.SwingUtilities.invokeLater( new Runnable() 
        {
            public void run() {
                createAndShowGUI();
            }
        }); 
    }
}
