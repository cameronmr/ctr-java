/*
 * RuleTab.java
 *
 * Created on 28 December 2005, 17:07
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;

import com.rochester.budget.core.IGUIComponent;
import com.rochester.budget.core.IRule;
import com.rochester.budget.core.IRule.RULE_TYPE;
import com.rochester.budget.core.IRuleCriterion;
import com.rochester.budget.core.RuleDetailsModel;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.ButtonGroup;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Cam
 */
public class RuleTab implements IGUITab, IGUIComponent, ListSelectionListener
{    
    public class RuleTypeEditor extends AbstractCellEditor implements DetailsPanelEditor, ActionListener
    {        
        public RuleTypeEditor( )
        {
        }
        
        public RuleTypeEditor( int index, boolean editable, Object value )
        {
            m_index = index;
            m_originalValue = value;
            
            configureButtons( (RULE_TYPE)value, editable );
        }
        
        public DetailsPanelEditor getDetailPanelEditorComponent( int index, boolean editable, Object value )
        {            
            return new RuleTypeEditor( index, editable, value );
        }
        
        public void configureButtons( RULE_TYPE type, boolean enabled )
        {
            ButtonGroup group = new ButtonGroup();
            for( RULE_TYPE option : RULE_TYPE.values() )
            {
                JRadioButton button = new JRadioButton( option.toString(), option.equals( m_originalValue ) ); 
                m_thePanel.add( button );
                group.add( button );                
                
                button.setActionCommand( option.name() );
                button.addActionListener( this );
            }
        }
        
        public void actionPerformed( ActionEvent evt )
        {
            m_newValue = Enum.valueOf( RULE_TYPE.class, evt.getActionCommand() );
            editingStopped();
        }
        
        public Component getComponent()
        {
            return m_thePanel;
        }
        
        public Object getCellEditorValue()
        {            
            return m_newValue;
        }
                
        public int getIndex( )
        {
            return m_index;
        }
        
        private void editingStopped()
        {
            // If there is no original value, and the text field hasn't been modified don't edit the cell
            if ( m_originalValue == null )
            {
                stopCellEditing();
                
                return;
            }
            
            if ( ! m_originalValue.equals( m_newValue ) )
            {
                stopCellEditing();
            }
        }
        
        private int m_index;
        private RULE_TYPE m_newValue;
        private Object m_originalValue;
        private JPanel m_thePanel = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
    }
    
    /** Creates a new instance of StatementTab */
    public RuleTab()
    {
        m_ruleList = new RuleListPanel();
        m_ruleList.addListSelectionListener( this );
        m_detailsPanel = new DetailsPanel();
        m_detailsPanel.setDefaultEditor( RULE_TYPE.class, new RuleTypeEditor() );
        m_detailsPanel.setDefaultEditor( IRuleCriterion.class, new CriteriaEditor() );
        
        JSplitPane split = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );
        
        split.setLeftComponent( m_ruleList.getComponent() );
        split.setRightComponent( m_detailsPanel.getComponent() );
        
        m_theComponent = split;
    }
    
    public String getTabTitle()
    {
        return "Rules";
    }
    
    public Component getComponent()
    {
        return m_theComponent;
    }
    
    public void stateChanged( ChangeEvent evt ) 
    {
        // Tab selection has changed
    }
    
    public void valueChanged( ListSelectionEvent e ) 
    {
        // Statement List selection has changed
        if ( e.getValueIsAdjusting() ) return;
        
        // Convert the value changed into an update on the ListDetailPanel
        JList source = (JList)e.getSource();
        
        // Single selection only. Returns null if none selected
        RuleDetailsModel model = new RuleDetailsModel( (IRule)source.getSelectedValue() );
        m_detailsPanel.updateView( model );
    }
    
    private Component m_theComponent = null;
    private DetailsPanel m_detailsPanel = null;
    private RuleListPanel m_ruleList = null;
    
}
