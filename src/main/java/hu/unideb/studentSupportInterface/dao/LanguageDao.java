/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.dao;

import hu.unideb.studentSupportInterface.model.Language;
import hu.unideb.studentSupportInterface.model.User;
import java.util.List;

/**
 *
 * @author Adam
 */
public interface LanguageDao {
    
    public Language createLanguage(Language language);
    
    public Language updateLanguage(Language language);
    
    public boolean deleteLanguage(Language language);
    
    public List<Language> getAllLanguage();
    
    public Language getLanguageById(int id);
    
    public boolean addLanguageToUser(Language language, User user);
    
    public boolean removeLanguageFromUser(Language language, User user);
    
    public List<Language> getLanguagesByUser(User user);
    
}
