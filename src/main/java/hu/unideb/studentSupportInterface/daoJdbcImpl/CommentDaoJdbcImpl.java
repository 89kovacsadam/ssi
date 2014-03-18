/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.daoJdbcImpl;

import hu.unideb.studentSupportInterface.dao.CommentDao;
import hu.unideb.studentSupportInterface.mappers.CommentMapper;
import hu.unideb.studentSupportInterface.model.Comment;
import hu.unideb.studentSupportInterface.model.Solution;
import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 *
 * @author Adam
 */
public class CommentDaoJdbcImpl extends JdbcDaoSupport implements CommentDao{
    
    @Override
    public Comment createComment(Comment comment){
        String sql = "insert into comment (user_id, solution_id, text, reply_to) values (:user_id, :solution_id, :text, :reply_to)";
        
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", comment.getUser().getId());
        params.addValue("solution_id", comment.getSolution().getId());
        params.addValue("text", comment.getText());
        params.addValue("reply_to", comment.getReplyTo() != null ? comment.getReplyTo().getId() : null);
        
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(getDataSource());
        KeyHolder key = new GeneratedKeyHolder();
        
        template.update(sql, params, key);
        
        comment.setId(key.getKey().intValue());
        
        return comment;
        
    }
    
    @Override
    public Comment updateComment(Comment comment){
        String sql = "update comment set user_id = :user_id, solution_id = :solution_id, text = :text, reply_to = :reply_to where id = :id";
        
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", comment.getUser().getId());
        params.addValue("solution_id", comment.getSolution().getId());
        params.addValue("text", comment.getText());
        params.addValue("reply_to", comment.getReplyTo().getId());
        params.addValue("id", comment.getId());
        
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(getDataSource());
        template.update(sql, params);
        
        return comment;
    }
    
    @Override
    public boolean deleteComment(Comment comment){
        String sql = "delete from comment where id = ?";
        
        getJdbcTemplate().update(sql, new Object[]{comment.getId()});
        
        return true;
        
    }
    
    @Override
    public List<Comment> getCommentsBySolution(Solution solution){
        String sql = "select * from comment where solution_id = ? order by time";
        List<Comment> list = null;
        
        list = (List)getJdbcTemplate().query(sql, new Object[]{solution.getId()}, new CommentMapper());
        
        return list;
        
    }
    
    @Override
    public Comment getCommentById(int id){
        String sql = "select * from comment where id = ?";
        List<Comment> list = null;
        
        list = (List)getJdbcTemplate().query(sql, new Object[]{id}, new CommentMapper());
        
        if(list != null && !list.isEmpty()){
            return list.get(0);
        }
        
        return null;
        
    }
    
}
