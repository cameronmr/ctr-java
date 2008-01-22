/*
 * LightsRendererImpl.java
 *
 * Created on 12 December 2007, 20:51
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package au.id.ctr.automation.server;

import au.id.ctr.automation.common.AbstractRenderer;
import au.id.ctr.automation.common.AutomationException;
import au.id.ctr.automation.mbeans.LightsRendererMXBean;

/**
 *
 * @author Cameron
 */
public class LightsRendererImpl extends AbstractRenderer implements LightsRendererMXBean
{
    
    /** Creates a new instance of LightsRendererImpl */
    public LightsRendererImpl(final String zone) throws AutomationException
    {
        super(LightsRendererMXBean.class, zone);
    }

    public void lightsOff()
    {
    }
    
}
