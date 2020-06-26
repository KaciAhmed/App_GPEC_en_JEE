package dz.elit.gpecpf.other.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Kaci Ahmed
 */
@Embeddable
public class NotecompetenceemployePK implements Serializable {

	@Basic(optional = false)
	@Column(name = "idevaluation")
	private int idevaluation;
	@Basic(optional = false)
	@NotNull
	@Column(name = "idcompetence")
	private int idcompetence;

	public NotecompetenceemployePK() {
	}

	public NotecompetenceemployePK(int idevaluation, int idcompetence) {
		this.idevaluation = idevaluation;
		this.idcompetence = idcompetence;
	}

	public int getIdevaluation() {
		return idevaluation;
	}

	public void setIdevaluation(int idevaluation) {
		this.idevaluation = idevaluation;
	}

	public int getIdcompetence() {
		return idcompetence;
	}

	public void setIdcompetence(int idcompetence) {
		this.idcompetence = idcompetence;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (int) idevaluation;
		hash += (int) idcompetence;
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof NotecompetenceemployePK)) {
			return false;
		}
		NotecompetenceemployePK other = (NotecompetenceemployePK) object;
		if (this.idevaluation != other.idevaluation) {
			return false;
		}
		if (this.idcompetence != other.idcompetence) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "otherEntity.NotecompetenceemployePK[ idevaluation=" + idevaluation + ", idcompetence=" + idcompetence + " ]";
	}

}
