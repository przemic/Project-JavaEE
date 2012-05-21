/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import com.project.entities.Comment;
import com.project.entities.User;
import com.project.fascades.UserFacade;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;

/**
 *
 * @author przemic
 */
@Stateless
@Remote(UserRemote.class)
public class UserBean implements UserRemote{
    
    @EJB
    private UserFacade userFacade;
    
    @Override
    public User getUser(Integer id) {
        User user = userFacade.find(id);
        
        return user;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
}
