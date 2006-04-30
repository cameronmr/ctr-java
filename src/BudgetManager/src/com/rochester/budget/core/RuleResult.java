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
    
    protected RuleResult( final IRule rule )
    {
        m_ruleKey = rule.getKey();
        m_resultAmount = new MonetaryValue( 0 );
        m_resultType = RESULT_TYPE.FULL;
    }
    
    protected void parseResultSet(ResultSet results) throws Exception
    {
        setKey( results.getString( "PKEY" ) );
        m_resultAmount = new MonetaryValue( results.getInt( "RESULT_AMOUNT" ) );
        m_resultType = Enum.valueOf( RESULT_TYPE.class, results.getString( "RESULT_TYPE" ) );
        m_category = DataObjectFactory.loadCategory( results.getString( "CATEGORY_FKEY" ) );
        m_ruleKey = results.getString( "RULE_FKEY" );
    }
    
    protected void populateResultSet( ResultSet results ) throws Exception
    { 
        // update the database object
        results.updateString( "PKEY", getKey() );
        results.updateInt( "RESULT_AMOUNT", m_resultAmount.getCentsAsInt() );
        results.updateString( "RESULT_TYPE", m_resultType.name() );
        results.updateString( "CATEGORY_FKEY", m_category.getKey() );
        results.updateString( "RULE_FKEY", m_ruleKey );
    }

    public String getTableName()
    {
        return "RULE_RESULT";
    }
    
    public RESULT_TYPE getResultType( )
    {
        return m_resultType;
    }
    
    public Enum[] getResultTypes()
    {
        return RESULT_TYPE.values();
    }           
    
    public void setResultType( RESULT_TYPE type )
    {
        m_resultType = type;
        storeMemento();
    }
    
    public void applyResult( ITransaction transaction, final String message ) throws Exception
    {
        // we need to reconcile the transaction
        // By default the amount will be fully reconciled
        IReconciliation recon = DataObjectFactory.newReconciliationForTransaction( transaction );
        if ( m_resultType != RESULT_TYPE.FULL )
        {
            recon.setValue( m_resultAmount );
        }
        
        recon.setCategory( m_category );   
        recon.setNote( "Automated Rule: " + message );
        
        transaction.addReconciliation( recon );
        recon.commit();
    }
    
    
    public void setReconciliationValue( final MonetaryValue value )
    {
        m_resultAmount = value;
        
        storeMemento();
    }
    
    public MonetaryValue getReconciliationValue( )
    {
        return m_resultAmount;
    }
    
    public ICategory getReconciliationCategory( )
    {
        return m_category;
    }
    
    public void setReconciliationCategory( final ICategory category )
    {
        m_category = category;
        
        storeMemento();
    }
    
    public boolean isValid( )
    {
        // Check to see if this item is valid at this current time
        return m_resultType != null &&
                m_resultAmount != null &&
                m_category != null &&
                m_ruleKey != null;
    }
    
    public Memento getMemento()
    {
        // Remember that everything that isn't primitive is a reference so make sure to copy 
        // objects that can change
        return new Memento(
                ( m_resultAmount == null ) ? null : new MonetaryValue( m_resultAmount ), 
                ( m_resultType == null ) ? null : m_resultType, 
                ( m_category == null ) ? null : new String(m_category.getKey()),                 
                ( m_ruleKey == null ) ? null : new String(m_ruleKey) );
    }
       
    public void restoreMemento( Memento state ) throws StateSyncException
    {
        m_resultAmount = (MonetaryValue)state.getSomeState();
        m_resultType = (RESULT_TYPE)state.getSomeState();
        m_ruleKey = (String)state.getSomeState();
        try
        {
            m_category = DataObjectFactory.loadCategory( (String)state.getSomeState() );
        }
        catch( Exception e )
        {
            throw new StateSyncException( e.getMessage() );
        }
    }
    
    private MonetaryValue m_resultAmount; 
    private RESULT_TYPE m_resultType;   
    private ICategory m_category;
    private String m_ruleKey;        
}
