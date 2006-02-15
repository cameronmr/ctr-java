/*
 * AbstractListPanel.java
 *
 * Created on 14 February 2006, 09:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rochester.budget.gui;

import com.rochester.budget.core.Comparators;
import com.rochester.budget.core.GenericListModel;
import com.rochester.budget.core.IDatabaseObject;
import com.rochester.budget.core.IGUIComponent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Cameron
 */
public abstract class AbstractListPanel<E extends IDatabaseObject> implements IGUIComponent, ListDataListener {
    
    /** Creates a new instance of AbstractListPanel */
    public AbstractListPanel( GenericListModel<E> model, final String label ) 
    {
        m_theModel = model;
        m_theModel.addListDataListener( this );
        m_theModel.setComparator( Comparators.ALPHABETICAL_ORDER );
        
        
        m_theList = new JList( m_theModel );
        m_theList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );      
        
        JScrollPane scrollPane = new JScrollPane( m_theList );
        scrollPane.setColumnHeaderView( new JLabel( label, JLabel.CENTER ) );
        m_thePanel.add( scrollPane, BorderLayout.CENTER );
        m_thePanel.setPreferredSize( new Dimension( 180, -1 ) );
        
        // New Account Button
        JPanel buttonPanel = new JPanel( new FlowLayout() );
        JButton newButton = new JButton( "New" );
        newButton.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent evt )
            {
                m_theList.clearSelection();
                
                E theItem = onNew();
                m_theModel.addItem( theItem );
                
                m_theList.setSelectedValue( theItem, true );
            }
        });
        buttonPanel.add( newButton );
        
        JButton deleteButton = new JButton( "Delete" );
        deleteButton.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent evt )
            {
                // Get the selection
                if ( !m_theList.isSelectionEmpty() )
                {
                    if ( JOptionPane.OK_OPTION == 
                            JOptionPane.showConfirmDialog( null, "Are you sure you want to delete the selected item?" ) )
                    {
                        m_theModel.removeItemAt( m_theList.getSelectedIndex() );
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog( null, "Please selecte an item to delete." );
                }
            }
        });
        buttonPanel.add( deleteButton );
        
        m_thePanel.add( buttonPanel, BorderLayout.SOUTH );
        
        m_thePanel.setBorder( BorderFactory.createEmptyBorder(5,5,5,5) );
    }
    
    public void setItems( Collection<E> items )
    {
        m_theModel.setItems( items );
    }
    
    public void addListSelectionListener( ListSelectionListener listener )
    {
        m_theList.addListSelectionListener( listener );
    }
    
    public void contentsChanged(ListDataEvent e)
    {
    }
    
    public void intervalAdded(ListDataEvent e)
    {
    }

    public void intervalRemoved(ListDataEvent e)
    {
        // Clear the selection just in case an item has been deleted
        m_theList.clearSelection();
        
    }
    
    public abstract E onNew();
    
    public void onDelete( E deleted )
    {
        // Do nothing
    }
        
    public Component getComponent()
    {
        return m_thePanel;
    }
    
    private JPanel m_thePanel = new JPanel( new BorderLayout( 5,5 ) );
    private JList m_theList;
    private GenericListModel<E> m_theModel;
}
