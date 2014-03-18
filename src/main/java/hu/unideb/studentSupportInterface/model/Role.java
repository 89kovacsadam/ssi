/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.model;

/**
 *
 * @author Adam
 */
public enum Role {
    
    UPLOADER(1), ASSESSOR(2), TUTOR(3), ADMIN(4);
    
    private int role;

    private Role(int role) {
        this.role = role;
    }

    public int getRole() {
        return role;
    }
    
    
    
    
}
