/*
 * Account.java
 *
 * Created on 14 February 2005, 21:52
 */

package com.rochester.budget.core;

import com.rochester.budget.core.exceptions.AccountNotFoundException;
import com.rochester.budget.core.exceptions.StateSyncException;
import java.sql.ResultSet;

/**
 *
 * @author Cameron
 */
public class Account extends AbstractDatabaseObject implements IAccount
{
    // Protected to ensure no access outside this package
    protected Account( final String accountNumber ) throws AccountNotFoundException
    {
        super( accountNumber );
        
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
        return m_accountName + " (" + getKey() + ")";
    }

    protected void parseResultSet(ResultSet results) throws Exception
    {
        setKey( results.getString( "PKEY" ) );
        m_accountName = results.getString( "ACCOUNT_NAME" );
        m_accountDescription = results.getString( "ACCOUNT_DESCRIPTION" );
    }
    
    protected void populateResultSet( ResultSet results ) throws Exception
    { 
        // TODO
    }

    public String getTableName()
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
      
    public boolean isValid( )
    {
        // Check to see if this item is valid at this current time
        return m_accountName != null &&
                m_accountDescription != null;
    }
    
    public Memento getMemento()
    {
        // Remember that everything that isn't primitive is a reference so make sure to copy 
        // objects that can change
        return new Memento( isValid(), 
                new String(m_accountName), 
                new String(m_accountDescription) );
    }
    
    
    public void restoreMemento( Memento state ) throws StateSyncException
    {
        m_accountName = (String)state.getSomeState();
        m_accountDescription = (String)state.getSomeState();
    }
    
    private String m_accountName;
    private String m_accountDescription; 
}
