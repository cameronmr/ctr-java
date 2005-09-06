/*
 * CategoryPanel.java
 *
 * Created on 6 September 2005, 19:22
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;

import com.rochester.budget.core.Category;
import com.rochester.budget.core.ICategory;
import com.rochester.budget.core.IGUIComponent;
import java.awt.Component;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Cam
 */
public class CategoryPanel implements IGUIComponent
{
    class CategoryNode extends DefaultMutableTreeNode
    {
        CategoryNode( ICategory category )
        {
            m_category = category;
            
            // add the children
            if ( m_category.hasChildren() )
            {
                for ( ICategory cat : m_category.getChildren() )
                {
                    add( new CategoryNode( cat ) );
                }
            }
        }
        
        public boolean isLeaf()
        {
            return !m_category.hasChildren();
        }
        
        /*public int getChildCount()
        {
            return m_category.getChildCount();
        }*/
        
        public String toString()
        {
            return m_category.getName();
        }
        
        private ICategory m_category;
    }
    
    /** Creates a new instance of CategoryPanel */
    public CategoryPanel() 
    {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents()
    {
        // Get the root category
        try
        {
            CategoryNode root = new CategoryNode( Category.getRootCategory() );
            m_categoryTree = new JTree( root );
            m_scrollPane = new JScrollPane( m_categoryTree );
        }
        catch( Exception e )
        {
            // TODO: handle exception
            e.printStackTrace();
        }        
        
    }
    
    public Component getComponent()
    {    
        return m_scrollPane;
    }
    
    JTree m_categoryTree;
    JScrollPane m_scrollPane;    
}
