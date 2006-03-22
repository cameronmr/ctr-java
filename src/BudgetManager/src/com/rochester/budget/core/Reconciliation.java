/*
 * Reconciliation.java
 *
 * Created on 23 May 2005, 15:51
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.core;
import com.rochester.budget.core.exceptions.ReconciliationNotFoundException;
import com.rochester.budget.core.exceptions.StateSyncException;
import java.sql.Date;
import java.sql.ResultSet;


/**
 *
 * @author Cam
 */
public class Reconciliation extends AbstractDatabaseObject implements IReconciliation 
{   
    // Protected to ensure no access outside this package
    protected Reconciliation( final String reconKey ) throws ReconciliationNotFoundException
    {
        super( reconKey );
        
        // Attempt to load the reconciliation
        try
        {
            load();            
        }
        catch ( Exception e )
        {
            throw new ReconciliationNotFoundException( e.toString() );
        }
    }    
    
    // Protected to ensure no access outside this package
    protected Reconciliation( final String note, final MonetaryValue value, final ITransaction transaction )
    {
        m_note = new String( note );
        m_value = new MonetaryValue( value );
        m_transaction = transaction;
        m_transactionKey = transaction.getKey();
        m_date = transaction.getDate();
    }
         
    protected void parseResultSet( ResultSet results ) throws Exception
    {        
        // Reload the database object
        setKey( results.getString( "PKEY" ) );
        m_value = new MonetaryValue( results.getInt( "RECON_VALUE" ) );
        m_note = results.getString( "RECON_NOTE" );
        
        // Delay loading of the transaction to prevent endless recursion
        m_transactionKey = results.getString( "RECON_TRANS_FKEY" );
        
        // Load the category associated with this reconciliation
        m_category = DataObjectFactory.loadCategory( results.getString( "RECON_CATEGORY_FKEY") );
        
        m_date = results.getDate( "RECON_TRANS_DATE" );
    }
    
    protected void populateResultSet( ResultSet results ) throws Exception
    {                
        // update the database object
        results.updateString( "PKEY", getKey() );
        results.updateInt( "RECON_VALUE", m_value.getCentsAsInt() );
        results.updateString( "RECON_NOTE", m_note );
        results.updateString( "RECON_TRANS_FKEY", m_transactionKey );
        results.updateString( "RECON_CATEGORY_FKEY", m_category.getKey() );
        results.updateDate( "RECON_TRANS_DATE", m_date );
    }
    
    public ITransaction getTransaction()
    {
        // Delay loading
        if ( null == m_transaction )
        {
            try
            {
                m_transaction = DataObjectFactory.loadTransaction( m_transactionKey );
            }
            catch ( Exception e )
            {
                // TODO: this should not happen... but what if it does?
            }
        }
        return m_transaction;
    }
    
    public void setTransaction( final ITransaction trans )
    {
        m_transaction = trans;
        m_transactionKey = trans.getKey();
        m_date = trans.getDate();
        
        storeMemento();
    }

    public ICategory getCatecory()
    {
        return m_category;
    }    
    
    public void setCategory( final ICategory category )
    {
        m_category = category;
        
        storeMemento();
    }

    public MonetaryValue getValue()
    {
        return m_value;
    }

    public void setValue( final MonetaryValue value )
    {
        // Just create a new one and let the GC worry about the rest...
        m_value = new MonetaryValue( value );
        
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
        
    public String getTableName()
    {
        return "RECONCILIATION";
    }        
    
    public boolean isValid( )
    {
        // Check to see if this item is valid at this current time
        return m_value != null &&
                m_category != null &&
                m_transactionKey != null &&
                m_note != null;
    }
    
    public Memento getMemento()
    {
        return new Memento(
                ( m_value == null ) ? null : new MonetaryValue( m_value ), 
                m_category, // Don't want a new copy
                ( m_transactionKey == null ) ? null : new String( m_transactionKey ), 
                ( m_note == null ) ? null : new String( m_note ), 
                ( m_date == null ) ? null : new Date( m_date.getTime() ) );
    }
    
    public void restoreMemento( Memento state ) throws StateSyncException
    {
        m_value = (MonetaryValue)state.getSomeState();
        m_category = (ICategory)state.getSomeState();
        m_transactionKey = (String)state.getSomeState();
        try
        {
            m_transaction = DataObjectFactory.loadTransaction( m_transactionKey );
        }
        catch( Exception e )
        {
            // TODO??
        }
        m_note = (String)state.getSomeState();
        m_date = (Date)state.getSomeState();
    }
    
    private MonetaryValue m_value;
    private ICategory m_category;
    private ITransaction m_transaction;
    // Only used to restore a memento
    private String m_transactionKey;
    private String m_note;    
    private Date m_date;
}
