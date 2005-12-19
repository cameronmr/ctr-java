/*
 * CategoryTreePanel.java
 *
 * Created on 6 September 2005, 19:22
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;
import com.rochester.budget.core.DataObjectFactory;
import com.rochester.budget.core.ICategory;
import com.rochester.budget.core.IGUIComponent;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author Cam
 */
public class CategoryTreePanel implements IGUIComponent, ChangeListener
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
        
        public String toString()
        {
            return m_category.getName();
        }
        
        public ICategory getCategory()
        {
            return m_category;
        }
        
        private ICategory m_category;
    }
    
    /**
     * Creates a new instance of CategoryTreePanel
     */
    public CategoryTreePanel()
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
            CategoryNode root = new CategoryNode( DataObjectFactory.loadRootCategory() );
            m_categoryTree = new JTree( m_treeModel = new DefaultTreeModel( root ) );
            m_scrollPane = new JScrollPane( m_categoryTree );
        }
        catch( Exception ex )
        {
            // TODO: handle exception
            ex.printStackTrace();
        }
        
        // Add a mouse listener
        MouseListener ml = new MouseAdapter()
        {
            public void mouseReleased( MouseEvent e)
            {
                TreePath selPath = m_categoryTree.getPathForLocation( e.getX(), e.getY() );
                if( selPath != null &&
                        e.isPopupTrigger() )
                {
                    m_categoryTree.clearSelection();
                    m_categoryTree.addSelectionPath( selPath );
                    
                    m_popupMenu.show( e.getComponent(), e.getX(), e.getY() );
                }
            }
        };
        m_categoryTree.addMouseListener(ml);
        
        // Build the PopupMenu
        Action addAction = new AbstractAction( "Add sub-category" )
        {
            public void actionPerformed( ActionEvent e )
            {
                // Popup menu
                addCategory();
            }
        };
        Action removeAction = new AbstractAction( "Disable" )
        {
            public void actionPerformed( ActionEvent e )
            {
                // Popup menu
                JOptionPane.showMessageDialog( null, "disable" );
            }
        };
        
        // Add the actions to the popupMenu
        m_popupMenu = new JPopupMenu();
        m_popupMenu.add( addAction );
        m_popupMenu.add( removeAction );
    }
    
    public Component getComponent()
    {
        return m_scrollPane;
    }
    
    public void stateChanged( ChangeEvent evt ) 
    {
        // If we are selected
        JTabbedPane pane = (JTabbedPane)evt.getSource();
    
        // Get current tab
        if( pane.getSelectedIndex() == 1 )
        {
            // TODO: redraw the tree
	    // May need to make a generic listenr for new object types...
	    // Create new category, receive event, update view.. That kind of
	    // thing
            Enumeration<TreePath> expanded = m_categoryTree.getExpandedDescendants( new TreePath( m_treeModel.getRoot() ) ); 
            try
            {
                m_treeModel.setRoot( new CategoryNode( DataObjectFactory.loadRootCategory() ) );
                while ( expanded != null &&
                        expanded.hasMoreElements() )
                {
                    m_categoryTree.expandPath( expanded.nextElement() );
                }
            }
            catch ( Exception ex )
            {
                JOptionPane.showMessageDialog( null, ex, "Error", JOptionPane.ERROR_MESSAGE );
            }
        }
    }
    
    private void addCategory( )
    {
        TreePath parentPath = m_categoryTree.getSelectionPath();
        
        if ( parentPath != null)
        {
            CategoryNode parentNode = (CategoryNode)(parentPath.getLastPathComponent());
            
            CategoryCreatePanel panel = new CategoryCreatePanel( parentNode.getCategory() );
            
            if ( JOptionPane.showConfirmDialog( null, panel, "Create New Category...", JOptionPane.OK_CANCEL_OPTION ) == JOptionPane.OK_OPTION )
            {
                try
                {
                    CategoryNode childNode = new CategoryNode( panel.getCategory() );
                    
                    m_treeModel.insertNodeInto( childNode, parentNode, parentNode.getChildCount() );                                 
                    
                    // Make sure the user can see the lovely new node.
                    m_categoryTree.makeVisible( new TreePath( childNode.getPath() ) );
                }
                catch ( Exception ex )
                {
                    JOptionPane.showMessageDialog( null, ex, "Error", JOptionPane.ERROR_MESSAGE );
                }
            }
        }
    }
    
    private void disableCategory( )
    {
        
    }
    
    JTree m_categoryTree;
    DefaultTreeModel m_treeModel;
    JScrollPane m_scrollPane;
    JPopupMenu m_popupMenu;
}
