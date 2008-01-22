/*
 * NodeManager.java
 *
 * Created on 4 December 2007, 19:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package au.id.ctr.automation.server;

import au.id.ctr.automation.common.AutomationException;
import au.id.ctr.automation.common.DefaultExecutor;
import au.id.ctr.automation.common.NodeNotification;
import au.id.ctr.automation.common.NodeRef;
import au.id.ctr.automation.mbeans.NodeManagerMXBean;
import au.id.ctr.automation.common.NodeStatus;
import au.id.ctr.automation.mbeans.LibraryMXBean;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.MBeanServer;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.StandardEmitterMBean;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

/**
 *
 * @author Cameron
 */
public class NodeManager extends StandardEmitterMBean implements NodeManagerMXBean
{
    private static final Logger logger = Logger.getLogger(NodeManager.class.getName());
    
    /**
     * Store the current nodes
     */
    private Set<NodeRef> nodes = new HashSet<NodeRef>();
    
    /**
     * Creates a new instance of NodeManager
     */
    public NodeManager()
    {
        super(NodeManagerMXBean.class,
                true,
                new NotificationBroadcasterSupport(DefaultExecutor.getExecutor()));
    }
    
    public void registerNode(final String nodeName, String url)
    {
        // A Node has come up so we need to resolve it & then we can load it's configuration
        logger.info("Node registration received for " + nodeName + ":\n" + url);
        try
        {
            // Resolve the connection to the node
            JMXServiceURL jmxUrl = new JMXServiceURL(url);
            JMXConnector conn = JMXConnectorFactory.connect(jmxUrl);
            
            // Create, or update, node references
            NodeRef ref = new NodeRef(nodeName, url, NodeStatus.ONLINE);
            
            nodes.remove(ref);
            nodes.add(ref);
            
            // Send a notification that a node has come online. Controllers
            // will received this notification so they can update their
            // various references
            sendNotification(new NodeNotification(ref));
        }
        catch (Exception ex)
        {
            logger.log(Level.WARNING, "Unable to connect to node", ex);
        }
    }

    public Set<NodeRef> getNodes()
    {
        return this.nodes;
    }
    
    public void start() throws AutomationException
    {
        logger.info("Starting node controller");
        // Get the platform MBeanServer and add the jmxserverconnection
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        try
        {
            mbs.addNotificationListener(new ObjectName("JMImplementation:type=MBeanServerDelegate"), new NotificationListener()
            {
                public void handleNotification(Notification notification, Object handback)
                {
                    logger.info(notification.toString());
                }
            }, null, null);
            
            // Register myself in the server
            mbs.registerMBean(this, ObjectName.getInstance("ctr.automation:type=NodeController"));
            
            // Create a jndi registry for storing the connector server
            LocateRegistry.createRegistry(1506);
            
            // The JMXServiceURL will identify the port number
            JMXServiceURL addr = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:1506/jmxrmi");
            JMXConnectorServer connector = JMXConnectorServerFactory.newJMXConnectorServer(addr, null, mbs);
            connector.start();
            logger.info("Connector started on: " + connector.getAddress().toString());
            
            connector.addNotificationListener(new NotificationListener()
            {
                public void handleNotification(Notification notification, Object handback)
                {
                    logger.info(notification.getMessage());
                }
            }, null, null);
        }
        catch (Exception ex)
        {
            logger.log(Level.SEVERE, "Unable to register server connection", ex);
            throw new AutomationException("Error starting", ex);
        }
    }
    
    public void stop()
    {
    }
    
    public void preDeregister() throws Exception
    {
        stop();
    }    
}
