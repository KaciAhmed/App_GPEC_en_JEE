/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.competence.entity;

import dz.elit.gpecpf.commun.util.StaticUtil;
import java.io.Serializable;
import java.util.Collection;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Dell
 */
@Entity
@Table(name = "typecompetence", schema = StaticUtil.COMPETENCE_SCHEMA)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Typecompetence.findAll", query = "SELECT t FROM Typecompetence t")
    , @NamedQuery(name = "Typecompetence.findById", query = "SELECT t FROM Typecompetence t WHERE t.id = :id")
    , @NamedQuery(name = "Typecompetence.findByCode", query = "SELECT t FROM Typecompetence t WHERE t.code = :code")
    , @NamedQuery(name = "Typecompetence.findByLibelle", query = "SELECT t FROM Typecompetence t WHERE t.libelle = :libelle")
    , @NamedQuery(name = "Typecompetence.findByDescription", query = "SELECT t FROM Typecompetence t WHERE t.description = :description")})
public class Typecompetence implements Serializable {

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
    @OneToMany(mappedBy = "idtypcom")
    private Collection<Competence> competenceCollection;

    public Typecompetence() {
    }

    public Typecompetence(Integer id) {
        this.id = id;
    }

    public Typecompetence(String code, String libelle, String description) {
        this.code = code;
        this.libelle = libelle;
        this.description = description;
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
        if (!(object instanceof Typecompetence)) {
            return false;
        }
        Typecompetence other = (Typecompetence) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "otherEntity.Typecompetence[ id=" + id + " ]";
    }
    
}
