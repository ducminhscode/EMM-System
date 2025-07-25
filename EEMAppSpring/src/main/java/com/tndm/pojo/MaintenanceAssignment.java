/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author ADMIN
 */
@Entity
@Table(name = "maintenance_assignment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MaintenanceAssignment.findAll", query = "SELECT m FROM MaintenanceAssignment m"),
    @NamedQuery(name = "MaintenanceAssignment.findById", query = "SELECT m FROM MaintenanceAssignment m WHERE m.id = :id"),
    @NamedQuery(name = "MaintenanceAssignment.findByIsCap", query = "SELECT m FROM MaintenanceAssignment m WHERE m.isCap = :isCap")})
public class MaintenanceAssignment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "is_cap")
    private Boolean isCap;
    @Column(name = "is_notify")
    private Boolean isNotify;
    @JoinColumn(name = "maintenance_schedule_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private MaintenanceSchedule maintenanceScheduleId;
    @JoinColumn(name = "technician_id", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnore
    private Technician technicianId;

    public MaintenanceAssignment() {
    }

    public MaintenanceAssignment(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getIsCap() {
        return isCap;
    }

    public void setIsCap(Boolean isCap) {
        this.isCap = isCap;
    }

    public MaintenanceSchedule getMaintenanceScheduleId() {
        return maintenanceScheduleId;
    }

    public void setMaintenanceScheduleId(MaintenanceSchedule maintenanceScheduleId) {
        this.maintenanceScheduleId = maintenanceScheduleId;
    }

    public Technician getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(Technician technicianId) {
        this.technicianId = technicianId;
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
        if (!(object instanceof MaintenanceAssignment)) {
            return false;
        }
        MaintenanceAssignment other = (MaintenanceAssignment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tndm.pojo.MaintenanceAssignment[ id=" + id + " ]";
    }

    /**
     * @return the isNotify
     */
    public Boolean getIsNotify() {
        return isNotify;
    }

    /**
     * @param isNotify the isNotify to set
     */
    public void setIsNotify(Boolean isNotify) {
        this.isNotify = isNotify;
    }
    
}
