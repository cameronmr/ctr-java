/*
 * StatementSummaryPanel.java
 *
 * Created on 20 December 2005, 20:25
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;

import com.rochester.budget.core.IGUIComponent;
import com.rochester.budget.core.StatementDetailsModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Date;
import javax.swing.JPanel;

/**
 *
 * @author Cam
 */
public class StatementSummaryPanel implements IGUIComponent
{
    
    /**
     * Creates a new instance of StatementSummaryPanel 
     */
    public StatementSummaryPanel()
    {
        m_detailsPanel.setDefaultEditor( Date.class, new DateEditor() );
        m_summaryPanel.add( m_detailsPanel.getComponent(), BorderLayout.NORTH );
    }
 
    public Component getComponent()
    {
        return m_summaryPanel;
    }
    
    public void updateView( StatementDetailsModel model )
    {
        // Update the details pane & additional info as required
        m_detailsPanel.updateView( model );
    }
    
    private JPanel m_summaryPanel = new JPanel( new BorderLayout() );
    private DetailsPanel m_detailsPanel = new DetailsPanel();
}
