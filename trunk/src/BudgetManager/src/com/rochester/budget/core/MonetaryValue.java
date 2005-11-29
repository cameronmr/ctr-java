/*
 * MonetaryValue.java
 *
 * Created on 14 February 2005, 21:58
 */

package com.rochester.budget.core;

import com.rochester.budget.core.exceptions.BudgetManagerException;

/**
 *
 * @author Cameron
 */
public class MonetaryValue
{
    /**
     * Creates a new instance of MonetaryValue 
     */
    public MonetaryValue( final int cents )
    {
        m_cents = cents;
    }
    
    public MonetaryValue( final MonetaryValue value )
    {
        m_cents = value.m_cents;
    }
    
    public MonetaryValue( final String value )
    {
        int pos = value.indexOf('.');
        m_cents = ( Integer.parseInt( value.substring( 0, pos ) ) * 100 ) + Integer.parseInt( value.substring( pos + 1) );
    }
        
    public MonetaryValue( final Object value )
    {
        if ( value instanceof Double )
        {            
            Double val = (Double)value;
            Double cents = val*100;
            m_cents = cents.intValue();
        }
        else if ( value instanceof Long )
        {            
            Long val = (Long)value;
            Long cents = val*100;
            m_cents = cents.intValue();
        }                    
    }
    
    public String getCents()
    {
        return Integer.toString( m_cents );
    }
    
    public int getCentsAsInt()
    {
        return m_cents;
    }   
    
    public void subtractValue( final MonetaryValue value ) throws BudgetManagerException
    {
        // TODO: throw specific exception!
        if ( m_cents - value.m_cents < 0 )
        {
            throw new BudgetManagerException( "Value to subtract larger than available value" );
        }
        
        m_cents -= value.m_cents;
    }
    
    public void addValue( final MonetaryValue value )
    {
        m_cents += value.m_cents;
    }
    
    public void setValue( final MonetaryValue value )
    {
        m_cents = value.m_cents;
    }
    
    public double getValue( )
    {
        double cents = m_cents;
        return cents / 100;
    }
    
    public boolean isZero()
    {
        return m_cents == 0;
    }
        
    public String toString()
    {
        int remainingCents = m_cents % 100;        
        StringBuffer amount = new StringBuffer();
        amount.append( "$");
        amount.append( m_cents / 100 );
        amount.append( remainingCents<10?".0":"." );
        amount.append( remainingCents );
        return amount.toString();
    }
    
    public boolean equals( Object o )
    {
        if ( o instanceof MonetaryValue )
        {
            MonetaryValue v = (MonetaryValue)o;
            if ( v.m_cents == m_cents )
            {
                return true;
            }
        }
        return false;
    }
    private int m_cents;
}
