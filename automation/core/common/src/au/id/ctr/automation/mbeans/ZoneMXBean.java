/*
 * ZoneMXBean.java
 *
 * Created on 10 December 2007, 20:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package au.id.ctr.automation.mbeans;

import au.id.ctr.automation.common.interfaces.Common;
import java.util.List;

/**
 *
 * @author Cameron
 */
public interface ZoneMXBean extends Common
{
    String getName();
    
    RendererMXBean getRenderer(String clss);
    
    List<RendererMXBean> getRenderers();
}
