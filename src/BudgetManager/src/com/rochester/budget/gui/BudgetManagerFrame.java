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
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
               
        // Setup the tabbed layout
        Container pane = getContentPane();
        JTabbedPane tabbedPane = new JTabbedPane();
        pane.add( tabbedPane, BorderLayout.CENTER );
        
        //Display the window.
        pack();
        setVisible(true);        
                
        this.setExtendedState(MAXIMIZED_BOTH);
        
        // Add the tabbed contents
        // Transactions & Reconciliations       
        recons = new ReconciliationTab();
        tabbedPane.add( recons.getTabTitle(), recons.getComponent() );
        
        // TODO: fix hack to make divider appear at the correct spot
        javax.swing.SwingUtilities.invokeLater( new Runnable() 
        {
            public void run()
            {
                recons.stateChanged( null );
            }
        });        
                
        // Categories
        CategoryTab categories = new CategoryTab();
        tabbedPane.add( categories.getTabTitle(), categories.getComponent() );
        tabbedPane.addChangeListener( categories );        
        
        // Statements
        StatementTab statements = new StatementTab();
        tabbedPane.add( statements.getTabTitle(), statements.getComponent() );
        
        // Accounts
        AccountTab accounts = new AccountTab();
        tabbedPane.add( accounts.getTabTitle(), accounts.getComponent() );
        
        // TODO: Rules
        tabbedPane.add( "Rules", new JTextField( "Rules" ) );        
    }
    
    private ReconciliationTab recons = null;
}
