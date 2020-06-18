/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.competence.entity;

import dz.elit.gpecpf.commun.util.StaticUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Kaci Ahmed
 */
@Entity
@Table(name = "comportement", schema = StaticUtil.COMPETENCE_SCHEMA)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Comportement.findAll", query = "SELECT c FROM Comportement c")
    , @NamedQuery(name = "Comportement.findById", query = "SELECT c FROM Comportement c WHERE c.id = :id")
    , @NamedQuery(name = "Comportement.findByCode", query = "SELECT c FROM Comportement c WHERE c.code = :code")
    , @NamedQuery(name = "Comportement.findByDescription", query = "SELECT c FROM Comportement c WHERE c.description = :description")})
public class Comportement implements Comparable,Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 50)
    @Column(name = "code")
    private String code;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
 //   @JoinColumn(name = "idcomp", referencedColumnName = "id")
    @ManyToMany(mappedBy = "listComportement")
    private List<Competence> listCompetence = new ArrayList<>();

    public Comportement() {
    }

    public Comportement(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Competence> getListCompetence() {
        return listCompetence;
    }

    public void setListCompetence(List<Competence> listCompetence) {
        this.listCompetence = listCompetence;
    }
    public void addCompetence(Competence cmt) {

        this.getListCompetence().add(cmt);
        cmt.getListComportement().add(this);
    }
    public void addListCompetenceForComportement(List<Competence> lstCompetence){
       for(Competence comt:lstCompetence)  
       {
           this.addCompetence(comt);
       }
    }
    public void removeCompetence(Competence cmt) {
        this.getListCompetence().remove(cmt);
        cmt.getListComportement().remove(this);
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
        if (!(object instanceof Comportement)) {
            return false;
        }
        Comportement other = (Comportement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    public int compareTo(Object o){
        Comportement cmp =(Comportement)o;
        return this.code.compareToIgnoreCase(cmp.getCode());
    }
    @Override
    public String toString() {
        return "otherEntity.Comportement[ id=" + id + " ]";
    }
    
}
