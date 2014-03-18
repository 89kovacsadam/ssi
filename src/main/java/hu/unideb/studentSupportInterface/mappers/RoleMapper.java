/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.mappers;

import hu.unideb.studentSupportInterface.model.Role;
import hu.unideb.studentSupportInterface.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 *
 * @author Adam
 */
public class RoleMapper implements ResultSetExtractor{
    
    public List extractData(ResultSet rs) throws SQLException, DataAccessException {
        ArrayList<Role> list = new ArrayList<Role>();
        
        while(rs.next()){
            
            switch(rs.getInt("role")){
                case 1:
                    list.add(Role.UPLOADER);
                    break;
                case 2:
                    list.add(Role.ASSESSOR);
                    break;
                case 3:
                    list.add(Role.TUTOR);
                    break;
                case 4:
                    list.add(Role.ADMIN);
                    break;
            }
        }
        
        return list;
    }
    
}
