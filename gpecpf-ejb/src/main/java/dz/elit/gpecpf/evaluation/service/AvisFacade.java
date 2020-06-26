package dz.elit.gpecpf.evaluation.service;

import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.StaticUtil;
import dz.elit.gpecpf.evaluation.entity.Evaluation;
import dz.elit.gpecpf.evaluation.entity.Avis;
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
public class AvisFacade extends AbstractFacade<Avis> {

	@PersistenceContext(unitName = StaticUtil.UNIT_NAME)
	private EntityManager em;

	public AvisFacade() {
		super(Avis.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public List<Avis> avisForEvaluation(Evaluation evaluation) {
		Query q = em.createNamedQuery("Avis.findByEvaluation");
		q.setParameter("evaluation", evaluation);
		return q.getResultList();
	}
}
