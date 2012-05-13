/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.fascades;

import com.project.entities.Event;
import com.project.entities.EventToUserAsoc;
import com.project.entities.User;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author przemic
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {
    @PersistenceContext(unitName = "com.project_Maven-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }    
    
    public UserFacade() {
        super(User.class);
    }
    
    public Collection<Event> getEventByName(String name){
        Query userEventsQuery = em.createNamedQuery("User.findUserEvents");
        userEventsQuery.setParameter("login", name);
        Collection events = userEventsQuery.getResultList();        
        return (Collection<Event>)events;
        
    }
    
    public User getUserByName(String login){
        return (User)em.createQuery("SELECT u FROM User u WHERE u.login = :login").setParameter("login", login).getSingleResult();
    }
    
    
    
}
