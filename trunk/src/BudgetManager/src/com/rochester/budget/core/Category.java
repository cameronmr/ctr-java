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
import java.util.HashMap;

/**
 *
 * @author Cam
 */
public class Category extends AbstractDatabaseObject implements ICategory
{
    
    public static ICategory loadCategory( final String categoryKey ) throws CategoryNotFoundException
    {
        // This will throw an exception if the account is not available
        /*IAccount account = m_accounts.get( accountNumber );
        if ( null == account )
        {
            account = new Account( accountNumber );
            
            m_accounts.put( accountNumber, account );
        }
        
        return account;*/
        
        return new Category( categoryKey );
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

    protected String getTableName()
    {
        return "CATEGORY";
    }
    
    private String m_name = new String();
    private String m_description = new String();
    private IAccount m_account = null;
    private ICategory m_parent = null;
 
    private static HashMap<String,ICategory> m_categorys = new HashMap<String,ICategory>();
}
