/*
 * NodeManagerMBean.java
 *
 * The Node controller is responsible for creating the MBeanServer and
 * the JMXConnectorServer for external connections to the server.
 *
 * It monitors the health of nodes and will generate events that notify
 * controllers of Node/Library/Zone failures
 */

package au.id.ctr.automation.mbeans;

import au.id.ctr.automation.common.NodeRef;
import java.util.Set;
import javax.management.remote.JMXServiceURL;

/**
 *
 * @author Cameron
 */
public interface NodeManagerMBean
{
    void registerNode(final String nodeName, JMXServiceURL url);
    
    Set<NodeRef> getNodes();
}
