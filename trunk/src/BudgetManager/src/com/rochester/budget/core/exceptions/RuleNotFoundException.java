/*
 * RuleNotFoundException.java
 *
 * Created on 28 December 2005, 17:53
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
public class RuleNotFoundException extends BudgetManagerException
{
    
    /** Creates a new instance of RuleNotFoundException */
    public RuleNotFoundException( final String message )
    {
        super( message );
    }    
    
}
