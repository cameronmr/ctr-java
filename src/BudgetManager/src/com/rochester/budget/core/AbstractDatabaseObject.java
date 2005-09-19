/*
 * AbstractDatabaseObject.java
 *
 * Created on 18 May 2005, 20:40
 */

package com.rochester.budget.core;

import com.rochester.budget.core.DataChangeObserver.ChangeType;
import com.rochester.budget.core.exceptions.StateSyncException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Observable;
import java.util.UUID;

/**
 *
 * @author Cam
 */
public abstract class AbstractDatabaseObject extends Observable implements IDatabaseObject
{    
        
    /** Creates a new instance of AbstractDatabaseObject from a known key (in the database) */
    public AbstractDatabaseObject( final String key )
    {
        m_key = key;        
    }
    
    /** Creates a new instance of AbstractDatabaseObject that is not in the database
     */    
    public AbstractDatabaseObject()
    {
        m_key = UUID.randomUUID().toString();
    }
                
    public String getKey()
    {
        return m_key;
    }
    
    public void setKey( final String key )
    {
        m_key = key;
    }
    
    public void delete()
    {
        try
        {
            Statement stmt = DatabaseManager.getStatement();
            stmt.executeUpdate( "delete from " + getTableName() + " where PKEY = '" + m_key + "'" );
            stmt.close();
            
            notifyObservers( ChangeType.DELETE );
        }
        catch ( Exception e )
        {
            // Do nothing
        }
    }
    
    public void load( ) throws Exception
    {
        // Get the data from the table
        Statement stmt = DatabaseManager.getStatement();
        ResultSet results = null;
        try
        { 
            results = stmt.executeQuery( "select * from " + getTableName() + " where PKEY = '" + m_key + "'" );    

            // Move to the first row
            results.first();
            
            // Parse the results
            parseResultSet( results );
        }
        finally
        {
            stmt.close();
            if ( null != results )
            {
                results.close();
            }
        }
        
        // Notify any observers that we have changed
        notifyObservers( ChangeType.UPDATE );
        
        // Store the loaded state
        storeMemento();
    }
            
    public void commit() throws Exception
    {
        // TODO: throw exception if not valid?
        if ( !isValid() )
        {
            // throw new StateSyncException()
        }
        
        // Make sure there is something to commit
        if ( !isModified() )
        {
            return;
        }
        
        // Get the data from the table
        Statement stmt = DatabaseManager.getStatement();
        ResultSet results = null;
        try
        { 
            results = stmt.executeQuery( "select * from " + getTableName() + " where PKEY = '" + m_key + "'" );    

            // Move to the first row
            results.first();
            
            // populate the results
            populateResultSet( results );
            
            // Store the updated results in the database
            results.updateRow();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        finally
        {
            stmt.close();
            if ( null != results )
            {
                results.close();
            }
        }
        
        // Notify any observers that we have changed
        notifyObservers( ChangeType.UPDATE );
    }
    
    public boolean isModified()
    {        
        // If there is more than one item in the list
        if ( m_storedState.size() == 1 )
        {
            return false;
        }
        
        // If the item has never been valid but
        // we have made changes
        Memento lastValidState = getLastValidMemento();
        if ( null == lastValidState &&
                m_storedState.size() > 1 )
        {
            return true;
        }                
            
        // compare the current state to the last valid state
        Memento currentState = getMemento();        
        
        return currentState.equals( lastValidState );
    }
    
    public void rollbackToLastValidState() throws StateSyncException
    {
        Memento state = getLastValidMemento();
        
        // TODO: what happens if there is no valid memento?
        if ( state != null )
        {
            // Restore the state
            restoreMemento( state );

            // Remove all items after this point
            ListIterator it = m_storedState.listIterator( m_storedState.indexOf( state ) );
            while ( it.hasNext() )
            {
                it.next();
                it.remove();
            }
        }        
    }
    
    public void storeMemento()
    {
        // Get the memento & store it
        m_storedState.add( getMemento() );
    }
        
    protected abstract void parseResultSet( ResultSet results ) throws Exception;        
    protected abstract void populateResultSet( ResultSet results ) throws Exception;
    protected abstract Memento getMemento( );
    
    private Memento getLastValidMemento()
    {
        // Go backwards through the list of changes and restore the last valid state!
        ListIterator<Memento> it = m_storedState.listIterator( m_storedState.size() );
        while ( it.hasPrevious() )
        {
            Memento state = it.previous();
            
            if ( state.isValid() )
            {
                // Restore the state
                return  state;
            }
        }       
        
        return null;
    }
        
    private String m_key;    
    private ArrayList<Memento> m_storedState = new ArrayList<Memento>();
}
