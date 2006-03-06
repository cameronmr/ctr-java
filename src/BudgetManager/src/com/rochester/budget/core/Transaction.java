/*
 * Transaction.java
 *
 * Created on 9 February 2005, 21:01
 */

package com.rochester.budget.core;
import com.rochester.budget.core.IDataChangeObserver.ChangeType;
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
public class Transaction extends AbstractDatabaseObject implements ITransaction, IDataChangeObserver
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
    
    protected Transaction( String narrative, IAccount account, MonetaryValue value, Date date )
    {
        m_value = value;
        setNarrative( narrative );
        setAccount( account );
        setDate( date );
    }
    
    public String getNarrative()
    {
        return m_narrative;
    }
    
    public void setNarrative( final String narrative )
    {
        m_narrative = narrative;
        
        storeMemento();
    }
    
    public String getNote()
    {
        return m_note;
    }
    
    public void setNote( final String note )
    {
        m_note = note;
        
        storeMemento();
    }
                
    public MonetaryValue getValue()
    {
        return m_value;
    }
    
    public void setValue( final MonetaryValue value )
    {
        m_value.setValue( value );
        
        storeMemento();
    }
    
    public Date getDate()
    {        
        return m_date;
    }
    
    public void setDate( final Date date )
    {
        m_date = date;
        
        storeMemento();
    }
        
    public IAccount getAccount()
    {
        return m_account;
    }   
    
    public void setAccount( final IAccount account )
    {
        m_account = account;
        
        storeMemento();
    }        
    
    public void addReconciliation( IReconciliation reconciliation ) throws BudgetManagerException
    {           
        if ( ! m_reconciliations.contains( reconciliation ) )
        {
            m_reconciliations.add( reconciliation );   
            
            // Observer the reconciliation so that we are notified when the reconciliation value changes
            reconciliation.addObserver( this );
        }
                
        /* Attempt to subtract the reconciliation - this will throw an exception if too much */
        calculateValueRemaining();        
        
        storeMemento();
    }
    
    // This can not be called externally, explicitly delete the reconciliation
    private void removeReconciliation(IReconciliation reconciliation)
    {
        if ( m_reconciliations.remove( reconciliation ) )
        {            
            storeMemento();
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
                m_reconciliations.isEmpty() ||
                m_valueRemaining.equals( m_value ) )
        {
            return ReconciliationState.NONE;
        }
        
        if ( m_valueRemaining.isZero() )
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
            return getNarrative() + " (" + getDate().toString() + " - " + getValue() + ")";
        }
        catch ( Throwable t )
        {
            return "Nothing to see here, move along";
        }
    }   
    
    /**
     * Listen for changes to reconciliations!
     */
    public void notifyDatabaseChange( ChangeType change, IDatabaseObject object ) throws BudgetManagerException
    {
        // If the update is for a reconciliation...
        if ( object instanceof IReconciliation )
        {
            switch ( change )
            {
                case UPDATE:
                    // Recalculate the remaining value
                    calculateValueRemaining();
                    break;
                case DELETE:
                    // Remove from the list
                    removeReconciliation( (IReconciliation)object );
                    calculateValueRemaining();
                    break;
            }
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
        // update the database object
        results.updateString( "PKEY", getKey() );
        results.updateDate( "TRANS_DATE", m_date );
        results.updateString( "TRANS_NARRATIVE", m_narrative );
        results.updateString( "TRANS_NOTE", m_note );        
        results.updateInt( "TRANS_VALUE", m_value.getCentsAsInt() );
        results.updateString( "TRANS_ACCOUNT_FKEY", m_account.getKey() );
        
    }

    public String getTableName()
    {
        return "TRANSACTION";
    }   

    public MonetaryValue getValueRemaining()
    {
        return m_valueRemaining;
    }
        
    public boolean isValid( )
    {
        // Check to see if this item is valid at this current time
        return /*m_note != null &&*/
                m_value != null &&
                m_account != null &&
                m_narrative != null &&
                m_date != null;        
    }
    
    public Memento getMemento()
    {
        return new Memento(
                (m_note == null) ? null : new String( m_note ),
                (m_value == null) ? null : new MonetaryValue( m_value ), 
                m_account,
                (m_narrative == null) ? null : new String( m_narrative ), 
                (m_date == null) ? null : new Date( m_date.getTime() ),
                new ArrayList<IReconciliation>( m_reconciliations ) );
    }
    
    public void restoreMemento( Memento state ) throws StateSyncException
    {
        m_note = (String)state.getSomeState();
        m_value = (MonetaryValue)state.getSomeState();
        m_account = (IAccount)state.getSomeState();
        m_narrative = (String)state.getSomeState();
        m_date = (Date)state.getSomeState();
        
        // Reconcile differences between items in container
        ArrayList<IReconciliation> src = (ArrayList<IReconciliation>)state.getSomeState();
        reconcileObjectList( src, m_reconciliations );
        m_reconciliations = src;
        
        try
        {
            calculateValueRemaining();
        }
        catch( Exception e )
        {
            // TODO;
        }
    }
        
    private void calculateValueRemaining() throws BudgetManagerException
    {
        MonetaryValue originalValue = new MonetaryValue( m_valueRemaining );
        m_valueRemaining.setValue( m_value );
        
        // Go through the list of reconciliations and calculate the value remaining
        for ( IReconciliation recon : m_reconciliations )
        {
            if ( recon.isValid() )
            {
                m_valueRemaining.subtractValue( recon.getValue() );
            }
        }
        
        if ( ! m_valueRemaining.equals( originalValue ) )
        {
            /* Notify any observers that the value reconciliation has changed */
            notifyObservers( ChangeType.UPDATE );
        }
    }
    
    private String m_note;
    private MonetaryValue m_value;
    private MonetaryValue m_valueRemaining;
    private IAccount m_account;
    private String m_narrative;
    private Date m_date;
    private ArrayList<IReconciliation> m_reconciliations = new ArrayList<IReconciliation>();    
}
