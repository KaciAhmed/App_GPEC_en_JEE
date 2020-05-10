/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.poste.entity;

import dz.elit.gpecpf.commun.util.StaticUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import otherEntity.Employe;
import otherEntity.Poste;
import org.eclipse.persistence.queries.QueryRedirector;

/**
 *
 * @author N
 */
@Entity
@Table(name = "formation",schema = StaticUtil.ADMINISTRATION_SCHEMA)
@NamedQueries({
    @NamedQuery(name = "Formation.findByCodeWithoutCurrentId", query = "SELECT t FROM Formation t WHERE t.code =:code AND t.id != :id ORDER BY t.code  ")
    ,@NamedQuery(name = "Formation.findAll", query = "SELECT f FROM Formation f")
    , @NamedQuery(name = "Formation.findById", query = "SELECT f FROM Formation f WHERE f.id = :id")
    , @NamedQuery(name = "Formation.findByCode", query = "SELECT f FROM Formation f WHERE f.code = :code")
    , @NamedQuery(name = "Formation.findByDescription", query = "SELECT f FROM Formation f WHERE f.description = :description")
    , @NamedQuery(name = "Formation.findByTypeform", query = "SELECT f FROM Formation f WHERE f.type = :type")
    , @NamedQuery(name = "Formation.findByExigence", query = "SELECT f FROM Formation f WHERE f.exigence = :exigence")})
public class Formation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)    
    @Column(name = "id")
    private Integer id;
    @Size(min = 1, max = 50)
    @Column(name = "code")
    @NotNull
    private String code;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Size(max = 255)
    @Column(name = "typeform")
    private String type;
    @Size(max = 255)
    @Column(name = "exigence")
    private String exigence;
    @Size(max = 255)
    @Column(name = "specialite")
    private String specialite;
     @ManyToMany(mappedBy = "listFormation")
    private List<Employe> listEmploye = new ArrayList();
    @JoinTable(name = "posteformation", joinColumns = {
        @JoinColumn(name = "idform", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "idposte", referencedColumnName = "id")})
    @ManyToMany
    private List<Poste> listPost= new ArrayList();

    
    public Formation() {
    }

    public Formation(Integer id) {
        this.id = id;
    }

	public Formation(Integer id, String code, String description, String type, String exigence) {
		this.id = id;
		this.code = code;
		this.description = description;
		this.type = type;
		this.exigence = exigence;
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

    public String getType() {
	return type;
    }

	public void setType(String type) {
		this.type = type;
	}

	public String getExigence() {
		return exigence;
	}

	public void setExigence(String exigence) {
		this.exigence = exigence;
	}

    public String getSpecialite() {
          return specialite;
    }

    public void setSpecialite(String specialite) {
          this.specialite = specialite;
    }

    public List<Employe> getListEmploye() {
        return listEmploye;
    }

    public void setListEmploye(List<Employe> listEmploye) {
        this.listEmploye = listEmploye;
    }
    public List<Poste> getListPost() {
        return listPost;
    }

    public void setListPost(List<Poste> listPost) {
        this.listPost = listPost;
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
        if (!(object instanceof Formation)) {
            return false;
        }
        Formation other = (Formation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dz.elit.gpecpf.poste.entity.Formation[ id=" + id + " ]";
    }
    
}
