/*
 * TransactionFactory.java
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
import com.rochester.budget.core.exceptions.TransactionNotFoundException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import java.util.Vector;

/**
 *
 * @author Cam
 */
public class TransactionFactory {
    
    /** Creates a new instance of TransactionFactory */    
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
                        TransactionFactory.createTransaction( parser );                        
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

                ResultSet results = AbstractDatabaseObject.getStatement().executeQuery( sql );

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
    
    private static HashMap<String,ITransaction> m_transactions = new HashMap<String,ITransaction>();
    private static boolean m_transactionsLoaded = false;
}
