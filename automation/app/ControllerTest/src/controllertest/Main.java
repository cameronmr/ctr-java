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
import au.id.ctr.automation.common.ManagerClient;
import au.id.ctr.automation.mbeans.LibraryMXBean;
import au.id.ctr.automation.mbeans.MusicLibraryMXBean;
import au.id.ctr.automation.mbeans.WinampRendererMXBean;
import au.id.ctr.automation.mbeans.ZoneMXBean;

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
            
            ManagerClient theManager = new ManagerClient("service:jmx:rmi:///jndi/rmi://cameron:1506/jmxrmi");
            theManager.connect();
            
            for (ZoneMXBean zone : Resolver.getZones("node1"))
            {
                System.out.println("Found zone: " + zone.getName());
                WinampRendererMXBean winamp = (WinampRendererMXBean)zone.getRenderer(WinampRendererMXBean.class.getName());
                winamp.playFile("Woot!");
            }

            for (LibraryMXBean lib : Resolver.getLibraries())
            {
                System.out.println("Found library: " + lib.getName());
            }
            
            MusicLibraryMXBean lib = Resolver.getLibrary(MusicLibraryMXBean.class);
            if (lib != null)
            {
                System.out.println("Found Music Lib: " + lib.getFile());
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
}
