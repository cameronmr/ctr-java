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
    
    protected Category( ICategory parent, IAccount account, final String name ) throws Exception
    {
        setParent( parent );
        m_name = new String( name );
        setAccount( account );
        setActive( true );
        
        // We have enough info to commit here
        commit();
    }
    
    protected void parseResultSet(ResultSet results) throws Exception
    {
        setKey( results.getString( "PKEY" ) );
        m_name = results.getString( "CATEGORY_NAME" );
        m_description = results.getString( "CATEGORY_DESCRIPTION" );
        m_isActive = results.getBoolean( "CATEGORY_ACTIVE" );
        
        // Look up the parent if it is something other than itself
        String parentKey = results.getString( "CATEGORY_PARENT_FKEY" );
        
        if ( ! parentKey.equals( getKey() ) )
        {
            m_parent = DataObjectFactory.loadCategory( parentKey );
        }
        else
        {
            // This is the root category!
            m_isRoot = true;
        }
        
        // Get the associated account
        m_account = DataObjectFactory.loadAccount( results.getString( "CATEGORY_ACC_FKEY" ) );
    }
    
    protected void populateResultSet( ResultSet results ) throws Exception
    { 
        // update the database object
        results.updateString( "PKEY", getKey() );
        results.updateString( "CATEGORY_NAME", m_name );
        results.updateString( "CATEGORY_DESCRIPTION", m_description );
        results.updateBoolean( "CATEGORY_ACTIVE", m_isActive );
        results.updateString( "CATEGORY_PARENT_FKEY", m_parent.getKey() );
        results.updateString( "CATEGORY_ACC_FKEY", m_account.getKey() );
    }
    
    public String getName()
    {
        return m_name;
    }
    
    public void setName( final String name )
    {
        m_name = new String( name );
        
        storeMemento();
    }
    
    public String getDescription()
    {
        return m_description;
    }
    
    public void setDescription( final String description )
    {
        m_description = new String( description );
        
        storeMemento();
    }
    
    public ICategory getParent()
    {
        return m_parent;
    }
    
    public void setParent( ICategory parent )
    {
        m_parent = parent;
        
        // Add ourselves to the parent
        parent.addCategory( this );
        
        storeMemento();
    }
    
    public IAccount getAccount()
    {
        return m_account;
    }
    
    public void setAccount( IAccount account )
    {
        // TODO: Should we be listening to changes to the account?
        m_account = account;
        
        storeMemento();
    }
    
    public boolean isActive()
    {
        return m_isActive;
    }
    
    public void setActive( boolean active )
    {
        m_isActive = active;
        
        storeMemento();
    }
    
    public String toString()
    {
        ICategory parent = getParent();
        if ( null == parent ||
                parent.isRootNode() )
        {
            return getName();
        }
        else
        {
            return parent.toString() + "/" + getName();
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
    
    public boolean isRootNode()
    {
        return m_isRoot;
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
                m_account != null &&
                m_parent != null;
        
        // TODO: do we care about children here? Probably not..
    }
    
    public Memento getMemento()
    {
        return new Memento(
                ( m_name == null ) ? null : new String( m_name ), 
                ( m_description == null ) ? null : new String( m_description ), 
                m_isActive,
                m_isRoot,
                m_account, // Original reference, not copy
                m_parent, // Original reference, not copy
                new ArrayList<ICategory>( m_children ) ); // new vector, but do not create new copy of all contents
    }
    
    public void restoreMemento( Memento state ) throws StateSyncException
    {
        m_name = (String)state.getSomeState();
        m_description = (String)state.getSomeState();
        m_isActive = (Boolean)state.getSomeState();
        m_isRoot = (Boolean)state.getSomeState();
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
    private boolean m_isRoot = false;
    private boolean m_isActive = false;
 
    private ArrayList<ICategory> m_children = new ArrayList<ICategory>();    
}
