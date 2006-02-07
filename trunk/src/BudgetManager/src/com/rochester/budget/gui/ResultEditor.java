/*
 * ResultEditor.java
 *
 * Created on 7 February 2006, 20:04
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;

import com.rochester.budget.core.DataObjectFactory;
import com.rochester.budget.core.GenericComboBoxModel;
import com.rochester.budget.core.ICategory;
import com.rochester.budget.core.IRule;
import com.rochester.budget.core.IRuleResult;
import com.rochester.budget.core.MonetaryValue;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

/**
 *
 * @author Cam
 */
public class ResultEditor extends AbstractCellEditor implements DetailsPanelEditor
{
    class ResultPanelHelper
    {
        public ResultPanelHelper( ResultEditor editor, IRuleResult result, JPanel thePanel )
        {                        
            m_editor = editor;
            m_result = result;
                     
            populateCategoryCombo(  );
            populateTypeCombo(  );
            setValue( );
            
            thePanel.add( m_categoryCombo );
            thePanel.add( m_typeCombo );    
            thePanel.add( m_value );     
            
            thePanel.validate();
        }
        
        private void populateCategoryCombo( )
        {
            try
            {
                m_categoryModel.setItems( DataObjectFactory.loadRootCategory().getDescendants() );
            }
            catch ( Exception e )
            {}
            
            m_categoryCombo.setSelectedItem( m_result.getReconciliationCategory() );
            m_categoryCombo.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {                    
                    // notify change
                    m_editor.editingStopped();                    
                }
            });
        }
        
        public IRuleResult getResult( )
        {
            m_result.setReconciliationCategory( m_categoryModel.getSelected() );
            m_result.setResultType( (IRuleResult.RESULT_TYPE)m_typeCombo.getSelectedItem() );
            m_result.setReconciliationValue( new MonetaryValue( m_value.getValue() ) );
            return m_result;
        }
                
        private void populateTypeCombo( )
        {            
            m_typeCombo = new JComboBox( m_result.getResultTypes() );
            m_typeCombo.setSelectedItem( m_result.getResultType() );
            
            m_typeCombo.addActionListener( new ActionListener( ) 
            {                
                public void actionPerformed( ActionEvent e )
                {
                    // Update the value
                    setValue();
                    
                    // Notify the change
                    m_editor.editingStopped();
                }
            });
        }
        
        private void setValue( )
        {
            if ( m_value == null )
            {
                m_value = new JFormattedTextField( NumberFormat.getCurrencyInstance() );
                
                // set the action event
                m_value.addKeyListener( new KeyAdapter()
                {
                    public void keyTyped( KeyEvent e )
                    {
                        if ( m_value.getText().length() > 0 )
                        {
                            m_editor.editingStopped();
                        }
                    }
                });
            }
            
            if ( ((IRuleResult.RESULT_TYPE)m_typeCombo.getSelectedItem()).equals( IRuleResult.RESULT_TYPE.FULL ) )
            {
                m_value.setValue( 0 );
                m_value.setEnabled( false );
            }
            else
            {
                m_value.setValue( m_result.getReconciliationValue().getValue() );
                m_value.setEnabled( true );
            }                             
            
            m_value.validate();
        }        
                        
        private GenericComboBoxModel<ICategory> m_categoryModel = new GenericComboBoxModel<ICategory>();
        private JComboBox m_categoryCombo = new JComboBox( m_categoryModel );
        private JComboBox m_typeCombo;  
        private JFormattedTextField m_value;
        private ResultEditor m_editor;
        private IRuleResult m_result;
    };
    
    /** Creates a new instance of ResultEditor */
    public ResultEditor()
    {
    }
    
    public ResultEditor( int index, boolean editable, Object value )
    {
        m_index = index;
        m_theRule = (IRule)value;
        
        m_thePanel = new JPanel( new BorderLayout( 5, 0 ) );  
        m_thePanel.setPreferredSize( new Dimension( -1, 150) );
        m_thePanel.setBorder( BorderFactory.createTitledBorder("") );
        
        JScrollPane scrollPane = new JScrollPane( m_resultPanel );
        m_thePanel.add( scrollPane, BorderLayout.CENTER );
        
        m_moreButton = new JButton( "more" );
        m_moreButton.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                m_theRule.addResult( DataObjectFactory.newResultForRule( m_theRule ) );
                updatePanel();
            }
        });
        
        m_fewerButton = new JButton( "fewer" ); 
        m_fewerButton.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                m_theRule.removeResult( );
                updatePanel();
            }
        });
        JPanel buttonPanel = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        buttonPanel.add( m_moreButton );
        buttonPanel.add( m_fewerButton );
        
        m_thePanel.add( buttonPanel, BorderLayout.SOUTH );  
        
        m_resultPanel.add( m_springPanel, BorderLayout.NORTH );        
        
        updatePanel( );
    }

    public DetailsPanelEditor getDetailPanelEditorComponent( int index, boolean editable, Object value )
    {            
        return new ResultEditor( index, editable, value );
    }

    public Component getComponent()
    {
        return m_thePanel;
    }

    public Object getCellEditorValue()
    {       
        ArrayList<IRuleResult> result = new ArrayList<IRuleResult>();
        for ( ResultPanelHelper helper : m_helpers )
        {
            IRuleResult res = helper.getResult();           
                        
            result.add( res );
        }    
        
        return result;
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
        for ( ResultPanelHelper helper : m_helpers )
        {
            IRuleResult result = helper.getResult();
            
            if( !result.isValid() )
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

        for ( IRuleResult result : m_theRule.getResults() )
        {
            // Draw the three items
            m_helpers.add( new ResultPanelHelper( this, result, m_springPanel ) );
        }

        SpringUtilities.makeCompactGrid( m_springPanel, m_theRule.getResults().size(), 3, 3, 3, 5, 5 );

        m_thePanel.validate();        
    }
    
    private int m_index;
    private JPanel m_thePanel;
    private JButton m_moreButton;
    private JButton m_fewerButton;
    
    private JPanel m_resultPanel = new JPanel( new BorderLayout() );
    private JPanel m_springPanel = new JPanel( new SpringLayout() );
    private IRule m_theRule;
    private ArrayList<ResultPanelHelper> m_helpers = new ArrayList<ResultPanelHelper>();
    
}
