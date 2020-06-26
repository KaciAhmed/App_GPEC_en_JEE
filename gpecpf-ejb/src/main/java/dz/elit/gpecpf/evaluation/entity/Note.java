package dz.elit.gpecpf.evaluation.entity;

import dz.elit.gpecpf.commun.util.StaticUtil;
import dz.elit.gpecpf.competence.entity.Competence;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Nadir Ben Mohand
 */
@Entity
@Table(name = "note", schema = StaticUtil.EVALUATION_SCHEMA)
@NamedQueries({
	@NamedQuery(name = "Note.findByEvaluation", query = "SELECT n FROM Note n WHERE n.evaluation = :evaluation"),
	@NamedQuery(name = "Note.findByCompetenceEvaluation", query = "SELECT n FROM Note n WHERE n.evaluation = :evaluation and n.competence = :competence"),
})
public class Note implements Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	private NoteID id;

	@ManyToOne
	@MapsId("id_evaluation")
	private Evaluation evaluation;

	@ManyToOne
	@MapsId("id_competence")
	private Competence competence;

	@Column(name = "note_employe")
	private Integer noteEmploye;

	@Column(name = "note_superieure")
	private Integer noteSuperieure;

	public Note() {
	}
	
	public Note(Integer id_competence, Integer id_evaluation) {
		this.id = new NoteID(id_competence, id_evaluation);
	}

	public NoteID getId() {
		return id;
	}

	public void setId(NoteID id) {
		this.id = id;
	}

	public Evaluation getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(Evaluation evaluation) {
		this.evaluation = evaluation;
	}

	public Competence getCompetence() {
		return competence;
	}

	public void setCompetence(Competence competence) {
		this.competence = competence;
	}

	public Integer getNoteEmploye() {
		return noteEmploye;
	}

	public void setNoteEmploye(Integer noteEmploye) {
		this.noteEmploye = noteEmploye;
	}

	public Integer getNoteSuperieure() {
		return noteSuperieure;
	}

	public void setNoteSuperieure(Integer noteSuperieure) {
		this.noteSuperieure = noteSuperieure;
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
		if (!(object instanceof Note)) {
			return false;
		}
		Note other = (Note) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "dz.elit.gpecpf.evaluation.entity.Note[ id=" + id + " ]";
	}

}
