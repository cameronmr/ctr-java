/*
 * IDatabaseObject.java
 *
 * Created on 18 May 2005, 20:27
 *
 */

package com.rochester.budget.core;

import java.sql.SQLException;

/**
 * A simple interface for a database object.
 * @author Cam
 */
public interface IDatabaseObject
{    
    /**
     * Return the primary key for this object. All database objects must have a primary key
     * @return The primary key of the database
     */
    public String getKey();
    
    /**
     * Load a database object. This can only be executed once the object has been initially loaded (ie: the key is valid)
     * @throws java.lang.Exception Thrown if the object can not be loaded from the database
     */
    public void load() throws Exception;
    
    /**
     * Delete a database object. Remove the key from the table
     * @throws java.lang.Exception Thrown if the object cannot be deleted from the database
     */
    public void delete() throws Exception;
    
    /**
     * Apply the current object to the database.
     * @throws java.lang.Exception Throw if the commit fails
     */
    public void commit() throws Exception;
    
    /**
     * Get the database table name for this object
     * @return The database table name
     */
    String getTableName( );
}
