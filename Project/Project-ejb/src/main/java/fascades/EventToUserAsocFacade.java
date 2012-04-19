/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fascades;

import entities.EventToUserAsoc;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author przemic
 */
@Stateless
public class EventToUserAsocFacade extends AbstractFacade<EventToUserAsoc> {
    @PersistenceContext(unitName = "com.mycompany_Project-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EventToUserAsocFacade() {
        super(EventToUserAsoc.class);
    }
    
}
