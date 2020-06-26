package dz.elit.gpecpf.other.service;

import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.StaticUtil;
import dz.elit.gpecpf.other.entity.Wilaya;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Kaci Ahmed
 */
@Stateless
public class WilayaFacade extends AbstractFacade<Wilaya> {

	@PersistenceContext(unitName = StaticUtil.UNIT_NAME)
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public WilayaFacade() {
		super(Wilaya.class);
	}
}
