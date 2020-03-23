/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.administration.entity;

import dz.elit.gpecpf.commun.util.StaticUtil;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

/**
 *
 * @author leghettas.rabah
 */
@Entity
@Table(name = "admin_module",schema = StaticUtil.ADMINISTRATION_SCHEMA)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AdminModule.findByOrdre", query = "SELECT m FROM AdminModule m WHERE m.ordre=:ordre "),
})
public class AdminModule implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)    
    @Column(name = "id")
    private Integer id;
    @Size(min = 1, max = 20)
    @Column(name = "code",nullable=false,unique=true,length = 20)
    @NotNull
    private String code;
    @Size(min = 1, max = 30)
    @Column(name = "libelle",nullable=false,unique=true,length = 30)
    @NotNull
    private String libelle;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    //url relatif de l'acceuil de module
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "url_module",nullable = false)
    private String urlModule;
    @Column(name = "ordre")
    private Integer ordre;
    @OneToMany(mappedBy = "adminModule", fetch = FetchType.LAZY)
    private List<AdminPrivilege> listAdminPrivilege;

    @OneToMany(mappedBy = "module")
    private List<AdminObjetVisibilite> listeObjectVisibilite;
    
    public AdminModule() {
    }

    public AdminModule(Integer id) {
        this.id = id;
    }
    
    
    public AdminModule(String code, String libelle,String urlModule, Integer ordre) {
        this.code = code;
        this.libelle = libelle;
        this.urlModule=urlModule;
        this.description=libelle;
        this.ordre = ordre;
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

    public List<AdminPrivilege> getListAdminPrivilege() {
        return listAdminPrivilege;
    }

    public void setListAdminPrivilege(List<AdminPrivilege> listAdminPrivilege) {
        this.listAdminPrivilege = listAdminPrivilege;
    }

    public String getUrlModule() {
        return urlModule;
    }

    public void setUrlModule(String urlModule) {
        this.urlModule = urlModule;
    }

    public Integer getOrdre() {
        return ordre;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    public List<AdminObjetVisibilite> getListeObjectVisibilite() {
        return listeObjectVisibilite;
    }

    public void setListeObjectVisibilite(List<AdminObjetVisibilite> listeObjectVisibilite) {
        this.listeObjectVisibilite = listeObjectVisibilite;
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
        if (!(object instanceof AdminModule)) {
            return false;
        }
        AdminModule other = (AdminModule) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dz.elit.gpecpf.administration.entity.AdminModule[ id=" + id + " ]";
    }

}
