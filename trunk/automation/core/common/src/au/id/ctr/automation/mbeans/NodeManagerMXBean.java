/*
 * NodeManagerMXBean.java
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

/**
 *
 * @author Cameron
 */
public interface NodeManagerMXBean
{
    void registerNode(final String nodeName, String url);
    
    Set<NodeRef> getNodes();
}
