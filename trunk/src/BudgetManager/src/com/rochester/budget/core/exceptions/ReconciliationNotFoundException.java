/*
 * ReconciliationNotFoundException.java
 *
 * Created on 7 September 2005, 18:44
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
public class ReconciliationNotFoundException extends BudgetManagerException
{
    
    /** Creates a new instance of ReconciliationNotFoundException */
    public ReconciliationNotFoundException( final String message )
    {
        super( message );
    }    
}
