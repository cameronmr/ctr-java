/*
 * DatabaseManager.java
 *
 * Created on 11 September 2005, 09:56
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.core;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Cam
 */
public class DatabaseManager
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
        
    public static void initiateDatabaseConnection() throws SQLException
    {
        // Connect to the necessary database       
        m_connection = DriverManager.getConnection("jdbc:mysql://gateway.rochester.lan/AccountManager?user=Cam&password=Cam");        
    }
    
    public static Statement getStatement() throws SQLException
    {
        return m_connection.createStatement( ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE );
    }        
        
    private static Connection m_connection = null;
}
