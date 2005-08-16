/*
 * Account.java
 *
 * Created on 14 February 2005, 21:52
 */

package com.rochester.budget.core;

import com.rochester.budget.core.exceptions.AccountNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 *
 * @author Cameron
 */
public class Account extends AbstractDatabaseObject implements IAccount
{
    public static IAccount loadAccount( final String accountNumber ) throws AccountNotFoundException
    {
        // This will throw an exception if the account is not available
        return new Account( accountNumber );        
    }
    
    /** Creates a new instance of Account */
    private Account( final String accountNumber ) throws AccountNotFoundException
    {
        setKey( accountNumber );
        
        // Attempt to load the account
        try
        {
            load();
        }
        catch ( Exception e )
        {
            throw new AccountNotFoundException( e.toString() );
        }
        
    }
    
    public String toString()
    {
        return m_accountName + " - " + m_accountDescription;
    }

    protected void parseResultSet(ResultSet results) throws SQLException
    {
        setKey( results.getString( "PKEY" ) );
        m_accountName = results.getString( "ACCOUNT_NAME" );
        m_accountDescription = results.getString( "ACCOUNT_DESCRIPTION" );
    }

    protected String getTableName()
    {
        return "ACCOUNT";
    }

    public String getAccountNumber()
    {
        return getKey();
    }

    public String getAccountDescription()
    {
        return m_accountDescription;
    }

    public String getAccountName()
    {
        return m_accountName;
    }
    
    private String m_accountName = new String();
    private String m_accountDescription = new String();
    
}
