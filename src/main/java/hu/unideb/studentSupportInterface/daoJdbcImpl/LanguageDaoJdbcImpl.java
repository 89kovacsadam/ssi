/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.daoJdbcImpl;

import hu.unideb.studentSupportInterface.dao.LanguageDao;
import hu.unideb.studentSupportInterface.mappers.LanguageMapper;
import hu.unideb.studentSupportInterface.model.Language;
import hu.unideb.studentSupportInterface.model.User;
import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 *
 * @author Adam
 */
public class LanguageDaoJdbcImpl extends JdbcDaoSupport implements LanguageDao{
    
    public Language createLanguage(Language language){
        String sql = "insert into language (name) values (:name)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", language.getName());
        
        KeyHolder key = new GeneratedKeyHolder();
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(getDataSource());
        
        template.update(sql, params, key);
        language.setId(key.getKey().intValue());
        
        return language;
        
    }
    
    public Language updateLanguage(Language language){
        String sql = "update language set name = :name where id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", language.getName());
        params.addValue("id", language.getId());
        
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(getDataSource());
        template.update(sql, params);
        
        return language;
        
    }
    
    public boolean deleteLanguage(Language language){
        String sql = "delete from language where id = ?";
        
        getJdbcTemplate().update(sql, new Object[]{language.getId()});
        
        return true;
        
    }
    
    public List<Language> getAllLanguage(){
        String sql = "select * from language";
        List<Language> list = null;
        
        list = (List)getJdbcTemplate().query(sql, new LanguageMapper());
        
        return list;
        
    }
    
    public Language getLanguageById(int id){
        String sql = "select * from language where id = ?";
        List<Language> list = null;
        
        list = (List)getJdbcTemplate().query(sql, new Object[]{id}, new LanguageMapper());
        
        if(list != null && !list.isEmpty()){
            return list.get(0);
        }
        
        return null;
        
    }
    
    @Override
    public boolean addLanguageToUser(Language language, User user){
        String sql = "insert into user_language (user_id, language_id) values (?, ?)";
        
        getJdbcTemplate().update(sql, new Object[]{user.getId(), language.getId()});
        
        return true;
        
    }
    
    @Override
    public boolean removeLanguageFromUser(Language language, User user){
        String sql = "delete from user_language where user_id = ? and language_id = ?";
        
        getJdbcTemplate().update(sql, new Object[]{user.getId(), language.getId()});
        
        return true;
        
    }
    
    public List<Language> getLanguagesByUser(User user){
        String sql = "select l.id id, l.name name "
                + "from language l, user_language ul, user u "
                + "where ul.language_id = l.id and ul.user_id = u.id and u.id = ?";
        
        List<Language> list = (List)getJdbcTemplate().query(sql, new Object[]{user.getId()}, new LanguageMapper());
        
        return list;        
    }
    
}
