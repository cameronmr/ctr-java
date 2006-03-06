/*
 * StatementSummaryDetails.java
 *
 * Created on 13 February 2006, 21:51
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;

import com.rochester.budget.core.IAccount;
import com.rochester.budget.core.IGUIComponent;
import com.rochester.budget.core.StatementSummary;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

/**
 *
 * @author Cam
 */
public class StatementSummaryDetails implements IGUIComponent
{
    
    /** Creates a new instance of StatementSummaryDetails */
    public StatementSummaryDetails( )
    {        
    }
    
    public void clearStatementSummary( )
    {        
        m_thePanel.removeAll();
    }
    
    public void setStatementSummary( StatementSummary summary )
    {               
        clearStatementSummary();
        if ( null == summary )
        {
            return;
        }
        
        JPanel panel = new JPanel( new SpringLayout() );
        
        panel.setBorder( BorderFactory.createTitledBorder( summary.toString() ) );        
        
        int rowCount = 0;
        if ( summary.hasTransactions() )
        {
            panel.add( new JLabel( Integer.toString( summary.getTransactionCount() ) + " Transactions:" ) );
            panel.add( new JLabel( summary.getTransactionValue().toString() ) );
            rowCount += 1;
        }
        
        if ( summary.hasReconciliations() )
        {
            panel.add( new JLabel( Integer.toString( summary.getReconciliationCount() ) + " Reconciliations:" ) );
            panel.add( new JLabel( summary.getReconciliationValue().toString() ) );
            rowCount += 1;

            for ( IAccount account : summary.getReconciliationAccounts() )
            {
                panel.add( new JLabel( "Reconciliations for ", JLabel.TRAILING) );
                panel.add( new JLabel( account.toString() ) );

                panel.add( new JLabel( Integer.toString( summary.getReconciliationCountForAccount( account ) ) +
                        " Reconciliations:" ) );
                panel.add( new JLabel( summary.getReconciliationValueForAccount( account ).toString() ) );

                rowCount += 2;
            }

            SpringUtilities.makeCompactGrid( panel,
                         rowCount, 2,        //rows, cols
                         5, 0,        //initX, initY
                         5, 2);       //xPad, yPad     
        }
        else
        {
            panel.add( new JLabel( "Nil" ) );
            SpringUtilities.makeCompactGrid( panel,
                         1, 1,        //rows, cols
                         5, 0,        //initX, initY
                         5, 2);       //xPad, yPad     
        }
        
        m_thePanel.add( panel, BorderLayout.NORTH );
        m_thePanel.validate();
    }
    
    public Component getComponent()
    {
        return m_thePanel;
    }        
 
    private JPanel m_thePanel = new JPanel( new BorderLayout() );
}
