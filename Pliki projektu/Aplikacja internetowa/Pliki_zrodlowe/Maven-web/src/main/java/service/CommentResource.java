/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import beans.CommentRemote;
import com.project.entities.Comment;
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
@Path("/comment")
public class CommentResource {
    CommentRemote commentRemote = lookupCommentRemoteBean(); 
    
    @GET
    @Path("/{eventId}")
    @Produces({"application/json"})
    public Collection<Comment> find(@PathParam("eventId") Integer eventId) {
        return commentRemote.getAllByEvent(eventId);
    }
    
    private CommentRemote lookupCommentRemoteBean() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CommentRemote) c.lookup("java:global/com.project_Maven-web_war_1.0-SNAPSHOT/CommentBean!beans.CommentRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
