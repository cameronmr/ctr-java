/*
 * TransactionNotFoundException.java
 *
 * Created on 4 July 2005, 19:50
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
public class TransactionNotFoundException extends BudgetManagerException
{
    
    /** Creates a new instance of AccountNotFoundException */
    public TransactionNotFoundException( final String message )
    {
        super( message );
    }
    
}
