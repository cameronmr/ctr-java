/*
 * NodeRef.java
 *
 * Created on 8 December 2007, 13:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package au.id.ctr.automation.common;

import java.io.Serializable;
import javax.management.remote.JMXServiceURL;

/**
 *
 * @author Cameron
 */
public class NodeRef implements Serializable
{
    private JMXServiceURL url;
    private NodeStatus status;
    private final String name;
    
    /** Creates a new instance of NodeRef */
    public NodeRef(final String nodeName, 
            final JMXServiceURL url, 
            final NodeStatus status)
    {
        this.name = nodeName;
        this.url = url;
        this.status = status;
    }

    public String getName()
    {
        return this.name;
    }
    
    public void setStatus(NodeStatus status)
    {
        this.status = status;
    }

    public NodeStatus getStatus()
    {
        return status;
    }
    
    public JMXServiceURL getURL()
    {
        return url;
    }

    public boolean equals(Object object)
    {
        if ( this == object )
        {
            return true;
        }
        
        if (this.getClass().isInstance(object))
        {
            return this.name.equals(((NodeRef)object).getName());
        }
        
        return false;
    }

    public int hashCode()
    {
        return name.hashCode();
    }
}
