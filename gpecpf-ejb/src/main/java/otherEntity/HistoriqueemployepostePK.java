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
import javax.validation.constraints.Size;

/**
 *
 * @author Dell
 */
@Embeddable
public class HistoriqueemployepostePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "idemploye")
    private String idemploye;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "idposte")
    private String idposte;

    public HistoriqueemployepostePK() {
    }

    public HistoriqueemployepostePK(String idemploye, String idposte) {
        this.idemploye = idemploye;
        this.idposte = idposte;
    }

    public String getIdemploye() {
        return idemploye;
    }

    public void setIdemploye(String idemploye) {
        this.idemploye = idemploye;
    }

    public String getIdposte() {
        return idposte;
    }

    public void setIdposte(String idposte) {
        this.idposte = idposte;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idemploye != null ? idemploye.hashCode() : 0);
        hash += (idposte != null ? idposte.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HistoriqueemployepostePK)) {
            return false;
        }
        HistoriqueemployepostePK other = (HistoriqueemployepostePK) object;
        if ((this.idemploye == null && other.idemploye != null) || (this.idemploye != null && !this.idemploye.equals(other.idemploye))) {
            return false;
        }
        if ((this.idposte == null && other.idposte != null) || (this.idposte != null && !this.idposte.equals(other.idposte))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "otherEntity.HistoriqueemployepostePK[ idemploye=" + idemploye + ", idposte=" + idposte + " ]";
    }
    
}
