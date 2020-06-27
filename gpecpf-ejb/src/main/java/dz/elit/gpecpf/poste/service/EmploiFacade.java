package dz.elit.gpecpf.poste.service;

import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.StaticUtil;
import dz.elit.gpecpf.poste.entity.Emploi;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Nadir Ben Mohand
 */
@Stateless
public class EmploiFacade extends AbstractFacade<Emploi> {

	@PersistenceContext(unitName = StaticUtil.UNIT_NAME)
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public EmploiFacade() {
		super(Emploi.class);
	}

	@Override
	public void create(Emploi emploi) throws MyException, Exception {
		if (isCodeEmploiExiste2(emploi)) {
			throw new MyException("Le code existe déjà");
		}
		if (isLibelleEmploiExiste2(emploi)) {
			throw new MyException("La libellé existe déjà");
		}
		super.create(emploi);
	}

	@Override
	public void edit(Emploi emploi) throws MyException, Exception {
		if (isCodeEmploiExiste(emploi)) {
			throw new MyException("Le code existe déjà");
		}
		if (isLibelleEmploiExiste(emploi)) {
			throw new MyException("La libellé existe déjà");
		}
		super.edit(emploi);
	}

	private boolean isCodeEmploiExiste(Emploi emploi) {
		Query q = em.createNamedQuery("Emploi.findByCodeWithoutCurrentId");
		q.setParameter("code", emploi.getCode());
		q.setParameter("id", emploi.getId());
		return !q.getResultList().isEmpty();
	}

	private boolean isCodeEmploiExiste2(Emploi emploi) {
		Query q = em.createNamedQuery("Emploi.findByCode");
		q.setParameter("code", emploi.getCode());
		return !q.getResultList().isEmpty();
	}

	private boolean isLibelleEmploiExiste(Emploi emploi) {
		Query q = em.createNamedQuery("Emploi.findByLibelleWithoutCurrentId");
		q.setParameter("libelle", emploi.getLibelle());
		q.setParameter("id", emploi.getId());
		return !q.getResultList().isEmpty();
	}

	private boolean isLibelleEmploiExiste2(Emploi emploi) {
		Query q = em.createNamedQuery("Emploi.findByLibelle");
		q.setParameter("libelle", emploi.getLibelle());
		return !q.getResultList().isEmpty();
	}
}
