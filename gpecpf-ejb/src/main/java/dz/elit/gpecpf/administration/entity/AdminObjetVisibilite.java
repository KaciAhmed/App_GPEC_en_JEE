/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */ 
package dz.elit.gpecpf.administration.entity;

import dz.elit.gpecpf.commun.util.StaticUtil;
import java.io.Serializable;
import java.util.List;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author laidani.youcef
 */ 

@Entity
@Table(name = "admin_objet_visibilite", schema = StaticUtil.ADMINISTRATION_SCHEMA)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AdminObjetVisibilite.findAll", query = "SELECT a FROM AdminObjetVisibilite a"),
    @NamedQuery(name = "AdminObjetVisibilite.findById", query = "SELECT a FROM AdminObjetVisibilite a WHERE a.id = :id"),
    @NamedQuery(name = "AdminObjetVisibilite.findByEntity", query = "SELECT a FROM AdminObjetVisibilite a WHERE a.entity = :entity")})
public class AdminObjetVisibilite implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 214)
    @Column(name = "entity", length = 214)
    private String entity;
    @Size(max = 2147483647)
    @Column(name = "libelle", length = 214)
    private String libelle;
    @OneToMany(mappedBy = "idObjetVisibilite")
    private List<AdminDroitVisibilite> adminDroitVisibiliteList;
    
    @JoinColumn(name = "module", referencedColumnName = "id")
    @ManyToOne
    private AdminModule module;
    
    public AdminObjetVisibilite() {
    }

    public AdminObjetVisibilite(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    @XmlTransient
    public List<AdminDroitVisibilite> getAdminDroitVisibiliteList() {
        return adminDroitVisibiliteList;
    }

    public void setAdminDroitVisibiliteList(List<AdminDroitVisibilite> adminDroitVisibiliteList) {
        this.adminDroitVisibiliteList = adminDroitVisibiliteList;
    }

    public AdminModule getModule() {
        return module;
    }

    public void setModule(AdminModule module) {
        this.module = module;
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
        if (!(object instanceof AdminObjetVisibilite)) {
            return false;
        }
        AdminObjetVisibilite other = (AdminObjetVisibilite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sd.AdminObjetVisibilite[ id=" + id + " ]";
    }
    
}
