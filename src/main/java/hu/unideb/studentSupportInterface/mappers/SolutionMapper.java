/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.mappers;

import hu.unideb.studentSupportInterface.dao.LanguageDao;
import hu.unideb.studentSupportInterface.dao.UserDao;
import hu.unideb.studentSupportInterface.model.Solution;
import java.sql.Blob;
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
public class SolutionMapper implements ResultSetExtractor {

    @Override
    public List<Solution> extractData(ResultSet rs) throws SQLException, DataAccessException {
        ArrayList<Solution> list = new ArrayList<Solution>();
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "applicationContext.xml");

        UserDao userDao = (UserDao) context.getBean("userDao");
        LanguageDao languageDao = (LanguageDao) context.getBean("languageDao");


        while (rs.next()) {

            Solution sol = new Solution();
            sol.setId(rs.getInt("id"));
            sol.setDefinition(rs.getString("definition"));
            sol.setCode(rs.getString("code"));

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(rs.getTimestamp("time").getTime()));
            sol.setTime(cal);

            sol.setUploader(userDao.getUserById(rs.getInt("uploader_id")));
            Blob blob = rs.getBlob("file");
            if (blob != null) {
                sol.setFile(blob.getBinaryStream());
            }

            sol.setLanguage(languageDao.getLanguageById(rs.getInt("language_id")));
            sol.setTitle(rs.getString("title"));

            list.add(sol);
        }

        return list;
    }
}
