/*
 * RuleDetailsModel.java
 *
 * Created on 28 December 2005, 17:59
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.core;

import com.rochester.budget.core.IRule.RULE_TYPE;
import java.util.Collection;

/**
 *
 * @author Cam
 */
public class RuleDetailsModel extends AbstractDetailsModel<IRule>
{
    
    /** Creates a new instance of RuleDetailsModel */
    public RuleDetailsModel( IRule theRule )
    {
        super( theRule );
    }
        
    public int getColumnCount() 
    {
        return m_labels.length;
    }
    
    public String getColumnName(int col) 
    {
        return m_labels[col];
    }
        
    public boolean isCellEditable(int row, int col) 
    {
        return true;
    }
    
    public Object getValueAt( int row, int column )
    {
        if ( isEmpty() )
        {
            return "";
        }
        
        IRule rule = get( row );
        switch ( column )
        {
            case 0:
                return rule.getName();
            case 1:
                return rule.getDescription();
            case 2:
                return rule.getType();
            case 3:
                return rule;
            case 4:
                // TODO: results
                return "";
        }
        
        return null;
    }
    
    public Class getColumnClass(int c)
    {               
        switch ( c )
        {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return RULE_TYPE.class;
            case 3:
                return IRuleCriterion.class;
            case 4:
                // TODO: results
                break;
        }
        
        return null;
    }    
    
    public void setValueAt(Object value, int row, int col)
    {        
        IRule rule = get( row );
        switch ( col )
        {
            case 0:
                rule.setName( (String) value );
                break;
            case 1:
                rule.setDescription( (String) value );
                break;
            case 2:
                rule.setType( (RULE_TYPE) value );
                break;
            case 3:
                rule.setCriteria( (Collection<IRuleCriterion>) value );
                break;
            case 4:
                // TODO: results
                break;
        }
                
        fireTableCellUpdated(row, col);
    }
    
    public String getTitle()
    {
        return "Rule Details";
    }
        
    private static final String[] m_labels = { "Name", "Description", "Rule Type", "Criteria", "Result" };
}
