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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author ADMIN
 */
@Entity
@Table(name = "maintenance_schedule")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MaintenanceSchedule.findAll", query = "SELECT m FROM MaintenanceSchedule m"),
    @NamedQuery(name = "MaintenanceSchedule.findById", query = "SELECT m FROM MaintenanceSchedule m WHERE m.id = :id"),
    @NamedQuery(name = "MaintenanceSchedule.findByStartDate", query = "SELECT m FROM MaintenanceSchedule m WHERE m.startDate = :startDate"),
    @NamedQuery(name = "MaintenanceSchedule.findByEndDate", query = "SELECT m FROM MaintenanceSchedule m WHERE m.endDate = :endDate"),
    @NamedQuery(name = "MaintenanceSchedule.findByTitle", query = "SELECT m FROM MaintenanceSchedule m WHERE m.title = :title"),
    @NamedQuery(name = "MaintenanceSchedule.findByDescription", query = "SELECT m FROM MaintenanceSchedule m WHERE m.description = :description"),
    @NamedQuery(name = "MaintenanceSchedule.findByFrequency", query = "SELECT m FROM MaintenanceSchedule m WHERE m.frequency = :frequency"),
    @NamedQuery(name = "MaintenanceSchedule.findByExpenseFirst", query = "SELECT m FROM MaintenanceSchedule m WHERE m.expenseFirst = :expenseFirst"),
    @NamedQuery(name = "MaintenanceSchedule.findByExpenseLast", query = "SELECT m FROM MaintenanceSchedule m WHERE m.expenseLast = :expenseLast"),
    @NamedQuery(name = "MaintenanceSchedule.findByMaintenanceStatus", query = "SELECT m FROM MaintenanceSchedule m WHERE m.maintenanceStatus = :maintenanceStatus"),
    @NamedQuery(name = "MaintenanceSchedule.findByMaintenanceDate", query = "SELECT m FROM MaintenanceSchedule m WHERE m.maintenanceDate = :maintenanceDate")})
public class MaintenanceSchedule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "title")
    private String title;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "frequency")
    private String frequency;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "expense_first")
    private BigDecimal expenseFirst;
    @Column(name = "expense_last")
    private BigDecimal expenseLast;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "maintenance_status")
    private String maintenanceStatus;
    @Column(name = "maintenance_date")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date maintenanceDate;
    @JoinColumn(name = "device_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Device deviceId;
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private MaintenanceType typeId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User userId;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "maintenanceScheduleId")
    private Set<MaintenanceAssignment> maintenanceAssignmentSet;

    public MaintenanceSchedule() {
    }

    public MaintenanceSchedule(Integer id) {
        this.id = id;
    }

    public MaintenanceSchedule(Integer id, Date startDate, Date endDate, String title, String frequency, String maintenanceStatus) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.frequency = frequency;
        this.maintenanceStatus = maintenanceStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public BigDecimal getExpenseFirst() {
        return expenseFirst;
    }

    public void setExpenseFirst(BigDecimal expenseFirst) {
        this.expenseFirst = expenseFirst;
    }

    public BigDecimal getExpenseLast() {
        return expenseLast;
    }

    public void setExpenseLast(BigDecimal expenseLast) {
        this.expenseLast = expenseLast;
    }

    public String getMaintenanceStatus() {
        return maintenanceStatus;
    }

    public void setMaintenanceStatus(String maintenanceStatus) {
        this.maintenanceStatus = maintenanceStatus;
    }

    public Date getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(Date maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    public Device getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Device deviceId) {
        this.deviceId = deviceId;
    }

    public MaintenanceType getTypeId() {
        return typeId;
    }

    public void setTypeId(MaintenanceType typeId) {
        this.typeId = typeId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    @XmlTransient
    public Set<MaintenanceAssignment> getMaintenanceAssignmentSet() {
        return maintenanceAssignmentSet;
    }

    public void setMaintenanceAssignmentSet(Set<MaintenanceAssignment> maintenanceAssignmentSet) {
        this.maintenanceAssignmentSet = maintenanceAssignmentSet;
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
