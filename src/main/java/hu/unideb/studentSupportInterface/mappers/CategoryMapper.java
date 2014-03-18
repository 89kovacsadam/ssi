/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.mappers;

import hu.unideb.studentSupportInterface.dao.RoleDao;
import hu.unideb.studentSupportInterface.model.Category;
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
public class CategoryMapper implements ResultSetExtractor{
    
    @Override
    public List<Category> extractData(ResultSet rs) throws SQLException, DataAccessException {
        ArrayList<Category> list = new ArrayList<Category>();
        
        while(rs.next()){
            
            Category cat = new Category();
            
            cat.setId(rs.getInt("id"));
            cat.setName(rs.getString("name"));
                        
            list.add(cat);
        }
        
        return list;
    }
    
}
