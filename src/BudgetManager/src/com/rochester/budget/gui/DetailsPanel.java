/*
 * DetailsPanel.java
 *
 * Created on 26 December 2005, 12:13
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;

import com.rochester.budget.core.IDetailsPanelModel;
import com.rochester.budget.core.IGUIComponent;
import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.UIDefaults;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;


/**
 *
 * @author Cam
 */
public class DetailsPanel implements IGUIComponent, CellEditorListener, IBudgetActionPublisher
{   
    public class ObjectEditor extends AbstractCellEditor implements DetailsPanelEditor
    {
        public DetailsPanelEditor getDetailPanelEditorComponent( int index, boolean editable, Object value )
        {
            m_theComponent = new JTextField( value != null ? value.toString() : "" );
            m_theComponent.setEnabled( false );
            
            return this;
        }
        
        public Component getComponent()
        {
            return m_theComponent;
        }
        
        public Object getCellEditorValue()
        {
            return null;
        }
                
        public int getIndex()
        {
            return 0;
        }
        
        private Component m_theComponent;
    }
    
    public class BooleanEditor extends AbstractCellEditor implements DetailsPanelEditor
    {        
        public BooleanEditor( )
        {
        }
        
        public BooleanEditor( int index, boolean editable, Object value )
        {
            m_index = index;
            m_field = new JCheckBox( "", value!=null?(Boolean)value:false );
            m_field.setEnabled( editable );
            m_originalValue = value;
            
            m_field.addItemListener( new ItemListener()
            {
                public void itemStateChanged( ItemEvent evt )
                {
                    editingStopped( );
                }
            }); 
        }
        
        public DetailsPanelEditor getDetailPanelEditorComponent( int index, boolean editable, Object value )
        {            
            return new BooleanEditor( index, editable, value );
        }
        
        public Component getComponent()
        {
            return m_field;
        }
        
        public Object getCellEditorValue()
        {            
            return m_field.isSelected();
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
            
            if ( ! m_originalValue.equals( m_field.isSelected() ) )
            {
                stopCellEditing();
            }
        }
        
        private int m_index;
        private JCheckBox m_field;
        private Object m_originalValue;
    }
        
    public class StringEditor extends AbstractCellEditor implements DetailsPanelEditor
    {        
        public StringEditor( )
        {
        }
        
