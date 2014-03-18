/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.dao;

import hu.unideb.studentSupportInterface.model.Comment;
import hu.unideb.studentSupportInterface.model.Solution;
import java.util.List;

/**
 *
 * @author Adam
 */
public interface CommentDao {
    
    public Comment createComment(Comment comment);
    
    public Comment updateComment(Comment comment);
    
    public boolean deleteComment(Comment comment);
    
    public Comment getCommentById(int id);
    
    public List<Comment> getCommentsBySolution(Solution solution);
    
}
