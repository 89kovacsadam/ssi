/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.mappers;

import hu.unideb.studentSupportInterface.dao.RoleDao;
import hu.unideb.studentSupportInterface.model.Language;
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
public class LanguageMapper implements ResultSetExtractor{
    
    @Override
    public List<Language> extractData(ResultSet rs) throws SQLException, DataAccessException {
        ArrayList<Language> list = new ArrayList<Language>();
        
        while(rs.next()){
            Language lang = new Language();
            lang.setId(rs.getInt("id"));
            lang.setName(rs.getString("name"));
            list.add(lang);
        }
        
        return list;
    }
    
}
