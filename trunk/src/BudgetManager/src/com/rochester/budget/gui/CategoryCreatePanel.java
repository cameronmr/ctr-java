/*
 * CategoryCreatePanel.java
 *
 * Created on 12 December 2005, 21:40
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;

import com.rochester.budget.core.DataObjectFactory;
import com.rochester.budget.core.IAccount;
import com.rochester.budget.core.ICategory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Cam
 */
public class CategoryCreatePanel extends JPanel
{
    
    /**
     * Creates a new instance of CategoryCreatePanel 
     */
    public CategoryCreatePanel( ICategory parent, IAccount account )
    {
        m_parent = parent;
        
        // If the parent is provided then we don't need to ask for it!        
        add( new JLabel("Parent Category: ") );
        if ( null == m_parent )
        {
            try
            {
                add( m_categoryCombo = new JComboBox(  DataObjectFactory.loadRootCategory().getDescendants().toArray() ) );
            }
            catch( Exception e )
            {
                // TODO?
            }
        }
        else
        {
            add( new JLabel( m_parent.getName() ) );
        }
        
        add( new JLabel("Name: ") );
        add( m_name = new JTextField( 20 ) );
        
        add( new JLabel("Account: ") );
        add( m_accountCombo = new JComboBox( DataObjectFactory.loadAccounts().toArray() ) );   
        if ( null != account )
        {
            m_accountCombo.setSelectedItem( account );
        }
    }
    
    ICategory getCategory() throws Exception
    {
        ICategory newCat = null;
        try
        {
            // Try to create the category
            if ( m_name.getText().length() == 0 )
            {
                return null;
            }

            if ( m_parent == null )
            {
                m_parent = (ICategory)m_categoryCombo.getSelectedItem();
            }

            IAccount account = (IAccount)m_accountCombo.getSelectedItem();

            newCat = DataObjectFactory.newChildCategory( m_parent, account, m_name.getText() );

            return newCat;
        }
        catch ( Exception e )
        {
            // If the category was created, clean it up
            if ( newCat != null )
            {
                newCat.delete();
            }
            
            throw e;
        }
    }
    
    private ICategory m_parent = null;
    private JTextField m_name = null;
    private JComboBox m_categoryCombo = null;
    private JComboBox m_accountCombo = null;
}
