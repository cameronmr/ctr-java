/*
 * Statement.java
 *
 * Created on 27 December 2005, 16:03
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.core;
import com.rochester.budget.core.IStatement.StatementSummary;
import com.rochester.budget.core.exceptions.StateSyncException;
import com.rochester.budget.core.exceptions.StatementNotFoundException;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.Collection;

/**
 *
 * @author Cam
 */
public class Statement extends AbstractDatabaseObject implements IStatement
{
    // Protected to ensure no access outside this package
    protected Statement( final String pkey ) throws StatementNotFoundException
    {
        super( pkey );
        
        // Attempt to load the account
        try
        {
            load();
        }
        catch ( Exception e )
        {
            throw new StatementNotFoundException( e.toString() );
        }        
    }
    
    protected Statement( IAccount account )
    {
        setAccount( account );
    }
    
    public String toString()
    {
        if ( m_beginDate == null ||
             m_endDate == null )
        {
            return "New Statement...";
        }
        else
        {
            return m_beginDate + " to " + m_endDate;
        }
    }

    protected void parseResultSet(ResultSet results) throws Exception
    {
        setKey( results.getString( "PKEY" ) );
        m_accountKey = results.getString( "STATEMENT_ACC_FKEY" );
        m_beginDate = results.getDate( "STATEMENT_BEGIN" );
        m_endDate = results.getDate( "STATEMENT_END" );
    }
    
    protected void populateResultSet( ResultSet results ) throws Exception
    { 
        // update the database object
        results.updateString( "PKEY", getKey() );
        results.updateString( "STATEMENT_ACC_FKEY", m_accountKey );
        results.updateDate( "STATEMENT_BEGIN", m_beginDate );
        results.updateDate( "STATEMENT_END", m_endDate );
    }

    public String getTableName()
    {
        return "STATEMENT";
    }
        
    public IAccount getAccount()
    { 
        if ( null == m_account )
        {
            try
            {
                m_account = DataObjectFactory.loadAccount( m_accountKey );
            }
            catch ( Exception e )
            {
                // TODO: this should not happen... but what if it does?
            }
        }
        return m_account;
    }
    
    public void setAccount( IAccount account )
    {
        if ( null != account )
        {
            m_account = account;
            m_accountKey = m_account.getKey();
            
            storeMemento();
        }        
    }
    
    public Date getStatementStart()
    {
        return m_beginDate;
    }
    
    public void setStatementStart( final Date startDate )
    {
        m_beginDate = startDate;
        
        storeMemento();
        
        // Clear the reconciliations
        m_reconciliations = null;
    }
    
    public Date getStatementEnd()
    {
        return m_endDate;
    }
    
    public void setStatementEnd( final Date endDate )
    {
        m_endDate = endDate;
        
        storeMemento();
        
        // Clear the reconciliations
        m_reconciliations = null;
    }
    
    public Collection<IAccount> getAccounts( final ICategory category, boolean flat )
    {
        loadReconciliations();
        
        // get the accounts used by this categories reconciliations
        
        // If isFlat get the transactions
        
        // if not flat, get the summary for category & all descendants
        return null;
    }
    
    public StatementSummary getSummary( final IAccount account, boolean flat )
    {
        loadReconciliations();
        
        // filter the list of reconciliations based on the account
        return null;
    }
    
    public StatementSummary getSummary( )
    {
        loadReconciliations();
        
        // Load the list of reconciliations for this statement period
        StatementSummary summary = new StatementSummary();
        for ( IReconciliation recon : m_reconciliations )
        {
            summary.addReconciliation(recon);
        }
        
        return summary;
    }
      
    public boolean isValid( )
    {
        // Check to see if this item is valid at this current time
        return m_accountKey != null &&
               m_beginDate != null &&
               m_endDate != null;
                
    }
    
    public Memento getMemento()
    {
        // Remember that everything that isn't primitive is a reference so make sure to copy 
        // objects that can change
        return new Memento(
                ( m_accountKey == null ) ? null : new String( m_accountKey ),
                ( m_beginDate == null ) ? null : new Date( m_beginDate.getTime() ), 
                ( m_endDate == null ) ? null : new Date( m_endDate.getTime() ));
    }
    
    
    public void restoreMemento( Memento state ) throws StateSyncException
    {
        m_accountKey = (String)state.getSomeState();
        try
        {
            m_account = DataObjectFactory.loadAccount( m_accountKey );
        }
        catch( Exception e )
        {
            // TODO??
        }
        m_beginDate = (Date)state.getSomeState();
        m_endDate = (Date)state.getSomeState();
    }
    
    private void loadReconciliations()
    {
        if ( null == m_reconciliations )
        {
            m_reconciliations = DataObjectFactory.loadReconciliationsForStatement( this );
        }
    }
    
    private IAccount m_account = null;
    private String m_accountKey = null;
    private Date m_beginDate = null;
    private Date m_endDate = null;
    private Collection<IReconciliation> m_reconciliations;
}
