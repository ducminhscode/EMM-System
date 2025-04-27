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
import java.math.BigDecimal;
import java.util.Date;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Tran Nguyen Duc Minh
 */
@Entity
@Table(name = "repair_history")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RepairHistory.findAll", query = "SELECT r FROM RepairHistory r"),
    @NamedQuery(name = "RepairHistory.findById", query = "SELECT r FROM RepairHistory r WHERE r.id = :id"),
    @NamedQuery(name = "RepairHistory.findByRepairDate", query = "SELECT r FROM RepairHistory r WHERE r.repairDate = :repairDate"),
    @NamedQuery(name = "RepairHistory.findByExpense", query = "SELECT r FROM RepairHistory r WHERE r.expense = :expense"),
    @NamedQuery(name = "RepairHistory.findByDescription", query = "SELECT r FROM RepairHistory r WHERE r.description = :description"),
    @NamedQuery(name = "RepairHistory.findByCreatedDate", query = "SELECT r FROM RepairHistory r WHERE r.createdDate = :createdDate"),
    @NamedQuery(name = "RepairHistory.findByUpdatedDate", query = "SELECT r FROM RepairHistory r WHERE r.updatedDate = :updatedDate")})
public class RepairHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "repair_date")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date repairDate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "expense")
    private BigDecimal expense;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdDate;
    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updatedDate;
    @JoinColumn(name = "repair_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Repair repairId;
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RepairType typeId;
    @JoinColumn(name = "technician_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Technician technicianId;

    public RepairHistory() {
    }

    public RepairHistory(Integer id) {
        this.id = id;
    }

    public RepairHistory(Integer id, Date repairDate, BigDecimal expense) {
        this.id = id;
        this.repairDate = repairDate;
        this.expense = expense;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getRepairDate() {
        return repairDate;
    }

    public void setRepairDate(Date repairDate) {
        this.repairDate = repairDate;
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public void setExpense(BigDecimal expense) {
        this.expense = expense;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Repair getRepairId() {
        return repairId;
    }

    public void setRepairId(Repair repairId) {
        this.repairId = repairId;
    }

    public RepairType getTypeId() {
        return typeId;
    }

    public void setTypeId(RepairType typeId) {
        this.typeId = typeId;
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
        if (!(object instanceof RepairHistory)) {
            return false;
        }
        RepairHistory other = (RepairHistory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tndm.pojo.RepairHistory[ id=" + id + " ]";
    }
    
}
