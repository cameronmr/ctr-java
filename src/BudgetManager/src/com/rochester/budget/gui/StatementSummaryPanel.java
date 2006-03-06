/*
 * StatementSummaryPanel.java
 *
 * Created on 20 December 2005, 20:25
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;

import com.rochester.budget.core.ICategory;
import com.rochester.budget.core.IDataChangeObserver;
import com.rochester.budget.core.IDataChangeObserver.ChangeType;
import com.rochester.budget.core.IDatabaseObject;
import com.rochester.budget.core.IGUIComponent;
import com.rochester.budget.core.IStatement;
import com.rochester.budget.core.StatementDetailsModel;
import com.rochester.budget.core.exceptions.BudgetManagerException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Date;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

/**
 *
 * @author Cam
 */
public class StatementSummaryPanel implements IGUIComponent, TreeSelectionListener, IDataChangeObserver
{    
    /**
     * Creates a new instance of StatementSummaryPanel 
     */
    public StatementSummaryPanel()
    {
        m_detailsPanel.setDefaultEditor( Date.class, new DateEditor() );
        
        JPanel innerPanel = new JPanel( );
        innerPanel.setLayout( new BoxLayout( innerPanel, BoxLayout.PAGE_AXIS ));
        innerPanel.add( m_detailsPanel.getComponent() );
        innerPanel.add( m_statementSummaryPanel.getComponent() );
        innerPanel.add( m_categorySummaryPanel.getComponent() );
        innerPanel.add( m_categoryFlatSummaryPanel.getComponent() );
        
        m_summaryPanel.add( innerPanel, BorderLayout.NORTH );
        
        m_categoryTree = new CategoryTreePanel( true );
        m_split.setLeftComponent( m_categoryTree.getComponent() );
        m_split.setRightComponent( m_summaryPanel );
        
        // Set up the listener
        m_categoryTree.addTreeSelectionListener( this );
    }
 
    public Component getComponent()
    {
        return m_split;
    }
    
    public void valueChanged( TreeSelectionEvent e ) 
    {
        TreePath path = e.getNewLeadSelectionPath();
        
        if ( path == null ||
             m_theStatement == null )
        {        
            m_categorySummaryPanel.setStatementSummary( null );
            m_categoryFlatSummaryPanel.setStatementSummary( null );
            return;
        }
        
        CategoryTreePanel.CategoryNode node = ( CategoryTreePanel.CategoryNode )path.getLastPathComponent();
        ICategory cat = node.getCategory();
        
        updateCategoryView( cat );
    }
    
    private void updateCategoryView( ICategory cat )
    {
        // Load the summary & apply it to the panel
        m_categorySummaryPanel.setStatementSummary( m_theStatement.getSummary( cat, false ) );
        
        if ( cat.hasChildren() )
        {
            // Load the children & apply to panel
            m_categoryFlatSummaryPanel.setStatementSummary( m_theStatement.getSummary( cat, true ) );
        }
        else
        {
            // clear the children
            m_categoryFlatSummaryPanel.setStatementSummary( null );
        }
        m_split.validate();
    }
    
    public void updateView( IStatement statement )
    {
        if ( null != m_theStatement )
        {
            m_theStatement.deleteObserver( this );
        }
        
        m_theStatement = statement;   
        if ( null != m_theStatement )
        {
            m_theStatement.addObserver( this );
        }
     
        if ( m_categoryTree.categorySelected() )
        {
            updateCategoryView( m_categoryTree.getSelectedCategory() );
        }
        updateViewInternal();
    }
    
    private void updateViewInternal( )
    {        
        StatementDetailsModel model = new StatementDetailsModel( m_theStatement );
        
        // Reset the category view
        // Update the details pane & additional info as required
        m_detailsPanel.updateView( model );
        if ( m_theStatement == null )
        {
            m_statementSummaryPanel.clearStatementSummary();
        }
        else
        {
            m_statementSummaryPanel.setStatementSummary( m_theStatement.getSummary() );
        }
        
        m_split.validate();
    }
    
    public void notifyDatabaseChange( ChangeType change, IDatabaseObject object ) throws BudgetManagerException
    {
        // If the update is for
        switch ( change )
        {
            case UPDATE:
                // Recalculate the remaining value
                updateViewInternal( );
                break;
        }
    }            
    
    private JPanel m_summaryPanel = new JPanel( new BorderLayout() );
    private JSplitPane m_split = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );      
    private DetailsPanel m_detailsPanel = new DetailsPanel();
    private StatementSummaryDetails m_statementSummaryPanel = new StatementSummaryDetails();
    private StatementSummaryDetails m_categorySummaryPanel = new StatementSummaryDetails();
    private StatementSummaryDetails m_categoryFlatSummaryPanel = new StatementSummaryDetails();
    private CategoryTreePanel m_categoryTree;
    private IStatement m_theStatement;
}
