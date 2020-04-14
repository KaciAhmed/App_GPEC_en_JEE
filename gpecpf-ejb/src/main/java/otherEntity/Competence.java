/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package otherEntity;

import dz.elit.gpecpf.commun.util.StaticUtil;
import java.io.Serializable;
import java.util.Collection;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Dell
 */
@Entity
@Table(name = "competence", schema = StaticUtil.ADMINISTRATION_SCHEMA)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Competence.findAll", query = "SELECT c FROM Competence c")
    , @NamedQuery(name = "Competence.findById", query = "SELECT c FROM Competence c WHERE c.id = :id")
    , @NamedQuery(name = "Competence.findByCode", query = "SELECT c FROM Competence c WHERE c.code = :code")
    , @NamedQuery(name = "Competence.findByLibelle", query = "SELECT c FROM Competence c WHERE c.libelle = :libelle")
    , @NamedQuery(name = "Competence.findByDescription", query = "SELECT c FROM Competence c WHERE c.description = :description")})
public class Competence implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 50)
    @Column(name = "code")
    private String code;
    @Size(max = 255)
    @Column(name = "libelle")
    private String libelle;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "idcomp")
    private Collection<Comportement> comportementCollection;
    @JoinColumn(name = "idcatcom", referencedColumnName = "id")
    @ManyToOne
    private Categoriecompetence idcatcom;
    @JoinColumn(name = "iddomcom", referencedColumnName = "id")
    @ManyToOne
    private Domainecompetence iddomcom;
    @JoinColumn(name = "idtypcom", referencedColumnName = "id")
    @ManyToOne
    private Typecompetence idtypcom;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "competence")
    private Collection<Notecompetenceemploye> notecompetenceemployeCollection;

    public Competence() {
    }

    public Competence(Integer id) {
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

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public Collection<Comportement> getComportementCollection() {
        return comportementCollection;
    }

    public void setComportementCollection(Collection<Comportement> comportementCollection) {
        this.comportementCollection = comportementCollection;
    }

    public Categoriecompetence getIdcatcom() {
        return idcatcom;
    }

    public void setIdcatcom(Categoriecompetence idcatcom) {
        this.idcatcom = idcatcom;
    }

    public Domainecompetence getIddomcom() {
        return iddomcom;
    }

    public void setIddomcom(Domainecompetence iddomcom) {
        this.iddomcom = iddomcom;
    }

    public Typecompetence getIdtypcom() {
        return idtypcom;
    }

    public void setIdtypcom(Typecompetence idtypcom) {
        this.idtypcom = idtypcom;
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
        if (!(object instanceof Competence)) {
            return false;
        }
        Competence other = (Competence) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "otherEntity.Competence[ id=" + id + " ]";
    }
    
}
