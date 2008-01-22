/*
 * Library.java
 *
 * Created on 10 December 2007, 20:38
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
public interface Library
{
    String getName();
    ObjectName getObjectName() throws MalformedObjectNameException;
}
