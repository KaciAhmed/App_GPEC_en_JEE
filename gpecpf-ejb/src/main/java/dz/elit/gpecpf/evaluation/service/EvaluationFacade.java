package dz.elit.gpecpf.evaluation.service;

import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.StaticUtil;
import dz.elit.gpecpf.compagne_evaluation.entity.Compagneevaluation;
import dz.elit.gpecpf.employe.entity.Employe;
import dz.elit.gpecpf.evaluation.entity.Evaluation;
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
public class EvaluationFacade extends AbstractFacade<Evaluation> {

	@PersistenceContext(unitName = StaticUtil.UNIT_NAME)
	private EntityManager em;

	public EvaluationFacade() {
		super(Evaluation.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public List<Evaluation> evaluationsForCompagne(Compagneevaluation compagne) {
		Query q = em.createNamedQuery("Evaluation.findByCompagne");
		q.setParameter("compagne", compagne);
		return q.getResultList();
	}

	public List<Evaluation> evaluationsForEmploye(Employe employe) {
		Query q = em.createNamedQuery("Evaluation.findByEmploye");
		q.setParameter("employe", employe);
		return q.getResultList();
	}

	public Evaluation evaluationCompagneEmploye(Compagneevaluation compagne, Employe employe) {
		Query q = em.createNamedQuery("Evaluation.findByCompagneEmploye");
		q.setParameter("compagne", compagne);
		q.setParameter("employe", employe);
		List<Evaluation> res = q.getResultList();
		if (!res.isEmpty()) {
			return res.get(0);
		}
		return null;
	}

	public List<Evaluation> evaluationsForSupN1(Employe employe) {
		Query q = em.createNamedQuery("Evaluation.findBySupN1");
		q.setParameter("supN1", employe);
		return q.getResultList();
	}

	public List<Evaluation> evaluationCompagneSupN1(Compagneevaluation compagne, Employe employe) {
		Query q = em.createNamedQuery("Evaluation.findByCompagneSupN1");
		q.setParameter("compagne", compagne);
		q.setParameter("supN1", employe);
		return q.getResultList();
	}

	public List<Evaluation> evaluationsForSupN2(Employe employe) {
		Query q = em.createNamedQuery("Evaluation.findBySupN2");
		q.setParameter("supN2", employe);
		return q.getResultList();
	}

	public List<Evaluation> evaluationCompagneSupN2(Compagneevaluation compagne, Employe employe) {
		Query q = em.createNamedQuery("Evaluation.findByCompagneSupN2");
		q.setParameter("compagne", compagne);
		q.setParameter("supN2", employe);
		return q.getResultList();
	}

	public List<Evaluation> evaluationsForSupN3(Employe employe) {
		Query q = em.createNamedQuery("Evaluation.findBySupN3");
		q.setParameter("supN3", employe);
		return q.getResultList();
	}

	public List<Evaluation> evaluationCompagneSupN3(Compagneevaluation compagne, Employe employe) {
		Query q = em.createNamedQuery("Evaluation.findByCompagneSupN3");
		q.setParameter("compagne", compagne);
		q.setParameter("supN3", employe);
		return q.getResultList();
	}
	
	public List<Evaluation> evaluationEndForCompagne(Compagneevaluation compagne) {
		Query q = em.createNamedQuery("Evaluation.findByEvaluatedFinish");
		q.setParameter("compagne", compagne);
		return q.getResultList();
	}
}
