/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package otherEntity;

import dz.elit.gpecpf.commun.util.StaticUtil;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Dell
 */
@Entity
@Table(name = "avis", schema = StaticUtil.ADMINISTRATION_SCHEMA)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Avis.findAll", query = "SELECT a FROM Avis a")
    , @NamedQuery(name = "Avis.findById", query = "SELECT a FROM Avis a WHERE a.id = :id")
    , @NamedQuery(name = "Avis.findByAvis", query = "SELECT a FROM Avis a WHERE a.avis = :avis")
    , @NamedQuery(name = "Avis.findByHote", query = "SELECT a FROM Avis a WHERE a.hote = :hote")
    , @NamedQuery(name = "Avis.findByDateavis", query = "SELECT a FROM Avis a WHERE a.dateavis = :dateavis")})
public class Avis implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 2147483647)
    @Column(name = "avis")
    private String avis;
    @Size(max = 255)
    @Column(name = "hote")
    private String hote;
    @Column(name = "dateavis")
    @Temporal(TemporalType.DATE)
    private Date dateavis;
    @JoinColumn(name = "idevaluation", referencedColumnName = "id")
    @ManyToOne
    private Evaluation idevaluation;

    public Avis() {
    }

    public Avis(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAvis() {
        return avis;
    }

    public void setAvis(String avis) {
        this.avis = avis;
    }

    public String getHote() {
        return hote;
    }

    public void setHote(String hote) {
        this.hote = hote;
    }

    public Date getDateavis() {
        return dateavis;
    }

    public void setDateavis(Date dateavis) {
        this.dateavis = dateavis;
    }

    public Evaluation getIdevaluation() {
        return idevaluation;
    }

    public void setIdevaluation(Evaluation idevaluation) {
        this.idevaluation = idevaluation;
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
        if (!(object instanceof Avis)) {
            return false;
        }
        Avis other = (Avis) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "otherEntity.Avis[ id=" + id + " ]";
    }
    
}
