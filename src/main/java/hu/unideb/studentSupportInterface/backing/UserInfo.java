/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.backing;

import hu.unideb.studentSupportInterface.dao.UserDao;
import hu.unideb.studentSupportInterface.model.Role;
import hu.unideb.studentSupportInterface.model.User;
import java.io.Serializable;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Adam
 */
public class UserInfo implements Serializable {

    private User user;
    private UserDao userDao;
    private HashMap<Role,String> roleMap;

    @PostConstruct
    public void initBean() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        user = (User)userDao.loadUserByUsername(auth.getName());
        
        roleMap = new HashMap<Role, String>();
        
        roleMap.put(Role.TUTOR, "Oktató");
        roleMap.put(Role.ADMIN, "Admin");
        roleMap.put(Role.ASSESSOR, "Értékelõ");
        roleMap.put(Role.UPLOADER, "Feltöltõ");
        
    }
    
    public String logout(){
        SecurityContextHolder.clearContext();
        return "/login?faces-redirect=true";
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

    public HashMap<Role, String> getRoleMap() {
        return roleMap;
    }

    public void setRoleMap(HashMap<Role, String> roleMap) {
        this.roleMap = roleMap;
    }
    
    

}
