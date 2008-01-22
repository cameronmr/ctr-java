/*
 * NodeNotification.java
 *
 * Created on 8 December 2007, 13:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package au.id.ctr.automation.common;

import au.id.ctr.automation.common.NodeStatus;
import javax.management.Notification;

/**
 *
 * @author Cameron
 */
public class NodeNotification extends Notification
{
    private static long sequence;
    
    /** Creates a new instance of NodeNotification */
    public NodeNotification(final NodeRef ref)
    {
        super(NodeNotification.class.getName(), ref, sequence++);
    }

    public NodeRef getNodeRef()
    {
        return (NodeRef)this.getSource();
    }    

    public String getNodeName()
    {
        return getNodeRef().getName();
    }
    
    public NodeStatus getStatus()
    {
        return getNodeRef().getStatus();
    }
}
