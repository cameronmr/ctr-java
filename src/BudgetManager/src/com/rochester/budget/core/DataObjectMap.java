/*
 * DatabaseMap.java
 *
 * Created on 7 September 2005, 18:06
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.core;
import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author Cam
 */
public class DataObjectMap<E extends IDatabaseObject> implements IDataChangeObserver
{
    
    /** Creates a new instance of DatabaseMap */
    public DataObjectMap() 
    {
    }
    
    public E put( E object )
    {
        // Add an observer so the cache can be updated if the item is deleted
        object.addObserver( this );        
        
        return m_dataObjects.put( object.getKey(), object );
    }
    
    public E get( final String key )
    {
        return m_dataObjects.get( key );
    }
    
    public Collection<E> values()
    {
        return m_dataObjects.values();
    }
    
    // Observer is automatically deregistered on delete change!
    public void notifyDatabaseChange( ChangeType change, IDatabaseObject object )
    {
        switch ( change )
        {
            // If we have received an update clear the item from the cache & allow
            // it to be loaded from the database the next time around
            case UPDATE:
                // do nothing
                break;
            case DELETE:
                // remove the item from the map
                m_dataObjects.remove( object.getKey() );
                break;
            default:
                // do nothing (MODIFY/ADD)               
        }
    }
    
    public void clear()
    {
        m_dataObjects.clear();
    }
        
    private final HashMap<String, E> m_dataObjects = new HashMap<String, E>();  
}
