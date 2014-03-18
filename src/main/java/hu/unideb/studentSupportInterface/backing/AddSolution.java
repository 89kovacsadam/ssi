/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.backing;

import hu.unideb.studentSupportInterface.dao.CategoryDao;
import hu.unideb.studentSupportInterface.dao.LanguageDao;
import hu.unideb.studentSupportInterface.dao.SolutionDao;
import hu.unideb.studentSupportInterface.dao.UserDao;
import hu.unideb.studentSupportInterface.model.Category;
import hu.unideb.studentSupportInterface.model.Language;
import hu.unideb.studentSupportInterface.model.Solution;
import hu.unideb.studentSupportInterface.model.User;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Adam
 */
public class AddSolution implements Serializable {

    private Solution solution;
    private SolutionDao solutionDao;
    private LanguageDao languageDao;
    private List<Language> languages;
    private Integer selectedInput;
    private UploadedFile file;
    private UserDao userDao;
    private User user;
    private CategoryDao categoryDao;
    private List<Category> categories;
    private List<Category> selectedCategories;

    @PostConstruct
    public void initBean() {
        solution = new Solution();
        languages = languageDao.getAllLanguage();
        selectedInput = 2;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        user = (User) userDao.loadUserByUsername(auth.getName());

        categories = categoryDao.getAllCategory();

    }

    public void uploadSolution() {
        solution.setUploader(user);
        if (selectedInput == 2) {
            try {
                solution.setCode(null);
                solution.setFile(file.getInputstream());
            } catch (IOException ex) {
                Logger.getLogger(AddSolution.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        solution = solutionDao.createSolution(solution);

        for (Category c : selectedCategories) {
            solutionDao.addSolutionToCategory(solution, c);
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        String outcome = "existingSolutions?faces-redirect=true";// Do your thing?
        facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, outcome);
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Az új feladat sikeresen feltöltve.", "Az új feladat sikeresen feltöltve."));

    }

    public void fileUploadListener(FileUploadEvent e) {
        file = e.getFile();
    }

    public void inputChangeListener(ValueChangeEvent e) {
        selectedInput = (Integer) e.getNewValue();
    }

    public LanguageDao getLanguageDao() {
        return languageDao;
    }

    public void setLanguageDao(LanguageDao languageDao) {
        this.languageDao = languageDao;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    public SolutionDao getSolutionDao() {
        return solutionDao;
    }

    public void setSolutionDao(SolutionDao solutionDao) {
        this.solutionDao = solutionDao;
    }

    public Integer getSelectedInput() {
        return selectedInput;
    }

    public void setSelectedInput(Integer selectedInput) {
        this.selectedInput = selectedInput;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public CategoryDao getCategoryDao() {
        return categoryDao;
    }

    public void setCategoryDao(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public List<Category> getSelectedCategories() {
        return selectedCategories;
    }

    public void setSelectedCategories(List<Category> selectedCategories) {
        this.selectedCategories = selectedCategories;
    }

}
