/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package otherEntity;

import dz.elit.gpecpf.employe.entity.Employe;
import dz.elit.gpecpf.compagne_evaluation.entity.Compagneevaluation;
import dz.elit.gpecpf.commun.util.StaticUtil;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Kaci Ahmed
 */
@Entity
@Table(name = "evaluation", schema = StaticUtil.ADMINISTRATION_SCHEMA)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Evaluation.findAll", query = "SELECT e FROM Evaluation e")
    , @NamedQuery(name = "Evaluation.findById", query = "SELECT e FROM Evaluation e WHERE e.id = :id")
    , @NamedQuery(name = "Evaluation.findByCode", query = "SELECT e FROM Evaluation e WHERE e.code = :code")
    , @NamedQuery(name = "Evaluation.findByDateeva", query = "SELECT e FROM Evaluation e WHERE e.dateeva = :dateeva")
    , @NamedQuery(name = "Evaluation.findByArchive", query = "SELECT e FROM Evaluation e WHERE e.archive = :archive")
    , @NamedQuery(name = "Evaluation.findByEtat", query = "SELECT e FROM Evaluation e WHERE e.etat = :etat")})
public class Evaluation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 50)
    @Column(name = "code")
    private String code;
    @Column(name = "dateeva")
    @Temporal(TemporalType.DATE)
    private Date dateeva;
    @Size(max = 2147483647)
    @Column(name = "archive")
    private String archive;
    @Size(max = 2147483647)
    @Column(name = "etat")
    private String etat;
    @JoinColumn(name = "idcompagne", referencedColumnName = "id")
    @ManyToOne
    private Compagneevaluation idcompagne;
    @JoinColumn(name = "idemploye", referencedColumnName = "id")
    @ManyToOne
    private Employe idemploye;
    @OneToMany(mappedBy = "idevaluation")
    private Collection<Avis> avisCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evaluation")
    private Collection<Notecompetenceemploye> notecompetenceemployeCollection;

    public Evaluation() {
    }

    public Evaluation(Integer id) {
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
    
    public Date getDateeva() {
        return dateeva;
    }

    public void setDateeva(Date dateeva) {
        this.dateeva = dateeva;
    }

    public String getArchive() {
        return archive;
    }

    public void setArchive(String archive) {
        this.archive = archive;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Compagneevaluation getIdcompagne() {
        return idcompagne;
    }

    public void setIdcompagne(Compagneevaluation idcompagne) {
        this.idcompagne = idcompagne;
    }

    public Employe getIdemploye() {
        return idemploye;
    }

    public void setIdemploye(Employe idemploye) {
        this.idemploye = idemploye;
    }

    @XmlTransient
    public Collection<Avis> getAvisCollection() {
        return avisCollection;
    }

    public void setAvisCollection(Collection<Avis> avisCollection) {
        this.avisCollection = avisCollection;
    }

    @XmlTransient
    public Collection<Notecompetenceemploye> getNotecompetenceemployeCollection() {
        return notecompetenceemployeCollection;
    }

    public void setNotecompetenceemployeCollection(Collection<Notecompetenceemploye> notecompetenceemployeCollection) {
        this.notecompetenceemployeCollection = notecompetenceemployeCollection;
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
        if (!(object instanceof Evaluation)) {
            return false;
        }
        Evaluation other = (Evaluation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "otherEntity.Evaluation[ id=" + id + " ]";
    }
    
}
