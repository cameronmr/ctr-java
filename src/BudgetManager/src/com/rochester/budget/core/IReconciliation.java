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
    public MonetaryValue getValue();
    
    public ITransaction getTransaction();
    
    public ICategory getCatecory();
    
    public IAccount getAccount();
    
    public String getNote();
}
