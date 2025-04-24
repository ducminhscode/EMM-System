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
@Table(name = "repair")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Repair.findAll", query = "SELECT r FROM Repair r"),
    @NamedQuery(name = "Repair.findById", query = "SELECT r FROM Repair r WHERE r.id = :id"),
    @NamedQuery(name = "Repair.findByRepairDate", query = "SELECT r FROM Repair r WHERE r.repairDate = :repairDate"),
    @NamedQuery(name = "Repair.findByType", query = "SELECT r FROM Repair r WHERE r.type = :type"),
    @NamedQuery(name = "Repair.findByPrice", query = "SELECT r FROM Repair r WHERE r.price = :price"),
    @NamedQuery(name = "Repair.findByDescribe", query = "SELECT r FROM Repair r WHERE r.describe = :describe")})
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
    private Date repairDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 22)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @NotNull
    @Column(name = "price")
    private long price;
    @Size(max = 255)
    @Column(name = "describe")
    private String describe;
    @JoinColumn(name = "personnel_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personnel personnelId;
    @JoinColumn(name = "problem_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Problem problemId;

    public Repair() {
    }

    public Repair(Integer id) {
        this.id = id;
    }

    public Repair(Integer id, Date repairDate, String type, long price) {
        this.id = id;
        this.repairDate = repairDate;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
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
