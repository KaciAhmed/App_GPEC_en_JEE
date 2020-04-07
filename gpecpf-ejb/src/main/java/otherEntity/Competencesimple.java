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
@Table(name = "competencesimple",schema = StaticUtil.ADMINISTRATION_SCHEMA)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Competencesimple.findAll", query = "SELECT c FROM Competencesimple c")
    , @NamedQuery(name = "Competencesimple.findById", query = "SELECT c FROM Competencesimple c WHERE c.id = :id")})
public class Competencesimple implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "idcommer", referencedColumnName = "id")
    @ManyToOne
    private Competence idcommer;

    public Competencesimple() {
    }

    public Competencesimple(Integer id, Competence idcommer) {
        this.id = id;
        this.idcommer = idcommer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        if (!(object instanceof Competencesimple)) {
            return false;
        }
        Competencesimple other = (Competencesimple) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "otherEntity.Competencesimple[ id=" + id + " ]";
    }
    
}
