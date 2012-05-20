package com.project.controllers;

import com.project.entities.*;
import com.project.fascades.UserFacade;
import com.project.fascades.util.JsfUtil;
import com.project.fascades.util.PaginationHelper;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.ProjectStage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import sun.security.provider.MD5;

@ManagedBean(name = "userController")
@SessionScoped
public class UserController implements Serializable {

    private User current;
    private DataModel items = null;
    @EJB
    private com.project.fascades.UserFacade ejbFacade;
    @EJB
    private com.project.fascades.EventToUserAsocFacade asocFacade;
    @EJB
    private com.project.fascades.EventFacade eventFacade;
    @EJB
    private com.project.fascades.CommentFacade commentFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public UserController() {
    }

    public Collection<Event> getUserEvents(String login){
       
        return ejbFacade.getEventByName(login);
    }
    
    public void addMessage(FacesMessage message) {  
        FacesContext.getCurrentInstance().addMessage(null, message);  
    }  
    
    public void approveAttendance(Event event, String login){
        try{
            
            EventToUserAsoc asoc = asocFacade.findByEventAndUser(ejbFacade.getUserByName(login), event);
            if(asoc.getAttendance()==1){
                asoc.setAttendance(5);        
                addAttendandce(event);
                asocFacade.edit(asoc);
                addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Potwierdzono chęć przybycia na wydarzenie: ",event.getName() ));
            }
            else
            {
                addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Udział w wydarzeniu został potwierdzony już wcześniej",""));
            }
            
         } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));            
        }
        
    }
    
     private void addAttendandce(Event event){
        BigInteger bb =  event.getAttendies().add(BigInteger.valueOf(1));
        event.setAttendies(bb); 
        eventFacade.edit(event);
    }
 
    private void reduceAttendance(Event event){
        BigInteger bb =  event.getAttendies().subtract(BigInteger.valueOf(1));
        event.setAttendies(bb);
        eventFacade.edit(event);
    }
    
    public User getUserByName(String login){
        return ejbFacade.getUserByName(login);
    }
    
    public void removeAttendance(Event event, String login){
        EventToUserAsoc asoc = asocFacade.findByEventAndUser(ejbFacade.getUserByName(login), event);
        asoc.setAttendance(1);
        reduceAttendance(event);
        asocFacade.edit(asoc);
        
    }
    
    public void addComment(String login,String text,Event event){
        Comment com = new Comment();
        com.setDescriptionText(text);
        com.setEventid(event);
        User user = ejbFacade.getUserByName(login);
        com.setUserid(user);
        try {
            commentFacade.create(com);
            JsfUtil.addSuccessMessage("Dodano komentarz dla wydarzenia");
            
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));            
        }
    }
    
    public String assignUserToEvent(String login,Event event){
        
        User user = ejbFacade.getUserByName(login);    
        if(!(this.findByEventAndUserExistance(user, event))){            
            asocFacade.assignUserToEvent(user, event);                 
        }
        
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Wydarzenie zostało dodane do listy ulubionych",event.getName() ));
        return "index?faces-redirect=true";
    }
    
    public boolean isAdmin(String login){
        
        User user = ejbFacade.getUserByName(login);
        if(user.getGroupid().getId() == 2){
            return true;
        }
        else
        {
            return false;
        }           
       
    }
    
    private boolean findByEventAndUserExistance(User user,Event event){        
        return asocFacade.findByEventAndUserExistance(user, event);
    }
    
    
    
    
    public User getSelected() {
        if (current == null) {
            current = new User();
            selectedItemIndex = -1;
        }
        return current;
    }

    private UserFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (User) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new User();
        selectedItemIndex = -1;
        return "Create";
    }
    
    public void submit(ActionEvent event) {  
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Pola zostały uzupełnione poprawnie", "");  
          
        FacesContext.getCurrentInstance().addMessage(null, msg); 
        create();
    }

    public String create() {
        try {
            Groups g = new Groups();
            g.setId(1);
            g.setName("user");
            current.setPassword(MD5(current.getPassword()));
            current.setGroupid(g);
            getFacade().create(current);
            JsfUtil.addSuccessMessage("Stworzono urzytkownika.");
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public String MD5(String md5) {
    try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
        }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public String prepareEdit() {
        current = (User) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UserUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (User) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UserDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    @FacesConverter(forClass = User.class)
    public static class UserControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UserController controller = (UserController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "userController");
            return controller.ejbFacade.find(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof User) {
                User o = (User) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + UserController.class.getName());
            }
        }
    }
}
