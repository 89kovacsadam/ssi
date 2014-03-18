/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.dao;

import hu.unideb.studentSupportInterface.model.Role;
import hu.unideb.studentSupportInterface.model.User;
import java.util.List;

/**
 *
 * @author Adam
 */
public interface RoleDao {
    
    public List<Role> getRolesForUser(User user);
    
    public boolean addRoleToUser(User user, Role role);
    
    public boolean revokeRoleFromUser(User user, Role role);
        
}
