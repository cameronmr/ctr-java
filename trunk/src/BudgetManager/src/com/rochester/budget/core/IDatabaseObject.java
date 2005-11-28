/*
 * IDatabaseObject.java
 *
 * Created on 18 May 2005, 20:27
 *
 */

package com.rochester.budget.core;
import com.rochester.budget.core.exceptions.StateSyncException;
import java.util.Observer;

/**
 * A simple interface for a database object.
 * @author Cam
 */
public interface IDatabaseObject
{    
    enum DBState
    {
        COMMITTED,
        NEW,
        DELETED,
        UNKNOWN
    };
    
    /**
     * Return the primary key for this object. All database objects must have a primary key
     * @return The primary key of the database
     */
    String getKey();
    
    /**
     * Load a database object. This can only be executed once the object has been initially loaded (ie: the key is valid)
     * @throws java.lang.Exception Thrown if the object can not be loaded from the database
     */
    void load() throws Exception;
    
    /**
     * Delete a database object. Remove the key from the table and notify observers (ie: Dataobject maps)
     */
    void delete();
    
    /**
     * Check to see if this item has been modified at all. This will generally involve 
     * comparing the current values to the defaults
     * @return true if the item has been modified, false otherwise
     */
    boolean isModified();
    
    boolean isNew();
        
    /**
     * Apply the current object to the database.
     * @throws java.lang.Exception Throw if the commit fails
     */
    void commit() throws Exception;
    
    /**
     * Get the database table name for this object
     * @return The database table name
     */
    String getTableName( );
    
    /**
     * Add an observer to this databaseobject. The observer will be notified of changes to this object
     * @param o The observer to add
     */
    void addObserver(Observer o);
    
    /**
     * Remove an observer from this object
     * @param o The observer to delete
     */
    void deleteObserver(Observer o);
    
    void storeMemento();
    void restoreMemento( Memento state ) throws StateSyncException;
    void rollbackToLastValidState() throws StateSyncException;
    
    boolean isValid( );
}
