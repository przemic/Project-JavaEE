/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import beans.UserRemote;
import com.project.entities.Comment;
import com.project.entities.User;
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
@Path("/user")
public class UserResource {
    
    UserRemote userRemote = lookupUserRemoteBean(); 
    
    @GET
    @Path("/{id}")
    @Produces({"application/json"})
    public User find(@PathParam("id") Integer id) {
        return userRemote.getUser(id);
    }
    
    private UserRemote lookupUserRemoteBean() {
        try {
            javax.naming.Context c = new InitialContext();
            return (UserRemote) c.lookup("java:global/com.project_Maven-web_war_1.0-SNAPSHOT/UserBean!beans.UserRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
