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
    protected Account( final String pkey ) throws AccountNotFoundException
    {
        super( pkey );
        
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
    
    protected Account( )
    {
        m_accountName = "New Account...";
    }
    
    public String toString()
    {
        return m_accountName + " (" + getNumber() + ")";
    }

    protected void parseResultSet(ResultSet results) throws Exception
    {
        setKey( results.getString( "PKEY" ) );
        m_accountNumber = results.getString( "ACCOUNT_NUMBER" );
        m_accountName = results.getString( "ACCOUNT_NAME" );
        m_accountDescription = results.getString( "ACCOUNT_DESCRIPTION" );
    }
    
    protected void populateResultSet( ResultSet results ) throws Exception
    { 
        // update the database object
        results.updateString( "PKEY", getKey() );
        results.updateString( "ACCOUNT_NUMBER", m_accountNumber );
        results.updateString( "ACCOUNT_NAME", m_accountName );
        results.updateString( "ACCOUNT_DESCRIPTION", m_accountDescription );
    }

    public String getTableName()
    {
        return "ACCOUNT";
    }
    
    public String getNumber()
    {
        return m_accountNumber;
    }
    
    public void setNumber( final String number )
    {        
        m_accountNumber = new String( number );
        storeMemento();
    }

    public String getDescription()
    {
        return m_accountDescription;
    }
    
    public void setDescription( final String description )
    {        
        m_accountDescription = new String( description );
        storeMemento();
    }

    public String getName()
    {
        return m_accountName;
    }
    
    public void setName( final String name )
    {
        m_accountName = new String( name );
        storeMemento();
    }
      
    public boolean isValid( )
    {
        // Check to see if this item is valid at this current time
        return m_accountName != null &&
                m_accountNumber != null &&
                m_accountDescription != null;
    }
    
    public Memento getMemento()
    {
        // Remember that everything that isn't primitive is a reference so make sure to copy 
        // objects that can change
        return new Memento(
                ( m_accountName == null ) ? null : new String(m_accountName), 
                ( m_accountNumber == null ) ? null : new String(m_accountNumber),
                ( m_accountDescription == null ) ? null : new String(m_accountDescription) );
    }
    
    
    public void restoreMemento( Memento state ) throws StateSyncException
    {
        m_accountName = (String)state.getSomeState();
        m_accountNumber = (String)state.getSomeState();
        m_accountDescription = (String)state.getSomeState();
    }
    
    private String m_accountName;
    private String m_accountNumber;
    private String m_accountDescription; 
}
