/*
 * Main.java
 *
 * Created on 10 December 2007, 20:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package controllertest;

import au.id.ctr.automation.client.Resolver;
import au.id.ctr.automation.common.AutomationException;
import au.id.ctr.automation.common.interfaces.Library;
import au.id.ctr.automation.common.interfaces.Zone;
import au.id.ctr.automation.mbeans.MusicLibraryMBean;
import au.id.ctr.automation.mbeans.WinampRendererMBean;

/**
 *
 * @author Cameron
 */
public class Main
{
    
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
            // TODO code application logic here
            for (Zone zone : Resolver.getZones("node1"))
            {
                System.out.println("Found zone: " + zone.getName());
                WinampRendererMBean winamp = zone.getRenderer(WinampRendererMBean.class);
                winamp.playFile("Woot!");
            }
            
            for (Library lib : Resolver.getLibraries())
            {                
                System.out.println("Found library: " + lib.getName());
                MusicLibraryMBean ml = (MusicLibraryMBean)lib;
                System.out.println("\t" + ml.getFile());
            }
        } 
        catch (AutomationException ex)
        {
            ex.printStackTrace();
        }
    }
    
}
