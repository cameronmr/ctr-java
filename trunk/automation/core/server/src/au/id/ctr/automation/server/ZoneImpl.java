/*
 * ZoneImpl.java
 *
 * Load the Renderers
 */

package au.id.ctr.automation.server;

import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.JMException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

/**
 *
 * @author Cameron
 */
public class ZoneImpl
{
    private final String name;
    
    /** Creates a new instance of ZoneImpl */
    public ZoneImpl(final String name)
    {
        this.name = name;
    }
    
    public void loadRenderers() throws JMException
    {
        try
        {
            WinampRendererImpl winamp = new WinampRendererImpl(name);
            ManagementFactory.getPlatformMBeanServer().registerMBean(winamp, winamp.getObjectName());
            
            LightsRendererImpl lights = new LightsRendererImpl(name);
            ManagementFactory.getPlatformMBeanServer().registerMBean(lights, lights.getObjectName());
        }
        catch (MalformedURLException ex)
        {
            ex.printStackTrace();
        }
    }
}
