/*
 * Memento.java
 *
 * Created on 14 September 2005, 21:03
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.core;

import com.rochester.budget.core.exceptions.StateSyncException;
import java.util.ArrayList;

/**
 *
 * @author Cam
 */
public class Memento
{
    
    /** Creates a new instance of Memento */
    public Memento( Object... objects )
    {
        for ( Object obj : objects )
        {
            m_state.add( obj );
        }
    }
        
    public Object getSomeState( ) throws StateSyncException
    {
        // We are now retrieving state
        if ( m_state.isEmpty() )
        {
            throw new StateSyncException( "No state remaining in mememnto" );
        }
        
        // Always return the item at the front of the queue
        return m_state.remove( 0 );
    }
    
    public boolean equals( Object obj )
    {
        // Go through our state and see if we are valid
        if ( obj instanceof Memento )
        {
            Memento m2 = (Memento)obj;
            
            if ( m_state.size() != m2.m_state.size() )
            {
                return false;
            }
            
            return m_state.equals( m2.m_state );
        }
        
        return false;
    }
        
    private ArrayList<Object> m_state = new ArrayList<Object>();
}
