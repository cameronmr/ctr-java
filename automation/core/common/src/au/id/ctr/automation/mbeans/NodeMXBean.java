/*
 * MusicLibraryMBean.java
 *
 * Created on 12 December 2007, 20:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package au.id.ctr.automation.mbeans;

import au.id.ctr.automation.common.NodeStatus;
import au.id.ctr.automation.common.interfaces.Common;
import java.util.List;


/**
 *
 * @author Cameron
 */
public interface NodeMXBean extends Common
{
    List<ZoneMXBean> getZones();
    
    ZoneMXBean getZone(final String name);
    
    NodeStatus getStatus();
    
    List<LibraryMXBean> getLibraries();
    
    LibraryMXBean getLibrary(String libClass);
}
