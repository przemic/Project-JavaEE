package com.project.controllers;

import com.project.entities.Place;
import com.project.fascades.PlaceFacade;
import com.project.fascades.util.JsfUtil;
import com.project.fascades.util.PaginationHelper;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
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
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

@ManagedBean(name = "placeController")
@SessionScoped
public class PlaceController implements Serializable {

    private MapModel emptyModel;  
      
    private String title;  
      
    private double lat;  
    private Marker pointMarker;
    private boolean pointSet;
      
    private double lng;
    private Place current;
    private DataModel items = null;
    @EJB
    private com.project.fascades.PlaceFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public PlaceController() {
        emptyModel = new DefaultMapModel();
    }
    
    public MapModel getEmptyModel() {  
        return emptyModel;  
    } 
     public void addMessage(FacesMessage message) {  
        FacesContext.getCurrentInstance().addMessage(null, message);  
    }  
      
    public String getTitle() {  
        return title;  
    }  
  
    public void setTitle(String title) {  
        this.title = title;  
    }  
  
    public double getLat() {  
        return lat;  
    }  
  
    public void setLat(double lat) {  
        this.lat = lat;  
    }  
  
    public double getLng() {  
        return lng;  
    }  
  
    public void setLng(double lng) {  
        this.lng = lng;  
    }  
      
    public void addMarker(ActionEvent actionEvent) {  
         
        if(!pointSet){
            pointMarker = new Marker(new LatLng(lat, lng), title);
            emptyModel.addOverlay(pointMarker);            
            pointSet = true;
            getSelected().setLatitude(Double.toString(lat));
            getSelected().setLongitude(Double.toString(lng));
        }
        else
        {
            pointMarker.setLatlng(new LatLng(lat, lng));
            
        }
        
        updateCords();   
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Dodano punkt o współrzędnych", "Lat:" + lat + ", Lng:" + lng));  
    }  

    public Place getSelected() {
        if (current == null) {
            current = new Place();
            selectedItemIndex = -1;
        }
        return current;
    }
    
    

    private PlaceFacade getFacade() {
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
        current = (Place) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Place();
        selectedItemIndex = -1;
        return "Create";
    }

    public void updateCords(){
        current.setLatitude(Double.toString(lat));
        current.setLongitude(Double.toString(lng));
    }
    public String create() {
        current.setLatitude(Double.toString(lat));
        current.setLongitude(Double.toString(lng));
        try {            
            getFacade().create(current);
            JsfUtil.addSuccessMessage("Utworzono nowe miejsce");
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Place) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PlaceUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Place) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PlaceDeleted"));
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
        return this.getSelectItems(ejbFacade.findAll(), true);
    }
    
    public SelectItem[] getSelectItems(List<Place> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] elements = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            elements[0] = new SelectItem("", "---");
            i++;
        }
        for (Place x : entities) {
            elements[i++] = new SelectItem(x, x.getName());
        }
        return elements;
    }
    public String getNameById(int id){
            return ejbFacade.getNameById(id);
    }

    @FacesConverter(forClass = Place.class)
    public static class PlaceControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PlaceController controller = (PlaceController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "placeController");
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
            if (object instanceof Place) {
                Place o = (Place) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + PlaceController.class.getName());
            }
        }
        
        
    }
}
