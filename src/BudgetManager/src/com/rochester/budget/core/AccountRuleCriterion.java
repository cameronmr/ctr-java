/*
 * AccountRuleCriterion.java
 *
 * Created on 1 January 2006, 15:08
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
public class AccountRuleCriterion extends AbstractRuleCriterion
{        
    enum MATCH_TYPE
    {
        IS
        {
            public String toString()
            {
                return "is";
            }
        }
    };
    
    /**
     * Creates a new instance of DescriptionRuleCriterion 
     */
    /** Creates a new instance of AccountRuleCriterion */
    public AccountRuleCriterion()
    {
        super( m_criteria );
        
        // Set the default
        setMatchType( MATCH_TYPE.IS.name() );
    }
    
    protected AccountRuleCriterion( final String pkey ) throws RuleCriterionNotFoundException
    {
        super( pkey, m_criteria );
    }
    
    public IRuleCriterion getNew( final String pkey ) throws RuleCriterionNotFoundException
    {
        return new AccountRuleCriterion( pkey );
    }
    
    public IRuleCriterion getNew( )
    {
        return new AccountRuleCriterion();
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
        switch ( MATCH_TYPE.valueOf( getSelectedMatchType() ) )
        {
            case IS:
                return transaction.getAccount().getNumber( ).equals( getMatchValue() );
        }
        
        return false;
    }
    
    private static final String m_criteria = "Account";    
}
