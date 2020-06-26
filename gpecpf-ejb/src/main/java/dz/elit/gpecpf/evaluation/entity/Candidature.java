package dz.elit.gpecpf.evaluation.entity;

import dz.elit.gpecpf.competence.entity.Competence;
import java.util.List;

/**
 *
 * @author Nadir Ben Mohand
 */
public class Candidature implements Comparable<Candidature> {

	private Evaluation evaluation;
	private int nbrComp;
	private int noteTotal;

	public Candidature(Evaluation evaluation, List<Note> mine, List<Competence> competencesNecessaires) {
		this.evaluation = evaluation;
		this.nbrComp = 0;
		this.noteTotal = 0;
		checkCompetences(mine, competencesNecessaires);
	}

	private void checkCompetences(List<Note> mine, List<Competence> needed) {
		for (Competence competence : needed) {
			for (Note note : mine) {
				if (competence.equals(note.getCompetence())) {
					int x = note.getNoteSuperieure();
					if (x > 0) {
						this.nbrComp++;
						this.noteTotal += x;
					}
					break;
				}
			}
		}
	}

	public Evaluation getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(Evaluation evaluation) {
		this.evaluation = evaluation;
	}

	public int getNbrComp() {
		return nbrComp;
	}

	public void setNbrComp(int nbrComp) {
		this.nbrComp = nbrComp;
	}

	public int getNoteTotal() {
		return noteTotal;
	}

	public void setNoteTotal(int noteTotal) {
		this.noteTotal = noteTotal;
	}

	@Override
	public int compareTo(Candidature o) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
