/*
 * WinampRendererMXBean.java
 *
 * Created on 12 December 2007, 20:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package au.id.ctr.automation.mbeans;

import au.id.ctr.automation.mbeans.RendererMXBean;

/**
 *
 * @author Cameron
 */
public interface WinampRendererMXBean extends RendererMXBean
{
    void playFile(final String file);
}
