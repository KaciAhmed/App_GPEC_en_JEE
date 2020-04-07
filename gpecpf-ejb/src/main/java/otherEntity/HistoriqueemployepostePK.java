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
    @Column(name = "idemploye")
    private Integer idemploye;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idposte")
    private Integer idposte;

    public HistoriqueemployepostePK() {
    }

    public HistoriqueemployepostePK(Integer idemploye, Integer idposte) {
        this.idemploye = idemploye;
        this.idposte = idposte;
    }

    public Integer getIdemploye() {
        return idemploye;
    }

    public void setIdemploye(Integer idemploye) {
        this.idemploye = idemploye;
    }

    public Integer getIdposte() {
        return idposte;
    }

    public void setIdposte(Integer idposte) {
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
