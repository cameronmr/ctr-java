/*
 * Renderer.java
 *
 * Created on 12 December 2007, 20:27
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
public interface Renderer
{
    String getName();
    String getZone();
    ObjectName getObjectName() throws MalformedObjectNameException;
}
