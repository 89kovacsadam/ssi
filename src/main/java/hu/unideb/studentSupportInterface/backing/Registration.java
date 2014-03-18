/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.unideb.studentSupportInterface.backing;

import hu.unideb.studentSupportInterface.dao.LanguageDao;
import hu.unideb.studentSupportInterface.dao.RoleDao;
import hu.unideb.studentSupportInterface.dao.UserDao;
import hu.unideb.studentSupportInterface.model.Language;
import hu.unideb.studentSupportInterface.model.Role;
import hu.unideb.studentSupportInterface.model.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Adam
 */
public class Registration implements Serializable{
    
    private User newUser;
    private UserDao userDao;
    private RoleDao roleDao;
    private List<Role> roles;
    private List<Language> languages;
    private LanguageDao languageDao;
    private List<Role> selectedRoles;
    private HashMap<Integer,String> roleLabels;
    private List<Language> selectedLanguages;
    private String pwdCheck;
    
    @PostConstruct
    public void initBean(){
        roles = new ArrayList<Role>();
        roles.add(Role.UPLOADER);
        roles.add(Role.ASSESSOR);
        roles.add(Role.TUTOR);
        
        languages = languageDao.getAllLanguage();
        newUser = new User();
        selectedRoles = new ArrayList<Role>();
        roleLabels = new HashMap<Integer,String>();
        roleLabels.put(1, "Felt�lt�");
        roleLabels.put(2, "�rt�kel�");
        roleLabels.put(3, "Oktat�");
    }
    
    public void addUser(){
        if(userDao.loadUserByUsername(newUser.getEmail()) != null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ez az e-mail c�m m�r szerepel az adatb�zisban.", "Ez az e-mail c�m m�r szerepel az adatb�zisban..."));
            return;
        }
        
        if(!newUser.getPassword().equals(pwdCheck)){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A k�t jelsz� nem egyezik.", "A k�t jelsz� nem egyezik."));
            return;
        }
        
        if(!selectedRoles.contains(Role.TUTOR)){
            newUser.setActive(true);
        }
        
        
        newUser = userDao.createUser(newUser, newUser.getPassword());
        
        for(Role r : selectedRoles){
            roleDao.addRoleToUser(newUser, r);
        }
        
        for(Language l : selectedLanguages){
            languageDao.addLanguageToUser(l, newUser);
        }
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String outcome =  "existingSolutions?faces-redirect=true";// Do your thing?
        facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, outcome);
        
    }

    public User getNewUser() {
        return newUser;
    }

    public void setNewUser(User newUser) {
        this.newUser = newUser;
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    public LanguageDao getLanguageDao() {
        return languageDao;
    }

    public void setLanguageDao(LanguageDao languageDao) {
        this.languageDao = languageDao;
    }

    public HashMap<Integer, String> getRoleLabels() {
        return roleLabels;
    }

    public void setRoleLabels(HashMap<Integer, String> roleLabels) {
        this.roleLabels = roleLabels;
    }

    public List<Language> getSelectedLanguages() {
        return selectedLanguages;
    }

    public void setSelectedLanguages(List<Language> selectedLanguages) {
        this.selectedLanguages = selectedLanguages;
    }

    public List<Role> getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(List<Role> selectedRoles) {
        this.selectedRoles = selectedRoles;
    }

    public String getPwdCheck() {
        return pwdCheck;
    }

    public void setPwdCheck(String pwdCheck) {
        this.pwdCheck = pwdCheck;
    }
    
    
    
}
