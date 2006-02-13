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

import com.rochester.budget.core.IDataChangeObserver;
import com.rochester.budget.core.IDatabaseObject;
import com.rochester.budget.core.IGUIComponent;
import com.rochester.budget.core.IStatement;
import com.rochester.budget.core.exceptions.BudgetManagerException;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

/**
 *
 * @author Cam
 */
public class StatementSummaryDetails implements IGUIComponent, IDataChangeObserver
{
    
    /** Creates a new instance of StatementSummaryDetails */
    public StatementSummaryDetails()
    {        
    }
    
    public void setStatement( final IStatement theStatement )
    {        
        if ( null != m_theStatement )
        {
            m_theStatement.deleteObserver( this );
        }
        
        m_theStatement = theStatement;        
        m_theStatement.addObserver( this );
        
        updateView();
    }
    
    public Component getComponent()
    {
        return m_thePanel;
    }
    
    private void updateView()
    {
        m_thePanel.removeAll();
        m_thePanel.setBorder( BorderFactory.createTitledBorder( m_theStatement.toString() + " Summary" ) );        
        
        IStatement.StatementSummary summary = m_theStatement.getSummary();
        m_thePanel.add( new JLabel( "Total Value" ) );
        m_thePanel.add( new JLabel( summary.m_value.toString() ) );
        m_thePanel.add( new JLabel( "Number Reconciliations" ) );
        m_thePanel.add( new JLabel( Integer.toString( summary.m_reconciliations ) ) );
        
        SpringUtilities.makeCompactGrid( m_thePanel,
                         2, 2,        //rows, cols
                         5, 0,        //initX, initY
                         5, 5);       //xPad, yPad     
        
        m_thePanel.validate();
    }
    
    public void notifyDatabaseChange( ChangeType change, IDatabaseObject object ) throws BudgetManagerException
    {
        // If the update is for
        switch ( change )
        {
            case UPDATE:
                // Recalculate the remaining value
                updateView();
                break;
        }
    }            
 
    private JPanel m_thePanel = new JPanel( new SpringLayout() );
    private IStatement m_theStatement;
}
