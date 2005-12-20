/*
 * StatementTab.java
 *
 * Created on 20 December 2005, 20:13
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;

import com.rochester.budget.core.IGUIComponent;
import java.awt.Component;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeEvent;

/**
 *
 * @author Cam
 */
public class StatementTab implements IGUITab, IGUIComponent
{
    
    /** Creates a new instance of StatementTab */
    public StatementTab()
    {
        StatementListPanel statementList = new StatementListPanel();
        CategoryTreePanel categoryTree = new CategoryTreePanel();
        StatementSummaryPanel summaryPanel = new StatementSummaryPanel();
        
        JSplitPane split1 = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );
        JSplitPane split2 = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );
        
        split1.setLeftComponent( statementList.getComponent() );
        split2.setDividerLocation( 0.40 );
        split1.setRightComponent( split2 );
        split2.setLeftComponent( categoryTree.getComponent() );
        split2.setRightComponent( summaryPanel.getComponent() );
        
        m_theComponent = split1;
    }
    
    public String getTabTitle()
    {
        return "Statements";
    }
    
    public Component getComponent()
    {
        return m_theComponent;
    }
    
    public void stateChanged( ChangeEvent evt ) 
    {
        // Selection has changed
    }
    
    private Component m_theComponent = null;
}
