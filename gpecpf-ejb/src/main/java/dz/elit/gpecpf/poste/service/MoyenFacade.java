package dz.elit.gpecpf.poste.service;

import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.StaticUtil;
import dz.elit.gpecpf.poste.entity.Moyen;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Nadir Ben Mohand
 */
@Stateless
public class MoyenFacade extends AbstractFacade<Moyen> {

	@PersistenceContext(unitName = StaticUtil.UNIT_NAME)
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public MoyenFacade() {
		super(Moyen.class);
	}

	@Override
	public void create(Moyen moyen) throws MyException, Exception {
		if (isCodeMoyenExiste(moyen)) {
			throw new MyException("Le code existe déjà");
		}
		super.create(moyen);
	}

	@Override
	public void edit(Moyen moyen) throws MyException, Exception {
		if (isCodeMoyenExiste(moyen)) {
			throw new MyException("Le code existe déjà");
		}
		super.edit(moyen);
	}

	private boolean isCodeMoyenExiste(Moyen moyen) {
		Query q = em.createNamedQuery("Moyen.findByCodeWithoutCurrentId");
		q.setParameter("code", moyen.getCode());
		q.setParameter("id", moyen.getId());
		return !q.getResultList().isEmpty();
	}

}
