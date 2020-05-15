/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package otherEntity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Dell
 */
@Embeddable
public class HistoriqueemployepostePK implements Serializable {

    @Basic(optional = false)
    @Column(name = "idemploye")
    private int idemploye;
    @Basic(optional = false)
    @Column(name = "idposte")
    private int idposte;

    public HistoriqueemployepostePK() {
    }

    public HistoriqueemployepostePK(int idemploye, int idposte) {
        this.idemploye = idemploye;
        this.idposte = idposte;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idemploye;
        hash += (int) idposte;
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
        return true;
    }

    @Override
    public String toString() {
        return "otherEntity.HistoriqueemployepostePK[ idemploye=" + idemploye + ", idposte=" + idposte + " ]";
    }
    
}
