/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.fascades;

import com.project.entities.Event;
import com.project.entities.EventToUserAsoc;
import com.project.entities.User;
import java.awt.Container;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author przemic
 */
@Stateless
public class EventToUserAsocFacade extends AbstractFacade<EventToUserAsoc> {
    @PersistenceContext(unitName = "com.project_Maven-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EventToUserAsocFacade() {
        super(EventToUserAsoc.class);
    }
    
    public boolean assignUserToEvent(User user,Event event){       
        EventToUserAsoc asoc = new EventToUserAsoc();
        asoc.setUserid(user);        
        asoc.setEventid(event); 
        this.create(asoc);
        return true;
    }
    
    public EventToUserAsoc findByEventAndUser(User user,Event event){
        return (EventToUserAsoc)em.createQuery("SELECT ue FROM EventToUserAsoc ue WHERE ue.eventid.id = :eventid AND ue.userid.id = :userid").setParameter("eventid", event.getId()).setParameter("userid",user.getId()).getSingleResult();
    }
    
    public boolean findByEventAndUserExistance(User user,Event event){
        Collection<EventToUserAsoc> usCollection = (Collection<EventToUserAsoc>)em.createQuery("SELECT ue FROM EventToUserAsoc ue WHERE ue.eventid.id = :eventid AND ue.userid.id = :userid").setParameter("eventid", event.getId()).setParameter("userid",user.getId()).getResultList();
        if(usCollection.isEmpty()){                   
            return false;
        }
        else
        {
            return true;
        }
    }
    
    
    
    
    
}
