/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.daoJdbcImpl;

import hu.unideb.studentSupportInterface.dao.CategoryDao;
import hu.unideb.studentSupportInterface.mappers.CategoryMapper;
import hu.unideb.studentSupportInterface.model.Category;
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
public class CategoryDaoJdbcImpl extends JdbcDaoSupport implements CategoryDao{
    
    @Override
    public Category createCategory(Category category){
        String sql = "insert into category (name) values (:name)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", category.getName());
        
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(getDataSource());
        KeyHolder key = new GeneratedKeyHolder();
        
        template.update(sql, params, key);
        category.setId(key.getKey().intValue());
        
        return category;
    }
    
    @Override
    public Category updateCategory(Category category){
        String sql = "update category set name = :name where id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", category.getName());
        params.addValue("id", category.getId());
        
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(getDataSource());
        template.update(sql, params);
        
        return category;
        
    }
    
    @Override
    public boolean deleteCategory(Category category){
        String sql = "delete from category where id = ?";
        
        getJdbcTemplate().update(sql, new Object[]{category.getId()});
        
        return true;
        
    }
    
    @Override
    public Category getCategoryById(int id){
        String sql = "select * from category where id = ?";
        List<Category> list = null;
        
        list = (List)getJdbcTemplate().query(sql, new Object[]{id}, new CategoryMapper());
        
        if(list != null && !list.isEmpty()){
            return list.get(0);
        }
        
        return null;
        
    }
    
    @Override
    public List<Category> getAllCategory(){
        String sql = "select * from category";
        List<Category> list = null;
        
        list = (List)getJdbcTemplate().query(sql, new CategoryMapper());
        
        return list;
        
    }
    
}
