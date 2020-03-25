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
public class NotecompetenceemployePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "idevaluation")
    private String idevaluation;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "idcompetence")
    private String idcompetence;

    public NotecompetenceemployePK() {
    }

    public NotecompetenceemployePK(String idevaluation, String idcompetence) {
        this.idevaluation = idevaluation;
        this.idcompetence = idcompetence;
    }

    public String getIdevaluation() {
        return idevaluation;
    }

    public void setIdevaluation(String idevaluation) {
        this.idevaluation = idevaluation;
    }

    public String getIdcompetence() {
        return idcompetence;
    }

    public void setIdcompetence(String idcompetence) {
        this.idcompetence = idcompetence;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idevaluation != null ? idevaluation.hashCode() : 0);
        hash += (idcompetence != null ? idcompetence.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotecompetenceemployePK)) {
            return false;
        }
        NotecompetenceemployePK other = (NotecompetenceemployePK) object;
        if ((this.idevaluation == null && other.idevaluation != null) || (this.idevaluation != null && !this.idevaluation.equals(other.idevaluation))) {
            return false;
        }
        if ((this.idcompetence == null && other.idcompetence != null) || (this.idcompetence != null && !this.idcompetence.equals(other.idcompetence))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "otherEntity.NotecompetenceemployePK[ idevaluation=" + idevaluation + ", idcompetence=" + idcompetence + " ]";
    }
    
}
