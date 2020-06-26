package dz.elit.gpecpf.evaluation.entity;

import java.io.Serializable;
import javax.persistence.Embeddable;

/**
 *
 * @author Nadir Ben Mohand
 */
@Embeddable
public class NoteID implements Serializable {

	private Integer id_competence;
	private Integer id_evaluation;

	public NoteID() {
	}

	public NoteID(Integer id_competence, Integer id_evaluation) {
		this.id_competence = id_competence;
		this.id_evaluation = id_evaluation;
	}

	public Integer getCompetence() {
		return id_competence;
	}

	public void setCompetence(Integer id_competence) {
		this.id_competence = id_competence;
	}

	public Integer getEvaluation() {
		return id_evaluation;
	}

	public void setEvaluation(Integer id_evaluation) {
		this.id_evaluation = id_evaluation;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id_competence != null ? id_competence.hashCode() : 0);
		hash += (id_evaluation != null ? id_evaluation.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof NoteID)) {
			return false;
		}
		NoteID other = (NoteID) object;
		if (((this.id_competence == null && other.id_competence != null) || (this.id_competence != null && !this.id_competence.equals(other.id_competence))) && ((this.id_evaluation == null && other.id_evaluation != null) || (this.id_evaluation != null && !this.id_evaluation.equals(other.id_evaluation)))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "dz.elit.gpecpf.evaluation.entity.NoteID[ id=" + (id_competence + id_evaluation) + " ]";
	}

}
