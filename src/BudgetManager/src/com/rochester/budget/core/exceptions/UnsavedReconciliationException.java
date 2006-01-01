/*
 * UnsavedReconciliationException.java
 *
 * Created on 27 December 2005, 14:39
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.core.exceptions;

/**
 *
 * @author Cam
 */
public class UnsavedReconciliationException extends BudgetManagerException
{
    
    /** Creates a new instance of UnsavedReconciliationException */
    public UnsavedReconciliationException(final String message )
    {
        super( message );
    }
    
}
