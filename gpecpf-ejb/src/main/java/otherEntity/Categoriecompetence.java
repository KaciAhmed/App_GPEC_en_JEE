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
@Table(name = "categoriecompetence", schema = StaticUtil.ADMINISTRATION_SCHEMA)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Categoriecompetence.findAll", query = "SELECT c FROM Categoriecompetence c")
    , @NamedQuery(name = "Categoriecompetence.findById", query = "SELECT c FROM Categoriecompetence c WHERE c.id = :id")
    , @NamedQuery(name = "Categoriecompetence.findByCode", query = "SELECT c FROM Categoriecompetence c WHERE c.code = :code")
    , @NamedQuery(name = "Categoriecompetence.findByLibelle", query = "SELECT c FROM Categoriecompetence c WHERE c.libelle = :libelle")})
public class Categoriecompetence implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Size(max = 50)
    @Column(name = "code")
    private String code;
    @Size(max = 255)
    @Column(name = "libelle")
    private String libelle;
    @OneToMany(mappedBy = "idcatmere")
    private Collection<Categoriecompetence> categoriecompetenceCollection;
    @JoinColumn(name = "idcatmere", referencedColumnName = "id")
    @ManyToOne
    private Categoriecompetence idcatmere;
    @OneToMany(mappedBy = "idcatcom")
    private Collection<Competence> competenceCollection;

    public Categoriecompetence() {
    }

    public Categoriecompetence(Integer id, String code, String libelle, Collection<Categoriecompetence> categoriecompetenceCollection, Categoriecompetence idcatmere, Collection<Competence> competenceCollection) {
        this.id = id;
        this.code = code;
        this.libelle = libelle;
        this.categoriecompetenceCollection = categoriecompetenceCollection;
        this.idcatmere = idcatmere;
        this.competenceCollection = competenceCollection;
    }
    
    public Categoriecompetence(Integer id) {
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

    @XmlTransient
    public Collection<Categoriecompetence> getCategoriecompetenceCollection() {
        return categoriecompetenceCollection;
    }

    public void setCategoriecompetenceCollection(Collection<Categoriecompetence> categoriecompetenceCollection) {
        this.categoriecompetenceCollection = categoriecompetenceCollection;
    }

    public Categoriecompetence getIdcatmere() {
        return idcatmere;
    }

    public void setIdcatmere(Categoriecompetence idcatmere) {
        this.idcatmere = idcatmere;
    }

    @XmlTransient
    public Collection<Competence> getCompetenceCollection() {
        return competenceCollection;
    }

    public void setCompetenceCollection(Collection<Competence> competenceCollection) {
        this.competenceCollection = competenceCollection;
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
        if (!(object instanceof Categoriecompetence)) {
            return false;
        }
        Categoriecompetence other = (Categoriecompetence) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "otherEntity.Categoriecompetence[ id=" + id + " ]";
    }
    
}
