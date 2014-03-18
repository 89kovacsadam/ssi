/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.backing;

import hu.unideb.studentSupportInterface.dao.LanguageDao;
import hu.unideb.studentSupportInterface.dao.SolutionDao;
import hu.unideb.studentSupportInterface.model.Language;
import hu.unideb.studentSupportInterface.model.Solution;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Adam
 */
public class ExistingLanguages implements Serializable {

    public List<Language> languages;
    public LanguageDao languageDao;
    public Language selectedLanguage;
    public SolutionDao solutionDao;
    public Language newLanguage;

    @PostConstruct
    public void initBean() {
        languages = languageDao.getAllLanguage();
        newLanguage = new Language();
    }

    public void deleteLanguage() {
        List<Solution> solutions = solutionDao.getSolutionsByLanguage(selectedLanguage);
        if (solutions != null && !solutions.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A nyelv nem törölhetõ, mert használatban van.", "A nyelv nem törölhetõ, mert használatban van."));
            return;
        }

        languageDao.deleteLanguage(selectedLanguage);

        languages = languageDao.getAllLanguage();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sikeres törlés.", "Sikeres törlés."));

    }

    public void addLanguage() {
        newLanguage = languageDao.createLanguage(newLanguage);
        initBean();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sikeresen hozzáadtad a nyelvet.", "Sikeresen hozzáadtad a nyelvet."));

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

    public Language getSelectedLanguage() {
        return selectedLanguage;
    }

    public void setSelectedLanguage(Language selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
    }

    public SolutionDao getSolutionDao() {
        return solutionDao;
    }

    public void setSolutionDao(SolutionDao solutionDao) {
        this.solutionDao = solutionDao;
    }

    public Language getNewLanguage() {
        return newLanguage;
    }

    public void setNewLanguage(Language newLanguage) {
        this.newLanguage = newLanguage;
    }
    
    

}
