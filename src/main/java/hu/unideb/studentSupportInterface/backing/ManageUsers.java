/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.backing;

import hu.unideb.studentSupportInterface.dao.RoleDao;
import hu.unideb.studentSupportInterface.dao.UserDao;
import hu.unideb.studentSupportInterface.model.Role;
import hu.unideb.studentSupportInterface.model.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 *
 * @author akovacs9
 */
public class ManageUsers implements Serializable{
    
    private UserDao userDao;
    private User selectedUser;
    private List<User> activeUsers;
    private List<User> inactiveUsers;
    private List<Role> roles;
    private List<Role> selectedRoles;
    private RoleDao roleDao;
    private HashMap<Role, String> roleLabelMap;
    private MailSender mailSender;
    
    @PostConstruct
    public void initBean(){
        List<User> users = userDao.getAllUser();
        activeUsers = new ArrayList<User>();
        inactiveUsers = new ArrayList<User>();
        
        for (User u : users){
            if(u.isActive()){
                activeUsers.add(u);
            }else{
                inactiveUsers.add(u);
            }
        }
        
        roles = new ArrayList<Role>();
        selectedRoles = new ArrayList<Role>();
        
        roles.add(Role.UPLOADER);
        roles.add(Role.ASSESSOR);
        roles.add(Role.TUTOR);
        roles.add(Role.ADMIN);
        
        roleLabelMap = new HashMap<Role, String>();
        roleLabelMap.put(Role.TUTOR, "Oktató");
        roleLabelMap.put(Role.ADMIN, "Admin");
        roleLabelMap.put(Role.ASSESSOR, "Értékelõ");
        roleLabelMap.put(Role.UPLOADER, "Feltöltõ");
        
    }
    
    public void activateUser(){
        selectedUser.setActive(true);
        selectedUser = userDao.updateUser(selectedUser);
        inactiveUsers.remove(selectedUser);
        activeUsers.add(selectedUser);
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(selectedUser.getEmail());
        msg.setSubject("Felhasználói fiók aktiválása");
        msg.setText("Kedves " + selectedUser.getFirstName() + "!\r\n\r\nFelhasználói fiókod aktiválásra került. Mostantól be tudsz jelentkezni e-mail címed és jelszavad megadásával.\r\n\r\nÜdvözlettel:\r\nStudent Support Interface");
        mailSender.send(msg);
    }
    
    public void inactivateUser(){
        selectedUser.setActive(false);
        selectedUser = userDao.updateUser(selectedUser);
        activeUsers.remove(selectedUser);
        inactiveUsers.add(selectedUser);
    }
    
    public void changeUsersRoles(){
        for(Role r : selectedUser.getRoles()){
            if(!selectedRoles.contains(r)){
                roleDao.revokeRoleFromUser(selectedUser, r);
            }
        }
        
        for(Role r : selectedRoles){
            if(!selectedUser.getRoles().contains(r)){
                roleDao.addRoleToUser(selectedUser, r);
            }
        }
        
        initBean();
        
    }

    public List<User> getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(List<User> activeUsers) {
        this.activeUsers = activeUsers;
    }

    public List<User> getInactiveUsers() {
        return inactiveUsers;
    }

    public void setInactiveUsers(List<User> inactiveUsers) {
        this.inactiveUsers = inactiveUsers;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Role> getSelectedRoles() {
        if(selectedUser != null){
            selectedRoles = selectedUser.getRoles();
        }
        return selectedRoles;
    }

    public void setSelectedRoles(List<Role> selectedRoles) {
        this.selectedRoles = selectedRoles;
    }

    public RoleDao getRoleDao() {
        return roleDao;
    }

    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public HashMap<Role, String> getRoleLabelMap() {
        return roleLabelMap;
    }

    public void setRoleLabelMap(HashMap<Role, String> roleLabelMap) {
        this.roleLabelMap = roleLabelMap;
    }

    public MailSender getMailSender() {
        return mailSender;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    
           
}
