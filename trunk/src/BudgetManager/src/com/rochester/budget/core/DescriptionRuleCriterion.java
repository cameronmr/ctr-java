/*
 * DescriptionRuleCriterion.java
 *
 * Created on 29 December 2005, 14:24
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
public class DescriptionRuleCriterion extends AbstractRuleCriterion
{    
    enum MATCH_TYPE
    {
        CONTAINS
        {
            public String toString()
            {
                return "contains";
            }
        },
        DOES_NOT_CONTAIN
        {            
            public String toString()
            {
                return "doesn't contain";
            }
        }
    };
    
    /**
     * Creates a new instance of DescriptionRuleCriterion 
     */
    protected DescriptionRuleCriterion()
    {
        super( m_criteria );
        
        // Set the default
        setMatchType( MATCH_TYPE.CONTAINS.name() );
    }
    
    protected DescriptionRuleCriterion( final String pkey ) throws RuleCriterionNotFoundException
    {
        super( pkey, m_criteria );
    }
    
    public IRuleCriterion getNew( final String pkey ) throws RuleCriterionNotFoundException
    {
        return new DescriptionRuleCriterion( pkey );
    }
    
    public IRuleCriterion getNew( )
    {
        return new DescriptionRuleCriterion();
    }
        
    public Enum[] getMatchTypes()
    {
        return MATCH_TYPE.values();
    }           
    
    // Allows us to have a specific editor
    public Class getMatchValueClass()
    {
        return String.class;
    }    
    
    public boolean matchTransaction( ITransaction transaction )
    {
        switch ( MATCH_TYPE.valueOf( getMatchType() ) )
        {
            case CONTAINS:
                return transaction.getNarrative().toLowerCase().indexOf( getMatchValue().toLowerCase() ) >= 0;
            case DOES_NOT_CONTAIN:
                return transaction.getNarrative().toLowerCase().indexOf( getMatchValue().toLowerCase() ) < 0;
        }
        
        return false;
    }
    
    private static final String m_criteria = "Description";
}
