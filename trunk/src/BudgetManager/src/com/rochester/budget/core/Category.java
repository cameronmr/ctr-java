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
import com.rochester.budget.core.exceptions.StateSyncException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Cam
 */
public class Category extends AbstractDatabaseObject implements ICategory
{    
    // Protected to ensure no access outside this package
    protected Category( final String categoryKey ) throws CategoryNotFoundException
    {
        super ( categoryKey );
        
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
            m_parent = DataObjectFactory.loadCategory( parentKey );
        }
        
        // Get the associated account
        m_account = DataObjectFactory.loadAccount( results.getString( "CATEGORY_ACC_FKEY" ) );
    }
    
    protected void populateResultSet( ResultSet results ) throws Exception
    { 
        // TODO
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
    
    public List<ICategory> getDescendants()
    {
        // Get all the children, of the children, of children
        ArrayList<ICategory> descendants = new ArrayList<ICategory>();
        for ( ICategory c : m_children )
        {
            // Add the child & it's children to the list
            descendants.add( c );
            descendants.addAll( c.getDescendants() );
        }
        
        return descendants;
    }
    
    public int getChildCount()
    {
        return m_children.size();
    }
    
    public String getTableName()
    {
        return "CATEGORY";
    }    
    
    public boolean isValid( )
    {
        // Check to see if this item is valid at this current time
        return m_name != null &&
                m_description != null &&
                m_account != null &&
                m_parent != null;
        
        // TODO: do we care about children here? Probably not..
    }
    
    public Memento getMemento()
    {
        return new Memento( isValid(), 
                new String( m_name ), 
                new String( m_description ), 
                m_account, // Original reference, not copy
                m_parent, // Original reference, not copy
                new ArrayList<ICategory>( m_children ) ); // new vector, but do not create new copy of all contents
    }
    
    public void restoreMemento( Memento state ) throws StateSyncException
    {
        m_name = (String)state.getSomeState();
        m_description = (String)state.getSomeState();
        m_account = (IAccount)state.getSomeState();
        m_parent = (ICategory)state.getSomeState();
        
        // Reconcile differences between items in container
        ArrayList<ICategory> src = (ArrayList<ICategory>)state.getSomeState();
        reconcileObjectList( src, m_children );
        m_children = src;
    }
        
    private String m_name = null;
    private String m_description = null;
    private IAccount m_account = null;
    private ICategory m_parent = null;
 
    private ArrayList<ICategory> m_children = new ArrayList<ICategory>();    
}
