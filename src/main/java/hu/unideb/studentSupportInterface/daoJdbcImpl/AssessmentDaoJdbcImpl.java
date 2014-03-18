/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.daoJdbcImpl;

import hu.unideb.studentSupportInterface.dao.AssessmentDao;
import hu.unideb.studentSupportInterface.dao.CommentDao;
import hu.unideb.studentSupportInterface.mappers.AssessmentMapper;
import hu.unideb.studentSupportInterface.model.Assessment;
import hu.unideb.studentSupportInterface.model.Solution;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 *
 * @author Adam
 */
public class AssessmentDaoJdbcImpl extends JdbcDaoSupport implements AssessmentDao {

    CommentDao commentDao;
    
    @Override
    public Assessment createAssessment(Assessment assessment) {
        if (assessment.getComment() != null && assessment.getComment().getText() != null && !assessment.getComment().getText().isEmpty()) {
            assessment.setComment(commentDao.createComment(assessment.getComment()));
        }
        String sql = "insert into assessment (solution_id, assessor_id, is_correct, comment_id)"
                + "values (:solution_id, :assessor_id, :is_correct, :comment_id)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("solution_id", assessment.getSolution().getId());
        params.addValue("assessor_id", assessment.getAssessor().getId());
        params.addValue("is_correct", assessment.isCorrect());
        params.addValue("comment_id", assessment.getComment() != null && assessment.getComment().getText() != null && !assessment.getComment().getText().isEmpty() ? assessment.getComment().getId() : null);
        KeyHolder key = new GeneratedKeyHolder();
        
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(getDataSource());
        
        template.update(sql, params, key);
        
        assessment.setId(key.getKey().intValue());


        return assessment;
    }
    
    @Override
    public Assessment updateAssessment(Assessment assessment){
        
        String sql = "update table set solution_id = :solution_id, assessor_id = :assessor_id, is_correct = :is_correct, comment_id = :comment_id where id = :id";
        
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("solution_id", assessment.getSolution().getId());
        params.addValue("assessor_id", assessment.getAssessor().getId());
        params.addValue("is_correct", assessment.isCorrect());
        params.addValue("comment_id", assessment.getComment() != null ? assessment.getComment().getId() : null);
        params.addValue("id", assessment.getId());
        
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(getDataSource());
        template.update(sql, params);
        
        return assessment;
    }
    
    @Override
    public boolean deleteAssessment(Assessment assessment){
        
        String sql = "delete from assessment where id = ?";
        
        getJdbcTemplate().update(sql, new Object[]{assessment.getId()});
        
        return true;
    }
    
    @Override
    public Assessment getAssessmentById(int id){
        String sql = "select * from assessment where id = ?";
        
        List<Assessment> list = (List)getJdbcTemplate().query(sql, new Object[]{id}, new AssessmentMapper());
        
        if(list != null && !list.isEmpty()){
            return list.get(0);
        }
        
        return null;
        
    }
    
    @Override
    public List<Assessment> getAllAssessment(){
        String sql = "select * from assessment";
        List<Assessment> list = null;
        
        list = (List)getJdbcTemplate().query(sql, new AssessmentMapper());
        
        return list;
        
    }
    
    @Override
    public List<Assessment> getAssessmentsBySolution(Solution solution){
        String sql = "select * from assessment where solution_id = ? order by time";
        List<Assessment> list = null;
        
        list = (List)getJdbcTemplate().query(sql, new Object[]{solution.getId()}, new AssessmentMapper());
        
        return list;
        
    }

    public CommentDao getCommentDao() {
        return commentDao;
    }

    public void setCommentDao(CommentDao commentDao) {
        this.commentDao = commentDao;
    }
    
    
    
}
