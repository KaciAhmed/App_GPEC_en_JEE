package dz.elit.gpecpf.evaluation.service;

import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.StaticUtil;
import dz.elit.gpecpf.competence.entity.Competence;
import dz.elit.gpecpf.evaluation.entity.Evaluation;
import dz.elit.gpecpf.evaluation.entity.Note;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Nadir Ben Mohand
 */
@Stateless
public class NoteFacade extends AbstractFacade<Note> {

	@PersistenceContext(unitName = StaticUtil.UNIT_NAME)
	private EntityManager em;

	public NoteFacade() {
		super(Note.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public List<Note> notesForEvaluation(Evaluation evaluation) {
		Query q = em.createNamedQuery("Note.findByEvaluation");
		q.setParameter("evaluation", evaluation);
		return q.getResultList();
	}

	public Note noteForCompetenceEvaluation(Competence competence, Evaluation evaluation) {
		Query q = em.createNamedQuery("Note.findByCompetenceEvaluation");
		q.setParameter("evaluation", evaluation);
		q.setParameter("competence", competence);
		return (Note) ((q.getResultList().isEmpty()) ? null : q.getResultList().get(0));
	}
}