        public StringEditor( int index, boolean editable, Object value )
        {
            m_index = index;
            m_field = new JTextField( value!=null?value.toString():"" );
            m_field.setEnabled( editable );
            m_originalValue = value;
            
            m_field.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent evt )
                {
                    editingStopped( );
                }
            }); 
            
            m_field.addFocusListener( new FocusListener()
            {
                public void focusGained( FocusEvent e )
                {
                    // Do nothing
                }
                
                public void focusLost( FocusEvent e )
                {
                    editingStopped( );
                }
            });
        }
        
        public DetailsPanelEditor getDetailPanelEditorComponent( int index, boolean editable, Object value )
        {            
            return new StringEditor( index, editable, value );
        }
        
        public Component getComponent()
        {
            return m_field;
        }
        
        public Object getCellEditorValue()
        {            
            return m_field.getText();
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
                if ( m_field.getText().length() != 0 )
                {
                    stopCellEditing();
                }
                
                return;
            }
            
            if ( ! m_originalValue.equals( m_field.getText() ) )
            {
                stopCellEditing();
            }
        }
        
        private int m_index;
        private JTextField m_field;
        private Object m_originalValue;
    }
    
    /**
     * Creates a new instance of DetailsPanel 
     */
    public DetailsPanel( )
    {
        // Objects (labels)
        m_editorsByClass.put( Object.class, new ObjectEditor() );
        
        // Strings
        m_editorsByClass.put( String.class, 
                //new UIDefaults.ProxyLazyValue("com.rochester.budget.gui.TableModelDetailsPanel$GenericDetailsEditor") );
                new StringEditor() );

        // Numbers

        // Booleans
        m_editorsByClass.put( Boolean.class, 
                //new UIDefaults.ProxyLazyValue("com.rochester.budget.gui.TableModelDetailsPanel$GenericDetailsEditor") );
                new BooleanEditor() );
    
        m_applyButton.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent emit )
            {
                try
                {
                    m_theModel.applyChanges();
                    m_applyButton.setEnabled( m_theModel.isValid() );
                    
                    // Notify listeners that we have had an update
                    fireActionEvent( DETAILS_UPDATED, m_theModel );
                }
                catch ( Exception e )
                {
                    JOptionPane.showMessageDialog( null, e, "Error", JOptionPane.ERROR_MESSAGE );
                }
            }
        });
        
        m_cancelButton.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent emit )
            {
                try
                {
                    m_theModel.cancelChanges();
                    
                    updateView( m_theModel );
                    
                    // Notify listeners that we have had an update
                    fireActionEvent( DETAILS_UPDATED, m_theModel );
                }
                catch ( Exception e )
                {
                    JOptionPane.showMessageDialog( null, e, "Error", JOptionPane.ERROR_MESSAGE );
                }
            }
        });
    }
    
    public void updateView( IDetailsPanelModel model )
    {
        m_theModel = model;
        
        m_thePanel.removeAll();     
        m_springPanel.removeAll();
        
        // If there is nothing to display, return at this point
        if ( m_theModel.getRowCount() == 0 )
        {
            m_thePanel.validate();
            return;
        }
        
        JPanel buttonPanel = new JPanel( );
        buttonPanel.setLayout( new BoxLayout( buttonPanel, BoxLayout.LINE_AXIS ) );
        
        // Make the panel stick to the right side of the window
        buttonPanel.add( Box.createHorizontalGlue() );
        buttonPanel.add( m_applyButton );
        buttonPanel.add( Box.createRigidArea(new Dimension(5, 0)) );
        buttonPanel.add( m_cancelButton );
        JPanel anotherBorder = new JPanel( new BorderLayout() );
        anotherBorder.add( buttonPanel, BorderLayout.NORTH );
           
        m_thePanel.add( m_springPanel, BorderLayout.NORTH );
        m_thePanel.add( anotherBorder, BorderLayout.EAST );
        m_thePanel.setBorder( BorderFactory.createEmptyBorder( 5,5,5,5 ) );
        
        // Remove all existing components
        m_applyButton.setEnabled( model.isValid() );
        
        // Always draw the first item
        int numPairs = model.getColumnCount();

        //Create and populate the buttonPanel.
        for (int i = 0; i < numPairs; i++) 
        {
            JLabel l = new JLabel( model.getColumnName( i ), JLabel.TRAILING);
            m_springPanel.add( l );
            
            // get the editor for this type of value
            DetailsPanelEditor defaultEditor = getDefaultEditor( model.getColumnClass( i ) );
            DetailsPanelEditor newEditor = defaultEditor.getDetailPanelEditorComponent( i, 
                    model.isEditable( i ),
                    model.getValueAt( i ) );
            
            newEditor.addCellEditorListener( this );
            
            l.setLabelFor( newEditor.getComponent() );
            m_springPanel.add( newEditor.getComponent() );
        }

        //Lay out the buttonPanel.
        SpringUtilities.makeCompactGrid( m_springPanel,
                         numPairs, 2, //rows, cols
                         5, 0,        //initX, initY
                         5, 5);       //xPad, yPad     
                
        m_springPanel.setBorder( BorderFactory.createTitledBorder( model.getTitle() ) );
        m_thePanel.validate();
    }
        
    public Component getComponent()
    {
        return m_thePanel;
    }   
    
    void setDefaultEditor( Class<?> columnClass, DetailsPanelEditor editor ) 
    {
        if ( null != editor )
        {
            m_editorsByClass.put( columnClass, editor );
        }
        else
        {
            m_editorsByClass.remove( columnClass );
        }
    } 
    
    public DetailsPanelEditor getDefaultEditor(Class<?> columnClass) 
    {
        if (columnClass == null) 
        {
            return getDefaultEditor( Object.class );
        }
        else
        {
            Object editor = m_editorsByClass.get(columnClass);
            if (editor != null) 
            {
                return (DetailsPanelEditor)editor;
            }
            else 
            {
                return getDefaultEditor(columnClass.getSuperclass());
            }
        }
    }
    
    public void editingStopped( ChangeEvent e ) 
    {
        if ( null != e )
        {
            DetailsPanelEditor editor = (DetailsPanelEditor)e.getSource();            
            m_theModel.setValueAt( editor.getCellEditorValue(), editor.getIndex() );
            
            // Enable the OK button
            if ( m_theModel.isValid() )
            {
                m_applyButton.setEnabled( true );
            }
        }
    }

    public void editingCanceled( ChangeEvent e ) 
    {
        // Do nothing
        int i = 0;
    }    
    
    public boolean isModified( )
    {
        return m_theModel.isModified();
    }
        
    public synchronized void addActionListener(ActionListener l)
    {   
        m_subscribers = AWTEventMulticaster.add(m_subscribers, l);
    }
    public synchronized void removeActionListener(ActionListener l)
    {   
        m_subscribers = AWTEventMulticaster.remove(m_subscribers, l);
    }
    private void fireActionEvent( int id, Object object )
    {   
        if (m_subscribers != null)
        {
             m_subscribers.actionPerformed(new ActionEvent(object, id, "") );
        }
    }
    
    private JPanel m_thePanel = new JPanel( new BorderLayout( 0, 5 ) );
    private JPanel m_springPanel = new JPanel( new SpringLayout() );
    private JButton m_applyButton = new JButton( "Apply" );
    private JButton m_cancelButton = new JButton( "Cancel" );
    private UIDefaults m_editorsByClass = new UIDefaults();
    private IDetailsPanelModel m_theModel = null;
    private ActionListener m_subscribers = null;    
}
