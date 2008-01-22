/*
 * AbstractLibrary.java
 *
 * Created on 12 December 2007, 21:01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package au.id.ctr.automation.common;

import au.id.ctr.automation.mbeans.LibraryMXBean;
import java.lang.management.ManagementFactory;
import javax.management.JMException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.StandardMBean;

/**
 *
 * @author Cameron
 */
public abstract class AbstractLibrary extends StandardMBean implements LibraryMXBean
{
    public AbstractLibrary(Class<?> interfaceClass) throws AutomationException
    {
        super(interfaceClass, true);
        
        try
        {
            ManagementFactory.getPlatformMBeanServer().registerMBean(this, this.getObjectName());
        } 
        catch (JMException ex)
        {
            throw new AutomationException(ex);
        }
    }

    public ObjectName getObjectName() throws MalformedObjectNameException
    {
        return ObjectName.getInstance("ctr.automation.node:type=Library,class=" + 
                this.getMBeanInterface().getName());
    }

    public String getName()
    {
        return this.getClass().getSimpleName();
    }
}
