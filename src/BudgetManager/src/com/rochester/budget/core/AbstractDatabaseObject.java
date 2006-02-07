/*
 * AbstractDatabaseObject.java
 *
 * Created on 18 May 2005, 20:40
 */

package com.rochester.budget.core;

import com.rochester.budget.core.IDataChangeObserver.ChangeType;
import com.rochester.budget.core.IDatabaseObject.DBState;
import com.rochester.budget.core.exceptions.BudgetManagerException;
import com.rochester.budget.core.exceptions.StateSyncException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.UUID;

/**
 *
 * @author Cam
 */
public abstract class AbstractDatabaseObject implements IDatabaseObject
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
        
        // We definately do not exist in the database yet!
        m_state = DBState.NEW;
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
        // only delete from the database if we are committed to the database
        if ( m_state == DBState.COMMITTED )
        {
            try
            {
                Statement stmt = DatabaseManager.getStatement();
                stmt.executeUpdate( "delete from " + getTableName() + " where PKEY = '" + m_key + "'" );
                stmt.close();

                // We are no longer in the database
                m_state = DBState.DELETED;

            }
            catch ( Exception e )
            {
                // Do nothing
            }
        }
        
        // We notify observers regardless of the committed state
        try
        {
            notifyObservers( ChangeType.DELETE );     
        }
        catch ( Exception emit )
        {}
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
            
            // we exist in the database
            m_state = DBState.COMMITTED;
            m_committedState = getMemento();
            
            // Notify any observers that we have changed
            notifyObservers( ChangeType.UPDATE );
        }
        finally
        {
            stmt.close();
            if ( null != results )
            {
                results.close();
            }
        }                
    }
            
    public void commit() throws Exception
    {
        // Make sure there is something to commit
        if ( !isModified() )
        {
            return;
        }        
        
        // throw exception if not valid
        if ( !isValid() )
        {
            throw new StateSyncException( "Item is not in a valid state to commit to the database" );
        }        
        
        // Get the data from the table
        Statement stmt = DatabaseManager.getStatement();
        ResultSet results = null;
        try
        { 
            if ( m_state == DBState.COMMITTED )
            {
                // Retrieve the existing entry..
                results = stmt.executeQuery( "select * from " + getTableName() + " where PKEY = '" + m_key + "'" );    
                
                // Move to the first row
                results.first();
                
                // populate the results
                populateResultSet( results );
                
                // Store the updated results in the database
                results.updateRow();
            }
            else
            {
                // Get the first result just to populate the resultset..
                results = stmt.executeQuery( "select * from " + getTableName() + " limit 1" );   
                
                // Prepare to insert a new entry
                results.moveToInsertRow();
                
                // populate the results
                populateResultSet( results );
                
                // Store the updated results in the database
                results.insertRow();
            }                            
            
            // we exist in the database
            m_state = DBState.COMMITTED;
            m_committedState = getMemento();
            
            // Notify any observers that we have changed
            notifyObservers( ChangeType.UPDATE );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw e;
        }
        finally
        {
            stmt.close();
            if ( null != results )
            {
                results.close();
            }
        }        
    }
    
    public boolean isModified()
    {        
        // If there is more than one item in the list
        if ( m_modifiedState.isEmpty() )
        {
            return false;
        }
        
        // If item is commited we must compare to the committed state
        switch ( m_state )
        {
            case COMMITTED:
            {
                Memento lastState = getLastMemento();
                if ( null == lastState )
                {
                    // If the item has no changes it has still been modified
                    return true;
                }        
                else
                {
                    // If the items are both equal than nothing has been modified!
                    return ! m_committedState.equals( lastState );
                }
            }
            case NEW:
            {
                // If there are any modifications at all then we must return true        
                return true;
            }
        }    
        
        // Everything else has not been modified
        return false;
    }
    
    public void rollbackToLastGoodState() throws StateSyncException
    {
        if ( null != m_committedState )
        {
            // Restore the committed state
            restoreMemento( m_committedState );
            
            // Clear any changes
            m_modifiedState.clear();
            
            // Grab a new copy of the committed state because we have consumed the previous state
            m_committedState = getMemento();
        }
        else
        {
            throw new StateSyncException( "No good state" );
        }        
    }
    
    public void rollbackToLastState() throws StateSyncException
    {
        Memento state = getLastMemento();
        
        // TODO: what happens if there is no memento?
        if ( state != null )
        {
            // Restore the state
            restoreMemento( state );

            // Remove all items after this point
            ListIterator it = m_modifiedState.listIterator( m_modifiedState.indexOf( state ) );
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
        m_modifiedState.add( getMemento() );
        
        // cap the maximum size of history to, say, 50 items?
        if ( m_modifiedState.size() > 50 )
        {
            m_modifiedState.remove( 0 );
        }
    }
    
    public boolean isNew()
    {
        return m_state == DBState.NEW;
    }
    
    public void addObserver( IDataChangeObserver o )
    {
        // If it doesn't exist, add it
        if ( ! m_observers.contains( o ) )   
        {
            m_observers.add( o );
        }
    }
    
    public void deleteObserver( IDataChangeObserver o )
    {
        m_observers.remove( o );
    }
              
    protected void notifyObservers( ChangeType change ) throws BudgetManagerException
    {
        for ( IDataChangeObserver o : m_observers )
        {
            o.notifyDatabaseChange( change, this );
        }
        
        // If this is a delete we remove all the observers automatically
        if ( ChangeType.DELETE == change )
        {
            m_observers.clear();
        }
    }
    
    protected abstract void parseResultSet( ResultSet results ) throws Exception;        
    protected abstract void populateResultSet( ResultSet results ) throws Exception;
    protected abstract Memento getMemento( );
    
    protected void reconcileObjectList( ArrayList src, ArrayList copy )
    {
        // find the items in the copy that don't exist in the src, and 'delete them' (remove them from
        // the database cache & from the database if they exist in the database
        for ( Object obj : copy )
        {            
            if ( ! src.contains( obj ) )
            {
                // The src doesn't contain this item so delete it!
                ((IDatabaseObject)obj).delete();
            }
        }
    }
    
    private Memento getLastMemento()
    {
        return m_modifiedState.get( m_modifiedState.size() - 1 );
        
        // Go backwards through the list of changes and restore the last valid state!
        /*ListIterator<Memento> it = m_modifiedState.listIterator( m_modifiedState.size() );
        while ( it.hasPrevious() )
        {
            Memento state = it.previous();
            
            if ( state.isValid() )
            {
                // Restore the state
                return state;
            }
        }       
        
        return null;*/
    }
        
    private String m_key;    
    private ArrayList<Memento> m_modifiedState = new ArrayList<Memento>();
    private Memento m_committedState = null;
    private DBState m_state = DBState.UNKNOWN;
    private ArrayList<IDataChangeObserver> m_observers = new ArrayList<IDataChangeObserver>();
}
