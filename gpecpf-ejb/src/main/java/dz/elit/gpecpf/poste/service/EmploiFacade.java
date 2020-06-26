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
		if (isCodeEmploiExiste(emploi)) {
			throw new MyException("Le code existe déjà");
		}
		super.create(emploi);
	}

	@Override
	public void edit(Emploi emploi) throws MyException, Exception {
		if (isCodeEmploiExiste(emploi)) {
			throw new MyException("Le code existe déjà");
		}
		super.edit(emploi);
	}

	private boolean isCodeEmploiExiste(Emploi emploi) {
		Query q = em.createNamedQuery("Emploi.findByCodeWithoutCurrentId");
		q.setParameter("code", emploi.getCode());
		q.setParameter("id", emploi.getId());
		return !q.getResultList().isEmpty();
	}
}
