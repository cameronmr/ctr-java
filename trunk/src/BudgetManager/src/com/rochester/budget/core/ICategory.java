/*
 * ICategory.java
 *
 * Created on 9 February 2005, 20:44
 */

package com.rochester.budget.core;

/**
 *
 * @author Cameron
 */
public interface ICategory
{
    String getName();
    
    String getDescription();
    
    ICategory getParent();
    
    IAccount getAccount();
}
