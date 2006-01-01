/*
 * IRuleCriterion.java
 *
 * Created on 29 December 2005, 12:44
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.core;
import com.rochester.budget.core.exceptions.RuleCriterionNotFoundException;

/**
 *
 * @author Cam
 */
public interface IRuleCriterion extends IDatabaseObject
{
    IRuleCriterion getNew( final String pkey ) throws RuleCriterionNotFoundException;
    IRuleCriterion getNew( );
    boolean matchTransaction( ITransaction transaction );
    
    String getCriteria();
    
    Enum[] getMatchTypes();
    String getSelectedMatchType();
    void setMatchType( final String matchType );
    
    // Allows us to have a specific editor
    Class getMatchValueClass();
    
    String getMatchValue( );
    void setMatchValue( final String value );
    
    void setRule( IRule rule );
}
