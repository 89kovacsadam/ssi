/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.model;

/**
 *
 * @author Adam
 */
public class Evaluation {
    
    private int id;
    private Assessment assessment;
    private User evaluator;
    private boolean correct;

    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    public User getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(User evaluator) {
        this.evaluator = evaluator;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Evaluation other = (Evaluation) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.assessment != other.assessment && (this.assessment == null || !this.assessment.equals(other.assessment))) {
            return false;
        }
        if (this.evaluator != other.evaluator && (this.evaluator == null || !this.evaluator.equals(other.evaluator))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (this.assessment != null ? this.assessment.hashCode() : 0);
        hash = 89 * hash + (this.evaluator != null ? this.evaluator.hashCode() : 0);
        return hash;
    }
    
    
    
}
