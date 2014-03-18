/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.converters;

import hu.unideb.studentSupportInterface.dao.CategoryDao;
import hu.unideb.studentSupportInterface.dao.LanguageDao;
import hu.unideb.studentSupportInterface.model.Category;
import hu.unideb.studentSupportInterface.model.Language;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Adam
 */
public class CategoryConverter implements Converter{
    
    ApplicationContext context = new ClassPathXmlApplicationContext(
                "applicationContext.xml");
    CategoryDao categoryDao = (CategoryDao) context.getBean("categoryDao");
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        
        return categoryDao.getCategoryById(Integer.parseInt(value));
        
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return Integer.toString(((Category)value).getId());
    }

    
    
}
