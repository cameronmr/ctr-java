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
import com.rochester.budget.core.exceptions.RuleCriterionNotFoundException;
import com.rochester.budget.core.exceptions.RuleNotFoundException;
import com.rochester.budget.core.exceptions.RuleResultNotFoundException;
import com.rochester.budget.core.exceptions.StatementNotFoundException;
import com.rochester.budget.core.exceptions.TransactionNotFoundException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

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
    
    public static Collection<ITransaction> loadTransactions()
    {
        if ( ! m_transactions.allLoaded() )        
        {            
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
                
                // Flag the data as loaded
                m_transactions.setLoaded();

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
    
    /***************************** RULE STUFF *******************************/
    
    public static IRule loadRule( final String pkey ) throws RuleNotFoundException
    {
        if ( pkey == null )
        {
            return null;
        }
        
        // This will throw an exception if the account is not available
        IRule rule = m_rules.get( pkey );
        if ( null == rule )
        {
            rule = new Rule( pkey );
            
            m_rules.put( pkey, rule );
        }
        
        return rule;
    }
    
    public static Collection<IRule> loadRules( )
    {
        if ( ! m_rules.allLoaded() )
        {
            
            ArrayList<IRule> rules = new ArrayList<IRule>();
            try
            {
                String sql = new String( "select PKEY from RULE" );

                // the statement object will be automatically cleaned up when garbage collected
                ResultSet results = DatabaseManager.getStatement().executeQuery( sql );

                while ( results.next() )
                {
                    try
                    {
                        rules.add( loadRule( results.getString("PKEY") ) );
                    }
                    catch( RuleNotFoundException e )
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
        
        return m_rules.values();
    }
    
    public static IRule newRule( )
    {
        return new Rule( );
    }
    
    public static IRuleCriterion loadRuleCriterion( final String pkey, final String type ) throws RuleCriterionNotFoundException
    {
        // Set up the available criteria
        if ( m_availableCriteria.isEmpty() )
        {
            IRuleCriterion criteria = new DescriptionRuleCriterion();
            m_availableCriteria.put( criteria.getCriteria(), criteria );
            
            criteria = new AccountRuleCriterion();
            m_availableCriteria.put( criteria.getCriteria(), criteria );
        }
        
        if ( pkey == null )
        {
            return null;
        }
        
        // This will throw an exception if the account is not available
        IRuleCriterion criterion = m_ruleCriteria.get( pkey );
        if ( null == criterion )
        {            
            // Get the available criterion
            criterion = m_availableCriteria.get( type );
            
            // Create a new one of that type
            criterion = criterion.getNew( pkey );
            
            m_ruleCriteria.put( pkey, criterion );
        }
        
        return criterion;
    }
    
    public static Collection<IRuleCriterion> loadCriteriaForRule( final IRule rule ) throws RuleCriterionNotFoundException
    {        
        ArrayList<IRuleCriterion> criteria = new ArrayList<IRuleCriterion>();
        try
        {
            String sql = new String( "select PKEY, CRITERIA from RULE_CRITERIA where RULE_FKEY = '" + rule.getKey() + "'" );

            // the statement object will be automatically cleaned up when garbage collected
            ResultSet results = DatabaseManager.getStatement().executeQuery( sql );

            while ( results.next() )
            {
                try
                {
                    criteria.add( loadRuleCriterion( results.getString("PKEY"), results.getString( "CRITERIA" ) ) );
                }
                catch( RuleCriterionNotFoundException e )
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
        
        return criteria;
    }
    
    public static IRuleResult loadRuleResult( final String pkey ) throws RuleResultNotFoundException
    {        
        if ( pkey == null )
        {
            return null;
        }
        
        // This will throw an exception if the account is not available
        IRuleResult result = m_ruleResults.get( pkey );
        if ( null == result )
        {
            result = new RuleResult( pkey );
            
            m_ruleResults.put( pkey, result );
        }
        
        return result;
    }
    
    public static Collection<IRuleResult> loadResultsForRule( final IRule rule ) throws RuleResultNotFoundException
    {        
        ArrayList<IRuleResult> ruleResults = new ArrayList<IRuleResult>();
        try
        {
            String sql = new String( "select PKEY from RULE_RESULT where RULE_FKEY = '" + rule.getKey() + "'" );

            // the statement object will be automatically cleaned up when garbage collected
            ResultSet results = DatabaseManager.getStatement().executeQuery( sql );

            while ( results.next() )
            {
                try
                {
                    ruleResults.add( loadRuleResult( results.getString("PKEY") ) );
                }
                catch( RuleResultNotFoundException e )
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
        
        return ruleResults;
    }
    
    public static Collection<IRuleCriterion> getAvailableCriteria( final IRuleCriterion selected )
    {
        // Set up the available criteria
        if ( m_availableCriteria.isEmpty() )
        {
            IRuleCriterion criteria = new DescriptionRuleCriterion();
            m_availableCriteria.put( criteria.getCriteria(), criteria );
            
            criteria = new AccountRuleCriterion();
            m_availableCriteria.put( criteria.getCriteria(), criteria );
        }
        
        ArrayList<IRuleCriterion> criteria = new ArrayList<IRuleCriterion>();
        for( IRuleCriterion crit : m_availableCriteria.values() )
        {
            if ( selected != null &&
                 crit.getCriteria().equals( selected.getCriteria() ) )
            {
                criteria.add( selected );
            }
            else
            {
                criteria.add( crit.getNew( ) );
            }
        }
        
        return criteria;
    }
    
    public static IRuleResult newResultForRule( final IRule rule )
    {
        return new RuleResult( rule );
    }
        
    /***************************** STATEMENT *******************************/
    
    public static IStatement loadStatement( final String pkey ) throws StatementNotFoundException
    {
        if ( pkey == null )
        {
            return null;
        }
        
        // This will throw an exception if the account is not available
        IStatement statement = m_statements.get( pkey );
        if ( null == statement )
        {
            statement = new Statement( pkey );
            
            m_statements.put( pkey, statement );
        }
        
        return statement;
    }
    
    public static Collection<IStatement> loadStatementsForAccount( IAccount account )
    {
        ArrayList<IStatement> statements = new ArrayList<IStatement>();
        try
        {
            String sql = new String( "select PKEY from STATEMENT where STATEMENT_ACC_FKEY = '" + account.getKey() + "'" );

            // the statement object will be automatically cleaned up when garbage collected
            ResultSet results = DatabaseManager.getStatement().executeQuery( sql );

            while ( results.next() )
            {
                try
                {
                    statements.add( loadStatement( results.getString("PKEY") ) );
                }
                catch( StatementNotFoundException e )
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
        
        return statements;
    }
    
    public static IStatement newStatement( IAccount account )
    {
        return new Statement( account );
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
    
    public static Collection<IAccount> loadAccounts( )
    {
        if ( ! m_accounts.allLoaded() )
        {
            try
            {
                String sql = new String( "select PKEY from ACCOUNT" );

                // the statement object will be automatically cleaned up when garbage collected
                ResultSet results = DatabaseManager.getStatement().executeQuery( sql );

                while ( results.next() )
                {
                    try
                    {
                        loadAccount( results.getString("PKEY") );
                    }
                    catch( AccountNotFoundException e )
                    {
                        // TODO: handling creation of new account 
                        System.out.println( e.toString() );
                    }
                }

                m_accounts.setLoaded();
                
                results.close();
            }
            catch ( Exception t )
            {
                t.printStackTrace();
                // TODO: error handling
                return null;
            }
        }
        
        return m_accounts.values();
    }
    
    public static IAccount newAccount( )
    {
        return new Account();
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
    
    public static ICategory newChildCategory( ICategory parent, IAccount account, final String name ) throws Exception
    {
        ICategory cat = new Category( parent, account, name );
                
        return cat;
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
    
    public static Collection<IReconciliation> loadReconciliationsForStatement( final IStatement statement ) 
    {
        ArrayList<IReconciliation> recons = new ArrayList<IReconciliation>();
        try
        {
            String sql = new String( 
                    "select r.PKEY from RECONCILIATION r, TRANSACTION t where r.RECON_TRANS_FKEY = t.PKEY" +
                    " and t.TRANS_DATE between '" + statement.getStatementStart().toString() + 
                    "' and '" + statement.getStatementEnd().toString() + "'" );

            ResultSet results = DatabaseManager.getStatement().executeQuery( sql );

            while ( results.next() )
            {
                recons.add( loadReconciliation( results.getString( "PKEY" ) ) );
            }
            
            results.close();
        }
        catch ( Exception t )
        {
            t.printStackTrace();
            
            // TODO: error handling?
        }
        return recons;
    }
    
    public static IReconciliation newReconciliationForTransaction( ITransaction transaction )
    {
        IReconciliation recon = new Reconciliation( "New Reconciliation...", transaction.getValueRemaining(), transaction );
                
        return recon;
    }
    
    // Storage for the database objects
    private static DataObjectMap<ITransaction> m_transactions = new DataObjectMap<ITransaction>();
    private static DataObjectMap<IAccount> m_accounts = new DataObjectMap<IAccount>();
    private static DataObjectMap<IRule> m_rules = new DataObjectMap<IRule>();
    private static DataObjectMap<IRuleCriterion> m_ruleCriteria = new DataObjectMap<IRuleCriterion>();
    private static DataObjectMap<IRuleResult> m_ruleResults = new DataObjectMap<IRuleResult>();
    private static DataObjectMap<IStatement> m_statements = new DataObjectMap<IStatement>();
    private static DataObjectMap<ICategory> m_categories = new DataObjectMap<ICategory>();
    private static DataObjectMap<IReconciliation> m_reconciliations = new DataObjectMap<IReconciliation>();
    
    private static HashMap<String, IRuleCriterion> m_availableCriteria = new HashMap<String, IRuleCriterion>();
}
