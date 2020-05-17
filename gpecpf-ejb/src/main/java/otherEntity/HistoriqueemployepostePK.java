/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package otherEntity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Kaci Ahmed
 */
@Embeddable
public class HistoriqueemployepostePK implements  Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "idemploye")
    private int idemploye;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idposte")
    private int idposte;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datedeb")
    @Temporal(TemporalType.DATE)
    private Date datedeb;

    public HistoriqueemployepostePK() {
    }

    public HistoriqueemployepostePK(int idemploye, int idposte, Date datedeb) {
        this.idemploye = idemploye;
        this.idposte = idposte;
        this.datedeb = datedeb;
    }

    public int getIdemploye() {
        return idemploye;
    }

    public void setIdemploye(int idemploye) {
        this.idemploye = idemploye;
    }

    public int getIdposte() {
        return idposte;
    }

    public void setIdposte(int idposte) {
        this.idposte = idposte;
    }

    public Date getDatedeb() {
        return datedeb;
    }

    public void setDatedeb(Date datedeb) {
        this.datedeb = datedeb;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idemploye;
        hash += (int) idposte;
        hash += (datedeb != null ? datedeb.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HistoriqueemployepostePK)) {
            return false;
        }
        HistoriqueemployepostePK other = (HistoriqueemployepostePK) object;
        if (this.idemploye != other.idemploye) {
            return false;
        }
        if (this.idposte != other.idposte) {
            return false;
        }
        if ((this.datedeb == null && other.datedeb != null) || (this.datedeb != null && !this.datedeb.equals(other.datedeb))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "otherEntity.HistoriqueemployepostePK[ idemploye=" + idemploye + ", idposte=" + idposte + ", datedeb=" + datedeb + " ]";
    }
    
}
