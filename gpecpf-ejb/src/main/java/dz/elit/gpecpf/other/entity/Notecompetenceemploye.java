package dz.elit.gpecpf.other.entity;

import dz.elit.gpecpf.evaluation.entity.Evaluation;
import dz.elit.gpecpf.competence.entity.Competence;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Kaci Ahmed
 */
@Entity
@Table(name = "notecompetenceemploye")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Notecompetenceemploye.findAll", query = "SELECT n FROM Notecompetenceemploye n")
	, @NamedQuery(name = "Notecompetenceemploye.findByIdevaluation", query = "SELECT n FROM Notecompetenceemploye n WHERE n.notecompetenceemployePK.idevaluation = :idevaluation")
	, @NamedQuery(name = "Notecompetenceemploye.findByIdcompetence", query = "SELECT n FROM Notecompetenceemploye n WHERE n.notecompetenceemployePK.idcompetence = :idcompetence")
	, @NamedQuery(name = "Notecompetenceemploye.findByNoteemplye", query = "SELECT n FROM Notecompetenceemploye n WHERE n.noteemplye = :noteemplye")
	, @NamedQuery(name = "Notecompetenceemploye.findByNotehiern1", query = "SELECT n FROM Notecompetenceemploye n WHERE n.notehiern1 = :notehiern1")})
public class Notecompetenceemploye implements Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	protected NotecompetenceemployePK notecompetenceemployePK;
	// @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
	@Column(name = "noteemplye")
	private Float noteemplye;
	@Column(name = "notehiern1")
	private Float notehiern1;
	@JoinColumn(name = "idcompetence", referencedColumnName = "id", insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private Competence competence;
	@JoinColumn(name = "idevaluation", referencedColumnName = "id", insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private Evaluation evaluation;

	public Notecompetenceemploye() {
	}

	public Notecompetenceemploye(NotecompetenceemployePK notecompetenceemployePK) {
		this.notecompetenceemployePK = notecompetenceemployePK;
	}

	public Notecompetenceemploye(int idevaluation, int idcompetence) {
		this.notecompetenceemployePK = new NotecompetenceemployePK(idevaluation, idcompetence);
	}

	public NotecompetenceemployePK getNotecompetenceemployePK() {
		return notecompetenceemployePK;
	}

	public void setNotecompetenceemployePK(NotecompetenceemployePK notecompetenceemployePK) {
		this.notecompetenceemployePK = notecompetenceemployePK;
	}

	public Float getNoteemplye() {
		return noteemplye;
	}

	public void setNoteemplye(Float noteemplye) {
		this.noteemplye = noteemplye;
	}

	public Float getNotehiern1() {
		return notehiern1;
	}

	public void setNotehiern1(Float notehiern1) {
		this.notehiern1 = notehiern1;
	}

	public Competence getCompetence() {
		return competence;
	}

	public void setCompetence(Competence competence) {
		this.competence = competence;
	}

	public Evaluation getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(Evaluation evaluation) {
		this.evaluation = evaluation;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (notecompetenceemployePK != null ? notecompetenceemployePK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Notecompetenceemploye)) {
			return false;
		}
		Notecompetenceemploye other = (Notecompetenceemploye) object;
		if ((this.notecompetenceemployePK == null && other.notecompetenceemployePK != null) || (this.notecompetenceemployePK != null && !this.notecompetenceemployePK.equals(other.notecompetenceemployePK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "otherEntity.Notecompetenceemploye[ notecompetenceemployePK=" + notecompetenceemployePK + " ]";
	}

}
