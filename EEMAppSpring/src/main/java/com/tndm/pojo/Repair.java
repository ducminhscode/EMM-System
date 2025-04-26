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
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Tran Nguyen Duc Minh
 */
@Entity
@Table(name = "repair")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Repair.findAll", query = "SELECT r FROM Repair r"),
    @NamedQuery(name = "Repair.findById", query = "SELECT r FROM Repair r WHERE r.id = :id"),
    @NamedQuery(name = "Repair.findByRepairDate", query = "SELECT r FROM Repair r WHERE r.repairDate = :repairDate"),
    @NamedQuery(name = "Repair.findByPrice", query = "SELECT r FROM Repair r WHERE r.price = :price"),
    @NamedQuery(name = "Repair.findByDescription", query = "SELECT r FROM Repair r WHERE r.description = :description")})
public class Repair implements Serializable {

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
    @Column(name = "price")
    private BigDecimal price;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    @JoinColumn(name = "personnel_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personnel personnelId;
    @JoinColumn(name = "problem_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Problem problemId;
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RepairType typeId;

    public Repair() {
    }

    public Repair(Integer id) {
        this.id = id;
    }

    public Repair(Integer id, Date repairDate, BigDecimal price) {
        this.id = id;
        this.repairDate = repairDate;
        this.price = price;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Personnel getPersonnelId() {
        return personnelId;
    }

    public void setPersonnelId(Personnel personnelId) {
        this.personnelId = personnelId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Repair)) {
            return false;
        }
        Repair other = (Repair) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tndm.pojo.Repair[ id=" + id + " ]";
    }
    
}
