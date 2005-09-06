/*
 * Category.java
 *
 * Created on 30 August 2005, 20:30
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.core;
import com.rochester.budget.core.exceptions.CategoryNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Cam
 */
public class Category extends AbstractDatabaseObject implements ICategory
{
    public static ICategory getRootCategory( ) throws CategoryNotFoundException
    {
        String sql = new String("select PKEY from CATEGORY where CATEGORY_PARENT_FKEY = PKEY" );
        
        try
        {
            ResultSet results = AbstractDatabaseObject.getStatement().executeQuery( sql );

            while ( results.next() )
            {
                // Make sure we don't load the root node in an infinite loop
                return loadCategory( results.getString("PKEY") );
            }
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
            Category.loadChildren( category );
        }
        
        return category;
    }
    
    public static void loadChildren( ICategory parent ) throws CategoryNotFoundException
    {
        String sql = new String("select PKEY from " + parent.getTableName() + 
                " where CATEGORY_PARENT_FKEY = '" + parent.getKey() + "'" );
        
        try
        {
            ResultSet results = AbstractDatabaseObject.getStatement().executeQuery( sql );

            while ( results.next() )
            {
                // Make sure we don't load the root node in an infinite loop
                if ( !results.getString("PKEY").equals( parent.getKey() ) )
                {
                    parent.addCategory( loadCategory( results.getString("PKEY") ) );
                }
            }
        }
        catch( SQLException e )
        {
            throw new CategoryNotFoundException( e.toString() );
        }        
    }
    
    /** Creates a new instance of Category */
    private Category( final String categoryKey ) throws CategoryNotFoundException
    {
        setKey( categoryKey );
        
        // Attempt to load the category
        try
        {
            load();            
        }
        catch ( Exception e )
        {
            throw new CategoryNotFoundException( e.toString() );
        }
    }
    
    protected void parseResultSet(ResultSet results) throws Exception
    {
        setKey( results.getString( "PKEY" ) );
        m_name = results.getString( "CATEGORY_NAME" );
        m_description = results.getString( "CATEGORY_DESCRIPTION" );
        
        // Look up the parent if it is something other than itself
        String parentKey = results.getString( "CATEGORY_PARENT_FKEY" );
        
        if ( ! parentKey.equals( getKey() ) )
        {
            m_parent = loadCategory( parentKey );
        }
        
        // Get the associated account
        m_account = Account.loadAccount( results.getString( "CATEGORY_ACC_FKEY" ) );
    }
    
    public String getName()
    {
        return m_name;
    }
    
    public String getDescription()
    {
        return m_description;
    }
    
    public ICategory getParent()
    {
        return m_parent;
    }
    
    public IAccount getAccount()
    {
        return m_account;
    }
    
    public String toString()
    {
        ICategory parent = getParent();
        if ( null == parent )
        {
            return getName();
        }
        else
        {
            return parent.getName() + "/" + getName();
        }
    }
    
    public void addCategory( ICategory category )
    {
        if ( ! m_children.contains( category) )
        {
            m_children.add( category );
        }
    }
    
    public boolean hasChildren( )
    {
        return !m_children.isEmpty();
    }
    
    public List<ICategory> getChildren()
    {
        return m_children;
    }
    
    public int getChildCount()
    {
        return m_children.size();
    }
    
    public String getTableName()
    {
        return "CATEGORY";
    }
    
    public void commit()
    {
        // TODO commit changes
    }
    
    private String m_name = new String();
    private String m_description = new String();
    private IAccount m_account = null;
    private ICategory m_parent = null;
 
    private ArrayList<ICategory> m_children = new ArrayList<ICategory>();
    
    private static HashMap<String,ICategory> m_categories = new HashMap<String,ICategory>();
}
