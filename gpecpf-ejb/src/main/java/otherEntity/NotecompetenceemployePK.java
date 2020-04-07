/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package otherEntity;

import java.io.Serializable;
import java.util.Objects;
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
    @Column(name = "idevaluation")
    private Integer idevaluation;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idcompetence")
    private Integer  idcompetence;

    public NotecompetenceemployePK() {
    }

    public NotecompetenceemployePK(Integer idevaluation, Integer  idcompetence) {
        this.idevaluation = idevaluation;
        this.idcompetence = idcompetence;
    }

    public Integer getIdevaluation() {
        return idevaluation;
    }

    public void setIdevaluation(Integer idevaluation) {
        this.idevaluation = idevaluation;
    }

    public Integer getIdcompetence() {
        return idcompetence;
    }

    public void setIdcompetence(Integer idcompetence) {
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
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NotecompetenceemployePK other = (NotecompetenceemployePK) obj;
        if (!Objects.equals(this.idevaluation, other.idevaluation)) {
            return false;
        }
        if (!Objects.equals(this.idcompetence, other.idcompetence)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "otherEntity.NotecompetenceemployePK[ idevaluation=" + idevaluation + ", idcompetence=" + idcompetence + " ]";
    }

 
    
}
