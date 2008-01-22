/*
 * NodeClient.java
 *
 * Created on 10 December 2007, 21:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package au.id.ctr.automation.client;

import au.id.ctr.automation.common.AutomationException;
import au.id.ctr.automation.common.NodeStatus;
import au.id.ctr.automation.common.interfaces.Library;
import au.id.ctr.automation.common.interfaces.Node;
import au.id.ctr.automation.common.interfaces.Renderer;
import au.id.ctr.automation.common.interfaces.Zone;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

/**
 *
 * @author Cameron
 */
public class NodeClient implements Node
{
    private static final Logger logger = Logger.getLogger(NodeClient.class.getName());
    private final String name;
    private NodeStatus status;
    private MBeanServerConnection nodeConnection;
    private Map<String, ZoneClient> zones = new HashMap<String, ZoneClient>();
    private Map<Class, Library> libraries = new HashMap<Class, Library>();
    
    /** Creates a new instance of NodeClient */
    public NodeClient(final String name, JMXServiceURL url) throws AutomationException
    {
        this.name = name;
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
            // Lookup the zones in the mbean repository
            Set<ObjectName> names = nodeConnection.queryNames(
                    ObjectName.getInstance("ctr.automation.node:type=Renderer,*"), null);
            for (ObjectName objName : names)
            {
                ZoneClient zone = zones.get(objName.getKeyProperty("zone"));
                if (zone == null)
                {
                    zone = new ZoneClient(objName.getKeyProperty("zone"));
                    zones.put(zone.getName(), zone);
                }
                
                Class<?> clss = Class.forName(objName.getKeyProperty("class"));
                zone.addRenderer(clss, (Renderer)(
                        JMX.newMBeanProxy(nodeConnection, objName, clss, true)));
            }
            
            // Lookup the libraries in the mbean repository
            names = nodeConnection.queryNames(
                    ObjectName.getInstance("ctr.automation.node:type=Library,*"), null);
            for (ObjectName objName : names)
            {                
                Class<?> clss = Class.forName(objName.getKeyProperty("class"));
                this.libraries.put(clss, (Library)(
                        JMX.newMBeanProxy(nodeConnection, objName, clss, true)));
            }
        } 
        catch (Exception ex)
        {
            throw new AutomationException("Error resolving zones ", ex);
        }
    }
    
    public String getName()
    {
        return name;
    }
    
    public List<Zone> getZones()
    {
        return new ArrayList(zones.values());
    }
    
    public Zone getZone(final String name)
    {
        return zones.get(name);
    }
    
    public NodeStatus getStatus()
    {
        return status;
    }

    public List<Library> getLibraries()
    {
        return new ArrayList<Library>(this.libraries.values());
    }

    public <T extends Library> T getLibrary(Class<T> libClass)
    {
        return (T)this.libraries.get(libClass);
    }
}
