package dz.elit.gpecpf.evaluation.entity;

import java.util.Comparator;

/**
 *
 * @author Nadir Ben Mohand
 */
public class CandidatureCompetence implements Comparator<Candidature> {

	@Override
	public int compare(Candidature o1, Candidature o2) {
		return ((Integer) o1.getNbrComp()).compareTo(((Integer) o2.getNbrComp()));
	}

}
