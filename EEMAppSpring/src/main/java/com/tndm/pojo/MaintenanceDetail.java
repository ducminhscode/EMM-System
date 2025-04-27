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
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Tran Nguyen Duc Minh
 */
@Entity
@Table(name = "maintenance_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MaintenanceDetail.findAll", query = "SELECT m FROM MaintenanceDetail m"),
    @NamedQuery(name = "MaintenanceDetail.findById", query = "SELECT m FROM MaintenanceDetail m WHERE m.id = :id"),
    @NamedQuery(name = "MaintenanceDetail.findByExpense", query = "SELECT m FROM MaintenanceDetail m WHERE m.expense = :expense")})
public class MaintenanceDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "expense")
    private BigDecimal expense;
    @JoinColumn(name = "device_id", referencedColumnName = "id")
    @ManyToOne
    private Device deviceId;
    @JoinColumn(name = "maintenance_schedule_id", referencedColumnName = "id")
    @ManyToOne
    private MaintenanceSchedule maintenanceScheduleId;

    public MaintenanceDetail() {
    }

    public MaintenanceDetail(Integer id) {
        this.id = id;
    }

    public MaintenanceDetail(Integer id, BigDecimal expense) {
        this.id = id;
        this.expense = expense;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public void setExpense(BigDecimal expense) {
        this.expense = expense;
    }

    public Device getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Device deviceId) {
        this.deviceId = deviceId;
    }

    public MaintenanceSchedule getMaintenanceScheduleId() {
        return maintenanceScheduleId;
    }

    public void setMaintenanceScheduleId(MaintenanceSchedule maintenanceScheduleId) {
        this.maintenanceScheduleId = maintenanceScheduleId;
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
        if (!(object instanceof MaintenanceDetail)) {
            return false;
        }
        MaintenanceDetail other = (MaintenanceDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tndm.pojo.MaintenanceDetail[ id=" + id + " ]";
    }
    
}
