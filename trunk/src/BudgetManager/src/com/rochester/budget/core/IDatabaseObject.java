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
     */
    public void load() throws Exception;
    
    /**
     * Delete a database object. Remove the key from the table
     */
    public void delete() throws Exception;
}
