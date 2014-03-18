/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.dao;

import hu.unideb.studentSupportInterface.model.Assessment;
import hu.unideb.studentSupportInterface.model.Solution;
import java.util.List;

/**
 *
 * @author Adam
 */
public interface AssessmentDao {
    
    public Assessment createAssessment(Assessment assessment);
    
    public Assessment updateAssessment(Assessment assessment);
    
    public boolean deleteAssessment(Assessment assessment);
    
    public Assessment getAssessmentById(int id);
    
    public List<Assessment> getAllAssessment();
    
    public List<Assessment> getAssessmentsBySolution(Solution solution);
    
}
