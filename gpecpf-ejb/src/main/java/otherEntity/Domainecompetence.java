/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package otherEntity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "domainecompetence")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Domainecompetence.findAll", query = "SELECT d FROM Domainecompetence d")
    , @NamedQuery(name = "Domainecompetence.findById", query = "SELECT d FROM Domainecompetence d WHERE d.id = :id")
    , @NamedQuery(name = "Domainecompetence.findByCode", query = "SELECT d FROM Domainecompetence d WHERE d.code = :code")
    , @NamedQuery(name = "Domainecompetence.findByLibelle", query = "SELECT d FROM Domainecompetence d WHERE d.libelle = :libelle")
    , @NamedQuery(name = "Domainecompetence.findByDescription", query = "SELECT d FROM Domainecompetence d WHERE d.description = :description")})
public class Domainecompetence implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "id")
    private String id;
    @Size(max = 50)
    @Column(name = "code")
    private String code;
    @Size(max = 255)
    @Column(name = "libelle")
    private String libelle;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "iddomcom")
    private Collection<Competence> competenceCollection;

    public Domainecompetence() {
    }

    public Domainecompetence(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        if (!(object instanceof Domainecompetence)) {
            return false;
        }
        Domainecompetence other = (Domainecompetence) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "otherEntity.Domainecompetence[ id=" + id + " ]";
    }
    
}
