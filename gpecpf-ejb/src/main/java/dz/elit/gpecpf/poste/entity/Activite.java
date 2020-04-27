/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.poste.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import dz.elit.gpecpf.commun.util.StaticUtil;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author N
 */
@Entity
@Table(name = "activite",schema = StaticUtil.POSTE_SCHEMA)
@NamedQueries({
    @NamedQuery(name = "Activite.findByCodeWithoutCurrentId", query = "SELECT a FROM Activite a WHERE a.code =:code AND a.id != :id ORDER BY a.code  "),})
public class Activite implements Serializable {

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
	
	@ManyToMany
	@JoinTable(
		name = "activite_tache",
		joinColumns = {
			@JoinColumn(name = "id_activite", referencedColumnName = "id")
		}, inverseJoinColumns = {
			@JoinColumn(name = "id_tache", referencedColumnName = "id")
		}, schema = StaticUtil.POSTE_SCHEMA
	)
	private List<Tache> listTaches = new ArrayList<>();
    
    public Activite() {
    }

    public Activite(Integer id) {
        this.id = id;
    }

	public Activite(Integer id, String code, String libelle, List<Tache> listTaches) {
		this.id = id;
		this.code = code;
		this.libelle = libelle;
		this.listTaches = listTaches;
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

	public List<Tache> getListTaches() {
		return listTaches;
	}

	public void setListTaches(List<Tache> listTaches) {
		this.listTaches = listTaches;
	}
	
	public void addTache(Tache tache) {
		this.getListTaches().add(tache);
	}
	
	public void removeTache(Tache tache) {
		this.getListTaches().remove(tache);
	}
	
	public void addListTaches(List<Tache> taches) {
		for (Tache tache : taches) {
			addTache(tache);
		}
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
        if (!(object instanceof Activite)) {
            return false;
        }
        Activite other = (Activite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dz.elit.gpecpf.poste.entity.Activite[ id=" + id + " ]";
    }
    
}
