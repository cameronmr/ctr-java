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
    protected Reconciliation()
    {
    }
         
    protected void parseResultSet( ResultSet results ) throws Exception
    {        
        // Reload the database object
        setKey( results.getString( "PKEY" ) );
        m_value = new MonetaryValue( results.getInt( "RECON_VALUE" ) );
        m_note = results.getString( "RECON_NOTE" );
        
        // Delay loading of the transaction to prevent endless recursion
        m_transactionKey = results.getString("RECON_TRANS_FKEY");
        
        // Load the category & accounts associated with this reconciliation
        m_category = DataObjectFactory.loadCategory( results.getString( "RECON_CATEGORY_FKEY") );
        //m_account = DataObjectFactory.loadAccount( results.getString( "RECON_ACCOUNT_FKEY") );
    }
    
    protected void populateResultSet( ResultSet results ) throws Exception
    {                
        // update the database object
        results.updateInt( "RECON_VALUE", m_value.getCentsAsInt() );
        results.updateString( "RECON_NOTE", m_note );
        
        // Transaction can't change
        
        // Load the category & accounts associated with this reconciliation
        results.updateString( "RECON_CATEGORY_FKEY", m_category.getKey() );
        //results.updateString( "RECON_ACCOUNT_FKEY", m_account.getKey() );
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
    }

    public ICategory getCatecory()
    {
        return m_category;
    }    
    
    public void setCategory( final ICategory category )
    {
        m_category = category;
    }

    public MonetaryValue getValue()
    {
        return m_value;
    }

    public void setValue( final MonetaryValue value )
    {
        m_value = value;
    }
    
    /*public IAccount getAccount()
    {
        return m_account;
    } */   
    
    public String getNote()
    {
        return m_note;
    }
    
    public void setNote( final String note )
    {
        m_note = note;
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
        //TODO
        return null;//new Memento( new MonetaryValue( m_value ));
    }
    
    public void restoreMemento( Memento state ) throws StateSyncException
    {
        //TODO
    }
    
    //private IAccount m_account;
    private MonetaryValue m_value;
    private ICategory m_category;
    private ITransaction m_transaction;
    private String m_transactionKey;
    private String m_note;    
}
