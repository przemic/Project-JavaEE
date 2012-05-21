/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import com.project.entities.Event;
import java.util.Collection;
import javax.ejb.Remote;

/**
 *
 * @author przemic
 */
public interface EventRemote {
    
    public Collection<Event> getAll ();
    public Event getById (Integer eventId);
    public Collection<Event> getAllByUser (Integer userId);
    public void addToFavourite (Integer eventId, Integer userId);
    public void removeFromFavourite (Integer eventId, Integer userId);
}
