/*
 * Zone.java
 *
 * Created on 10 December 2007, 20:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package au.id.ctr.automation.common.interfaces;

import java.util.List;

/**
 *
 * @author Cameron
 */
public interface Zone
{
    String getName();
    
    <T extends Renderer> T getRenderer(Class<T> clss);
    
    List<Renderer> getRenderers();
}
