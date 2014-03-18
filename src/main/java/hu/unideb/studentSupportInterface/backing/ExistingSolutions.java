/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.backing;

import hu.unideb.studentSupportInterface.dao.CategoryDao;
import hu.unideb.studentSupportInterface.dao.SolutionDao;
import hu.unideb.studentSupportInterface.dao.UserDao;
import hu.unideb.studentSupportInterface.model.Category;
import hu.unideb.studentSupportInterface.model.Solution;
import hu.unideb.studentSupportInterface.model.User;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Adam
 */
public class ExistingSolutions implements Serializable {

    private List<Solution> solutions;
    private Solution selectedSolution;
    private SolutionDao solutionDao;
    private HashMap<Solution, String> timeMap;
    private List<Category> categories;
    private List<Category> selectedCategories;
    private String pattern;
    private CategoryDao categoryDao;
    private User user;
    private UserDao userDao;

    @PostConstruct
    public void initBean() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        user = (User) userDao.loadUserByUsername(auth.getName());

        if (FacesContext.getCurrentInstance().getViewRoot().getViewId().contains("existingSolutions")) {
            solutions = solutionDao.getAllSolution();
        } else if (FacesContext.getCurrentInstance().getViewRoot().getViewId().contains("mySolutions")) {
            solutions = solutionDao.getSolutionsByUploader(user);
        } else if (FacesContext.getCurrentInstance().getViewRoot().getViewId().contains("assessedSolutions")) {
            solutions = solutionDao.getAssessedSolutionsByAssessor(user, null, null);
        } else if (FacesContext.getCurrentInstance().getViewRoot().getViewId().contains("solutionsToAssess")) {
            solutions = solutionDao.getSolutionsForAssessByAssessor(user, null, null);
        }

        selectedCategories = new ArrayList<Category>();
        categories = categoryDao.getAllCategory();

        timeMap = new HashMap<Solution, String>();
        for (Solution s : solutions) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            timeMap.put(s, format.format(s.getTime().getTime()));
        }

    }

    public void filterSolutions() {

        if (FacesContext.getCurrentInstance().getViewRoot().getViewId().contains("existingSolutions")) {
            if (selectedCategories.isEmpty() && (pattern == null || pattern.isEmpty())) {
                solutions = solutionDao.getAllSolution();
            } else {
                solutions = solutionDao.filterSolutions(selectedCategories, pattern, null);
            }
        } else if (FacesContext.getCurrentInstance().getViewRoot().getViewId().contains("mySolutions")) {
            if (selectedCategories.isEmpty() && (pattern == null || pattern.isEmpty())) {
                solutions = solutionDao.getSolutionsByUploader(user);
            } else {
                solutions = solutionDao.filterSolutions(selectedCategories, pattern, user);
            }
        } else if (FacesContext.getCurrentInstance().getViewRoot().getViewId().contains("assessedSolutions")) {
            if (selectedCategories.isEmpty() && (pattern == null || pattern.isEmpty())) {
                solutions = solutionDao.getAssessedSolutionsByAssessor(user, null, null);
            } else {
                solutions = solutionDao.getAssessedSolutionsByAssessor(user, selectedCategories, pattern);
            }
        } else if (FacesContext.getCurrentInstance().getViewRoot().getViewId().contains("solutionsToAssess")) {
            if (selectedCategories.isEmpty() && (pattern == null || pattern.isEmpty())) {
                solutions = solutionDao.getSolutionsForAssessByAssessor(user, null, null);
            } else {
                solutions = solutionDao.getSolutionsForAssessByAssessor(user, selectedCategories, pattern);
            }
        }

    }

    public String viewSolution() {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.put("solution_id", selectedSolution.getId());
        return "/viewSolution.jsf";
    }
    
    public void deleteSolution(){
        solutionDao.deleteSolution(selectedSolution);
        solutions.remove(selectedSolution);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sikeres törlés.", "Sikeres törlés."));
    }

    public boolean isNotFiltered() {
        return (selectedCategories == null || selectedCategories.isEmpty()) && (pattern == null || pattern.isEmpty());
    }

    public Solution getSelectedSolution() {
        return selectedSolution;
    }

    public void setSelectedSolution(Solution selectedSolution) {
        this.selectedSolution = selectedSolution;
    }

    public List<Solution> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<Solution> solutions) {
        this.solutions = solutions;
    }

    public SolutionDao getSolutionDao() {
        return solutionDao;
    }

    public void setSolutionDao(SolutionDao solutionDao) {
        this.solutionDao = solutionDao;
    }

    public HashMap<Solution, String> getTimeMap() {
        return timeMap;
    }

    public void setTimeMap(HashMap<Solution, String> timeMap) {
        this.timeMap = timeMap;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public List<Category> getSelectedCategories() {
        return selectedCategories;
    }

    public void setSelectedCategories(List<Category> selectedCategories) {
        this.selectedCategories = selectedCategories;
    }

    public CategoryDao getCategoryDao() {
        return categoryDao;
    }

    public void setCategoryDao(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

}
