/*
 * Transaction.java
 *
 * Created on 9 February 2005, 21:01
 */

package com.rochester.budget.core;

import com.Ostermiller.util.ExcelCSVParser;
import com.Ostermiller.util.LabeledCSVParser;
import com.rochester.budget.core.ITransaction.ReconciliationState;
import com.rochester.budget.core.exceptions.AccountNotFoundException;
import com.rochester.budget.core.exceptions.BudgetManagerException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;


/**
 * A transaction, once created, can not be deleted. It observes reconciliations to monitor the current reconciliation state
 * @author Cameron
 */
public class Transaction extends AbstractDatabaseObject implements ITransaction
{    
    public static void importTransactionsFromFile( File csvFile )
    {        
        try
        {
            LabeledCSVParser parser = new LabeledCSVParser(
                    new ExcelCSVParser( new BufferedInputStream( new FileInputStream( csvFile ) ) ) );
            
            int count = 0;
            
            while ( true )
            {
                count ++;
                try
                {
                    // Go to the next line
                    if ( null != parser.getLine() )
                    {              
                        Transaction.createTransaction( parser );                        
                    }   
                    else
                    {
                        break;
                    }
                }
                catch ( SQLException e )
                {
                    System.out.println( e.getMessage() );
                }    
                catch ( Exception e )
                {
                    // TODO We have reached the end of the file??
                    break;
                }
            }
        }
        catch ( Exception e )
        {
            // TODO??
        }            
    }
    
    /** Creates a new instance of Transaction */
    public static void createTransaction( LabeledCSVParser parser ) throws Exception
    {
        // Load all the available fields from the parser
        // TODO: Load field names from XML file/specific object type
        if ( parser.getValueByLabel( " Categories" ).compareTo( "OTHER" ) == 0 || 
             parser.getValueByLabel( " Categories" ).compareTo( "FEE" ) == 0 )
        {            
            String sql = new String();
                sql = "INSERT INTO TRANSACTION VALUES ( '" + UUID.randomUUID() + "','" +
                    AbstractDatabaseObject.escapeSQL( parser.getValueByLabel( " Narrative" ) ) + "', '" +
                    parser.getValueByLabel( "Bank Account" ) + "', " +
                    new MonetaryValue( parser.getValueByLabel( " Debit Amount" ) ).getCents() + ", '" +
                    parser.getValueByLabel( " Date" ) + "', null )"; // must be formatted according to mysql spec!
                                                                    // see http://dev.mysql.com/doc/mysql/en/datetime.html

            AbstractDatabaseObject.getStatement().executeUpdate( sql ); 
        }
    }
    
    public static Vector<ITransaction> loadTransactions()
    {
        try
        {
            Vector<ITransaction> transactions = new Vector<ITransaction>();
            String sql = new String( "select * from TRANSACTION" );

            ResultSet results = AbstractDatabaseObject.getStatement().executeQuery( sql );

            while ( results.next() )
            {
                try
                {
                    transactions.add( new Transaction( results ) );
                }
                catch( AccountNotFoundException e )
                {
                    // TODO: handling creation of new account 
                    System.out.println( e.toString() );
                }
            }

            results.close();
            return transactions;
        }
        catch ( Exception t )
        {
            t.printStackTrace();
            // TODO: error handling
            return null;
        }
    }
    
    public Transaction( ResultSet rs ) throws Exception
    {
        parseResultSet( rs );
    }
    
    public String getNarrative()
    {
        return m_narrative;
    }
    
    public String getNote()
    {
        return m_note;
    }
                
    public MonetaryValue getValue()
    {
        return m_value;
    }
    
    public Date getDate()
    {        
        return m_date;
    }
    
    static public String[] getColumns()
    {
        String[] columnNames = { "Reconciled", "Date", "Account", "Description", "Value"};
        return columnNames;
    }
    
    public IAccount getAccount()
    {
        return null;
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
    
    public java.util.List<IReconciliation> getReconciliations()
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
            // TODO
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
    
    protected void parseResultSet( ResultSet results ) throws Exception
    {
        // This will basically re-create the object
        
        // De-register with observed objects
        
        // Reload the database object
        m_date = results.getDate( "TRANS_DATE" );
        m_narrative = results.getString( "TRANS_NARRATIVE" );
        m_note = results.getString( "TRANS_NOTE" );
        setKey( results.getString( "PKEY" ) );
        m_value = new MonetaryValue( results.getInt( "TRANS_VALUE" ) );
        
        // The remaining value is the same as the transaction value initially
        m_valueRemaining = new MonetaryValue( m_value );
        
        // Load the account
        m_account = Account.loadAccount( results.getString( "TRANS_ACCOUNT_FKEY" ) );
        
        // Load reconciliations 
        Reconciliation.loadReconciliationsForTransactions( this );
    }

    public String getTableName()
    {
        return "TRANSACTION";
    }
    
    /**
     * This is called upon an observer when the source object changes.
     * @param change The type of change
     * @param object The object that has changed
     */
    /*public void notifyDatabaseChange( ChangeType change, Object object )
    {
        // TODO: What to do in this scenario
    }*/
    
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
    
    private String m_note;
    private MonetaryValue m_value;
    private MonetaryValue m_valueRemaining;
    private IAccount m_account;
    private String m_narrative;
    private Date m_date;
    private ArrayList<IReconciliation> m_reconciliations = new ArrayList<IReconciliation>();
}
