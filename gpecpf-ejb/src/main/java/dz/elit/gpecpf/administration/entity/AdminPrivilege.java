/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.administration.entity;

import dz.elit.gpecpf.commun.util.StaticUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ayadi
 */
@Entity
@Table(name = "admin_privilege", schema = StaticUtil.ADMINISTRATION_SCHEMA)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AdminPrivilege.findByModule", query = "SELECT p FROM AdminPrivilege p WHERE p.adminModule.id =:idModule ORDER BY p.code "),
    @NamedQuery(name = "AdminPrivilege.findByLogin", query = "SELECT p.listAdminPrivilege  FROM AdminUtilisateur u JOIN u.listAdminProfil p WHERE u.login=:login"),
    @NamedQuery(name = "AdminPrivilege.findListModuleByLogin", query = "SELECT DISTINCT pr.adminModule FROM AdminPrivilege pr WHERE pr.id IN (SELECT lp.id FROM AdminUtilisateur u JOIN u.listAdminProfil p JOIN p.listAdminPrivilege lp WHERE u.login=:login) ORDER BY pr.adminModule.ordre")
})
public class AdminPrivilege implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "code", nullable = false, unique = true, length = 20)
    private String code;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "description", nullable = false)
    private String description;
    @JoinColumn(name = "id_module", referencedColumnName = "id")
    @ManyToOne
    private AdminModule adminModule;
    @ManyToMany(mappedBy = "listAdminPrivilege")
    private List<AdminProfil> listAdminProfils = new ArrayList<>();

    public AdminPrivilege() {
    }

    public AdminPrivilege(String code, String description, AdminModule adminModule) {
        this.code = code;
        this.description = description;
        this.adminModule = adminModule;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AdminModule getAdminModule() {
        return adminModule;
    }

    public void setAdminModule(AdminModule adminModule) {
        this.adminModule = adminModule;
    }

    public List<AdminProfil> getListAdminProfils() {
        return listAdminProfils;
    }

    public void setListAdminProfils(List<AdminProfil> listAdminProfils) {
        this.listAdminProfils = listAdminProfils;
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
        if (!(object instanceof AdminPrivilege)) {
            return false;
        }
        AdminPrivilege other = (AdminPrivilege) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dz.elit.gpecpf.administration.entity.AdminPrivilege[ id=" + id + " ]";
    }

}
