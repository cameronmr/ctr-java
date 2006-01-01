/*
 * IRule.java
 *
 * Created on 28 December 2005, 17:29
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.core;

import java.util.Collection;

/**
 *
 * @author Cam
 */
public interface IRule extends IDatabaseObject
{
    public enum RULE_TYPE
    {
        ANY { 
            public String toString()
            {
                return "Any";
            }
        },
        ALL { 
            public String toString()
            {
                return "All";
            }
        }
    };
    
    String getName();
    void setName( final String name );
    
    String getDescription();
    void setDescription( final String description );
    
    RULE_TYPE getType( );
    void setType( RULE_TYPE type );
    
    boolean matchTransaction( ITransaction transaction );
    void applyRule( ITransaction transaction );
    
    Collection<IRuleCriterion> getCriteria( );
    void setCriteria( Collection<IRuleCriterion> criteria );
    
    boolean criteriaValid( );
}
