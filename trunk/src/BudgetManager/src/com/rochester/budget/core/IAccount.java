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
    String getNumber();
    void setNumber( final String number );
    
    String getName();
    void setName( final String name );
    
    String getDescription();
    void setDescription( final String description );
}
