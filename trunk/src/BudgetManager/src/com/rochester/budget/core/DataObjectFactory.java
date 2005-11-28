/*
 * DataObjectFactory.java
 *
 * Created on 6 September 2005, 21:06
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.core;

import com.Ostermiller.util.ExcelCSVParser;
import com.Ostermiller.util.LabeledCSVParser;
import com.rochester.budget.core.exceptions.AccountNotFoundException;
import com.rochester.budget.core.exceptions.CategoryNotFoundException;
import com.rochester.budget.core.exceptions.ReconciliationNotFoundException;
import com.rochester.budget.core.exceptions.TransactionNotFoundException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.UUID;


/**
 *
 * @author Cam
 */
public class DataObjectFactory
{
    
    /****************** TRANSACTIONS **************************/
    
    /**
     * Creates a new instance of DataObjectFactory 
     */    
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
                        DataObjectFactory.createTransaction( parser );                        
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
            /*String sql = new String();
                sql = "INSERT INTO TRANSACTION VALUES ( '" + UUID.randomUUID() + "','" +
                    DatabaseManager.escapeSQL( parser.getValueByLabel( " Narrative" ) ) + "', '" +
                    parser.getValueByLabel( "Bank Account" ) + "', " +
                    new MonetaryValue( parser.getValueByLabel( " Debit Amount" ) ).getCents() + ", '" +
                    parser.getValueByLabel( " Date" ) + "', null )"; // must be formatted according to mysql spec!
                                                                    // see http://dev.mysql.com/doc/mysql/en/datetime.html

            DatabaseManager.getStatement().executeUpdate( sql ); */
            
            // Create a new Transaction with a new pkey
            Transaction trans = new Transaction();
            trans.setNarrative( parser.getValueByLabel( " Narrative" ) );
            trans.setAccount( loadAccount( parser.getValueByLabel( "Bank Account" ) ) );
            trans.setMonetaryValue( new MonetaryValue( parser.getValueByLabel( " Debit Amount" ) ) );
            
            // TODO: date
            //trans.
            //trans.commit();
        }
    }
    
    public static ITransaction loadTransaction( final String transactionKey ) throws TransactionNotFoundException
    {
        // This will throw an exception if the transaction is not available
        ITransaction transaction = m_transactions.get( transactionKey );
        if ( null == transaction )
        {
            transaction = new Transaction( transactionKey );
            
            m_transactions.put( transactionKey, transaction );
        }
        
        return transaction;
    }
    
    public static Collection<ITransaction> getTransactions()
    {
        if ( !m_transactionsLoaded )
        {
            m_transactionsLoaded = true; 
            
            try
            {
                String sql = new String( "select PKEY from TRANSACTION" );

                // the statement object will be automatically cleaned up when garbage collected
                ResultSet results = DatabaseManager.getStatement().executeQuery( sql );

                while ( results.next() )
                {
                    try
                    {
                        loadTransaction( results.getString("PKEY") );
                    }
                    catch( TransactionNotFoundException e )
                    {
                        // TODO: handling creation of new account 
                        System.out.println( e.toString() );
                    }
                }

                results.close();
            }
            catch ( Exception t )
            {
                t.printStackTrace();
                // TODO: error handling
                return null;
            }
        }
        
        return m_transactions.values();
    }
    
    /****************************** ACCOUNT ********************************/
    
    public static IAccount loadAccount( final String accountNumber ) throws AccountNotFoundException
    {
        if ( accountNumber == null )
        {
            return null;
        }
        
        // This will throw an exception if the account is not available
        IAccount account = m_accounts.get( accountNumber );
        if ( null == account )
        {
            account = new Account( accountNumber );
            
            m_accounts.put( accountNumber, account );
        }
        
        return account;
    }
    
    /******************************** CATEGORY ************************************/
    
    public static ICategory loadRootCategory( ) throws CategoryNotFoundException
    {
        String sql = new String("select PKEY from CATEGORY where CATEGORY_PARENT_FKEY = PKEY" );
        
        try
        {
            // the statement object will be automatically cleaned up when garbage collected
            ResultSet results = DatabaseManager.getStatement().executeQuery( sql );

            while ( results.next() )
            {
                // Make sure we don't load the root node in an infinite loop
                return loadCategory( results.getString("PKEY") );
            }
            
            results.close();
        }
        catch( SQLException e )
        {
            throw new CategoryNotFoundException( e.toString() );
        }                
        
        // If we get here we couldn't load the category
        throw new CategoryNotFoundException( "Unable to find root category" );
    }
    
    public static ICategory loadCategory( final String categoryKey ) throws CategoryNotFoundException
    {
        // This will throw an exception if the account is not available
        ICategory category = m_categories.get( categoryKey );
        if ( null == category )
        {
            category = new Category( categoryKey );
            
            m_categories.put( categoryKey, category );
            
            // Attempt to load children
            DataObjectFactory.loadChildrenOfCategory( category );
        }
        
        return category;
    }
    
    public static void loadChildrenOfCategory( ICategory parent ) throws CategoryNotFoundException
    {
        String sql = new String("select PKEY from " + parent.getTableName() + 
                " where CATEGORY_PARENT_FKEY = '" + parent.getKey() + "'" );
        
        try
        {
            ResultSet results = DatabaseManager.getStatement().executeQuery( sql );

            while ( results.next() )
            {
                // Make sure we don't load the root node in an infinite loop
                if ( !results.getString("PKEY").equals( parent.getKey() ) )
                {
                    parent.addCategory( loadCategory( results.getString("PKEY") ) );
                }
            }
            
            results.close();
        }
        catch( SQLException e )
        {
            throw new CategoryNotFoundException( e.toString() );
        }        
    }
    
    /************************** RECONCILIATION ***********************************/
    
    public static IReconciliation loadReconciliation( final String reconKey ) throws ReconciliationNotFoundException
    {
        // This will throw an exception if the account is not available
        IReconciliation recon = m_reconciliations.get( reconKey );
        if ( null == recon )
        {
            recon = new Reconciliation( reconKey );
            
            m_reconciliations.put( reconKey, recon );
        }
        
        return recon;
    }
    
    public static void loadReconciliationsForTransaction( ITransaction transaction ) 
    {
        try
        {
            String sql = new String( "select PKEY from RECONCILIATION where RECON_TRANS_FKEY = '" + transaction.getKey() + "'" );

            ResultSet results = DatabaseManager.getStatement().executeQuery( sql );

            while ( results.next() )
            {
                transaction.addReconciliation( loadReconciliation( results.getString( "PKEY" ) ) );
            }
            
            results.close();
        }
        catch ( Exception t )
        {
            t.printStackTrace();
            
            // TODO: error handling?
        }
    }
    
    public static IReconciliation newReconciliationForTransaction( ITransaction transaction )
    {
        IReconciliation recon = new Reconciliation( "New Reconciliation...", transaction.getValueRemaining(), transaction );
                
        return recon;
    }
    
    // Storage for the database objects
    private static DataObjectMap<ITransaction> m_transactions = new DataObjectMap<ITransaction>();
    private static DataObjectMap<IAccount> m_accounts = new DataObjectMap<IAccount>();
    private static DataObjectMap<ICategory> m_categories = new DataObjectMap<ICategory>();
    private static DataObjectMap<IReconciliation> m_reconciliations = new DataObjectMap<IReconciliation>();
    private static boolean m_transactionsLoaded = false;
}
