/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.pojo;

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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Tran Nguyen Duc Minh
 */
@Entity
@Table(name = "maintenance_schedule")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MaintenanceSchedule.findAll", query = "SELECT m FROM MaintenanceSchedule m"),
    @NamedQuery(name = "MaintenanceSchedule.findById", query = "SELECT m FROM MaintenanceSchedule m WHERE m.id = :id"),
    @NamedQuery(name = "MaintenanceSchedule.findByType", query = "SELECT m FROM MaintenanceSchedule m WHERE m.type = :type"),
    @NamedQuery(name = "MaintenanceSchedule.findByStartDate", query = "SELECT m FROM MaintenanceSchedule m WHERE m.startDate = :startDate"),
    @NamedQuery(name = "MaintenanceSchedule.findByEndDate", query = "SELECT m FROM MaintenanceSchedule m WHERE m.endDate = :endDate"),
    @NamedQuery(name = "MaintenanceSchedule.findByTitle", query = "SELECT m FROM MaintenanceSchedule m WHERE m.title = :title"),
    @NamedQuery(name = "MaintenanceSchedule.findByDescribe", query = "SELECT m FROM MaintenanceSchedule m WHERE m.describe = :describe"),
    @NamedQuery(name = "MaintenanceSchedule.findByFrequency", query = "SELECT m FROM MaintenanceSchedule m WHERE m.frequency = :frequency")})
public class MaintenanceSchedule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @NotNull
    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "title")
    private String title;
    @Size(max = 255)
    @Column(name = "describe")
    private String describe;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "frequency")
    private String frequency;
    @JoinColumn(name = "device_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Device deviceId;
    @JoinColumn(name = "personnel_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personnel personnelId;

    public MaintenanceSchedule() {
    }

    public MaintenanceSchedule(Integer id) {
        this.id = id;
    }

    public MaintenanceSchedule(Integer id, String type, Date startDate, Date endDate, String title, String frequency) {
        this.id = id;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.frequency = frequency;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public Device getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Device deviceId) {
        this.deviceId = deviceId;
    }

    public Personnel getPersonnelId() {
        return personnelId;
    }

    public void setPersonnelId(Personnel personnelId) {
        this.personnelId = personnelId;
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
        if (!(object instanceof MaintenanceSchedule)) {
            return false;
        }
        MaintenanceSchedule other = (MaintenanceSchedule) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tndm.pojo.MaintenanceSchedule[ id=" + id + " ]";
    }
    
}
