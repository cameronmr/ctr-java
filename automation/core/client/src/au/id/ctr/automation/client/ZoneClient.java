/*
 * ZoneClient.java
 *
 * Created on 22 December 2007, 21:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package au.id.ctr.automation.client;

import au.id.ctr.automation.common.interfaces.Renderer;
import au.id.ctr.automation.common.interfaces.Zone;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Cameron
 */
public class ZoneClient implements Zone
{
    private final String name;
    private Map<Class<?>, Renderer> rendererMap;
    
    /** Creates a new instance of ZoneClient */
    public ZoneClient(final String name)
    {
        this.name = name;
        rendererMap = new HashMap<Class<?>, Renderer>();
    }
    
    public void addRenderer(Class<?> clss, Renderer theRenderer)
    {
        rendererMap.put(clss, theRenderer);
    }

    public String getName()
    {
        return name;
    }

    public <T extends Renderer> T getRenderer(Class<T> clss)
    {
        if (rendererMap.containsKey(clss))
        {
            return (T)rendererMap.get(clss);
        }
        
        return null;
    }

    public List<Renderer> getRenderers()
    {
        return new ArrayList(rendererMap.values());
    }
    
}
