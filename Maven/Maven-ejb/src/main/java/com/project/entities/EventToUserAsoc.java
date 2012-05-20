/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author steq
 */
@Entity
@Table(name = "event_to_user_asoc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EventToUserAsoc.findAll", query = "SELECT e FROM EventToUserAsoc e"),
    @NamedQuery(name = "EventToUserAsoc.findByAttendance", query = "SELECT e FROM EventToUserAsoc e WHERE e.attendance = :attendance"),
    @NamedQuery(name = "EventToUserAsoc.findById", query = "SELECT e FROM EventToUserAsoc e WHERE e.id = :id")})
public class EventToUserAsoc implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "attendance")
    private Integer attendance = 1;
    @Id
    @GeneratedValue
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "Event_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Event eventid;
    @JoinColumn(name = "User_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userid;

    public EventToUserAsoc() {
    }

    public EventToUserAsoc(Integer id) {
        this.id = id;
    }

    public Integer getAttendance() {
        return attendance;
    }

    public void setAttendance(Integer attendance) {
        this.attendance = attendance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Event getEventid() {
        return eventid;
    }

    public void setEventid(Event eventid) {
        this.eventid = eventid;
    }

    public User getUserid() {
        return userid;
    }

    public void setUserid(User userid) {
        this.userid = userid;
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
        if (!(object instanceof EventToUserAsoc)) {
            return false;
        }
        EventToUserAsoc other = (EventToUserAsoc) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.project.entities.EventToUserAsoc[ id=" + id + " ]";
    }
    
}
