/*
 * ManagerClient.java
 *
 * Created on 22 December 2007, 20:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package au.id.ctr.automation.common;

import au.id.ctr.automation.mbeans.NodeManagerMBean;
import java.net.MalformedURLException;
import java.util.Observable;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectionNotification;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

/**
 *
 * @author Cameron
 */
public class ManagerClient extends Observable implements NotificationListener, NodeManagerMBean
{
    private final ObjectName managerName;
    private static final Logger logger = Logger.getLogger(ManagerClient.class.getName());
    private JMXServiceURL controllerURL;
    private NodeManagerMBean theBean;
    private String nodeName;
    private JMXServiceURL nodeURL;
    
    /** Creates a new instance of ManagerClient */
    public ManagerClient(final String url) throws AutomationException
    {
        try
        {
            controllerURL = new JMXServiceURL(url);
            managerName = new ObjectName("ctr.automation:type=NodeController");
        }
        catch (Exception ex)
        {
            throw new AutomationException("Unable to parse manager url: " + url, ex);
        }
    }
    
    public void handleNotification(Notification notification, Object handback)
    {
        // Nodes have gone online/offline
        NodeNotification notif = (NodeNotification)notification;
        if (!notif.getNodeName().equals(this.nodeName))
        {
            logger.info("Notification received. Node " + notif.getNodeName() + " " + notif.getStatus().toString());
        }
    }
    
    public void connect()
    {
        // This will create the connection initially & then monitor it
        logger.info("Attempting to contact controller on: " + controllerURL.toString());
        
        boolean connected = false;
        while (!connected)
        {
            try
            {
                JMXConnector controllerConnector = JMXConnectorFactory.connect(controllerURL);
                
                controllerConnector.addConnectionNotificationListener(new NotificationListener()
                {
                    public void handleNotification(Notification notification, Object handback)
                    {
                        logger.info("notification received: " + notification.toString());
                        // If we get disconnected we want to reconnect!
                        if (notification.getType().equals(JMXConnectionNotification.CLOSED))
                        {
                            new Thread(new Runnable()
                            {
                                public void run()
                                {
                                    connect();
                                }
                            }).start();
                        }
                    }
                }, null, null);
                
                MBeanServerConnection controllerServer =
                        controllerConnector.getMBeanServerConnection();
                
                logger.info("Connected to controller on: " + controllerURL.toString());
                connected = true;
                
                theBean = JMX.newMBeanProxy(controllerServer,
                        managerName,
                        NodeManagerMBean.class);
                
                // Listen for notification of node registration
                controllerServer.addNotificationListener(managerName,
                        this,
                        null,
                        null);
                
                // Re-register if the manager went offline
                if (nodeURL != null && nodeName != null)
                {
                    registerNode(nodeName, nodeURL);
                }
            }
            catch (Exception ex)
            {
                // Wait for 10secs before trying to reconnect
                logger.log(Level.INFO, "Connection failed, trying again");
                try
                {
                    Thread.sleep(10000);
                }
                catch (InterruptedException e)
                {
                    // do nothing
                }
            }
        }
    }
    
    public void registerNode(final String nodeName, JMXServiceURL url)
    {
        this.nodeName = nodeName;
        this.nodeURL = url;
        logger.info("Registering node with url:" + url.toString());
        theBean.registerNode(this.nodeName, this.nodeURL);
    }
    
    public Set<NodeRef> getNodes()
    {
        return theBean.getNodes();
    }
    
    
}
