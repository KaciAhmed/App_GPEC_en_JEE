package dz.elit.gpecpf.poste.service;

import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.StaticUtil;
import dz.elit.gpecpf.poste.entity.Activite;
import dz.elit.gpecpf.poste.entity.Tache;
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
public class TacheFacade extends AbstractFacade<Tache> {

	@PersistenceContext(unitName = StaticUtil.UNIT_NAME)
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public TacheFacade() {
		super(Tache.class);
	}

	@Override
	public void create(Tache tache) throws MyException, Exception {
		if (isCodeTacheExiste2(tache)) {
			throw new MyException("Le code existe déjà");
		}
		super.create(tache);
	}

	public void edit(Tache tache) throws MyException, Exception {
		if (isCodeTacheExiste(tache)) {
			throw new MyException("Le code existe déjà");
		}
		super.edit(tache);
	}

	@Override
	public void remove(Tache tache) throws MyException, Exception {
		if (isTacheAffectForActivite(tache)) {
			throw new MyException("Il y a des activités ayant cette tache, vous ne peuvez pas la supprimer ");
		}
		super.remove(tache);
	}

	private boolean isCodeTacheExiste(Tache tache) {
		Query q = em.createNamedQuery("Tache.findByCodeWithoutCurrentId");
		q.setParameter("code", tache.getCode());
		q.setParameter("id", tache.getId());
		return !q.getResultList().isEmpty();
	}
	
	private boolean isCodeTacheExiste2(Tache tache) {
		Query q = em.createNamedQuery("Tache.findByCode");
		q.setParameter("code", tache.getCode());
		return !q.getResultList().isEmpty();
	}

	private boolean isTacheAffectForActivite(Tache tache) {
		Query q = em.createNamedQuery("Activite.findByTache");
		q.setParameter("tache", tache);
		List<Activite> list = q.getResultList();
		return !list.isEmpty() && list.size() >= 1;
	}

}
