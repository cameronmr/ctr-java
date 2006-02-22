/*
 * CriteriaEditor.java
 *
 * Created on 29 December 2005, 17:03
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;
import com.rochester.budget.core.DataObjectFactory;
import com.rochester.budget.core.GenericComboBoxModel;
import com.rochester.budget.core.IRule;
import com.rochester.budget.core.IRuleCriterion;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;


/**
 *
 * @author Cam
 */
public class CriteriaEditor extends AbstractCellEditor implements DetailsPanelEditor
{     
    class CriterionPanelHelper
    {
        public CriterionPanelHelper( CriteriaEditor editor, IRuleCriterion criteria, JPanel thePanel )
        {                        
            m_editor = editor;
                 
            populateCriteriaCombo( criteria );
            populateTypeCombo( criteria );
            setEditor( criteria );
            
            thePanel.add( m_criteriaCombo );
            thePanel.add( m_typePanel );    
            thePanel.add( m_editorPanel );     
            
            thePanel.validate();
        }
        
        private void populateCriteriaCombo( IRuleCriterion criteria )
        {
            m_criteriaModel.setItems( DataObjectFactory.getAvailableCriteria( criteria ) );
            m_criteriaCombo.setSelectedItem( criteria );
            m_criteriaCombo.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    // Repopulate the type combo based on the selection
                    populateTypeCombo( m_criteriaModel.getSelected() );
                    
                    // Repopulate the editor panel based on the selection
                    populateTypeCombo( m_criteriaModel.getSelected() );
                    
                    // notify change
                    m_editor.editingStopped();                    
                }
            });
        }
        
        public IRuleCriterion getCriterion( )
        {
            IRuleCriterion criterion = m_criteriaModel.getSelected();
            
            criterion.setMatchType( ((Enum) m_typeCombo.getSelectedItem() ).name() );      
            criterion.setMatchValue( m_text.getText() );
            
            return criterion;
        }
                
        private void populateTypeCombo( IRuleCriterion criteria )
        {            
            m_typePanel.removeAll();
            m_typeCombo = new JComboBox( criteria.getMatchTypes() );
            m_typeCombo.setSelectedItem( criteria.getMatchType() );
            m_typePanel.add( m_typeCombo );
            
            m_typeCombo.addActionListener( new ActionListener( ) 
            {                
                public void actionPerformed( ActionEvent e )
                {
                    m_editor.editingStopped();
                }
            });
        }
        
        private void setEditor( IRuleCriterion criteria )
        {
            // Get the editor
            m_editorPanel.removeAll();
            m_editorPanel.add( m_text = new JTextField( criteria.getMatchValue() ));
            
            m_text.addKeyListener( new KeyAdapter()
            {
                public void keyTyped( KeyEvent e )
                {
                    if ( m_text.getText().length() > 0 )
                    {
                        m_editor.editingStopped();
                    }
                }
            });
        }        
                        
        private GenericComboBoxModel<IRuleCriterion> m_criteriaModel = new GenericComboBoxModel<IRuleCriterion>();
        private JComboBox m_criteriaCombo = new JComboBox( m_criteriaModel );
        private JPanel m_typePanel = new JPanel( new GridLayout(1,1) );
        private JComboBox m_typeCombo;
        private JPanel m_editorPanel = new JPanel( new GridLayout(1,1) );      
        private JTextField m_text;
        private CriteriaEditor m_editor;
    };
        
    /**
     * Creates a new instance of CriteriaEditor 
     */
    public CriteriaEditor()
    {
    }     

    public CriteriaEditor( int index, boolean editable, Object value )
    {
        m_index = index;
        m_theRule = (IRule)value;
        
        m_thePanel = new JPanel( new BorderLayout( 5, 0 ) );  
        m_thePanel.setPreferredSize( new Dimension( -1, 150) );
        m_thePanel.setBorder( BorderFactory.createTitledBorder("") );
        
        JScrollPane scrollPane = new JScrollPane( m_criteriaPanel );
        
        //TODO m_detailsPanel.setDefaultEditor( IRuleCriterion.class, new CriterionEditor() );
        m_thePanel.add( scrollPane, BorderLayout.CENTER );
        
        m_moreButton = new JButton( "more" );
        m_moreButton.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                m_theRule.addCriterion( DataObjectFactory.getAvailableCriteria( null ).iterator().next() );
                updatePanel();
            }
        });
        
        m_fewerButton = new JButton( "fewer" ); 
        m_fewerButton.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                m_theRule.removeCriterion( );
                updatePanel();
            }
        });
        JPanel buttonPanel = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        buttonPanel.add( m_moreButton );
        buttonPanel.add( m_fewerButton );
        
        m_thePanel.add( buttonPanel, BorderLayout.SOUTH );  
        
        m_criteriaPanel.add( m_springPanel, BorderLayout.NORTH );        
        
        updatePanel( );
    }

    public DetailsPanelEditor getDetailPanelEditorComponent( int index, boolean editable, Object value )
    {            
        return new CriteriaEditor( index, editable, value );
    }

    public Component getComponent()
    {
        return m_thePanel;
    }

    public Object getCellEditorValue()
    {       
        ArrayList<IRuleCriterion> criteria = new ArrayList<IRuleCriterion>();
        for ( CriterionPanelHelper helper : m_helpers )
        {
            IRuleCriterion crit = helper.getCriterion();           
                        
            criteria.add( crit );
        }    
        
        return criteria;
    }

    public int getIndex( )
    {
        return m_index;
    }
    
    public void editingStopped()
    {          
        m_thePanel.validate();
        
        // Go through the helpers & get the criteria
        boolean allValid = true;
        for ( CriterionPanelHelper helper : m_helpers )
        {
            IRuleCriterion crit = helper.getCriterion();
            
            // The rules aren't set yet
            crit.setRule( m_theRule );
            
            if( !crit.isValid() )
            {
                allValid = false;
            }
        }        
                         
        if ( allValid )
        {
            stopCellEditing();
        }
    }
        
    private void updatePanel( )
    {
        m_springPanel.removeAll();
        m_helpers.clear();

        for ( IRuleCriterion criterion : m_theRule.getCriteria() )
        {
            // Draw the three items
            m_helpers.add( new CriterionPanelHelper( this, criterion, m_springPanel ) );
        }

        SpringUtilities.makeCompactGrid( m_springPanel, m_theRule.getCriteria().size(), 3, 3, 3, 5, 5 );

        m_thePanel.validate();        
    }
    
    private int m_index;
    private JPanel m_thePanel;
    private JButton m_moreButton;
    private JButton m_fewerButton;
    
    private JPanel m_criteriaPanel = new JPanel( new BorderLayout() );
    private JPanel m_springPanel = new JPanel( new SpringLayout() );
    private IRule m_theRule;
    private ArrayList<CriterionPanelHelper> m_helpers = new ArrayList<CriterionPanelHelper>();
}
