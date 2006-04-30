/*
 * ReconciliationPanel.java
 *
 * Created on 25 June 2005, 18:00
 */

package com.rochester.budget.gui;

import com.rochester.budget.core.IDataChangeObserver;
import com.rochester.budget.core.IDataChangeObserver.ChangeType;
import com.rochester.budget.core.IDatabaseObject;
import com.rochester.budget.core.ReconciliationTableModel;
import com.rochester.budget.core.exceptions.UnsavedReconciliationException;
import java.awt.BorderLayout;
import java.awt.Component;
import com.rochester.budget.core.ITransaction;
import com.rochester.budget.core.ITransaction.ReconciliationState;
import com.rochester.budget.core.exceptions.BudgetManagerException;
import java.awt.AWTEventMulticaster;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;


/**
 *
 * @author  Cam
 */
public class ReconciliationPanel extends JPanel implements IDataChangeObserver, ActionListener, IBudgetActionPublisher
{
    
    /** Creates new form ReconciliationPanel */
    public ReconciliationPanel()
    {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     */
    public void initComponents()
    {
        setPreferredSize( new Dimension( -1, 140 ) );
        
        m_reconciliationModel = new ReconciliationTableModel();
        m_reconciliationLabel = new javax.swing.JLabel();
        m_reconciliationTable = new ReconciliationTable( m_reconciliationModel );
        m_reconciliationTable.setCellSelectionEnabled( true );
        m_amountRemainingLabel = new javax.swing.JLabel();
        
        this.setLayout(new java.awt.BorderLayout());
        
        m_reconciliationLabel.setText( m_title );
        this.add( m_reconciliationLabel, java.awt.BorderLayout.NORTH );
        this.add( new JScrollPane( m_reconciliationTable ), java.awt.BorderLayout.CENTER);
        
        m_amountRemainingLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_amountRemainingLabel.setText( m_remaining );
        
        // Action associated with the delete button and 'D' key event
        Action deleteAction = new AbstractAction("Delete Reconciliation")
        {
            public void actionPerformed(ActionEvent e)
            {
                // delete the transaction!
                m_reconciliationModel.deleteReconciliation( m_reconciliationTable.getSelectedRow() );
            }
        };
        
        // Action that will be associated with the next button and with
        // the 'N' key event
        Action nextAction = new AbstractAction("Next Transaction")
        {
            public void actionPerformed(ActionEvent e)
            {
                // Next Transaction, notify the transaction table
                fireActionEvent( NEXT_TRANSACTION );
            }
        };
        
        // Action that will be associated with the Previous button and with
        // the 'P' key event
        Action previousAction = new AbstractAction("Previous Transaction")
        {
            public void actionPerformed(ActionEvent e)
            {
                // Previous Transaction, notify the transaction table
                fireActionEvent( PREV_TRANSACTION );
            }
        };
        
        // Register Key Bindings for the 'O' and 'C' keys:
        InputMap im = this.getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW );
        ActionMap am = this.getActionMap();
        
        // TODO: use constants, rather than strings!
        im.put( KeyStroke.getKeyStroke( KeyEvent.VK_D, java.awt.event.InputEvent.ALT_MASK ), "delete");
        am.put( "delete", deleteAction );
        im.put( KeyStroke.getKeyStroke( KeyEvent.VK_N, java.awt.event.InputEvent.ALT_MASK ), "next");
        am.put( "next", nextAction );
        im.put( KeyStroke.getKeyStroke( KeyEvent.VK_P, java.awt.event.InputEvent.ALT_MASK ), "previous");
        am.put( "previous", previousAction );
        
        // Bottom buttons
        JPanel buttonPanel = new JPanel( new BorderLayout(5,1) );
        JButton deleteButton = new JButton( deleteAction );
        deleteButton.setIcon( createImageIcon( "res/delete.png" ) );
        deleteButton.setMnemonic( KeyEvent.VK_D );
        
        JButton nextButton = new JButton( nextAction );
        nextButton.setIcon( createImageIcon( "res/next.gif" ) );
        nextButton.setHorizontalTextPosition( SwingConstants.LEFT );
        nextButton.setMnemonic( KeyEvent.VK_N );
        
        JButton previousButton = new JButton( previousAction );
        previousButton.setIcon( createImageIcon("res/prev.gif" ) );
        previousButton.setMnemonic( KeyEvent.VK_P );
        
        buttonPanel.add( deleteButton, BorderLayout.WEST );
        JPanel middlePanel = new JPanel( new FlowLayout( FlowLayout.CENTER, 5, 1 ) );
        middlePanel.add( previousButton );
        middlePanel.add( nextButton );
        buttonPanel.add( middlePanel, BorderLayout.CENTER );
        buttonPanel.add( m_amountRemainingLabel, BorderLayout.EAST );
        m_amountRemainingLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_amountRemainingLabel.setText( m_remaining );
        
        this.add( buttonPanel, java.awt.BorderLayout.SOUTH);
    }
    
