/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author ADMIN
 */
@Entity
@Table(name = "maintenance_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MaintenanceType.findAll", query = "SELECT m FROM MaintenanceType m"),
    @NamedQuery(name = "MaintenanceType.findById", query = "SELECT m FROM MaintenanceType m WHERE m.id = :id"),
    @NamedQuery(name = "MaintenanceType.findByName", query = "SELECT m FROM MaintenanceType m WHERE m.name = :name")})
public class MaintenanceType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "name")
    private String name;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "typeId")
    private Set<MaintenanceSchedule> maintenanceScheduleSet;

    public MaintenanceType() {
    }

    public MaintenanceType(Integer id) {
        this.id = id;
    }

    public MaintenanceType(Integer id, String name) {
        this.id = id;
        this.name = name;
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

    @XmlTransient
    public Set<MaintenanceSchedule> getMaintenanceScheduleSet() {
        return maintenanceScheduleSet;
    }

    public void setMaintenanceScheduleSet(Set<MaintenanceSchedule> maintenanceScheduleSet) {
        this.maintenanceScheduleSet = maintenanceScheduleSet;
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
        if (!(object instanceof MaintenanceType)) {
            return false;
        }
        MaintenanceType other = (MaintenanceType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tndm.pojo.MaintenanceType[ id=" + id + " ]";
    }
    
}
