/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.daoJdbcImpl;

import hu.unideb.studentSupportInterface.dao.EvaluationDao;
import hu.unideb.studentSupportInterface.mappers.EvaluationMapper;
import hu.unideb.studentSupportInterface.model.Assessment;
import hu.unideb.studentSupportInterface.model.Evaluation;
import hu.unideb.studentSupportInterface.model.User;
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
public class EvaluationDaoJdbcImpl extends JdbcDaoSupport implements EvaluationDao {
    
    @Override
    public Evaluation createEvaluation(Evaluation evaluation){
        String sql = "insert into evaluation (assessment_id, evaluator_id, is_correct) values (:assessment_id, :evaluator_id, :is_correct)";
        
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("assessment_id", evaluation.getAssessment().getId());
        params.addValue("evaluator_id", evaluation.getEvaluator().getId());
        params.addValue("is_correct", evaluation.isCorrect());
        
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(getDataSource());
        KeyHolder key = new GeneratedKeyHolder();
        
        template.update(sql, params, key);
        
        evaluation.setId(key.getKey().intValue());
        
        return evaluation;
        
    }
    
    @Override
    public Evaluation updateEvaluation(Evaluation evaluation){
        String sql = "update evaluation set assessment_id = :assessment_id, evaluator_id = :evaluator_id, is_correct = :is_correct where id = :id";
        
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("assessment_id", evaluation.getAssessment().getId());
        params.addValue("evaluator_id", evaluation.getEvaluator().getId());
        params.addValue("is_correct", evaluation.isCorrect());
        params.addValue("id", evaluation.getId());
        
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(getDataSource());
        template.update(sql, params);
        
        return evaluation;
        
    }
    
    @Override
    public boolean deleteEvaluation(Evaluation evaluation){
        String sql = "delete from evaluation where id = ?";
        
        getJdbcTemplate().update(sql, new Object[]{evaluation.getId()});
        
        return true;
        
    }
    
    @Override
    public List<Evaluation> getEvaluationsByAssessor(User assessor){
        String sql = "select e.id id, e.assessment_id assessment_id, e.solution_id solution_id, e.is_correct is_correct from evaluation e, assessment a where a.id = e.assessment_id and a.assessor_id = ?";
        List<Evaluation> list = null;
        
        list = (List)getJdbcTemplate().query(sql, new Object[]{assessor.getId()}, new EvaluationMapper());
        
        return list;        
        
    }
    
    @Override
    public float getCorrectEvaluationRatioForAssessor(User assessor){
                String sql = "select count(*) from evaluation e, assessment a where a.id = e.assessment_id and a.assessor_id = ? and is_correct is true";
                String sql2 = "select count(*) from evaluation e, assessment a where a.id = e.assessment_id and a.assessor_id = ?";
                int countCorrect;
                int countTotal;
                
                countCorrect = getJdbcTemplate().queryForInt(sql, new Object[]{assessor.getId()});
                countTotal = getJdbcTemplate().queryForInt(sql2, new Object[]{assessor.getId()});
                
                return new Float(countCorrect / countTotal);

    }
    
    @Override
    public Evaluation getEvaluationByAssessmentAndEvaluator(Assessment assessment, User evaluator){
        String sql = "select * from evaluation where assessment_id = ? and evaluator_id = ?";
        
        List<Evaluation> list = (List)getJdbcTemplate().query(sql, new Object[]{assessment.getId(), evaluator.getId()}, new EvaluationMapper());
        
        if(list != null && !list.isEmpty()){
            return list.get(0);
        }
        
        return null;
        
    }
    
    
    @Override
    public int getEvaluationDifferenceForAssessment(Assessment assessment){
        String sql = "select count(*) from evaluation where assessment_id = ? and is_correct = ?";
        
        int pos = getJdbcTemplate().queryForInt(sql, new Object[]{assessment.getId(), true});
        int neg = getJdbcTemplate().queryForInt(sql, new Object[]{assessment.getId(), false});
        
        return pos - neg;
        
    }
    
}
