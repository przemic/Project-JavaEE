/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.controllers;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author przemic
 */
@ManagedBean
@RequestScoped
public class AuthBackingBean {

    /**
     * Creates a new instance of AuthBackingBean
     */
    public AuthBackingBean() {
    }
    
    private static Logger log = Logger.getLogger(AuthBackingBean.class.getName());
   
    public String logout() {
        String result="/index?faces-redirect=true";
     
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
     
        try {
            request.logout();
        } catch (ServletException e) {
            log.log(Level.SEVERE, "Failed to logout user!", e);
            result = "/logoutError?faces-redirect=true";
        }
     
        return result;
    }
}
