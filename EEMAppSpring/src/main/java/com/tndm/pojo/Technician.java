/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author Tran Nguyen Duc Minh
 */
@Entity
@Table(name = "technician")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Technician.findAll", query = "SELECT t FROM Technician t"),
    @NamedQuery(name = "Technician.findById", query = "SELECT t FROM Technician t WHERE t.id = :id"),
    @NamedQuery(name = "Technician.findBySpecialization", query = "SELECT t FROM Technician t WHERE t.specialization = :specialization")})
public class Technician implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Size(max = 50)
    @Column(name = "specialization")
    private String specialization;
    @JoinColumn(name = "facility_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Facility facilityId;
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    @JsonIgnore
    @MapsId
    private User user;
    @OneToMany(mappedBy = "technicianId")
    @JsonIgnore
    private Set<MaintenanceAssignment> maintenanceAssignmentSet;
    @OneToMany(mappedBy = "technicianId")
    @JsonIgnore
    private Set<RepairHistory> repairHistorySet;

    public Technician() {
    }

    public Technician(Integer id) {
        this.id = id;
    }

    public Technician(Integer id, String specialization) {
        this.id = id;
        this.specialization = specialization;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Facility getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Facility facilityId) {
        this.facilityId = facilityId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @XmlTransient
    public Set<MaintenanceAssignment> getMaintenanceAssignmentSet() {
        return maintenanceAssignmentSet;
    }

    public void setMaintenanceAssignmentSet(Set<MaintenanceAssignment> maintenanceAssignmentSet) {
        this.maintenanceAssignmentSet = maintenanceAssignmentSet;
    }

    @XmlTransient
    public Set<RepairHistory> getRepairHistorySet() {
        return repairHistorySet;
    }

    public void setRepairHistorySet(Set<RepairHistory> repairHistorySet) {
        this.repairHistorySet = repairHistorySet;
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
        if (!(object instanceof Technician)) {
            return false;
        }
        Technician other = (Technician) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tndm.pojo.Technician[ id=" + id + " ]";
    }

    public String getNameTech() {
        return this.getUser().getFirstName() + " " + this.getUser().getLastName();
    }
}
