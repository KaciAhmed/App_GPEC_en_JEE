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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
@Table(name = "poste",schema = StaticUtil.ADMINISTRATION_SCHEMA)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Poste.findAll", query = "SELECT p FROM Poste p")
    , @NamedQuery(name = "Poste.findById", query = "SELECT p FROM Poste p WHERE p.id = :id")
    , @NamedQuery(name = "Poste.findByCode", query = "SELECT p FROM Poste p WHERE p.code = :code")
    , @NamedQuery(name = "Poste.findByLibelle", query = "SELECT p FROM Poste p WHERE p.libelle = :libelle")
    , @NamedQuery(name = "Poste.findByDescription", query = "SELECT p FROM Poste p WHERE p.description = :description")
    , @NamedQuery(name = "Poste.findByTelburaux", query = "SELECT p FROM Poste p WHERE p.telburaux = :telburaux")})
public class Poste implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "id")
    private String id;
    @Size(max = 50)
    @Column(name = "code")
    private String code;
    @Size(max = 255)
    @Column(name = "libelle")
    private String libelle;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Size(max = 20)
    @Column(name = "telburaux")
    private String telburaux;
    @ManyToMany(mappedBy = "posteCollection")
    private Collection<Mission> missionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "poste")
    private Collection<Historiqueemployeposte> historiqueemployeposteCollection;
    @JoinColumn(name = "idemploi", referencedColumnName = "id")
    @ManyToOne
    private Emploi idemploi;
    @JoinColumn(name = "idtypeposte", referencedColumnName = "id")
    @ManyToOne
    private Typeposte idtypeposte;

    public Poste() {
    }

    public Poste(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getTelburaux() {
        return telburaux;
    }

    public void setTelburaux(String telburaux) {
        this.telburaux = telburaux;
    }

    @XmlTransient
    public Collection<Mission> getMissionCollection() {
        return missionCollection;
    }

    public void setMissionCollection(Collection<Mission> missionCollection) {
        this.missionCollection = missionCollection;
    }

    @XmlTransient
    public Collection<Historiqueemployeposte> getHistoriqueemployeposteCollection() {
        return historiqueemployeposteCollection;
    }

    public void setHistoriqueemployeposteCollection(Collection<Historiqueemployeposte> historiqueemployeposteCollection) {
        this.historiqueemployeposteCollection = historiqueemployeposteCollection;
    }

    public Emploi getIdemploi() {
        return idemploi;
    }

    public void setIdemploi(Emploi idemploi) {
        this.idemploi = idemploi;
    }

    public Typeposte getIdtypeposte() {
        return idtypeposte;
    }

    public void setIdtypeposte(Typeposte idtypeposte) {
        this.idtypeposte = idtypeposte;
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
        if (!(object instanceof Poste)) {
            return false;
        }
        Poste other = (Poste) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "otherEntity.Poste[ id=" + id + " ]";
    }
    
}
