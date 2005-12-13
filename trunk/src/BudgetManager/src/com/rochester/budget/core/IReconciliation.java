/*
 * IReconciliation.java
 *
 * Created on 23 May 2005, 13:40
 *
 */

package com.rochester.budget.core;

/**
 *
 * @author Cam
 */
public interface IReconciliation extends IDatabaseObject
{
    MonetaryValue getValue();
    void setValue( final MonetaryValue value );
    
    ITransaction getTransaction();
    void setTransaction( final ITransaction trans );           
    
    ICategory getCatecory();          
    void setCategory( final ICategory category );
        
    String getNote();
    void setNote( final String note );
}
