/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.backing;

import hu.unideb.studentSupportInterface.dao.CategoryDao;
import hu.unideb.studentSupportInterface.model.Category;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Adam
 */
public class ExistingCategories implements Serializable {

    private List<Category> categories;
    private Category selectedCategory;
    private Category newCategory;
    private CategoryDao categoryDao;

    @PostConstruct
    public void initBean() {
        categories = categoryDao.getAllCategory();
        newCategory = new Category();
    }

    public void deleteCategory() {
        categoryDao.deleteCategory(selectedCategory);
        categories.remove(selectedCategory);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sikeres törlés.", "Sikeres törlés."));
    }

    public void addCategory() {
        newCategory = categoryDao.createCategory(newCategory);
        categories.add(newCategory);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Az új címke létrehozása sikeres.", "Az új címke létrehozása sikeres."));

    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Category getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public Category getNewCategory() {
        return newCategory;
    }

    public void setNewCategory(Category newCategory) {
        this.newCategory = newCategory;
    }

    public CategoryDao getCategoryDao() {
        return categoryDao;
    }

    public void setCategoryDao(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

}
