/*
 * IAccount.java
 *
 * Created on 9 February 2005, 20:56
 */

package com.rochester.budget.core;

/**
 *
 * @author Cameron
 */
public interface IAccount extends IDatabaseObject
{
    String getAccountNumber();
    
    String getAccountName();
    
    String getAccountDescription();
}
