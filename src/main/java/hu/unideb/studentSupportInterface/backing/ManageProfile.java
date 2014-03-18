/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.unideb.studentSupportInterface.backing;

import hu.unideb.studentSupportInterface.dao.LanguageDao;
import hu.unideb.studentSupportInterface.dao.UserDao;
import hu.unideb.studentSupportInterface.model.Language;
import hu.unideb.studentSupportInterface.model.User;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Adam
 */
public class ManageProfile implements Serializable{
    
    private User user;
    private UserDao userDao;
    private List<Language> languages;
    private LanguageDao languageDao;
    private List<Language> selectedLanguages;
    private List<Language> oldSelectedLanguages;
    
    @PostConstruct
    public void initBean(){
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        user = (User)userDao.loadUserByUsername(auth.getName());
        
        selectedLanguages = oldSelectedLanguages = languageDao.getLanguagesByUser(user);
        languages = languageDao.getAllLanguage();
        
    }
    
    public void updateUser(){
        User temp = userDao.getUserByEmail(user.getEmail());
        if(temp != null && temp.getId() != user.getId()){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ez az e-mail cím már foglalt.", "Ez az e-mail cím már foglalt."));
            return;
        }
        
        user = userDao.updateUser(user);
        
        for(Language l : oldSelectedLanguages){
            if(!selectedLanguages.contains(l)){
                languageDao.removeLanguageFromUser(l, user);
            }
        }
        
        for(Language l : selectedLanguages){
            if(!oldSelectedLanguages.contains(l)){
                languageDao.addLanguageToUser(l, user);
            }
        }
        
        oldSelectedLanguages = selectedLanguages;
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Az adatok módosítása sikeres.", "Az adatok módosítása sikeres."));
        
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

    public List<Language> getSelectedLanguages() {
        return selectedLanguages;
    }

    public void setSelectedLanguages(List<Language> selectedLanguages) {
        this.selectedLanguages = selectedLanguages;
    }
    
    
    
}
