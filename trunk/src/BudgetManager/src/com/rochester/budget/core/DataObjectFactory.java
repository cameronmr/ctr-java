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
import com.rochester.budget.core.exceptions.BudgetManagerException;
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
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import javax.swing.JOptionPane;

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
        LabeledCSVParser parser = null;   
        try
        {
             parser = new LabeledCSVParser(
                    new ExcelCSVParser( new BufferedInputStream( new FileInputStream( csvFile ) ) ) );
        }
        catch ( Exception e )
        {
            JOptionPane.showMessageDialog( null,
                    "Error while importing transactions: " + e.getMessage(),
                    "Error", 
                    JOptionPane.ERROR_MESSAGE );
        }           

        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyyMMddhhmmss" );
        //SimpleDateFormat dateFormat = new SimpleDateFormat( "ddMMyy" );
        while ( true )
        {
            try
            {
                // Go to the next line
                if ( null != parser.getLine() )
                {              
                    DataObjectFactory.createTransaction( parser, dateFormat );                        
                }   
                else
                {
                    break;
                }
            }
            catch ( SQLException e )
            {
                // Do nothing..
                int i = 0;
            }    
            catch ( BudgetManagerException e )
            {
                JOptionPane.showMessageDialog( null,
                        "Error while importing transactions: " + e.getMessage(),
                        "Error", 
                        JOptionPane.ERROR_MESSAGE );
                break;
            }
            catch ( IOException e )
            {
                // Reached the end of the file
                break;
            }
            catch ( Exception e )
            {
                JOptionPane.showMessageDialog( null,
                        "Error while importing transactions: " + e.getMessage(),
                        "Error", 
                        JOptionPane.ERROR_MESSAGE );
                break;
            }
        } 
    }
    
    /** Creates a new instance of Transaction */
    public static void createTransaction( LabeledCSVParser parser, SimpleDateFormat df ) throws Exception
    {
        // Load all the available fields from the parser
        // TODO: Load field names from XML file/specific object type
        if ( parser.getValueByLabel( " Categories" ).compareTo( "DEBIT" ) == 0 || 
             parser.getValueByLabel( " Categories" ).compareTo( "FEE" ) == 0 )
        {                        
            // Create a new Transaction with a new pkey
            Transaction trans = new Transaction(
                    parser.getValueByLabel( " Narrative" ), 
                    loadAccountByAccountNumber( parser.getValueByLabel( "Bank Account" ) ),
                    new MonetaryValue( parser.getValueByLabel( " Debit Amount" ) ), 
                    new java.sql.Date( df.parse( parser.getValueByLabel( " Date" ) ).getTime() ) ); 
            
            trans.commit();
            
            m_transactions.put( trans );
        }
    }
    
    public static ITransaction loadTransaction( final String transactionKey ) throws TransactionNotFoundException
    {
        // This will throw an exception if the transaction is not available
        ITransaction transaction = m_transactions.get( transactionKey );
        if ( null == transaction )
        {
            transaction = new Transaction( transactionKey );
            
            m_transactions.put( transaction );
        }
        
        return transaction;
    }
    
    public static Collection<ITransaction> loadTransactions()
    {
        try
        {
            String sql = new String( "SELECT DISTINCT pkey " +
                    "FROM TRANSACTION t " +
                    "WHERE t.trans_value != " +
                    "( " +
                    "	SELECT sum( recon_value ) " +
                    " 	FROM RECONCILIATION " +
                    " 	WHERE RECON_TRANS_FKEY = t.PKEY " +
                    ") " +
                    " OR t.pkey NOT IN " +
                    "( " +
                    "	SELECT recon_trans_fkey " +
                    "	FROM RECONCILIATION " + 
                    ")" );

            // the statement object will be automatically cleaned up when garbage collected
            ResultSet results = DatabaseManager.getStatement().executeQuery( sql );

            while ( results.next() )
            {
                try
                {
                    loadTransaction( results.getString(PKEY) );
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
        
        return m_transactions.values();
    }
    
    public static Collection<ITransaction> loadTransactionsForStatement( final IStatement statement )
    {
        ArrayList<ITransaction> transactions = new ArrayList<ITransaction>();
        try
        {
            String sql = new String(
                "select PKEY from TRANSACTION where TRANS_DATE between '" + statement.getStatementStart().toString() + 
                "' and '" + statement.getStatementEnd().toString() + 
                "' and TRANS_ACCOUNT_FKEY = '" + statement.getTransactionAccount().getKey() + "'" );

            // the statement object will be automatically cleaned up when garbage collected
            ResultSet results = DatabaseManager.getStatement().executeQuery( sql );

            while ( results.next() )
            {
                try
                {
                    transactions.add( loadTransaction( results.getString(PKEY) ) );
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
        }
        
        return transactions;
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
            
            m_rules.put( rule );
        }
        
        return rule;
    }
    
    public static Collection<IRule> loadRules( )
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
                    rules.add( loadRule( results.getString(PKEY) ) );
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
        
        return m_rules.values();
    }
    
    public static IRule newRule( )
    {
        IRule rule = new Rule( );
        m_rules.put( rule );
        return rule;
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
            
            m_ruleCriteria.put( criterion );
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
                    criteria.add( loadRuleCriterion( results.getString(PKEY), results.getString( "CRITERIA" ) ) );
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
            
            m_ruleResults.put( result );
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
                    ruleResults.add( loadRuleResult( results.getString(PKEY) ) );
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
                IRuleCriterion newCrit = crit.getNew( );
                m_ruleCriteria.put( newCrit );
                criteria.add( newCrit );
            }
        }
        
        return criteria;
    }
    
    public static IRuleResult newResultForRule( final IRule rule )
    {
        IRuleResult result = new RuleResult( rule );
        m_ruleResults.put( result );
        return result;
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
            
            m_statements.put( statement );
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
                    statements.add( loadStatement( results.getString(PKEY) ) );
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
        IStatement statement = new Statement( account );
        m_statements.put( statement );
        return statement;
    }
    
    /****************************** ACCOUNT ********************************/
    
    public static IAccount loadAccount( final String pkey ) throws AccountNotFoundException
    {
        if ( pkey == null )
        {
            return null;
        }
        
        // This will throw an exception if the account is not available
        IAccount account = m_accounts.get( pkey );
        if ( null == account )
        {
            account = new Account( pkey );
            
            m_accounts.put( account );
        }
        
        return account;
    }
    
    public static IAccount loadAccountByAccountNumber( final String accountNumber ) throws AccountNotFoundException
    {
        String sql = new String( "select PKEY from ACCOUNT where ACCOUNT_NUMBER = '" + accountNumber + "'" );
        
        try
        {
            // the statement object will be automatically cleaned up when garbage collected
            ResultSet results = DatabaseManager.getStatement().executeQuery( sql );

            while ( results.next() )
            {
                // Make sure we don't load the root node in an infinite loop
                return loadAccount( results.getString(PKEY) );
            }
            
            results.close();
        }
        catch( SQLException e )
        {
            throw new AccountNotFoundException( e.toString() );
        }                
        
        // If we get here we couldn't load the category
        throw new AccountNotFoundException( "Unable to load Account with Account Number: " + accountNumber );
    }
    
    public static Collection<IAccount> loadAccounts( )
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
                    loadAccount( results.getString(PKEY) );
                }
                catch( AccountNotFoundException e )
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
        
        return m_accounts.values();
    }
    
    public static IAccount newAccount( )
    {
        IAccount account = new Account();
        m_accounts.put( account );
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
                return loadCategory( results.getString(PKEY) );
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
            
            m_categories.put( category );
            
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
                if ( !results.getString(PKEY).equals( parent.getKey() ) )
                {
                    parent.addCategory( loadCategory( results.getString(PKEY) ) );
                }
            }
            
            results.close();
        }
        catch( SQLException e )
        {
            throw new CategoryNotFoundException( e.toString() );
        }        
    }
    
    public static Collection<IReconciliation> loadReconciliationsForStatement( final IStatement statement,
            final ICategory category )
    {
        ArrayList<IReconciliation> recons = new ArrayList<IReconciliation>();
        try
        {
            String sql = new String( 
                    "select r.PKEY from RECONCILIATION r, TRANSACTION t where r.RECON_TRANS_FKEY = t.PKEY" +
                    " and r.RECON_TRANS_DATE between '" + statement.getStatementStart().toString() + 
                    "' and '" + statement.getStatementEnd().toString() + 
                    "' and t.TRANS_ACCOUNT_FKEY = '" + statement.getTransactionAccount().getKey() +
                    "' and r.RECON_CATEGORY_FKEY = '" + category.getKey() + "'" );

            ResultSet results = DatabaseManager.getStatement().executeQuery( sql );

            while ( results.next() )
            {
                recons.add( loadReconciliation( results.getString( PKEY ) ) );
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
    
    public static Collection<IReconciliation> loadReconciliationsForPeriod( final ICategory category, 
            final Date start, final Date end )
    {
        ArrayList<IReconciliation> recons = new ArrayList<IReconciliation>();
        try
        {
            String sql = new String( 
                    "select PKEY from RECONCILIATION where RECON_TRANS_DATE between '" + start.toString() + 
                    "' and '" + end.toString() + 
                    "' and RECON_CATEGORY_FKEY = '" + category.getKey() + "'" );

            ResultSet results = DatabaseManager.getStatement().executeQuery( sql );

            while ( results.next() )
            {
                recons.add( loadReconciliation( results.getString( PKEY ) ) );
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
    
    public static ICategory newChildCategory( ICategory parent, IAccount account, final String name ) throws Exception
    {
        ICategory cat = new Category( parent, account, name );
        m_categories.put( cat );
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
            
            m_reconciliations.put( recon );
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
                transaction.addReconciliation( loadReconciliation( results.getString( PKEY ) ) );
            }
            
            results.close();
        }
        catch ( Exception t )
        {
            t.printStackTrace();
            
            // TODO: error handling?
        }
    }
    
    /*public static Collection<IReconciliation> loadReconciliationsForStatement( final IStatement statement ) 
    {
        ArrayList<IReconciliation> recons = new ArrayList<IReconciliation>();
        try
        {
            String sql = new String( 
                    "select r.PKEY from RECONCILIATION r, TRANSACTION t where r.RECON_TRANS_FKEY = t.PKEY" +
                    " and r.RECON_TRANS_DATE between '" + statement.getStatementStart().toString() + 
                    "' and '" + statement.getStatementEnd().toString() + 
                    "' and t.TRANS_ACCOUNT_FKEY = '" + statement.getTransactionAccount().getKey() + "'" );

            ResultSet results = DatabaseManager.getStatement().executeQuery( sql );

            while ( results.next() )
            {
                recons.add( loadReconciliation( results.getString( PKEY ) ) );
            }
            
            results.close();
        }
        catch ( Exception t )
        {
            t.printStackTrace();
            
            // TODO: error handling?
        }
        return recons;
    }*/
    
    public static IReconciliation newReconciliationForTransaction( ITransaction transaction )
    {
        IReconciliation recon = new Reconciliation( "", transaction.getValueRemaining(), transaction );
        m_reconciliations.put( recon );        
        return recon;
    }
    
    public static void clearAll()
    {
        m_transactions.clear();
        m_accounts.clear();
        m_rules.clear();
        m_ruleResults.clear();
        m_ruleCriteria.clear();
        m_statements.clear();
        m_categories.clear();
        m_reconciliations.clear();
        m_availableCriteria.clear();
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
    
    private static final String PKEY = new String("PKEY");
}
