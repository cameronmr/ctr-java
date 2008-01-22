/*
 * ZoneImpl.java
 *
 * Load the Renderers
 */

package au.id.ctr.automation.server;

import au.id.ctr.automation.common.AutomationException;
import au.id.ctr.automation.mbeans.LightsRendererMXBean;
import au.id.ctr.automation.mbeans.RendererMXBean;
import au.id.ctr.automation.mbeans.WinampRendererMXBean;
import au.id.ctr.automation.mbeans.ZoneMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.management.JMException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.StandardMBean;

/**
 *
 * @author Cameron
 */
public class ZoneImpl extends StandardMBean implements ZoneMXBean
{
    private final String name;
    
    private Map<String, RendererMXBean> renderers = new HashMap<String, RendererMXBean>();
    
    /** Creates a new instance of ZoneImpl */
    public ZoneImpl(final String name) throws AutomationException
    {
        super(ZoneMXBean.class, true);
        this.name = name;
        
        try
        {
            ManagementFactory.getPlatformMBeanServer().registerMBean(this, this.getObjectName());
        } 
        catch (JMException ex)
        {
            throw new AutomationException(ex);
        }
    }
    
    public void loadRenderers() throws AutomationException
    {
        renderers.put(WinampRendererMXBean.class.getName(), new WinampRendererImpl(name));
        renderers.put(LightsRendererMXBean.class.getName(), new LightsRendererImpl(name));
    }
    
    public String getName()
    {
        return name;
    }
    
    public RendererMXBean getRenderer(String clss)
    {
        return renderers.get(clss);
    }
    
    public List<RendererMXBean> getRenderers()
    {
        return new ArrayList<RendererMXBean>(renderers.values());
    }
    
    public ObjectName getObjectName() throws MalformedObjectNameException
    {
        return ObjectName.getInstance("ctr.automation.node:type=Zone,name=" + getName());
    }
}
