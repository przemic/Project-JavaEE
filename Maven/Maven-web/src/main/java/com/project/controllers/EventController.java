package com.project.controllers;

import com.project.entities.Event;
import com.project.fascades.EventFacade;
import com.project.fascades.util.JsfUtil;
import com.project.fascades.util.PaginationHelper;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import org.primefaces.event.DateSelectEvent;
import org.primefaces.model.DualListModel;

@ManagedBean(name = "eventController")
@SessionScoped
public class EventController implements Serializable {

    private Event current,choosen;
    private DataModel items = null;
    @EJB
    private com.project.fascades.EventFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private DualListModel<Event> newestSet,oldestSet,allSet; 
    private List<Event> toDelete;

    private String currentCommentText;
    public EventController() {
        toDelete = new  ArrayList<Event>();
        currentCommentText = "";
        choosen = new Event();
    }

    public void approveEvent(Event event){
        Short app = 1;
        event.setApproved(app);
    }
    
    public Event getChoosen(){
        return choosen;
    }
    
   
    
    
    public void setChoosen(Event choosen){
        this.choosen = choosen;
    }
    public void deleteSelected(){
        try {           
            
            for(Event ev: newestSet.getTarget()){
                ejbFacade.remove((Event)ev);
            }
            JsfUtil.addSuccessMessage("Usunięto wybrane wydarzenia");  
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Nie udało się usunąć wydarzeń");
        }
        toDelete = null;
        newestSet = null;
    }
    
    public String updateSelected(){
         try {
            getFacade().edit(choosen);
            JsfUtil.addSuccessMessage("Pomyślnie zaktualizowano wydarzenie");
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Nie udało się zaktualizować wyadarzenia");
            return null;
        }
    }
    public DualListModel<Event> getNewestSet(){
        int size = ejbFacade.count();
        int min = size -100;
        if(min<0){
            min=0;
        }        
        newestSet= new DualListModel<Event>(ejbFacade.findRange(new int[]{min, size-1}),toDelete);
        return newestSet;
    }
    
    public void setNewestSet(DualListModel<Event> newestSet){
        this.newestSet=newestSet;
    }
    
    public List<Event> getOldest(){
        int size = ejbFacade.count();
        return (List<Event>)ejbFacade.findRange(new int[]{0, 100});
    }
    
    public List<Event> getAll(){
        return ejbFacade.findAll();
    }
    public Event getSelected() {
        if (current == null) {
            current = new Event();
            selectedItemIndex = -1;
        }
        return current;
    }
    
     public void handleDateSelect(DateSelectEvent event) {  
        FacesContext facesContext = FacesContext.getCurrentInstance();  
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        current.setDate(event.getDate());
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(current.getDate())));  
    }
    
    private EventFacade getFacade() {
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
        current = (Event) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Event();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage("Utworzono nowe wydarzenie.");
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e,"Nie udało się utworzyć wydarzenia.");
            return null;
        }
    }

    public String prepareEdit() {
        current = (Event) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage("Pomyślnie zaktualizowano wydarzenie");
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Nie udało się zaktualizować wyadarzenia");
            return null;
        }
    }

    public String destroy() {
        current = (Event) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("EventDeleted"));
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

    @FacesConverter(forClass = Event.class)
    public static class EventControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EventController controller = (EventController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "eventController");
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
            if (object instanceof Event) {
                Event o = (Event) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + EventController.class.getName());
            }
        }
    }
}
