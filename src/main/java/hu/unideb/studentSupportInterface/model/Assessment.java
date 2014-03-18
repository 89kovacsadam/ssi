/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.model;

import java.util.Calendar;

/**
 *
 * @author Adam
 */
public class Assessment {
    
    private int id;
    private Solution solution;
    private User assessor;
    private boolean correct;
    private Comment comment;
    private Calendar time;

    public User getAssessor() {
        return assessor;
    }

    public void setAssessor(User assessor) {
        this.assessor = assessor;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Assessment other = (Assessment) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.solution != other.solution && (this.solution == null || !this.solution.equals(other.solution))) {
            return false;
        }
        if (this.assessor != other.assessor && (this.assessor == null || !this.assessor.equals(other.assessor))) {
            return false;
        }
        if (this.time != other.time && (this.time == null || !this.time.equals(other.time))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.solution != null ? this.solution.hashCode() : 0);
        hash = 97 * hash + (this.assessor != null ? this.assessor.hashCode() : 0);
        hash = 97 * hash + (this.time != null ? this.time.hashCode() : 0);
        return hash;
    }
    
    
    
}
