/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package otherEntity;

import dz.elit.gpecpf.commun.util.StaticUtil;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Dell
 */
@Entity
@Table(name = "compagneevaluation", schema = StaticUtil.ADMINISTRATION_SCHEMA)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Compagneevaluation.findAll", query = "SELECT c FROM Compagneevaluation c")
    , @NamedQuery(name = "Compagneevaluation.findById", query = "SELECT c FROM Compagneevaluation c WHERE c.id = :id")
    , @NamedQuery(name = "Compagneevaluation.findByDatedeb", query = "SELECT c FROM Compagneevaluation c WHERE c.datedeb = :datedeb")
    , @NamedQuery(name = "Compagneevaluation.findByDatefin", query = "SELECT c FROM Compagneevaluation c WHERE c.datefin = :datefin")})
public class Compagneevaluation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "datedeb")
    @Temporal(TemporalType.DATE)
    private Date datedeb;
    @Column(name = "datefin")
    @Temporal(TemporalType.DATE)
    private Date datefin;
    @OneToMany(mappedBy = "idcompagne")
    private Collection<Evaluation> evaluationCollection;

    public Compagneevaluation() {
    }

    public Compagneevaluation(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDatedeb() {
        return datedeb;
    }

    public void setDatedeb(Date datedeb) {
        this.datedeb = datedeb;
    }

    public Date getDatefin() {
        return datefin;
    }

    public void setDatefin(Date datefin) {
        this.datefin = datefin;
    }

    @XmlTransient
    public Collection<Evaluation> getEvaluationCollection() {
        return evaluationCollection;
    }

    public void setEvaluationCollection(Collection<Evaluation> evaluationCollection) {
        this.evaluationCollection = evaluationCollection;
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
        if (!(object instanceof Compagneevaluation)) {
            return false;
        }
        Compagneevaluation other = (Compagneevaluation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "otherEntity.Compagneevaluation[ id=" + id + " ]";
    }
    
}
