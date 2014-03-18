/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.mappers;

import hu.unideb.studentSupportInterface.dao.CommentDao;
import hu.unideb.studentSupportInterface.dao.RoleDao;
import hu.unideb.studentSupportInterface.dao.SolutionDao;
import hu.unideb.studentSupportInterface.dao.UserDao;
import hu.unideb.studentSupportInterface.model.Assessment;
import hu.unideb.studentSupportInterface.model.Solution;
import hu.unideb.studentSupportInterface.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 *
 * @author Adam
 */
public class AssessmentMapper implements ResultSetExtractor {

    @Override
    public List<Assessment> extractData(ResultSet rs) throws SQLException, DataAccessException {
        ArrayList<Assessment> list = new ArrayList<Assessment>();
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "applicationContext.xml");

        UserDao userDao = (UserDao) context.getBean("userDao");
        CommentDao commentDao = (CommentDao) context.getBean("commentDao");
        SolutionDao solutionDao = (SolutionDao) context.getBean("solutionDao");

        while (rs.next()) {

            Assessment assess = new Assessment();

            assess.setId(rs.getInt("id"));
            assess.setCorrect(rs.getBoolean("is_correct"));
            assess.setAssessor(userDao.getUserById(rs.getInt("assessor_id")));

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(rs.getTimestamp("time").getTime()));
            assess.setTime(cal);

            assess.setComment(commentDao.getCommentById(rs.getInt("comment_id")));
            //assess.setSolution(solutionDao.getSolutionById(rs.getInt("solution_id")));
            Solution sol = new Solution();
            sol.setId(rs.getInt("solution_id"));
            assess.setSolution(sol);

            list.add(assess);
        }

        return list;
    }
}
