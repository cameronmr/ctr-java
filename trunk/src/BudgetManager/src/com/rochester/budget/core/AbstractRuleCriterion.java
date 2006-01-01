/*
 * AbstractRuleCriterion.java
 *
 * Created on 29 December 2005, 12:45
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.core;

import com.rochester.budget.core.IRule.RULE_TYPE;
import com.rochester.budget.core.exceptions.RuleCriterionNotFoundException;
import com.rochester.budget.core.exceptions.StateSyncException;
import java.sql.ResultSet;

/**
 *
 * @author Cam
 */
public abstract class AbstractRuleCriterion extends AbstractDatabaseObject implements IRuleCriterion
{
         
    /** Creates a new instance of Rule */    
    protected AbstractRuleCriterion( final String pkey, final String criteria  ) throws RuleCriterionNotFoundException
    {
        super( pkey );
        m_criteria = criteria;
        
        // Attempt to load the account
        try
        {
            load();
        }
        catch ( Exception e )
        {
            throw new RuleCriterionNotFoundException( e.toString() );
        }        
    }
    
    protected AbstractRuleCriterion( final String criteria )
    {
        m_criteria = criteria;
    }
    
    protected void parseResultSet(ResultSet results) throws Exception
    {
        setKey( results.getString( "PKEY" ) );
        m_criteriaType = results.getString( "CRITERIA_TYPE" );
        m_criteria = results.getString( "CRITERIA" );
        m_criteriaValue = results.getString( "CRITERIA_VALUE" );
        m_ruleKey = results.getString( "RULE_FKEY" );        
    }
    
    protected void populateResultSet( ResultSet results ) throws Exception
    { 
        // update the database object
        results.updateString( "PKEY", getKey() );
        results.updateString( "CRITERIA_TYPE", m_criteriaType );
        results.updateString( "CRITERIA", m_criteria );
        results.updateString( "CRITERIA_VALUE", m_criteriaValue );
        results.updateString( "RULE_FKEY", m_ruleKey );
    }

    public String getTableName()
    {
        return "RULE_CRITERIA";
    }
    
    public String toString()
    {
        return m_criteria;
    }
    
    public String getCriteria()
    {
        return m_criteria;
    }
          
    public String getSelectedMatchType()
    {
        return m_criteriaType;
    }
    
    public void setMatchType( final String matchType )
    {
        m_criteriaType = matchType;
        
        storeMemento();
    }
    
    public String getMatchValue( )
    {
        return m_criteriaValue;
    }
    
    public void setMatchValue( final String value )
    {
        m_criteriaValue = value;
        
        storeMemento();
    }
    
    public void setRule( IRule rule )
    {
        m_ruleKey = rule.getKey();
        
        storeMemento();
    }
    
    public boolean isValid( )
    {
        // Check to see if this item is valid at this current time
        return m_criteriaType != null &&
                m_criteria != null &&
                m_criteriaValue != null &&
                m_ruleKey != null;
    }
    
    public Memento getMemento()
    {
        // Remember that everything that isn't primitive is a reference so make sure to copy 
        // objects that can change
        return new Memento(
                ( m_criteriaType == null ) ? null : new String( m_criteriaType ), 
                ( m_criteria == null ) ? null : new String( m_criteria ), 
                ( m_criteriaValue == null ) ? null : new String( m_criteriaValue ), 
                ( m_ruleKey == null ) ? null : new String( m_ruleKey ) );
    }    
    
    public void restoreMemento( Memento state ) throws StateSyncException
    {
        m_criteriaType = (String)state.getSomeState();
        m_criteria = (String)state.getSomeState();
        m_criteriaValue = (String)state.getSomeState();
        m_ruleKey = (String)state.getSomeState();
    }
    
    private String m_criteriaType;
    private String m_criteria;
    private String m_criteriaValue;    
    private String m_ruleKey;        
}
