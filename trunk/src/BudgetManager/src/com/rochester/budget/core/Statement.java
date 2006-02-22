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
import com.rochester.budget.core.IDataChangeObserver.ChangeType;
import com.rochester.budget.core.IStatement.StatementSummary;
import com.rochester.budget.core.exceptions.BudgetManagerException;
import com.rochester.budget.core.exceptions.StateSyncException;
import com.rochester.budget.core.exceptions.StatementNotFoundException;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Cam
 */
public class Statement extends AbstractDatabaseObject implements IStatement, IDataChangeObserver
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
        setTransactionAccount( account );
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
        
        loadTransactions();
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
        
    public IAccount getTransactionAccount()
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
    
    public void setTransactionAccount( IAccount account )
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
        
        // reload the Transactions
        loadTransactions();
    }
    
    public Date getStatementEnd()
    {
        return m_endDate;
    }
    
    public void setStatementEnd( final Date endDate )
    {
        m_endDate = endDate;
        
        storeMemento();
        
        // reload the Transactions
        loadTransactions();
    }
    
    public Collection<IAccount> getAccounts( final ICategory category, boolean flat )
    {        
        // get the accounts used by this categories reconciliations
        
        // If isFlat get the transactions
        
        // if not flat, get the summary for category & all descendants
        return null;
    }
    
    public StatementSummary getSummary( final ICategory category, boolean flat )
    {        
        // filter the list of reconciliations based on the account
        return null;
    }
    
    public StatementSummary getSummary( )
    {
        Collection<IReconciliation> reconciliations = loadReconciliations();
        
        // Load the list of reconciliations for this statement period
        StatementSummary summary = new StatementSummary();
        for ( IReconciliation recon : reconciliations )
        {
            summary.addReconciliation( recon );
        }
        
        return summary;
    }
    
    public StatementSummary getTransactionsSummary( )
    {        
        // Load the list of reconciliations for this statement period
        StatementSummary summary = new StatementSummary();
        for ( ITransaction trans : m_transactions )
        {
            trans.addObserver( this );
            summary.addValue( trans.getMonetaryValue() );
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
    
    public void notifyDatabaseChange( ChangeType change, IDatabaseObject object ) throws BudgetManagerException
    {
        switch ( change )
        {
            case UPDATE:
                m_reconciliations = null;
                break;
        }
        
        // Notify the panel that we have received an udpate from the transactions
        notifyObservers( ChangeType.UPDATE );
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
    
    private Collection<IReconciliation> loadReconciliations()
    {
        if ( m_beginDate != null &&
             m_endDate != null ) 
        {
            return DataObjectFactory.loadReconciliationsForStatement( this );
        }
       
        return new ArrayList<IReconciliation>();
    }
    
    private void loadTransactions()
    {        
        if ( m_beginDate == null ||
                m_endDate == null )
        {
            return;
        }
        
        if ( null != m_transactions )
        {
            for ( ITransaction trans : m_transactions )
            {
                trans.deleteObserver( this );
            }
        }
        
        m_transactions = DataObjectFactory.loadTransactionsForStatement( this );   
        
        for ( ITransaction trans : m_transactions )
        {
            trans.addObserver( this );
        }
    }
    
    private IAccount m_account = null;
    private String m_accountKey = null;
    private Date m_beginDate = null;
    private Date m_endDate = null;
    private Collection<IReconciliation> m_reconciliations;
    private Collection<ITransaction> m_transactions = new ArrayList<ITransaction>();
}
