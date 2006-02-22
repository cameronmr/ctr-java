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

import com.rochester.budget.core.DataObjectFactory;
import com.rochester.budget.core.DatabaseManager;
import com.rochester.budget.core.PrefsDetailsModel;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;


/**
 *
 * @author Cam
 */
public class BudgetManagerFrame extends JFrame implements ActionListener
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
        menuItem.addActionListener( this );
        
        menuItem = new JMenuItem( "Preferences",
                         KeyEvent.VK_P);
        
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_2, ActionEvent.ALT_MASK) );
        fileMenu.add(menuItem);
        menuItem.addActionListener( this );
        
        menuItem = new JMenuItem( "Reload",
                         KeyEvent.VK_R);
        
        fileMenu.add(menuItem);
        menuItem.addActionListener( this );
        
        setJMenuBar( menuBar );
        
        // Setup the tabbed layout
        Container pane = getContentPane();
        m_tabbedPane = new JTabbedPane();
        pane.add( m_tabbedPane, BorderLayout.CENTER );
        
        //Display the window.
        pack();
        setVisible(true);        
                
        this.setExtendedState(MAXIMIZED_BOTH);
        
        Preferences prefs = Preferences.userNodeForPackage( DatabaseManager.class );
        if ( prefs.getBoolean( "DBconfigured", false ) )
        {
            initialiseGUI();
        }
        else
        {
            JOptionPane.showMessageDialog( null, "Please correct/configure your database connection." );
        }        
    }
    
    public void actionPerformed( ActionEvent e )
    {
        if ( e.getActionCommand().equals( "Preferences") )
        {
            DetailsPanel panel = new DetailsPanel( new PrefsDetailsModel() );
            
            JOptionPane.showMessageDialog( this,
                    panel.getComponent(),
                    "Preferences",
                    JOptionPane.PLAIN_MESSAGE);
            
            initialiseGUI();
        }
        else if ( e.getActionCommand().equals( "Import") )
        {
            // import dialog
            JFileChooser chooser = new JFileChooser();   
            if( JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(this) )
            {
                DataObjectFactory.importTransactionsFromFile( chooser.getSelectedFile() );
            }
            
            // Reload the transactions
            initialiseGUI();
        }        
        else if ( e.getActionCommand().equals( "Reload") )
        {
            initialiseGUI();
        }
    }
    
    private void initialiseGUI()
    {        
        m_tabbedPane.removeAll();
        if ( !DatabaseManager.isConnected() )
        {
            try
            {
                DatabaseManager.initiateDatabaseConnection();                
            }
            catch ( Exception e )
            {
                JOptionPane.showMessageDialog( this, 
                        "The database connection is not valid. Please check the configuration.", 
                        "Error", JOptionPane.ERROR_MESSAGE );
                return;
            }            
        }
        
        // Clear out any existing DataObjects
        DataObjectFactory.clearAll();

        // Add the tabbed contents
        // Transactions & Reconciliations       
        recons = new ReconciliationTab();
        m_tabbedPane.add( recons.getTabTitle(), recons.getComponent() );
        m_tabbedPane.addChangeListener( recons );
                      
        // Categories
        CategoryTab categories = new CategoryTab();
        m_tabbedPane.add( categories.getTabTitle(), categories.getComponent() );
        m_tabbedPane.addChangeListener( categories );        
        
        // Statements
        StatementTab statements = new StatementTab();
        m_tabbedPane.add( statements.getTabTitle(), statements.getComponent() );
        m_tabbedPane.addChangeListener( statements );        
        
        // Accounts
        AccountTab accounts = new AccountTab();
        m_tabbedPane.add( accounts.getTabTitle(), accounts.getComponent() );
        m_tabbedPane.addChangeListener( accounts );        
        
        //Rules
        RuleTab rules = new RuleTab();
        m_tabbedPane.add( rules.getTabTitle(), rules.getComponent() );        
        m_tabbedPane.addChangeListener( rules );        
    }
    
    private ReconciliationTab recons = null;
    private JTabbedPane m_tabbedPane = null;
}
