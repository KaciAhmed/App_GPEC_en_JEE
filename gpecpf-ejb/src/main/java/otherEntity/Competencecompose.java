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
@Table(name = "competencecompose")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Competencecompose.findAll", query = "SELECT c FROM Competencecompose c")
    , @NamedQuery(name = "Competencecompose.findById", query = "SELECT c FROM Competencecompose c WHERE c.id = :id")})
public class Competencecompose implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "id")
    private String id;
    @OneToMany(mappedBy = "idcomcom")
    private Collection<Competence> competenceCollection;
    @JoinColumn(name = "idcommer", referencedColumnName = "id")
    @ManyToOne
    private Competence idcommer;

    public Competencecompose() {
    }

    public Competencecompose(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlTransient
    public Collection<Competence> getCompetenceCollection() {
        return competenceCollection;
    }

    public void setCompetenceCollection(Collection<Competence> competenceCollection) {
        this.competenceCollection = competenceCollection;
    }

    public Competence getIdcommer() {
        return idcommer;
    }

    public void setIdcommer(Competence idcommer) {
        this.idcommer = idcommer;
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
        if (!(object instanceof Competencecompose)) {
            return false;
        }
        Competencecompose other = (Competencecompose) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "otherEntity.Competencecompose[ id=" + id + " ]";
    }
    
}
