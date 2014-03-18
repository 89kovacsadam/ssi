/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.model;

import java.io.InputStream;
import java.util.Calendar;

/**
 *
 * @author Adam
 */
public class Solution {
    
    private int id;
    private User uploader;
    private Language language;
    private String title;
    private String definition;
    private Calendar time;
    private InputStream file;
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public InputStream getFile() {
        return file;
    }

    public void setFile(InputStream file) {
        this.file = file;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public User getUploader() {
        return uploader;
    }

    public void setUploader(User uploader) {
        this.uploader = uploader;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Solution other = (Solution) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.uploader != other.uploader && (this.uploader == null || !this.uploader.equals(other.uploader))) {
            return false;
        }
        if (this.language != other.language && (this.language == null || !this.language.equals(other.language))) {
            return false;
        }
        if (this.time != other.time && (this.time == null || !this.time.equals(other.time))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + (this.uploader != null ? this.uploader.hashCode() : 0);
        hash = 43 * hash + (this.language != null ? this.language.hashCode() : 0);
        hash = 43 * hash + (this.time != null ? this.time.hashCode() : 0);
        return hash;
    }
    
    
    
}
