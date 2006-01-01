/*
 * RuleResult.java
 *
 * Created on 29 December 2005, 12:46
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.core;

import com.rochester.budget.core.IRule.RULE_TYPE;
import com.rochester.budget.core.IRuleResult.RESULT_TYPE;
import com.rochester.budget.core.exceptions.RuleResultNotFoundException;
import com.rochester.budget.core.exceptions.StateSyncException;
import java.sql.ResultSet;

/**
 *
 * @author Cam
 */
public class RuleResult extends AbstractDatabaseObject implements IRuleResult
{
         
    /** Creates a new instance of Rule */    
    protected RuleResult( final String pkey ) throws RuleResultNotFoundException
    {
        super( pkey );
        
        // Attempt to load the account
        try
        {
            load();
        }
        catch ( Exception e )
        {
            throw new RuleResultNotFoundException( e.toString() );
        }        
    }
    
    protected RuleResult()
    {
    }
    
    protected void parseResultSet(ResultSet results) throws Exception
    {
        setKey( results.getString( "PKEY" ) );
        m_resultAmount = new MonetaryValue( results.getInt( "RESULT_AMOUNT" ) );
        m_resultType = Enum.valueOf( RESULT_TYPE.class, results.getString( "RESULT_TYPE" ) );
    }
    
    protected void populateResultSet( ResultSet results ) throws Exception
    { 
        // update the database object
        results.updateString( "PKEY", getKey() );
        results.updateInt( "RESULT_AMOUNT", m_resultAmount.getCentsAsInt() );
        results.updateString( "RESULT_TYPE", m_resultType.name() );
    }

    public String getTableName()
    {
        return "RULE_RESULT";
    }
    
    public RESULT_TYPE getType( )
    {
        return m_resultType;
    }
    
    public void setType( RESULT_TYPE type )
    {
        m_resultType = type;
        storeMemento();
    }
    
    public void applyResult( ITransaction transaction )
    {
        // TODO
    }
    
    public boolean isValid( )
    {
        // Check to see if this item is valid at this current time
        return m_resultType != null &&
                ( m_resultType == RESULT_TYPE.FULL && m_resultAmount != null );
    }
    
    public Memento getMemento()
    {
        // Remember that everything that isn't primitive is a reference so make sure to copy 
        // objects that can change
        return new Memento(
                ( m_resultAmount == null ) ? null : new MonetaryValue( m_resultAmount ), 
                ( m_resultType == null ) ? null : m_resultType );
    }
    
    
    public void restoreMemento( Memento state ) throws StateSyncException
    {
        m_resultAmount = (MonetaryValue)state.getSomeState();
        m_resultType = (RESULT_TYPE)state.getSomeState();
    }
    
    private MonetaryValue m_resultAmount; 
    private RESULT_TYPE m_resultType = null;
    
}
