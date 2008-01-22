package au.id.ctr.automation.server;
/*
 * NodeImpl.java
 *
 * Created on 4 December 2007, 22:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import au.id.ctr.automation.common.AutomationException;
import au.id.ctr.automation.common.ManagerClient;
import au.id.ctr.automation.common.NodeStatus;
import au.id.ctr.automation.mbeans.LibraryMXBean;
import au.id.ctr.automation.mbeans.MusicLibraryMXBean;
import au.id.ctr.automation.mbeans.NodeMXBean;
import au.id.ctr.automation.mbeans.ZoneMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.management.JMException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

/**
 *
 * @author Cameron
 */
public class NodeImpl extends StandardMBean implements NodeMXBean
{
    private static final Logger logger = Logger.getLogger(NodeImpl.class.getName());
    
    private transient JMXConnectorServer connector;
    
    private ManagerClient manager;
    
    private String name;
    
    private Map<String, LibraryMXBean> libraries = new HashMap<String, LibraryMXBean>();
    private Map<String, ZoneMXBean> zones = new HashMap<String, ZoneMXBean>();
    
    /**
     * Creates a new instance of NodeImpl
     */
    public NodeImpl(final String nodeName) throws AutomationException
    {
        super(NodeMXBean.class, true);
        this.name = nodeName;
        
        try
        {
            ManagementFactory.getPlatformMBeanServer().registerMBean(this, this.getObjectName());
        } 
        catch (JMException ex)
        {
            throw new AutomationException(ex);
        }
    }
    
    public void start() throws Exception
    {
        logger.info("Starting node");
        
        // Create a connectorserver for this server
        JMXServiceURL addr = new JMXServiceURL("rmi", null, 0);
        connector = JMXConnectorServerFactory.newJMXConnectorServer(
                addr, null, ManagementFactory.getPlatformMBeanServer());
        connector.start();
        
        // Register with the Manager
        manager = new ManagerClient("service:jmx:rmi:///jndi/rmi://cameron:1506/jmxrmi");
        manager.connect();
        manager.registerNode(name, connector.getAddress().toString());
        
        loadLibraries();
        loadZones();
    }
    
    private void loadLibraries() throws AutomationException
    {
        // Load the music library
        libraries.put(MusicLibraryMXBean.class.getName(), new MusicLibraryImpl());
    }
    
    private void loadZones() throws AutomationException
    {
        ZoneImpl zone = new ZoneImpl("Home Theatre");
        zone.loadRenderers();
        zones.put(zone.getName(), zone);
//        zone = new ZoneImpl("Lounge");
//        zone.loadRenderers();
    }

    public List<ZoneMXBean> getZones()
    {
        return new ArrayList<ZoneMXBean>(zones.values());
    }

    public ZoneMXBean getZone(final String name)
    {
        return zones.get(name);
    }

    public NodeStatus getStatus()
    {
        return NodeStatus.ONLINE;
    }

    public List<LibraryMXBean> getLibraries()
    {
        return new ArrayList<LibraryMXBean>(libraries.values());
    }

    public LibraryMXBean getLibrary(String libClass)
    {
        return libraries.get(libClass);
    }

    public String getName()
    {
        return this.name;
    }

    public ObjectName getObjectName() throws MalformedObjectNameException
    {
        return ObjectName.getInstance("ctr.automation.node:type=Node");
    }
    
}
