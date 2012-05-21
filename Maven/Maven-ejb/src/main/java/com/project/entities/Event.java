/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.resource.spi.AuthenticationMechanism;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author steq
 */
@Entity
@Table(name = "event")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Event.findAll", query = "SELECT e FROM Event e"),
    @NamedQuery(name = "Event.findById", query = "SELECT e FROM Event e WHERE e.id = :id"),
    @NamedQuery(name = "Event.findByDate", query = "SELECT e FROM Event e WHERE e.date = :date"),
    @NamedQuery(name = "Event.findByAttendies", query = "SELECT e FROM Event e WHERE e.attendies = :attendies"),
    @NamedQuery(name = "Event.findByApproved", query = "SELECT e FROM Event e WHERE e.approved = :approved ORDER BY e.date DESC ")})
public class Event implements Serializable {
    @Column(name =     "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "approved")
    private Short approved = 0;
    @Column(name = "attendies")
    private BigInteger attendies= BigInteger.valueOf(0);
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue
    @Column(name = "id")
    private Integer id;
    @Lob
    @Size(max = 65535)
    @Column(name = "name")
    private String name;
    @Lob
    @Size(max = 65535)
    @Column(name = "description_text")
    private String descriptionText;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "Place_id", referencedColumnName = "id")
    private Place placeid;
    
    @OneToMany
    @JoinColumn(name = "Event_id", referencedColumnName = "id")
    private Collection<EventToUserAsoc> eventToUserAsocCollection;
    
    @OneToMany
    @JoinColumn(name = "Event_id", referencedColumnName = "id")
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

        public BigInteger getAttendies() {
        return attendies;
    }

    public void setAttendies(BigInteger attendies) {
        this.attendies = attendies;
    }

    public Short getApproved() {
        return approved;
    }

    public void setApproved(Short approved) {
        this.approved = approved;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }

    public Place getPlaceid() {
        return placeid;
    }

    public void setPlaceid(Place placeid) {
        this.placeid = placeid;
    }

    @XmlTransient
    public Collection<EventToUserAsoc> getEventToUserAsocCollection() {
        return eventToUserAsocCollection;
    }

    public void setEventToUserAsocCollection(Collection<EventToUserAsoc> eventToUserAsocCollection) {
        this.eventToUserAsocCollection = eventToUserAsocCollection;
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

    

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
}
