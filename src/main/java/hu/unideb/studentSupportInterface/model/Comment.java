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
public class Comment {
    
    private int id;
    private User user;
    private Solution solution;
    private String text;
    private Comment replyTo;
    private Calendar time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Comment getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(Comment replyTo) {
        this.replyTo = replyTo;
    }

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Comment other = (Comment) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.user != other.user && (this.user == null || !this.user.equals(other.user))) {
            return false;
        }
        if (this.solution != other.solution && (this.solution == null || !this.solution.equals(other.solution))) {
            return false;
        }
        if (this.replyTo != other.replyTo && (this.replyTo == null || !this.replyTo.equals(other.replyTo))) {
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
        hash = 13 * hash + (this.user != null ? this.user.hashCode() : 0);
        hash = 13 * hash + (this.solution != null ? this.solution.hashCode() : 0);
        hash = 13 * hash + (this.replyTo != null ? this.replyTo.hashCode() : 0);
        hash = 13 * hash + (this.time != null ? this.time.hashCode() : 0);
        return hash;
    }
    
    
    
}
