/*
 * RuleResultNotFoundException.java
 *
 * Created on 29 December 2005, 12:48
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
public class RuleResultNotFoundException extends BudgetManagerException
{
    
    /**
     * Creates a new instance of RuleResultNotFoundException 
     */
    public RuleResultNotFoundException( final String message )
    {
        super( message );
    }    
    
}
