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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "mission",schema = StaticUtil.ADMINISTRATION_SCHEMA)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Mission.findAll", query = "SELECT m FROM Mission m")
    , @NamedQuery(name = "Mission.findById", query = "SELECT m FROM Mission m WHERE m.id = :id")
    , @NamedQuery(name = "Mission.findByCode", query = "SELECT m FROM Mission m WHERE m.code = :code")
    , @NamedQuery(name = "Mission.findByLibelle", query = "SELECT m FROM Mission m WHERE m.libelle = :libelle")
    , @NamedQuery(name = "Mission.findByDescription", query = "SELECT m FROM Mission m WHERE m.description = :description")})
public class Mission implements Serializable {

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
    @JoinTable(name = "misssionposte", joinColumns = {
        @JoinColumn(name = "idmission", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "idposte", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Poste> posteCollection;
    @OneToMany(mappedBy = "idmission")
    private Collection<Activite> activiteCollection;

    public Mission() {
    }

    public Mission(Integer id, String code, String libelle, String description, Collection<Poste> posteCollection, Collection<Activite> activiteCollection) {
        this.id = id;
        this.code = code;
        this.libelle = libelle;
        this.description = description;
        this.posteCollection = posteCollection;
        this.activiteCollection = activiteCollection;
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
    public Collection<Poste> getPosteCollection() {
        return posteCollection;
    }

    public void setPosteCollection(Collection<Poste> posteCollection) {
        this.posteCollection = posteCollection;
    }

    @XmlTransient
    public Collection<Activite> getActiviteCollection() {
        return activiteCollection;
    }

    public void setActiviteCollection(Collection<Activite> activiteCollection) {
        this.activiteCollection = activiteCollection;
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
        if (!(object instanceof Mission)) {
            return false;
        }
        Mission other = (Mission) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "otherEntity.Mission[ id=" + id + " ]";
    }
    
}
