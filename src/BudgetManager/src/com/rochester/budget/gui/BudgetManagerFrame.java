/*
 * BudgetManagerFrame.java
 *
 * Created on 19 June 2005, 11:05
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 *
 * @author Cam
 */
public class BudgetManagerFrame extends JFrame
{
    
    /** Creates a new instance of BudgetManagerFrame */
    public BudgetManagerFrame()
    {
        super("Budget Manager");
        
        initComponents();
    }
    
    public void initComponents()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create the menu Bar
        JMenuBar menuBar = new JMenuBar();
        
        // File Menu
        JMenu fileMenu = new JMenu( "File" );
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.getAccessibleContext().setAccessibleDescription(
                "File Menu" );
        menuBar.add( fileMenu );
        
        // File Menu Items
        JMenuItem menuItem = new JMenuItem( "Import",
                         KeyEvent.VK_I);
        
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_1, ActionEvent.ALT_MASK) );
        fileMenu.add(menuItem);
        
        setJMenuBar( menuBar );
        
        this.setPreferredSize( new Dimension(1024, 768) );
        
        // Setup the tabbed layout
        Container pane = getContentPane();
        JTabbedPane tabbedPane = new JTabbedPane();
        pane.add( tabbedPane, BorderLayout.CENTER );
        
        // Add the tabbed contents
        // Transactions & Reconciliations
        ReconciliationPanel reconciliationPanel = new ReconciliationPanel();       
        /* The ReconciliationPanel is an observer of the TransactionPanel! */ 
        TransactionPanel transactionPanel = new TransactionPanel( reconciliationPanel );
        
                
        JSplitPane reconPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
        reconPane.setOneTouchExpandable( true );
        reconPane.setTopComponent( reconciliationPanel.getComponent() );
        reconPane.setBottomComponent( transactionPanel.getComponent() );
        tabbedPane.add( "Transactions", reconPane );
        
        // TODO: Categories
        CategoryPanel categoryTree = new CategoryPanel();
        tabbedPane.add( "Categories", categoryTree.getComponent() );
        
        
        // TODO: Statements
        tabbedPane.add( "Statements", new JTextField( "Statements" ) );
        
        // TODO: Accounts
        tabbedPane.add( "Accounts", new JTextField( "Accounts" ) );
        
        // TODO: Rules
        tabbedPane.add( "Rules", new JTextField( "Rules" ) );
        
        // TODO: Filter?
        tabbedPane.add( "Filters", new JTextField( "Filters" ) );
        
        //Display the window.
        pack();
        setVisible(true);        
        
        // Set the divider to be 15% of the screen size
        reconPane.setDividerLocation( 0.15 );
    }
}
