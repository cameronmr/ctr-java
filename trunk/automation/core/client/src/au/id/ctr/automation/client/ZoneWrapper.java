/*
 * ZoneClient.java
 *
 * Created on 22 December 2007, 21:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package au.id.ctr.automation.client;

import au.id.ctr.automation.mbeans.RendererMXBean;
import au.id.ctr.automation.mbeans.ZoneMXBean;
import java.util.List;
import java.util.logging.Logger;
import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 *
 * @author Cameron
 */
public class ZoneWrapper implements ZoneMXBean
{
    private static final Logger logger = Logger.getLogger(ZoneWrapper.class.getName());
    private ZoneMXBean wrapped;
    private MBeanServerConnection nodeConnection;
    
    /** Creates a new instance of ZoneClient */
    public ZoneWrapper(final ZoneMXBean toWrap,
            MBeanServerConnection connection)
    {
        this.wrapped = toWrap;
        this.nodeConnection = connection;
    }
    
    public String getName()
    {
        return this.wrapped.getName();
    }
    
    public RendererMXBean getRenderer(String clss)
    {
        RendererMXBean renderer = wrapped.getRenderer(clss);
        if (renderer != null)
        {
            try
            {
                return (RendererMXBean)JMX.newMBeanProxy(nodeConnection,
                        renderer.getObjectName(),
                        Class.forName(clss));
            }
            catch (Exception ex)
            {
                logger.warning(ex.getMessage());
            }
        }
        return null;
    }
    
    public List<RendererMXBean> getRenderers()
    {
        return wrapped.getRenderers();
    }
    
    public ObjectName getObjectName() throws MalformedObjectNameException
    {
        return wrapped.getObjectName();
    }
    
}
