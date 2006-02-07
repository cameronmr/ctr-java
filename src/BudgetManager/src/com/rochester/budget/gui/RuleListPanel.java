/*
 * RuleListPanel.java
 *
 * Created on 28 December 2005, 17:10
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;

import com.rochester.budget.core.Comparators;
import com.rochester.budget.core.DataObjectFactory;
import com.rochester.budget.core.GenericListModel;
import com.rochester.budget.core.IGUIComponent;
import com.rochester.budget.core.IRule;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Cam
 */
public class RuleListPanel implements IGUIComponent
{
    
    /** Creates a new instance of RuleListPanel */
    public RuleListPanel()
    {    
        // Load the account list
        m_ruleModel = new GenericListModel<IRule>( DataObjectFactory.loadRules() );
        m_ruleModel.setComparator( Comparators.ALPHABETICAL_ORDER );
        
        m_ruleList = new JList( m_ruleModel );
        m_ruleList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );        
        
        JScrollPane scrollPane = new JScrollPane( m_ruleList );
        scrollPane.setColumnHeaderView( new JLabel( "Rules", JLabel.CENTER ) );
        m_rulePanel.add( scrollPane, BorderLayout.CENTER );
        m_rulePanel.setPreferredSize( new Dimension( 180, -1 ) );
        
        // New Account Button
        JButton newAccount = new JButton( "New Rule" );
        newAccount.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent evt )
            {
                m_ruleList.clearSelection();
                
                // do something
                IRule rule = DataObjectFactory.newRule();
                m_ruleModel.addItem( rule );
                
                m_ruleList.setSelectedValue( rule, true );
            }
        });
        
        m_rulePanel.add( newAccount, BorderLayout.SOUTH );
        
        m_rulePanel.setBorder( BorderFactory.createEmptyBorder(5,5,5,5) );
    }    
        
    public Component getComponent()
    {
        return m_rulePanel;
    }
    
    public void addListSelectionListener( ListSelectionListener listener )
    {
        m_ruleList.addListSelectionListener( listener );
    }
            
    private JPanel m_rulePanel = new JPanel( new BorderLayout( 5,5 ) );
    private JList m_ruleList;
    private GenericListModel<IRule> m_ruleModel;
}
