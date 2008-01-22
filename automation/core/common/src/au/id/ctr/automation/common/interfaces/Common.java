/*
 * Common.java
 *
 * Created on 22 January 2008, 21:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package au.id.ctr.automation.common.interfaces;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 *
 * @author Cameron
 */
public interface Common
{    
    String getName();
    ObjectName getObjectName() throws MalformedObjectNameException;
}
