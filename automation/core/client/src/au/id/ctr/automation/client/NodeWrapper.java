/*
 * NodeWrapper.java
 *
 * Created on 10 December 2007, 21:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package au.id.ctr.automation.client;

import au.id.ctr.automation.common.AutomationException;
import au.id.ctr.automation.common.NodeStatus;
import au.id.ctr.automation.mbeans.LibraryMXBean;
import au.id.ctr.automation.mbeans.NodeMXBean;
import au.id.ctr.automation.mbeans.ZoneMXBean;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

/**
 *
 * @author Cameron
 */
public class NodeWrapper implements NodeMXBean
{
    private static final Logger logger = Logger.getLogger(NodeWrapper.class.getName());
    private NodeStatus status;
    private MBeanServerConnection nodeConnection;
    private NodeMXBean wrapped;
    
    /**
     * Creates a new instance of NodeWrapper
     */
    public NodeWrapper(final String name, JMXServiceURL url) throws AutomationException
    {
        try
        {
            JMXConnector nodeConnector = JMXConnectorFactory.connect(url);
            nodeConnection = nodeConnector.getMBeanServerConnection();
        }
        catch (IOException ex)
        {
            throw new AutomationException("Unable to connect to Node " + name, ex);
        }
        this.status = NodeStatus.ONLINE;
        
        try
        {
            // Lookup the node in the mbean repository
            this.wrapped = JMX.newMXBeanProxy(nodeConnection,
                    ObjectName.getInstance("ctr.automation.node:type=Node"),
                    NodeMXBean.class);
        }
        catch (Exception ex)
        {
            throw new AutomationException("Error resolving zones ", ex);
        }
    }
    
    public String getName()
    {
        return wrapped.getName();
    }
    
    public List<ZoneMXBean> getZones()
    {
        List<ZoneMXBean> zones = new ArrayList<ZoneMXBean>();
        for (ZoneMXBean bean : wrapped.getZones())
        {
            zones.add(new ZoneWrapper(bean, nodeConnection));
        }
        
        return zones;
    }
    
    public ZoneMXBean getZone(final String name)
    {
        return new ZoneWrapper(wrapped.getZone(name), nodeConnection);
    }
    
    public NodeStatus getStatus()
    {
        return status;
    }
    
    public List<LibraryMXBean> getLibraries()
    {
        return wrapped.getLibraries();
    }
    
    public LibraryMXBean getLibrary(String libClass)
    {
        LibraryMXBean lib = wrapped.getLibrary(libClass);
        if (lib != null)
        {
            try
            {
                return (LibraryMXBean)JMX.newMXBeanProxy(nodeConnection,
                        lib.getObjectName(),
                        Class.forName(libClass));
            }
            catch (Exception ex)
            {
                logger.warning(ex.getMessage());
            }
        }
        return null;
    }
    
    public ObjectName getObjectName() throws MalformedObjectNameException
    {
        return wrapped.getObjectName();
    }
}
