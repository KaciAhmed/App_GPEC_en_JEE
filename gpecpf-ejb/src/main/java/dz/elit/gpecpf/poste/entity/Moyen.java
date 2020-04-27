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
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
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
@Table(name = "moyen",schema = StaticUtil.POSTE_SCHEMA)
@NamedQueries({
    @NamedQuery(name = "Moyen.findByCodeWithoutCurrentId", query = "SELECT t FROM Moyen t WHERE t.code =:code AND t.id != :id ORDER BY t.code  "),})
public class Moyen implements Serializable {

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
    @Size(max = 255)
    @Column(name = "description")
    private String description;
	
	@ManyToMany(mappedBy = "listMoyen")
	private List<Poste> listPoste;
    
    public Moyen() {
    }

    public Moyen(Integer id) {
        this.id = id;
    }
    
    public Moyen(Integer id, String code, String description) {
        this.id = id;
        this.code = code;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
		this.description = description;
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
        if (!(object instanceof Moyen)) {
            return false;
        }
        Moyen other = (Moyen) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dz.elit.gpecpf.poste.entity.Moyen[ id=" + id + " ]";
    }
    
}
