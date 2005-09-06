/*
 * ICategory.java
 *
 * Created on 9 February 2005, 20:44
 */

package com.rochester.budget.core;

import java.util.List;

/**
 *
 * @author Cameron
 */
public interface ICategory extends IDatabaseObject
{
    String getName();
    
    String getDescription();
    
    ICategory getParent();
    
    IAccount getAccount();
    
    void addCategory( ICategory category );
    
    boolean hasChildren( );
    
    List<ICategory> getChildren();
    
    int getChildCount();
}
