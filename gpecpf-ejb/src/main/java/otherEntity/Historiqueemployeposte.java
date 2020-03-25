/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package otherEntity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Dell
 */
@Entity
@Table(name = "historiqueemployeposte")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Historiqueemployeposte.findAll", query = "SELECT h FROM Historiqueemployeposte h")
    , @NamedQuery(name = "Historiqueemployeposte.findByIdemploye", query = "SELECT h FROM Historiqueemployeposte h WHERE h.historiqueemployepostePK.idemploye = :idemploye")
    , @NamedQuery(name = "Historiqueemployeposte.findByIdposte", query = "SELECT h FROM Historiqueemployeposte h WHERE h.historiqueemployepostePK.idposte = :idposte")
    , @NamedQuery(name = "Historiqueemployeposte.findByDatedeb", query = "SELECT h FROM Historiqueemployeposte h WHERE h.datedeb = :datedeb")
    , @NamedQuery(name = "Historiqueemployeposte.findByDatefin", query = "SELECT h FROM Historiqueemployeposte h WHERE h.datefin = :datefin")})
public class Historiqueemployeposte implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HistoriqueemployepostePK historiqueemployepostePK;
    @Column(name = "datedeb")
    @Temporal(TemporalType.DATE)
    private Date datedeb;
    @Column(name = "datefin")
    @Temporal(TemporalType.DATE)
    private Date datefin;
    @JoinColumn(name = "idemploye", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Employe employe;
    @JoinColumn(name = "idposte", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Poste poste;

    public Historiqueemployeposte() {
    }

    public Historiqueemployeposte(HistoriqueemployepostePK historiqueemployepostePK) {
        this.historiqueemployepostePK = historiqueemployepostePK;
    }

    public Historiqueemployeposte(String idemploye, String idposte) {
        this.historiqueemployepostePK = new HistoriqueemployepostePK(idemploye, idposte);
    }

    public HistoriqueemployepostePK getHistoriqueemployepostePK() {
        return historiqueemployepostePK;
    }

    public void setHistoriqueemployepostePK(HistoriqueemployepostePK historiqueemployepostePK) {
        this.historiqueemployepostePK = historiqueemployepostePK;
    }

    public Date getDatedeb() {
        return datedeb;
    }

    public void setDatedeb(Date datedeb) {
        this.datedeb = datedeb;
    }

    public Date getDatefin() {
        return datefin;
    }

    public void setDatefin(Date datefin) {
        this.datefin = datefin;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public Poste getPoste() {
        return poste;
    }

    public void setPoste(Poste poste) {
        this.poste = poste;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (historiqueemployepostePK != null ? historiqueemployepostePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Historiqueemployeposte)) {
            return false;
        }
        Historiqueemployeposte other = (Historiqueemployeposte) object;
        if ((this.historiqueemployepostePK == null && other.historiqueemployepostePK != null) || (this.historiqueemployepostePK != null && !this.historiqueemployepostePK.equals(other.historiqueemployepostePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "otherEntity.Historiqueemployeposte[ historiqueemployepostePK=" + historiqueemployepostePK + " ]";
    }
    
}
