/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.unideb.studentSupportInterface.converters;

import hu.unideb.studentSupportInterface.dao.LanguageDao;
import hu.unideb.studentSupportInterface.model.Language;
import hu.unideb.studentSupportInterface.model.Role;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Adam
 */
public class LanguageConverter implements Converter{
    
    ApplicationContext context = new ClassPathXmlApplicationContext(
                "applicationContext.xml");
    LanguageDao languageDao = (LanguageDao) context.getBean("languageDao");
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        
        return languageDao.getLanguageById(Integer.parseInt(value));
        
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return Integer.toString(((Language)value).getId());
    }

    
}
