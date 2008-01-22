/*
 * WinampRendererMBean.java
 *
 * Created on 12 December 2007, 20:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package au.id.ctr.automation.mbeans;

import au.id.ctr.automation.common.interfaces.Renderer;

/**
 *
 * @author Cameron
 */
public interface WinampRendererMBean extends Renderer
{
    void playFile(final String file);
}
