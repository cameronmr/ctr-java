/*
 * AbstractDatabaseObject.java
 *
 * Created on 18 May 2005, 20:40
 */

package com.rochester.budget.core;

import com.rochester.budget.core.AbstractDataChangeObserver.ChangeType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Observable;

/**
 *
 * @author Cam
 */
public abstract class AbstractDatabaseObject extends Observable implements IDatabaseObject
{    
    public static String escapeSQL(String sql)
	{
        StringBuffer newSql = new StringBuffer( sql );
        int pos = 0;
        do
        {
            pos = newSql.indexOf("'", pos );          
            if ( pos > 0 )
            {
                newSql.insert( pos, "\\" );
                // Increment one to get past the current pos, 
                // increment another to cater for the inserted item
                pos += 2;
            }
            else
            {
                break;
            }
        }
        while ( true );
        
        return newSql.toString();
	}
    
    /** Creates a new instance of AbstractDatabaseObject */
    public AbstractDatabaseObject( )
    {
        // Notify observers of new??
    }
    
    public static void initiateDatabaseConnection() throws SQLException
    {
        // Connect to the necessary database       
        m_connection = DriverManager.getConnection("jdbc:mysql://gateway.rochester.lan/AccountManager?user=Cam&password=Cam");
    }
    
    public static Statement getStatement() throws SQLException
    {
        return m_connection.createStatement( );
    }    
                
    public String getKey()
    {
        return m_key;
    }
    
    public void setKey( final String key )
    {
        m_key = key;
    }
    
    public void delete()
    {
        try
        {
            Statement stmt = getStatement();
            stmt.executeUpdate( "delete from " + getTableName() + " where PKEY = '" + m_key + "'" );
            stmt.close();
            
            notifyObservers( ChangeType.DELETE );
        }
        catch ( Exception e )
        {
            // Do nothing
        }
    }
    
    public void load( ) throws Exception
    {
        // Get the data from the table
        Statement stmt = getStatement();
        ResultSet results = null;
        try
        { 
            results = stmt.executeQuery( "select * from " + getTableName() + " where PKEY = '" + m_key + "';" );    

            // Move to the first row
            results.first();
            
            // Parse the results
            parseResultSet( results );
        }
        finally
        {
            stmt.close();
            if ( null != results )
            {
                results.close();
            }
        }
        
        // Notify any observers that we have changed
        notifyObservers( ChangeType.UPDATE );
    }
    
    protected abstract void parseResultSet( ResultSet results ) throws Exception;
    protected abstract String getTableName( );
    
    private static Connection m_connection = null;
    private static int m_observerCount;
        
    private HashMap<Integer, AbstractDataChangeObserver> m_observers;
    private String m_key;
}
