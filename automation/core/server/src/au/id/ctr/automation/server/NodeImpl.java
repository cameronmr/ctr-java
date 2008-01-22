package au.id.ctr.automation.server;
/*
 * NodeImpl.java
 *
 * Created on 4 December 2007, 22:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import au.id.ctr.automation.common.ManagerClient;
import au.id.ctr.automation.mbeans.NodeManagerMBean;
import java.lang.management.ManagementFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.JMException;
import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectionNotification;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

/**
 *
 * @author Cameron
 */
public class NodeImpl
{
    private static final Logger logger = Logger.getLogger(NodeImpl.class.getName());
    
    private transient JMXConnectorServer connector;
    
    private ManagerClient manager;
    
    private String name;
    
    /**
     * Creates a new instance of NodeImpl
     */
    public NodeImpl(final String nodeName)
    {
        this.name = nodeName;
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
        manager.registerNode(name, connector.getAddress());
        
        loadLibraries();
        loadZones();
    }
    
    private void loadLibraries() throws JMException
    {
        // Load the music library
        MusicLibraryImpl music = new MusicLibraryImpl();
        ManagementFactory.getPlatformMBeanServer().registerMBean(music, music.getObjectName());
    }
    
    private void loadZones() throws JMException
    {
        ZoneImpl zone = new ZoneImpl("Home Theatre");
        zone.loadRenderers();
//        zone = new ZoneImpl("Lounge");
//        zone.loadRenderers();
    }
    
}
