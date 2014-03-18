/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.daoJdbcImpl;

import hu.unideb.studentSupportInterface.dao.UserDao;
import hu.unideb.studentSupportInterface.mappers.UserMapper;
import hu.unideb.studentSupportInterface.model.Role;
import hu.unideb.studentSupportInterface.model.User;
import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 *
 * @author Adam
 */
public class UserDaoJdbcImpl extends JdbcDaoSupport implements UserDao, UserDetailsService {
    
    @Override
    public int countUsers(){
        int count;
        String sql = "select count(*) from user";
        
        count = getJdbcTemplate().queryForInt(sql);
        
        return count;
        
    }
    
    @Override
    public User getUserById(int id){
        String sql = "select * from user where id = ?";
        
        List<User> users = (List)getJdbcTemplate().query(sql, new Object[]{id}, new UserMapper());
        
        if(users != null && !users.isEmpty()){
            return users.get(0);
        }
        
        return null;
        
    }
    
    @Override
    public User createUser(User user, String password){
        
        String sql = "insert into user (email, first_name, last_name, password, public, active, neptun_code) values (:email, :first_name, :last_name, :password, :public, :active, :neptun_code)";
        
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(super.getDataSource());
        
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", user.getEmail());
        params.addValue("first_name", user.getFirstName());
        params.addValue("last_name", user.getLastName());
        params.addValue("password", password);
        params.addValue("public", user.isIsPublic());
        params.addValue("active", user.isActive());
        params.addValue("neptun_code", user.getNeptunCode());
        
        KeyHolder key = new GeneratedKeyHolder();
        
        template.update(sql, params, key);
        
        user.setId(key.getKey().intValue());
        
        return user;
    }
    
    @Override
    public User updateUser(User user){
        
        String sql = "update user set email = :email, first_name = :first_name, last_name = :last_name, public = :public, active = :active, neptun_code = :neptun_code where id = :id";
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(super.getDataSource());
        
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", user.getId());
        params.addValue("first_name", user.getFirstName());
        params.addValue("last_name", user.getLastName());
        params.addValue("email", user.getEmail());
        params.addValue("public", user.isIsPublic());
        params.addValue("active", user.isActive());
        params.addValue("neptun_code", user.getNeptunCode());
        
        template.update(sql, params);
        
        return user;
    }
    
    @Override
    public User getUserByEmail(String email){
        String sql = "select * from user where email = ?";
        
        List<User> list = (List)getJdbcTemplate().query(sql, new Object[]{email}, new UserMapper());
        
        if(list != null && !list.isEmpty()){
            return list.get(0);
        }
        
        return null;
    }
    
    @Override
    public List<User> getUsersInRole(Role role){
        List<User> list;
        
        String sql = "select u.id id, u.email email, u.first_name first_name, u.last_name last_name, u.public public, u.active active, u.neptun_code neptun_code, u.password password"
                + "from user u, user_role r"
                + "where u.id = r.user_id and role = ?";
        
        list = (List)getJdbcTemplate().query(sql, new Object[]{role.getRole()}, new UserMapper());
        
        return list;
        
    }
    
    @Override
    public List<User> getAllUser(){
        String sql = "select * from user";
        
        List<User> list = (List)getJdbcTemplate().query(sql, new UserMapper());
        
        return list;
        
    }
    
    @Override
    public UserDetails loadUserByUsername(String username){
        String sql = "select * from user where email = ?";
        List<User> list = null;
        
        list = (List)getJdbcTemplate().query(sql, new Object[]{username}, new UserMapper());
        
        if(list != null && !list.isEmpty()){
            return list.get(0);
        }
        
        return null;
        
    }
    
}
