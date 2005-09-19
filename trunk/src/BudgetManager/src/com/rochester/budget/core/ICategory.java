/*
 * ICategory.java
 *
 * Created on 9 February 2005, 20:44
 */

package com.rochester.budget.core;

import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Cameron
 */
public interface ICategory extends IDatabaseObject
{
    static final Comparator<ICategory> CATEGORY_NAME_ORDER = 
                                 new Comparator<ICategory>()
    {
        public int compare(ICategory c1, ICategory c2) 
        {
            return c1.toString().compareTo( c2.toString() );
        }
    };
    
    String getName();
    
    String getDescription();
    
    ICategory getParent();
    
    IAccount getAccount();
    
    void addCategory( ICategory category );
    
    boolean hasChildren( );
    
    List<ICategory> getChildren();
    
    List<ICategory> getDescendants();    
    
    int getChildCount();
}
