/*
 * Main.java
 *
 * Created on 4 December 2007, 22:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nodetest;

import au.id.ctr.automation.server.NodeImpl;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cameron
 */
public class Main
{
    private static Logger logger = Logger.getLogger(Main.class.getName());
    
    /** Creates a new instance of Main */
    public Main()
    {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        try
        {
            // Register the NodeImpl!
            NodeImpl n = new NodeImpl(args[0]);
            n.start();
        } 
        catch (Exception ex)
        {
            logger.log(Level.SEVERE, "Error resolving NodeController", ex);
        }
        
        while(true)
        {
            try
            {
                Thread.sleep(100);
            } catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }
        }
    }
    
}
