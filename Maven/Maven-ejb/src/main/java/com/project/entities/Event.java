/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author przemic
 */
@Entity
@Table(name = "Event")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Event.findAll", query = "SELECT e FROM Event e"),
    @NamedQuery(name = "Event.findById", query = "SELECT e FROM Event e WHERE e.id = :id"),
    @NamedQuery(name = "Event.findByDate", query = "SELECT e FROM Event e WHERE e.date = :date")})
public class Event implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Lob
    @Size(max = 65535)
    @Column(name = "name")
    private String name;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Lob
    @Size(max = 65535)
    @Column(name = "description_text")
    private String descriptionText;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eventid")
    private Collection<EventToUserAsoc> eventToUserAsocCollection;
    @JoinColumn(name = "Place_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Place placeid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eventid")
    private Collection<Comment> commentCollection;

    public Event() {
    }

    public Event(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }

    @XmlTransient
    public Collection<EventToUserAsoc> getEventToUserAsocCollection() {
        return eventToUserAsocCollection;
    }

    public void setEventToUserAsocCollection(Collection<EventToUserAsoc> eventToUserAsocCollection) {
        this.eventToUserAsocCollection = eventToUserAsocCollection;
    }

    public Place getPlaceid() {
        return placeid;
    }

    public void setPlaceid(Place placeid) {
        this.placeid = placeid;
    }

    @XmlTransient
    public Collection<Comment> getCommentCollection() {
        return commentCollection;
    }

    public void setCommentCollection(Collection<Comment> commentCollection) {
        this.commentCollection = commentCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Event)) {
            return false;
        }
        Event other = (Event) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.project.entities.Event[ id=" + id + " ]";
    }
    
}
