/*
 * Transaction.java
 *
 * Created on 9 February 2005, 21:01
 */

package com.rochester.budget.core;
import com.rochester.budget.core.ITransaction.ReconciliationState;
import com.rochester.budget.core.exceptions.BudgetManagerException;
import com.rochester.budget.core.exceptions.StateSyncException;
import com.rochester.budget.core.exceptions.TransactionNotFoundException;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;



/**
 * A transaction, once created, can not be deleted. It observes reconciliations to monitor the current reconciliation state
 * @author Cameron
 */
public class Transaction extends AbstractDatabaseObject implements ITransaction
{       
    // Protected to ensure no access outside this package
    protected Transaction( final String transactionKey ) throws TransactionNotFoundException
    {
        super( transactionKey );
        
        // Attempt to load the account
        try
        {
            load();
        }
        catch ( Exception e )
        {
            throw new TransactionNotFoundException( e.toString() );
        }        
    }
    
    protected Transaction( )
    {
    }
    
    public String getNarrative()
    {
        return m_narrative;
    }
    
    public void setNarrative( final String narrative )
    {
        m_narrative = narrative;
    }
    
    public String getNote()
    {
        return m_note;
    }
    
    public void setNote( final String note )
    {
        m_note = note;
    }
                
    public MonetaryValue getMonetaryValue()
    {
        return m_value;
    }
    
    public void setMonetaryValue( final MonetaryValue value )
    {
        m_value = value;
    }
    
    public Date getDate()
    {        
        return m_date;
    }
    
    public void setDate( final Date date )
    {
        m_date = date;
    }
    
    static public String[] getColumns()
    {
        String[] columnNames = { "Reconciled", "Date", "Account", "Description", "Value"};
        return columnNames;
    }
    
    public IAccount getAccount()
    {
        return m_account;
    }   
    
    public void setAccount( final IAccount account )
    {
        m_account = account;
    }        
    
    public void addReconciliation( IReconciliation reconciliation ) throws BudgetManagerException
    {   
        /* Attempt to subtract the reconciliation - this will throw an exception if too much */
        m_valueRemaining.subtractValue( reconciliation.getValue() );
        
        m_reconciliations.add( reconciliation );
        
        /* Notify any observers that a reconciliation has been added */
        notifyObservers();
    }
    
    public void removeReconciliation(IReconciliation reconciliation)
    {
        if ( m_reconciliations.remove( reconciliation ) )
        {
            /* If the reconciliation existed, we remove it from the remaining value */
            m_valueRemaining.addValue( reconciliation.getValue() );
                    
            /* Notify any observers that a reconciliation has been removed */
            notifyObservers();
        }
    }
    
    public ArrayList<IReconciliation> getReconciliations()
    {
        return m_reconciliations;
    }

    public ReconciliationState getReconciliationState()
    {
        // If there are no reconciliations at all then it is easy
        if ( null == m_reconciliations ||
                m_reconciliations.isEmpty() )
        {
            return ReconciliationState.NONE;
        }
        
        else if ( m_valueRemaining.isZero() )
        {
            return ReconciliationState.FULL;
        }
        else
        {
            return ReconciliationState.PARTIAL;
        }
    }
    
    public String toString()
    {
        try
        {
            return getNarrative() + " (" + getDate().toString() + " - " + getMonetaryValue() + ")";
        }
        catch ( Throwable t )
        {
            return "Nothing to see here, move along";
        }
    }   
    
    protected void parseResultSet( ResultSet results ) throws Exception
    {        
        // Reload the database object
        m_date = results.getDate( "TRANS_DATE" );
        m_narrative = results.getString( "TRANS_NARRATIVE" );
        m_note = results.getString( "TRANS_NOTE" );
        m_value = new MonetaryValue( results.getInt( "TRANS_VALUE" ) );
        
        // The remaining value is the same as the transaction value initially
        m_valueRemaining = new MonetaryValue( m_value );
        
        // Load the account
        m_account = DataObjectFactory.loadAccount( results.getString( "TRANS_ACCOUNT_FKEY" ) );
        
        // Load reconciliations 
        DataObjectFactory.loadReconciliationsForTransaction( this );
    }
    
    protected void populateResultSet( ResultSet results ) throws Exception
    { 
        // TODO
    }

    public String getTableName()
    {
        return "TRANSACTION";
    }
            
    /** 
     * stuff for displaying values in the JTable
     **/
    public Object getValue( int col )
    {
        switch ( col )
        {
            case 0:
                return getReconciliationState();
            case 1:
                return m_date;
            case 2:
                return m_account.getAccountName();
            case 3:
                return m_narrative;
            case 4:
                return m_value;
        }
        return "unknown";
    }

    public MonetaryValue getValueRemaining()
    {
        return m_valueRemaining;
    }
        
    public boolean isValid( )
    {
        // Check to see if this item is valid at this current time
        return m_note != null &&
                m_value != null &&
                m_account != null &&
                m_narrative != null &&
                m_date != null;
        
        // TODO: do we care about reconciliations here?
    }
    
    public Memento getMemento()
    {
        //TODO
        return null;//new Memento( );
    }
    
    public void restoreMemento( Memento state ) throws StateSyncException
    {
        //TODO
    }
    
    private String m_note;
    private MonetaryValue m_value;
    private MonetaryValue m_valueRemaining;
    private IAccount m_account;
    private String m_narrative;
    private Date m_date;
    private ArrayList<IReconciliation> m_reconciliations = new ArrayList<IReconciliation>();
}
