/*
 * Rule.java
 *
 * Created on 28 December 2005, 17:52
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.core;

import com.rochester.budget.core.IRule.RULE_TYPE;
import com.rochester.budget.core.exceptions.RuleNotFoundException;
import com.rochester.budget.core.exceptions.StateSyncException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Cam
 */
public class Rule extends AbstractDatabaseObject implements IRule
{        
    /** Creates a new instance of Rule */    
    protected Rule( final String pkey ) throws RuleNotFoundException
    {
        super( pkey );
        
        // Attempt to load the account
        try
        {
            load();
        }
        catch ( Exception e )
        {
            throw new RuleNotFoundException( e.toString() );
        }        
    }
    
    protected Rule()
    {
        m_ruleName = "New Rule...";
    }
    
    public String toString()
    {
        return m_ruleName;
    }

    protected void parseResultSet(ResultSet results) throws Exception
    {
        setKey( results.getString( "PKEY" ) );
        m_ruleName = results.getString( "RULE_NAME" );
        m_ruleDescription = results.getString( "RULE_DESCRIPTION" );
        m_ruleType = Enum.valueOf( RULE_TYPE.class, results.getString( "RULE_TYPE" ) );
        
        // Load the criteria
        m_criteria = new ArrayList<IRuleCriterion>( DataObjectFactory.loadCriteriaForRule( this ) );
        m_results = new ArrayList<IRuleResult>( DataObjectFactory.loadResultsForRule( this ) );
        synchronise();
    }
    
    protected void populateResultSet( ResultSet results ) throws Exception
    { 
        // update the database object
        results.updateString( "PKEY", getKey() );
        results.updateString( "RULE_NAME", m_ruleName );
        results.updateString( "RULE_DESCRIPTION", m_ruleDescription );
        results.updateString( "RULE_TYPE", m_ruleType.name() );
        
        // Remove the no longer used criteria
        for ( IRuleCriterion criterion : m_criteria )
        {
            if ( ! m_newCriteria.contains( criterion ) )
            {                
                criterion.delete();
            }
        }    
        
        m_criteria.clear();
        m_criteria.addAll( m_newCriteria );
        
        // Commit the changes
        for ( IRuleCriterion criterion : m_newCriteria )
        {
            criterion.commit();
        }
        
        // Remove the no longer used results
        for ( IRuleResult result : m_results )
        {
            if ( ! m_newResults.contains( result ) )
            {                
                result.delete();
            }
        }    
        
        m_results.clear();
        m_results.addAll( m_newResults );
        
        // Commit the changes
        for ( IRuleResult result : m_newResults )
        {
            result.commit();
        }
    }

    public String getTableName()
    {
        return "RULE";
    }
    
    public String getDescription()
    {
        return m_ruleDescription;
    }
    
    public void setDescription( final String description )
    {        
        m_ruleDescription = new String( description );
        storeMemento();
    }

    public String getName()
    {
        return m_ruleName;
    }
    
    public void setName( final String name )
    {
        m_ruleName = new String( name );
        storeMemento();
    }
    
    public RULE_TYPE getType( )
    {
        return m_ruleType;
    }
        
    public void setType( RULE_TYPE type )
    {
        m_ruleType = type;
        storeMemento();
    }
    
    public boolean matchTransaction( ITransaction transaction )
    {
        if ( m_newCriteria.isEmpty() )
        {
            return false;
        }
        
        // if one criterion fails, then we don't have a match
        for ( IRuleCriterion criterion : m_newCriteria )
        {
            if ( !criterion.matchTransaction( transaction ) )
            {
                return false;
            }
        }
        
        return true;
    }
    
    public void applyRule( ITransaction transaction ) throws Exception
    {
        for ( IRuleResult result : m_results )
        {
            result.applyResult( transaction );
        }
    }
        
    public Collection<IRuleCriterion> getCriteria( )
    {
        return m_newCriteria;
    }
    
    public void setCriteria( Collection<IRuleCriterion> criteria )
    {
        m_newCriteria = new ArrayList( criteria );
        
        storeMemento();
    }
    
    public void addCriterion( IRuleCriterion criteria )
    {
        m_newCriteria.add( criteria );
        
        storeMemento();
    }
    