    public void actionPerformed( ActionEvent e )
    {
        switch ( e.getID() )
        {
            case TRANS_SELECTION_REMOVED:
                removeTransaction();
                break;
            case TRANS_SELECTION_CHANGED:
                // Handle the changed transaction!
                ITransaction trans = (ITransaction)e.getSource();
                try
                {
                    updateTransaction( trans, false );
                }
                catch ( UnsavedReconciliationException ex )
                {
                    // Ask whether we want to delete the unsaved reconciliation, or not
                    if ( JOptionPane.CANCEL_OPTION == JOptionPane.showConfirmDialog( null,
                            "There are unsaved reconciliations for this transaction. Press 'OK' to delete the incomplete reconcilation, or 'Cancel' to complete the reconciliation.",
                            "Delete Reconciliation?", JOptionPane.OK_CANCEL_OPTION ) )
                    {
                        fireActionEvent( PREV_TRANSACTION );
                    }
                    else
                    {
                        try
                        {
                            // If we want to continue then we force the model to discard the
                            // reconciliation
                            updateTransaction( trans, true );
                        }
                        catch( Exception ex2 )
                        {
                            // None thrown when forcing discard
                        }
                    }
                }
                break;
        }
    }
    
    public void notifyDatabaseChange( ChangeType change, IDatabaseObject object ) throws BudgetManagerException
    {
        if ( change == ChangeType.UPDATE )
        {
            ITransaction trans = (ITransaction)object;
            updateLabels( trans );
            
            // If the update results in the transaction still having funds available we set the
            // transaction on the panel again to add another transaction
            if ( trans.getReconciliationState() != ReconciliationState.FULL )
            {
                updateTransaction( trans, false );
            }
        }
    }
    
    public Component getComponent()
    {
        return m_reconciliationPanel;
    }
    
    public synchronized void addActionListener(ActionListener l)
    {
        m_subscribers = AWTEventMulticaster.add(m_subscribers, l);
    }
    
    public synchronized void removeActionListener(ActionListener l)
    {
        m_subscribers = AWTEventMulticaster.remove(m_subscribers, l);
    }
    
    private void fireActionEvent( int id )
    {
        if (m_subscribers != null)
        {
            m_subscribers.actionPerformed(new ActionEvent(this, id, "") );
        }
    }
    
    private void removeTransaction()
    {
        m_reconciliationTable.editingCanceled( null );
        m_reconciliationModel.removeTransaction();
        
        updateLabels( null );
    }
    
    private void updateTransaction( ITransaction trans, boolean discardIncomplete ) throws UnsavedReconciliationException
    {
        m_reconciliationTable.editingCanceled( null );
        m_reconciliationTable.requestFocusInWindow();
        
        // Listen to changes to the transaction so that we can update our labels
        // Note: even if we are already an observer this can be called.
        trans.addObserver( this );
        
        /* Pass to the ReconciliationTable to do some magic */
        m_reconciliationModel.setTransaction( trans, discardIncomplete );
        
        // Update the labels
        updateLabels( trans );
        
        // select the item at the starting point..
        m_reconciliationTable.changeSelection( m_reconciliationModel.getRowCount() - 1, 0, false, false );
    }
    
    private ImageIcon createImageIcon(String path)
    {
        java.net.URL imgURL = ClassLoader.getSystemClassLoader().getResource(path);
        
        //error handling omitted for clarity...
        return new ImageIcon(imgURL);
    }
    
    private void updateLabels( ITransaction trans )
    {
        /* Update the title! */
        m_reconciliationLabel.setText( m_title + ( trans==null?"":trans ) );
        
        /* Get the value remaining & update the label! */
        m_amountRemainingLabel.setText( m_remaining + ( trans==null?"":trans.getValueRemaining() ) );
    }
    
    private javax.swing.JLabel m_amountRemainingLabel;
    private javax.swing.JLabel m_reconciliationLabel;
    private ReconciliationTable m_reconciliationTable;
    private ReconciliationTableModel m_reconciliationModel;
    private JPanel m_reconciliationPanel;
    private ActionListener m_subscribers = null;
    
    private static final String m_title = "Reconciliations for ";
    private static final String m_remaining = "Amount Remaining to Reconcile: ";
}
