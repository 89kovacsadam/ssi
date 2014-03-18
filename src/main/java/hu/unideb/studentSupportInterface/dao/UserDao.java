/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.dao;

import hu.unideb.studentSupportInterface.model.Role;
import hu.unideb.studentSupportInterface.model.User;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author Adam
 */
public interface UserDao {
    
    public int countUsers();
    
    public User getUserById(int id);
    
    public User createUser(User user, String password);
    
    public User updateUser(User user);
    
    public User getUserByEmail(String email);
    
    public List<User> getUsersInRole(Role role);
    
    public List<User> getAllUser();
    
    public UserDetails loadUserByUsername(String username);
    
}
