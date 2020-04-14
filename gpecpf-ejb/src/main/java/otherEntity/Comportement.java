/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package otherEntity;

import dz.elit.gpecpf.commun.util.StaticUtil;
import java.io.Serializable;
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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Dell
 */
@Entity
@Table(name = "comportement", schema = StaticUtil.ADMINISTRATION_SCHEMA)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Comportement.findAll", query = "SELECT c FROM Comportement c")
    , @NamedQuery(name = "Comportement.findById", query = "SELECT c FROM Comportement c WHERE c.id = :id")
    , @NamedQuery(name = "Comportement.findByCode", query = "SELECT c FROM Comportement c WHERE c.code = :code")
    , @NamedQuery(name = "Comportement.findByDescription", query = "SELECT c FROM Comportement c WHERE c.description = :description")})
public class Comportement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Size(max = 50)
    @Column(name = "code")
    private String code;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @JoinColumn(name = "idcomp", referencedColumnName = "id")
    @ManyToOne
    private Competence idcomp;

    public Comportement() {
    }

    public Comportement(Integer id, String code, String description, Competence idcomp) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.idcomp = idcomp;
    }
    
    public Comportement(Integer id) {
        this.id = id;
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

    public Competence getIdcomp() {
        return idcomp;
    }

    public void setIdcomp(Competence idcomp) {
        this.idcomp = idcomp;
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
        if (!(object instanceof Comportement)) {
            return false;
        }
        Comportement other = (Comportement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "otherEntity.Comportement[ id=" + id + " ]";
    }
    
}
