/*
 * ReconciliationPanel.java
 *
 * Created on 25 June 2005, 18:00
 */

package com.rochester.budget.gui;

import com.rochester.budget.core.ReconciliationTableModel;
import java.awt.Component;
import java.util.Observable;
import com.rochester.budget.core.IObservingGUIComponent;
import com.rochester.budget.core.ITransaction;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author  Cam
 */
public class ReconciliationPanel implements IObservingGUIComponent
{
    
    /** Creates new form ReconciliationPanel */
    public ReconciliationPanel()
    {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents()
    {
        m_reconciliationModel = new ReconciliationTableModel();
        m_reconciliationLabel = new javax.swing.JLabel();
        m_reconciliationPanel = new JPanel();
        m_reconciliationTable = new ReconciliationTable( m_reconciliationModel );
        m_amountRemainingLabel = new javax.swing.JLabel();

        m_reconciliationPanel.setLayout(new java.awt.BorderLayout());

        m_reconciliationLabel.setText( m_title );
        m_reconciliationPanel.add( m_reconciliationLabel, java.awt.BorderLayout.NORTH );
        m_reconciliationPanel.add( new JScrollPane( m_reconciliationTable ), java.awt.BorderLayout.CENTER);

        m_amountRemainingLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_amountRemainingLabel.setText( m_remaining );
        m_reconciliationPanel.add(m_amountRemainingLabel, java.awt.BorderLayout.SOUTH);
        
        Dimension d = new Dimension();
        d.setSize( m_reconciliationTable.getPreferredScrollableViewportSize().getWidth(),
                m_reconciliationTable.getPreferredSize().getHeight() +
                m_reconciliationLabel.getPreferredSize().getHeight() +
                m_amountRemainingLabel.getPreferredSize().getHeight() );
        m_reconciliationPanel.setPreferredSize( d );
    }

    public void update( Observable observable, Object obj )
    {
        // Handle the new transaction!
        ITransaction trans = (ITransaction)obj;
        
        /* TODO: what to do if NULL? */
        if ( null == trans )
        {
            return;
        }
        
        /* Update the title! */
        m_reconciliationLabel.setText( m_title + trans );
        
        /* Get the value remaining & update the label! */
        m_amountRemainingLabel.setText( m_remaining + trans.getValueRemaining() );
        
        /* Pass to the ReconciliationTable to do some magic */
        m_reconciliationModel.setTransaction( trans );
    }

    public Component getComponent()
    {
        return m_reconciliationPanel;
    }
    
    private javax.swing.JLabel m_amountRemainingLabel;
    private javax.swing.JLabel m_reconciliationLabel;
    private ReconciliationTable m_reconciliationTable;
    private ReconciliationTableModel m_reconciliationModel;
    private JPanel m_reconciliationPanel;
    
    private static final String m_title = "Reconciliations for ";
    private static final String m_remaining = "Amount Remaining to Reconcile: ";
}
