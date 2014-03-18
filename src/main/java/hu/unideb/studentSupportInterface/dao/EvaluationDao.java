/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.dao;

import hu.unideb.studentSupportInterface.model.Assessment;
import hu.unideb.studentSupportInterface.model.Evaluation;
import hu.unideb.studentSupportInterface.model.User;
import java.util.List;

/**
 *
 * @author Adam
 */
public interface EvaluationDao {
    
    public Evaluation createEvaluation(Evaluation evaluation);
    
    public Evaluation updateEvaluation(Evaluation evaluation);
    
    public boolean deleteEvaluation(Evaluation evaluation);
    
    public List<Evaluation> getEvaluationsByAssessor(User assessor);
    
    public float getCorrectEvaluationRatioForAssessor(User assessor);
    
    public Evaluation getEvaluationByAssessmentAndEvaluator(Assessment assessment, User evaluator);
    
    public int getEvaluationDifferenceForAssessment(Assessment assessment);
    
}
