/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.converters;

import hu.unideb.studentSupportInterface.dao.RoleDao;
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
public class RoleConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        
        if(value.toString().equals("UPLOADER"))
                return Role.UPLOADER;
        if(value.toString().equals("ASSESSOR"))
                return Role.ASSESSOR;
        if(value.toString().equals("TUTOR"))
                return Role.TUTOR;
        if(value.toString().equals("ADMIN"))
                return Role.ADMIN;
        
        return null;
        
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        
        return value.toString();
    }

}
