/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.dao;

import hu.unideb.studentSupportInterface.model.Category;
import hu.unideb.studentSupportInterface.model.Language;
import hu.unideb.studentSupportInterface.model.Solution;
import hu.unideb.studentSupportInterface.model.User;
import java.util.List;

/**
 *
 * @author Adam
 */
public interface SolutionDao {
    
    public Solution createSolution(Solution solution);
    
    public Solution updateSolution(Solution solution);
    
    public boolean deleteSolution(Solution solution);
    
    public List<Solution> getAllSolution();
    
    public Solution getSolutionById(int id);
    
    public List<Solution> getSolutionsByUploader(User uploader);
    
    public List<Solution> getSolutionsByAssessor(User assessor);
    
    public List<Solution> getSolutionsByCategory(Category category);
    
    public List<Solution> getSolutionsByLanguage(Language language);
    
    public boolean addSolutionToAssessor(Solution solution, User assessor);
    
    public boolean removeSolutionFromAssessor(Solution solution, User Assessor);
    
    public boolean addSolutionToCategory(Solution solution, Category category);
    
    public boolean removeSolutionFromCategory(Solution solution, Category category);
    
    public List<Solution> filterSolutions(List<Category> catList, String pattern, User uploader);
    
    public List<Solution> getAssessedSolutionsByAssessor(User assessor, List<Category> catList, String pattern);
    
    public List<Solution> getSolutionsForAssessByAssessor(User assessor, List<Category> catList, String pattern);
    
}
