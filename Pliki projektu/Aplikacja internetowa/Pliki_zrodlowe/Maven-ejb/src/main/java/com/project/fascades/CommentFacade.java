/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.fascades;

import com.project.entities.Comment;
import com.project.entities.Event;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author przemic
 */
@Stateless
public class CommentFacade extends AbstractFacade<Comment> {
    @PersistenceContext(unitName = "com.project_Maven-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    
    public CommentFacade() {
        super(Comment.class);
    }
    
    public List<Comment> getAllByEvent(Event event){
        return (List<Comment>)em.createQuery("SELECT c FROM Comment c WHERE c.eventid.id=:id ORDER BY c.id DESC").setParameter("id", event.getId()).getResultList();
    }
    
}
