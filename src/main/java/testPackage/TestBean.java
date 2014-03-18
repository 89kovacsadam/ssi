/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testPackage;

import hu.unideb.studentSupportInterface.dao.RoleDao;
import hu.unideb.studentSupportInterface.dao.UserDao;
import hu.unideb.studentSupportInterface.model.Role;
import hu.unideb.studentSupportInterface.model.User;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Adam
 */
public class TestBean implements Serializable{
    
    private User user;
    private UserDao userDao;
    private RoleDao roleDao;
    
    @PostConstruct
    public void initBean(){
        user = userDao.getUserByEmail("89kovacsadam@gmail.com");
        
        
    }
    
    public String logout(){
        SecurityContextHolder.clearContext();
        return "/default.jsf";
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public RoleDao getRoleDao() {
        return roleDao;
    }

    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }
    
    
    
    
}
