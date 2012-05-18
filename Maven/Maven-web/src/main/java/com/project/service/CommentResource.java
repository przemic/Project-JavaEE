/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.service;

import com.project.beans.CommentRemote;
import com.project.entities.Comment;
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
    @Path("/{id}")
    @Produces({"application/json"})
    public Comment find(@PathParam("id") Integer id) {
        return commentRemote.getComment(id);
    }
    
    private CommentRemote lookupCommentRemoteBean() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CommentRemote) c.lookup("java:global/com.project_Maven-ear_ear_1.0-SNAPSHOT/Maven-web-1.0-SNAPSHOT/CommentBean");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
