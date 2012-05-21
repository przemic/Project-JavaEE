/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import beans.EventRemote;
import beans.UserRemote;
import com.project.entities.Comment;
import com.project.entities.Event;
import com.project.fascades.EventFacade;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.ws.rs.*;

/**
 *
 * @author przemic
 */
@Path("/event")
public class EventsResource {
    EventRemote eventRemote = lookupEventRemoteBean();
    
    @GET
    @Path("/{userId}")
    @Produces({"application/json"})
    public Collection<Event> find(@PathParam("userId") String userId) {
        
        if (userId.equals("all")) {
            return eventRemote.getAll();
        } else {
            return eventRemote.getAllByUser(Integer.valueOf(userId));
        }
    }
    
    private EventRemote lookupEventRemoteBean() {
        try {
            javax.naming.Context c = new InitialContext();
            return (EventRemote) c.lookup("java:global/com.project_Maven-web_war_1.0-SNAPSHOT/EventBean!beans.EventRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
