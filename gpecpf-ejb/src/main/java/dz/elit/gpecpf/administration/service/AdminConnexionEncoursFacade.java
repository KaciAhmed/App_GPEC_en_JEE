package dz.elit.gpecpf.administration.service;

import dz.elit.gpecpf.administration.entity.AdminConnexionEncours;
import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.StaticUtil;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author laidani.youcef
 */
@Stateless
public class AdminConnexionEncoursFacade extends AbstractFacade<AdminConnexionEncours> {

	@PersistenceContext(unitName = StaticUtil.UNIT_NAME)
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public AdminConnexionEncoursFacade() {
		super(AdminConnexionEncours.class);
	}

}
