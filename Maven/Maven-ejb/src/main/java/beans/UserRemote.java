/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import com.project.entities.User;
import javax.ejb.Remote;

/**
 *
 * @author przemic
*/
public interface UserRemote {
    
    public User getUser(Integer id);
    
}
