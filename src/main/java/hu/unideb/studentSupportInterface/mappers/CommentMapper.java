/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.mappers;

import hu.unideb.studentSupportInterface.dao.CommentDao;
import hu.unideb.studentSupportInterface.dao.RoleDao;
import hu.unideb.studentSupportInterface.dao.SolutionDao;
import hu.unideb.studentSupportInterface.dao.UserDao;
import hu.unideb.studentSupportInterface.model.Comment;
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
public class CommentMapper implements ResultSetExtractor {

    @Override
    public List<Comment> extractData(ResultSet rs) throws SQLException, DataAccessException {
        ArrayList<Comment> list = new ArrayList<Comment>();
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "applicationContext.xml");

        UserDao userDao = (UserDao) context.getBean("userDao");
        CommentDao commentDao = (CommentDao) context.getBean("commentDao");
        SolutionDao solutionDao = (SolutionDao) context.getBean("solutionDao");

        while (rs.next()) {

            Comment comment = new Comment();

            comment.setId(rs.getInt("id"));
            comment.setText(rs.getString("text"));
            //comment.setUser(userDao.getUserById(rs.getInt("user_id")));
            User user = new User();
            user.setId(rs.getInt("user_id"));
            comment.setUser(user);

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(rs.getTimestamp("time").getTime()));
            comment.setTime(cal);

            comment.setReplyTo(commentDao.getCommentById(rs.getInt("reply_to")));
            //comment.setSolution(solutionDao.getSolutionById(rs.getInt("solution_id")));
            Solution sol = new Solution();
            sol.setId(rs.getInt("solution_id"));
            comment.setSolution(sol);

            list.add(comment);
        }

        return list;
    }
}
