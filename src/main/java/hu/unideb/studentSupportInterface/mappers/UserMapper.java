/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.mappers;

import hu.unideb.studentSupportInterface.dao.RoleDao;
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
public class UserMapper implements ResultSetExtractor {
    
    @Override
    public List extractData(ResultSet rs) throws SQLException, DataAccessException {
        ArrayList<User> list = new ArrayList<User>();
        ApplicationContext context = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
 
		RoleDao roleDao =  (RoleDao) context.getBean("roleDao");
        
        while(rs.next()){
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setIsPublic(rs.getBoolean("public"));
            user.setActive(rs.getBoolean("active"));
            user.setNeptunCode(rs.getString("neptun_code"));
            user.setPassword(rs.getString("password"));
            
            user.setRoles(roleDao.getRolesForUser(user));
            
            list.add(user);
        }
        
        return list;
    }
    
}
