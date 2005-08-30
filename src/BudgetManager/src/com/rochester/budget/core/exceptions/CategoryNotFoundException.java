/*
 * CategoryNotFoundException.java
 *
 * Created on 4 July 2005, 19:50
 *
 */

package com.rochester.budget.core.exceptions;

/**
 *
 * @author Cam
 */
public class CategoryNotFoundException extends BudgetManagerException
{
    
    /** Creates a new instance of AccountNotFoundException */
    public CategoryNotFoundException( final String message )
    {
        super( message );
    }    
}
