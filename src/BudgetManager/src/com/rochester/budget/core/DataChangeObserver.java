/*
 * AbstractDataChangeObserver.java
 *
 * Created on 23 May 2005, 13:48
 */

package com.rochester.budget.core;

import java.util.Observable;
import java.util.Observer;

/**
 * This allows database objects to register as observers of other database objects. 
 * Allows observing objects to reload whenever a reload occurs
 * @author Cam
 */
public abstract class AbstractDataChangeObserver implements Observer
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
    public abstract void notifyDatabaseChange( ChangeType change, Object object );
    
    public void update( Observable observable, Object obj )
    {       
        notifyDatabaseChange( (ChangeType)obj, observable );
    }
    
}
