/*
 * Main.java
 *
 * Created on 4 December 2007, 20:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package controllertest;

import au.id.ctr.automation.common.AutomationException;
import au.id.ctr.automation.server.NodeManager;
import java.lang.management.ManagementFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.MBeanServer;

/**
 *
 * @author Cameron
 */
public class Main
{
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    /** Creates a new instance of Main */
    public Main()
    {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        
        NodeManager node = new NodeManager();
        try
        {
            node.start();
        } 
        catch (AutomationException ex)
        {
            logger.log(Level.SEVERE, "Unable to start controller", ex);
            System.exit(1);
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
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        new Main();
    }
    
}
