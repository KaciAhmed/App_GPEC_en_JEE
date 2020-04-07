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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
 * @author Dell
 */
@Entity
@Table(name = "evaluation",schema = StaticUtil.ADMINISTRATION_SCHEMA)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Evaluation.findAll", query = "SELECT e FROM Evaluation e")
    , @NamedQuery(name = "Evaluation.findById", query = "SELECT e FROM Evaluation e WHERE e.id = :id")
    , @NamedQuery(name = "Evaluation.findByDateeva", query = "SELECT e FROM Evaluation e WHERE e.dateeva = :dateeva")
    , @NamedQuery(name = "Evaluation.findByArchive", query = "SELECT e FROM Evaluation e WHERE e.archive = :archive")
    , @NamedQuery(name = "Evaluation.findByEtat", query = "SELECT e FROM Evaluation e WHERE e.etat = :etat")
    , @NamedQuery(name = "Evaluation.findByAvisemp", query = "SELECT e FROM Evaluation e WHERE e.avisemp = :avisemp")
    , @NamedQuery(name = "Evaluation.findByAvishiern1", query = "SELECT e FROM Evaluation e WHERE e.avishiern1 = :avishiern1")
    , @NamedQuery(name = "Evaluation.findByAvishiern2", query = "SELECT e FROM Evaluation e WHERE e.avishiern2 = :avishiern2")})
public class Evaluation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "id")
    private String id;
    @Column(name = "dateeva")
    @Temporal(TemporalType.DATE)
    private Date dateeva;
    @Size(max = 2147483647)
    @Column(name = "archive")
    private String archive;
    @Size(max = 2147483647)
    @Column(name = "etat")
    private String etat;
    @Size(max = 2147483647)
    @Column(name = "avisemp")
    private String avisemp;
    @Size(max = 2147483647)
    @Column(name = "avishiern1")
    private String avishiern1;
    @Size(max = 2147483647)
    @Column(name = "avishiern2")
    private String avishiern2;
    @JoinColumn(name = "idcompagne", referencedColumnName = "id")
    @ManyToOne
    private Compagneevaluation idcompagne;
    @JoinColumn(name = "idemploye", referencedColumnName = "id")
    @ManyToOne
    private Employe idemploye;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evaluation")
    private Collection<Notecompetenceemploye> notecompetenceemployeCollection;

    public Evaluation() {
    }

    public Evaluation(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAvisemp() {
        return avisemp;
    }

    public void setAvisemp(String avisemp) {
        this.avisemp = avisemp;
    }

    public String getAvishiern1() {
        return avishiern1;
    }

    public void setAvishiern1(String avishiern1) {
        this.avishiern1 = avishiern1;
    }

    public String getAvishiern2() {
        return avishiern2;
    }

    public void setAvishiern2(String avishiern2) {
        this.avishiern2 = avishiern2;
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
