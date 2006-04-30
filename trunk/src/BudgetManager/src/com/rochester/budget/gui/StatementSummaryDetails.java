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
import com.rochester.budget.core.ViewReconTableModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
            panel.add( getReconciliationRow( summary ) );
            rowCount += 1;

            for ( IAccount account : summary.getReconciliationAccounts() )
            {
                StatementSummary accSummary = summary.getSummaryForAccount( account );
                panel.add( new JLabel( "Reconciliations for ", JLabel.TRAILING) );
                panel.add( new JLabel( account.toString() ) );

                panel.add( new JLabel( Integer.toString( accSummary.getReconciliationCount() ) +
                        " Reconciliations:" ) );
                
                panel.add( getReconciliationRow( accSummary ) );

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
    
    Component getReconciliationRow( final StatementSummary summary )
    {        
        JPanel viewPanel = new JPanel( new BorderLayout());
        JPanel viewPanel2 = new JPanel( );
        viewPanel2.add( new JLabel( summary.getReconciliationValue().toString() ) );
        
        JButton viewButton = new JButton( new AbstractAction( "view" )
        {
            public void actionPerformed( ActionEvent e )
            {
                JPanel panel = new JPanel( new BorderLayout( 5, 5 ) );
                panel.add( new JLabel( "Reconciliations for " + summary.getLabel() ), BorderLayout.NORTH );
                ViewReconTableModel model = new ViewReconTableModel( summary );
                TableSorter sorter = new TableSorter( model );
                
                ViewReconciliationTable table = new ViewReconciliationTable( sorter );
                sorter.setTableHeader( table.getTableHeader() );
                JScrollPane pane = new JScrollPane( table );
                panel.add( pane, BorderLayout.SOUTH );

                JOptionPane.showMessageDialog( null, panel );
            }
        });
            
        viewButton.setMargin( new Insets( 0, 1, 0, 1 ) );
        viewPanel2.add( viewButton );
        viewPanel.add( viewPanel2, BorderLayout.WEST );
        
        return viewPanel;        
    }
 
    private JPanel m_thePanel = new JPanel( new BorderLayout() );
}
