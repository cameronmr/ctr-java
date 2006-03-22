/*
 * CategoryStatsPanel.java
 *
 * Created on 20 December 2005, 20:39
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;

import com.rochester.budget.core.DataObjectFactory;
import com.rochester.budget.core.ICategory;
import com.rochester.budget.core.IGUIComponent;
import com.rochester.budget.core.IReconciliation;
import com.rochester.budget.core.MonetaryValue;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

/**
 *
 * @author Cam
 */
public class CategoryStatsPanel implements IGUIComponent, TreeSelectionListener
{
    public class CatRecPair
    {
        public CatRecPair( final ICategory cat )
        {
            m_category = cat; 
        }
        
        public ICategory m_category;
        public ArrayList<IReconciliation> m_reconciliations;
    };
    
    public class RangeComponent extends JPanel implements ActionListener
    {
        public RangeComponent()
        {
            setBorder( BorderFactory.createTitledBorder( "Reconciliation Period" ) );
            setLayout( new BorderLayout( 5, 0 ) );
            add( m_rangeCombo, BorderLayout.WEST );
            add( m_label, BorderLayout.CENTER );
            
            m_rangeCombo.addItem( "2 months" );
            m_rangeCombo.addItem( "6 months" );
            m_rangeCombo.addItem( "12 months" );
            m_rangeCombo.addItem( "Select range" );
            
            m_rangeCombo.addActionListener( this );
            
            m_rangeCombo.setSelectedIndex( 0 );
        }
        
        public void actionPerformed(ActionEvent e) 
        {                        
            m_endDate = new GregorianCalendar();
            // Update the label
            if ( m_rangeCombo.getSelectedIndex() == 0 )            
            {
                m_endDate.add( GregorianCalendar.MONTH, -2 );
            }
            else if ( m_rangeCombo.getSelectedIndex() == 1 )
            {
                m_endDate.add( GregorianCalendar.MONTH, -6 );
            }
            else if ( m_rangeCombo.getSelectedIndex() == 2 )
            {
                m_endDate.add( GregorianCalendar.MONTH, -12 );
            }            
            /*else if ( m_rangeCombo.getSelectedIndex() == 3 )
            {
                // all
            }*/
            else // ( m_rangeCombo.getSelectedIndex() == 3 )
            {
                // select
            }
            
            m_label.setText( "Reconciliations for period: " + df.format( new java.util.Date() ) +
                    " to " + df.format( m_endDate.getTime() ) );
            
            // Notify any observers
        }
        
        public java.util.Date getEndDate()
        {
            return new java.util.Date();
        }
        
        public java.util.Date getStartDate()
        {
            return m_endDate.getTime();
        }
        
        private JComboBox m_rangeCombo = new JComboBox();
        private GregorianCalendar m_endDate;
        private JLabel m_label = new JLabel();
        
    };
    
    public class GraphComponent extends JPanel
    {
        public GraphComponent()
        {
            setBorder( BorderFactory.createTitledBorder( "Data for Period" ) );
        }         
        
        void updateView( final ArrayList<CatRecPair> catRecs, final java.util.Date begin, final java.util.Date end )
        {
            // Calculate the averages, over the given timeperiod
        }
    };    
    
    public class AveragesComponent extends JPanel
    {
        public AveragesComponent()
        {
            setLayout( new BorderLayout( ) );
            setBorder( BorderFactory.createTitledBorder( "Averages for Period" ) );
            setPreferredSize( new Dimension( 150, -1 ) );
            JPanel panel = new JPanel( new GridLayout( 3, 2 ) );
            add( panel, BorderLayout.NORTH );
            panel.add( new JLabel("Weekly:") );
            panel.add( m_weekly );
            panel.add( new JLabel("Monthly:") );
            panel.add( m_monthly );
            panel.add( new JLabel("Yearly:") );
            panel.add( m_yearly );
        }        
        
        void updateView( final ArrayList<CatRecPair> catRecs, final java.util.Date begin, final java.util.Date end )
        {
            // Calculate the averages, over the given timeperiod
            int days = getDaysDifference( begin, end );
            
            MonetaryValue value = new MonetaryValue();
            for ( CatRecPair catRec : catRecs )
            {
                for ( IReconciliation rec : catRec.m_reconciliations )
                {
                    value.addValue( rec.getValue() );
                }
            }
            
            double daily = value.getCentsAsInt() / days;
            double weekly = daily * 7;
            double monthly = daily * 30.44;
            double yearly = daily * 365.25;
            
            m_weekly.setText( new MonetaryValue( (int)weekly ).toString() );
            m_monthly.setText( new MonetaryValue( (int)monthly ).toString() );
            m_yearly.setText( new MonetaryValue( (int)yearly ).toString() );
            
        }
        
        private JLabel m_weekly = new JLabel();
        private JLabel m_monthly = new JLabel();
        private JLabel m_yearly = new JLabel();
    };
    
    /** Creates a new instance of CategoryStatsPanel */
    public CategoryStatsPanel()
    {
        m_statsPanel.setBorder( BorderFactory.createEmptyBorder(5, 5, 5, 5 ) );
        // We want the range component along the top
        m_statsPanel.add( m_range, BorderLayout.NORTH );
        
        // The Averages component on the right
        m_statsPanel.add( m_graph, BorderLayout.CENTER );
        
        // The Graph component in the middle
        m_statsPanel.add( m_averages, BorderLayout.EAST );
    }
        
    public Component getComponent()
    {
        return m_statsPanel;
    }
    
    public void valueChanged( TreeSelectionEvent e ) 
    {
        TreePath path = e.getNewLeadSelectionPath();
        
        if ( path == null )
        {        
            // Nothing selected
            return;
        }
        
        CategoryTreePanel.CategoryNode node = ( CategoryTreePanel.CategoryNode )path.getLastPathComponent();
        ICategory cat = node.getCategory();
        
        ArrayList<CatRecPair> catRecs = new ArrayList<CatRecPair>();
        CatRecPair catRec = new CatRecPair( cat );
        catRecs.add( catRec );
        
        // Load the reconciliations
        try
        {
            catRec.m_reconciliations = new ArrayList( DataObjectFactory.loadReconciliationsForPeriod( cat, 
                    new java.sql.Date( m_range.getStartDate().getTime()),
                    new java.sql.Date( m_range.getEndDate().getTime()) ) );
        }
        catch ( Exception ex )
        {
            JOptionPane.showMessageDialog( null, "Unable to load reconciliations: " + ex.toString(), "Error", JOptionPane.ERROR_MESSAGE );
        }
        
        // Update the components to reflect the changes
        m_averages.updateView( catRecs, m_range.getStartDate(), m_range.getEndDate() );
        m_graph.updateView( catRecs, m_range.getStartDate(), m_range.getEndDate() );
    }
    
    static private int getDaysDifference( final java.util.Date start, final java.util.Date end )
    {
        long d1 = start.getTime();
        long d2 = end.getTime();
        long difMil = d2-d1;
        long milPerDay = 1000*60*60*24; 
        Long l = new Long( difMil / milPerDay );
        return l.intValue();
    }
    
    private JPanel m_statsPanel = new JPanel( new BorderLayout( 5, 5 ) );    
    private RangeComponent m_range = new RangeComponent();    
    private GraphComponent m_graph = new GraphComponent();
    private AveragesComponent m_averages = new AveragesComponent();
    
    static final DateFormat df = DateFormat.getDateInstance( DateFormat.MEDIUM );
}
