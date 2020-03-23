package dz.elit.gpecpf.administration.entity;

import dz.elit.gpecpf.commun.util.StaticUtil;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author chekor.samir
 */
@Entity
@Table(name = "admin_connecxion_historique", schema = StaticUtil.ADMINISTRATION_SCHEMA)
public class AdminConnecxionHistorique implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //  @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "utilisateur")
    private String utilisateur;
    @Column(name = "date_connexion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateConnexion;
    @Column(name = "date_deconnexion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDeconnexion;
    @Column(name = "adresse_ip")
    private String adresseIp;
    
    @Column(name = "idunique")
    private String idunique;
    
    public AdminConnecxionHistorique() {
    }

    public AdminConnecxionHistorique(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Date getDateConnexion() {
        return dateConnexion;
    }

    public void setDateConnexion(Date dateConnexion) {
        this.dateConnexion = dateConnexion;
    }

    public Date getDateDeconnexion() {
        return dateDeconnexion;
    }

    public void setDateDeconnexion(Date dateDeconnexion) {
        this.dateDeconnexion = dateDeconnexion;
    }

    public String getAdresseIp() {
        return adresseIp;
    }

    public void setAdresseIp(String adresseIp) {
        this.adresseIp = adresseIp;
    }

    public String getIdunique() {
        return idunique;
    }

    public void setIdunique(String idunique) {
        this.idunique = idunique;
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
        if (!(object instanceof AdminConnecxionHistorique)) {
            return false;
        }
        AdminConnecxionHistorique other = (AdminConnecxionHistorique) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dz.elit.gpecpf.administration.entity.AdminHistorique[ id=" + id + " ]";
    }

}
