/*
 * Node.java
 *
 * A node is basically a container for
 * Libraries and Zones.
 */

package au.id.ctr.automation.common.interfaces;

import au.id.ctr.automation.common.NodeStatus;
import au.id.ctr.automation.common.interfaces.Zone;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Cameron
 */
public interface Node
{
    String getName();
    
    List<Zone> getZones();
    
    Zone getZone(final String name);
    
    NodeStatus getStatus();
    
    List<Library> getLibraries();
    
    <T extends Library> T getLibrary(Class<T> libClass);
}