    public void removeCriterion()
    {
        // Remove the last criteria
        m_newCriteria.remove( m_newCriteria.size() - 1 );
        
        storeMemento();
    }
    
    public Collection<IRuleResult> getResults()
    {
        return m_newResults;
    }
    
    public void setResults( Collection<IRuleResult> results )
    {
        m_newResults = new ArrayList( results );
        
        storeMemento();        
    }
    
    public void addResult( IRuleResult result )
    {
        m_newResults.add( result );
        
        storeMemento();        
    }
    
    public void removeResult()
    {
        // Remove the last criteria
        m_newResults.remove( m_newResults.size() - 1 );
        
        storeMemento();
    }
    
    private boolean criteriaValid( )
    {
        // Rules need at least one criteria
        if ( m_newCriteria.isEmpty() )
        {
            return false;
        }
        
        for ( IRuleCriterion criterion : m_newCriteria )                
        {
            // If one of the criteria is not valid, then return false;
            if ( !criterion.isValid() )
            {
                return false;
            }
        }
                
        return true;
    }
    
    private boolean resultsValid( )
    {
        // Rules need at least one criteria
        if ( m_newResults.isEmpty() )
        {
            return false;
        }
        
        for ( IRuleResult result : m_newResults )                
        {
            // If one of the criteria is not valid, then return false;
            if ( !result.isValid() )
            {
                return false;
            }
        }
                
        return true;
    }
      
    public boolean isValid( )
    {
        // We need at least one criteria
        if ( ! criteriaValid() )
        {
            return false;
        }
        
        // We need at least one result
        if ( ! resultsValid() )
        {
            return false;
        }
        
        // We need at least one outcome
        
        // Check to see if this item is valid at this current time
        return m_ruleName != null &&
                m_ruleDescription != null &&
                m_ruleType != null;
    }
    
    public boolean isModified()
    {
        // We want to check the elements that we are handling for modifications!
        
        // If we are already modified then return
        if ( super.isModified() )
        {
            return true;
        }
        else
        {
            // See if our elements contain any modifications
            for ( IRuleResult result : m_newResults )
            {
                if ( result.isModified() )
                {
                    return true;
                }
            }
            
            for ( IRuleCriterion criterion : m_newCriteria )
            {
                if ( criterion.isModified() )
                {
                    return true;
                }
            }
            
            return false;
        }
    }
    
    public Memento getMemento()
    {
        // Remember that everything that isn't primitive is a reference so make sure to copy 
        // objects that can change
        return new Memento(
                ( m_ruleName == null ) ? null : new String(m_ruleName), 
                ( m_ruleDescription == null ) ? null : new String(m_ruleDescription), 
                ( m_ruleType == null ) ? null : m_ruleType,
                new ArrayList<IRuleCriterion>( m_newCriteria ),
                new ArrayList<IRuleResult>( m_newResults ) );
    }
    
    
    public void restoreMemento( Memento state ) throws StateSyncException
    {
        m_ruleName = (String)state.getSomeState();
        m_ruleDescription = (String)state.getSomeState();
        m_ruleType = (RULE_TYPE)state.getSomeState();
        
        // Load the criteria
        // TODO? load from state?
        try
        {
            m_criteria = new ArrayList<IRuleCriterion>( DataObjectFactory.loadCriteriaForRule( this ) );
        }
        catch( Exception e )
        {}
        
        // Load the results
        try
        {
            m_results = new ArrayList<IRuleResult>( DataObjectFactory.loadResultsForRule( this ) );
        }
        catch( Exception e )
        {}
        
        synchronise();
    }
    
    private void synchronise()
    {
        m_newCriteria.clear();
        m_newCriteria.addAll( m_criteria );
        
        m_newResults.clear();
        m_newResults.addAll( m_results );
    }
    
    private String m_ruleName;
    private String m_ruleDescription; 
    private RULE_TYPE m_ruleType = null;
    private ArrayList<IRuleCriterion> m_criteria = new ArrayList<IRuleCriterion>();
    private ArrayList<IRuleCriterion> m_newCriteria = new ArrayList<IRuleCriterion>();
    private ArrayList<IRuleResult> m_results = new ArrayList<IRuleResult>();
    private ArrayList<IRuleResult> m_newResults = new ArrayList<IRuleResult>();
}
