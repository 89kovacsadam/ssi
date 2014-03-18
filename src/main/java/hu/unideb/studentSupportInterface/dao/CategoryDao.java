/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.dao;

import hu.unideb.studentSupportInterface.model.Category;
import java.util.List;

/**
 *
 * @author Adam
 */
public interface CategoryDao {
    
    public Category createCategory(Category category);
    
    public Category updateCategory(Category category);
    
    public boolean deleteCategory(Category category);
    
    public Category getCategoryById(int id);
    
    public List<Category> getAllCategory();
    
}
