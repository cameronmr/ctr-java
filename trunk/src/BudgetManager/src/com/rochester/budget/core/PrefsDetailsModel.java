/*
 * PrefsDetailsModel.java
 *
 * Created on 21 February 2006, 09:32
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.core;

import java.util.HashMap;
import java.util.prefs.Preferences;
import javax.swing.JOptionPane;

/**
 *
 * @author Cam
 */
public class PrefsDetailsModel implements IDetailsPanelModel
{
    
    /** Creates a new instance of PrefsDetailsModel */
    public PrefsDetailsModel( )
    {
        m_preferences = Preferences.userNodeForPackage( DatabaseManager.class );
        setupValues();
    }
    
    public boolean isEditable(int col) 
    {
        return true;
    }
    
    public Object getValueAt( int column )
    {
        if ( isEmpty() )
        {
            return "";
        }
        
        // "Host (IP or hostname)", "Database", "Username", "Password"
        return m_values.get( m_keys[ column ] );
    }
    
    public Class getColumnClass(int c)
    {      
        return String.class;
    }    
    
    public void setValueAt(Object value, int col)
    {        
        // "Host (IP or hostname)", "Database", "Username", "Password"
        m_values.put( m_keys[col], (String)value );
    }
        
    public String getTitle()
    {
        return "Database Connection";
    }
    
    public int getColumnCount() 
    {
        return m_labels.length;
    }
    
    public String getColumnName(int col) 
    {
        return m_labels[col];
    }
    
    public boolean isValid()
    {
        // If everything is set then it is valid
        for ( int i = 0; i < m_keys.length; i++ )
        {
            // If one of the values is the same as the default then we are not valid
            if ( m_values.get( m_keys[i] ).equals( m_defaults[i] ) )
            {
                return false;
            }
        }
        
        return true;
    }
    
    public boolean isModified()
    {
        for ( int i = 0; i < m_keys.length; i++ )
        {
            // If the values have changed then we have been modified
            if ( ! m_values.get( m_keys[i]).equals( m_preferences.get( m_keys[i], m_defaults[i]) ) )
            {
                return true;
            }
        }
        
        return false;
    }
    
    public void applyChanges() throws Exception
    {
        // Store the changes in the Preferences
        m_preferences.putBoolean( "DBconfigured", true );
        
        for ( String str : m_keys )
        {
            m_preferences.put( str, m_values.get( str ) );
        }
        
        // Test the database connection
        try
        {
            DatabaseManager.initiateDatabaseConnection();
        }
        catch ( Exception e )
        {
            JOptionPane.showMessageDialog( null, e.getMessage(), "Error", JOptionPane.OK_OPTION );
        }
    }
    
    public void cancelChanges() throws Exception
    {
        if ( isModified() )
        {
            if ( JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog( null, 
                    "Your changes have not yet been saved. Do you wish to continue?", 
                    "Lose Changes",  
                    JOptionPane.YES_NO_OPTION ) )
            {
                setupValues();
            }
        }
    }
        
    public boolean isEmpty()
    {
        return false;
    }
    
    private void setupValues()
    {
        for ( int i = 0; i < m_keys.length; i++ )
        {
            m_values.put( m_keys[i], m_preferences.get( m_keys[i], m_defaults[i]) );   
        }
    }
            
    private static final String[] m_labels = { "Host (IP or hostname)", "Database", "Username", "Password" };
    private static final String[] m_keys = { "DBhost", "DBname", "DBusername", "DBpassword" };
    private static final String[] m_defaults = { "some.host.com", "aDatabase", "aUser", "aPassword" };
    private Preferences m_preferences;
    private HashMap<String, String> m_values = new HashMap<String, String>();
}
