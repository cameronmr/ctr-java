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

import com.rochester.budget.core.AbstractDataChangeObserver.ChangeType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Cam
 */
public class Reconciliation extends AbstractDatabaseObject implements IReconciliation 
{
    public static void loadReconciliationsForTransactions( ITransaction transaction ) 
    {
        try
        {
            String sql = new String( "select * from RECONCILIATION where RECON_TRANS_FKEY = '" + transaction.getKey() + "'" );

            Statement stmt = AbstractDatabaseObject.getStatement();
            ResultSet results = stmt.executeQuery( sql );

            while ( results.next() )
            {
                transaction.addReconciliation( new Reconciliation( results, transaction ) );
            }
            
            results.close();
            stmt.close();
        }
        catch ( Exception t )
        {
            t.printStackTrace();
            
            // TODO: error handling?
            return;
        }
    }
    
    /** Creates a new instance of Reconciliation */
    public Reconciliation( ResultSet rs, ITransaction transaction ) throws Exception
    {
        m_transaction = transaction;
        parseResultSet( rs );
    }    
         
    protected void parseResultSet( ResultSet results ) throws Exception
    {
        // This will basically re-create the object        
        
        // De-register with observed objects
        
        // Reload the database object
        setKey( results.getString( "PKEY" ) );
        m_value = new MonetaryValue( results.getInt( "RECON_VALUE" ) );
        m_note = results.getString( "RECON_NOTE" );
        
        if ( null == m_transaction )
        {
            // TODO: Load transaction
            // Transaction.
        }
        
        // Load the category & accounts associated with this reconciliation
        m_category = Category.loadCategory( results.getString( "RECON_CATEGORY_FKEY") );
        m_account = Account.loadAccount( results.getString( "RECON_ACCOUNT_FKEY") );
    }
    
    public ITransaction getTransaction()
    {
        return m_transaction;
    }

    public ICategory getCatecory()
    {
        return m_category;
    }

    public MonetaryValue getValue()
    {
        return m_value;
    }

    public IAccount getAccount()
    {
        return m_account;
    }    
    
    public String getNote()
    {
        return m_note;
    }
    
    /**
     * This is called upon an observer when the source object changes.
     * @param change The type of change
     * @param object The object that has changed
     */
    public void notifyDatabaseChange( ChangeType change, Object object )
    {
        // TODO: What to do in this scenario
    }
    
    public String getTableName()
    {
        return "RECONCILIATION";
    }
    
    private IAccount m_account;
    private MonetaryValue m_value;
    private ICategory m_category;
    private ITransaction m_transaction;
    private String m_note;
}
