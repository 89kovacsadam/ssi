/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.daoJdbcImpl;

import hu.unideb.studentSupportInterface.dao.RoleDao;
import hu.unideb.studentSupportInterface.mappers.RoleMapper;
import hu.unideb.studentSupportInterface.model.Role;
import hu.unideb.studentSupportInterface.model.User;
import java.util.List;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 *
 * @author Adam
 */
public class RoleDaoJdbcImpl extends JdbcDaoSupport implements RoleDao {
    
    @Override
    public List<Role> getRolesForUser(User user){
        String sql = "select r.role role from user u, user_role r where u.id = r.user_id and r.user_id = ?";
        
        List<Role> list = (List)getJdbcTemplate().query(sql, new Object[]{user.getId()}, new RoleMapper());
        
        return list;
        
    }
    
    @Override
    public boolean addRoleToUser(User user, Role role){
        String sql = "insert into user_role (user_id, role) values (?, ?)";
        
        getJdbcTemplate().update(sql, new Object[]{user.getId(), role.getRole()});
        
        return true;
        
    }
    
    @Override
    public boolean revokeRoleFromUser(User user, Role role){
        String sql = "delete from user_role where user_id = ? and role = ?";
        
        getJdbcTemplate().update(sql, new Object[]{user.getId(), role.getRole()});
        
        return true;
    }
    
}
