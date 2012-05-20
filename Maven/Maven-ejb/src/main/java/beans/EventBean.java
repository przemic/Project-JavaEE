/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import com.project.entities.Event;
import com.project.entities.EventToUserAsoc;
import com.project.entities.User;
import com.project.fascades.EventFacade;
import com.project.fascades.EventToUserAsocFacade;
import com.project.fascades.UserFacade;
import java.util.Collection;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;

/**
 *
 * @author przemic
 */
@Stateless
@Remote(EventRemote.class)
public class EventBean implements EventRemote{
    
    @EJB
    private EventFacade eventFacade;
    @EJB
    private UserFacade userFacade;
    @EJB
    private EventToUserAsocFacade eventToUserFacade;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Collection<Event> getAll () {
        
        return eventFacade.findAll();
    }
    
    @Override
    public Event getById (Integer eventId) {
        Event event = eventFacade.find(eventId);
        
        return event;
    }
    
    @Override
    public Collection<Event> getAllByUser (Integer userId) {
        User user = userFacade.find(userId);
        return userFacade.getEventByName(user.getLogin());
    }
    
    @Override
    public void addToFavourite (Integer eventId, Integer userId) {
        User user = userFacade.find(userId);
        Event event = eventFacade.find(eventId);
        if (!eventToUserFacade.findByEventAndUserExistance(user, event)) {
            eventToUserFacade.assignUserToEvent(user, event);
        }
    }
    
    @Override
    public void removeFromFavourite (Integer eventId, Integer userId) {
        User user = userFacade.find(userId);
        Event event = eventFacade.find(eventId);
        if (eventToUserFacade.findByEventAndUserExistance(user, event)) {
            EventToUserAsoc eu = eventToUserFacade.findByEventAndUser(user, event);
            eventToUserFacade.remove(eu);
        }
    }
    
}
