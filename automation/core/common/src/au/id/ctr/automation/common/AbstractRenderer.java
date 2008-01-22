/*
 * AbstractRenderer.java
 *
 * Created on 12 December 2007, 20:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package au.id.ctr.automation.common;

import au.id.ctr.automation.mbeans.RendererMXBean;
import java.lang.management.ManagementFactory;
import javax.management.JMException;
import javax.management.MalformedObjectNameException;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;
import javax.management.StandardEmitterMBean;

/**
 *
 * @author Cameron
 */
public abstract class AbstractRenderer extends StandardEmitterMBean implements RendererMXBean
{
    private final String zone;
    
    /** Creates a new instance of AbstractRenderer */
    public AbstractRenderer(Class<?> interfaceClass, final String zone) throws AutomationException
    {
        super(interfaceClass,
                true,
                new NotificationBroadcasterSupport(DefaultExecutor.getExecutor()));
        this.zone = zone;
        
        try
        {
            ManagementFactory.getPlatformMBeanServer().registerMBean(this, this.getObjectName());
        } 
        catch (JMException ex)
        {
            throw new AutomationException(ex);
        }
    }

    public String getZone()
    {
        return this.zone;
    }    

    public ObjectName getObjectName() throws MalformedObjectNameException
    {
        return ObjectName.getInstance("ctr.automation.node:type=Renderer,zone=" + zone + 
                ",class=" + this.getMBeanInterface().getName() );
    }

    public String getName()
    {
        return this.getClass().getSimpleName();
    }        
}
