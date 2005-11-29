/*
 * IDataChangeObserver.java
 *
 * Created on 23 May 2005, 13:48
 */

package com.rochester.budget.core;
import com.rochester.budget.core.exceptions.BudgetManagerException;

/**
 * This allows database objects to register as observers of other database objects. 
 * Allows observing objects to reload whenever a reload occurs
 * @author Cam
 */
public interface IDataChangeObserver
{
    enum ChangeType
    {
        NEW,
        UPDATE,
        DELETE
    };
        
    /**
     * This is called upon an observer when the source object changes.
     * @param change The type of change
     * @param object The object that has changed
     */
    public void notifyDatabaseChange( ChangeType change, IDatabaseObject object ) throws BudgetManagerException;
}
