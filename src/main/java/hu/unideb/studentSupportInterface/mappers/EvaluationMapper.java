/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.mappers;

import hu.unideb.studentSupportInterface.dao.AssessmentDao;
import hu.unideb.studentSupportInterface.dao.RoleDao;
import hu.unideb.studentSupportInterface.dao.UserDao;
import hu.unideb.studentSupportInterface.model.Assessment;
import hu.unideb.studentSupportInterface.model.Evaluation;
import hu.unideb.studentSupportInterface.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 *
 * @author Adam
 */
public class EvaluationMapper implements ResultSetExtractor {

    @Override
    public List<Evaluation> extractData(ResultSet rs) throws SQLException, DataAccessException {
        ArrayList<Evaluation> list = new ArrayList<Evaluation>();
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "applicationContext.xml");

        UserDao userDao = (UserDao) context.getBean("userDao");
        AssessmentDao assessmentDao = (AssessmentDao) context.getBean("assessmentDao");

        while (rs.next()) {
           
            Evaluation eval = new Evaluation();
            eval.setId(rs.getInt("id"));
            eval.setCorrect(rs.getBoolean("is_correct"));
            eval.setEvaluator(userDao.getUserById(rs.getInt("evaluator_id")));
            
            //eval.setAssessment(assessmentDao.getAssessmentById(rs.getInt("assessment_id")));
            Assessment assess = new Assessment();
            assess.setId(rs.getInt("assessment_id"));
            eval.setAssessment(assess);
            
            list.add(eval);
        }

        return list;
    }
}
