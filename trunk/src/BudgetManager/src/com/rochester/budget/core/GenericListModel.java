/*
 * GenericListModel.java
 *
 * Created on 27 December 2005, 15:27
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.core;

import com.rochester.budget.core.exceptions.BudgetManagerException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.AbstractListModel;

/**
 *
 * @author Cam
 */   
public class GenericListModel< E extends IDatabaseObject > extends AbstractListModel implements IDataChangeObserver
{    
    public GenericListModel( )
    {
    }
    
    public GenericListModel( Collection<E> contents )
    {
        setItems( contents );
    }

    public void addItem( E newItem )
    {
        newItem.addObserver( this );

        m_contents.add( newItem );   
        
        sortItems();

        fireContentsChanged( this, 0, m_contents.size() );
    }
    
    public void setItems( Collection<E> contents )
    {
        fireIntervalRemoved( this, 0 , m_contents.size() );
        
        for ( E item : m_contents )
        {
            item.deleteObserver( this );
        }
        
        m_contents.clear();
        
        m_contents.addAll( contents );                
        
        sortItems();
        
        for ( E item : m_contents )
        {
            item.addObserver( this );
        }
        
        fireIntervalAdded( this, 0, m_contents.size() );
    }

    public Object getElementAt(int index)
    {
        return m_contents.get( index );
    }

    public int getSize()
    {
        return m_contents.size();
    }

    public void notifyDatabaseChange( ChangeType change, IDatabaseObject object ) throws BudgetManagerException
    {
        switch ( change )
        {
            case UPDATE:
                // TODO: resort the list.. Unfortunately the selection will change if the index moves
                //sortItems();
                break;
            case DELETE:
                // Remove from the list
                m_contents.remove( object );
                break;
        }
        fireContentsChanged( this, 0, m_contents.size() );
    }
    
    public void setComparator( Comparator<? super E> comp )
    {
        m_comparator = comp;
        
        sortItems();
    }
    
    public Collection<E> getContents()
    {
        return m_contents;
    }
    
    private void sortItems()
    {        
        if ( null != m_comparator )
        {
            Collections.sort( m_contents, m_comparator );
        }
    }

    private ArrayList<E> m_contents = new ArrayList<E>( );
    private Comparator< ? super E > m_comparator = null;
}
