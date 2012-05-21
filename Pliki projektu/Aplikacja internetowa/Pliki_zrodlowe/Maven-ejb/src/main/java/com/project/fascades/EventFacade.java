/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.fascades;

import com.project.entities.Event;
import com.project.entities.EventToUserAsoc;
import com.project.entities.User;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author przemic
 */
@Stateless
public class EventFacade extends AbstractFacade<Event> {
    @PersistenceContext(unitName = "com.project_Maven-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

   public void assignWithUser(User user){
        EventToUserAsoc tmp = new EventToUserAsoc();
        tmp.setUserid(user);
        tmp.setEventid(null);
    }
    public EventFacade() {
        super(Event.class);
    }
    
    public List<Event> getAllApproved(){
        Query q = em.createNamedQuery("Event.findByApproved");
        q.setParameter("approved", 1);
        return q.getResultList();
    }
    
    public List<Event> getAllNonApproved(){
        Query q = em.createNamedQuery("Event.findByApproved");
        q.setParameter("approved", 0);
        return q.getResultList();
    }
    
    
    
    public Collection<User> getAttediesForEvent(Event event){
        return em.createQuery("SELECT u FROM User u WHERE u.id = ANY(SELECT etou.userid.id FROM EventToUserAsoc etou WHERE etou.eventid.id=:id)").setParameter("id", event.getId()).getResultList();
    }
}
