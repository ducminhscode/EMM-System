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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
    @NamedQuery(name = "RepairHistory.findByExpense", query = "SELECT r FROM RepairHistory r WHERE r.expense = :expense"),
    @NamedQuery(name = "RepairHistory.findByDescription", query = "SELECT r FROM RepairHistory r WHERE r.description = :description"),
    @NamedQuery(name = "RepairHistory.findByStartDate", query = "SELECT r FROM RepairHistory r WHERE r.startDate = :startDate"),
    @NamedQuery(name = "RepairHistory.findByEndDate", query = "SELECT r FROM RepairHistory r WHERE r.endDate = :endDate")})
public class RepairHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "expense")
    private BigDecimal expense;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    @JoinColumn(name = "problem_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private Problem problemId;
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    @ManyToOne
    private RepairType typeId;
    @JoinColumn(name = "technician_id", referencedColumnName = "id")
    @ManyToOne
    private Technician technicianId;

    public RepairHistory() {
    }

    public RepairHistory(Integer id) {
        this.id = id;
    }

    public RepairHistory(Integer id, BigDecimal expense, Date startDate, Date endDate) {
        this.id = id;
        this.expense = expense;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Problem getProblemId() {
        return problemId;
    }

    public void setProblemId(Problem problemId) {
        this.problemId = problemId;
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

    public boolean isDone() {
        return this.description != null && this.expense != null && this.endDate != null && this.typeId != null;
    }

}
