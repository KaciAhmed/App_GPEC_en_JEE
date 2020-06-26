package dz.elit.gpecpf.administration.service;

import dz.elit.gpecpf.administration.entity.Prefixcodification;
import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.StaticUtil;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Kaci Ahmed
 */
@Stateless
public class AdminPrefixCodificationFacade extends AbstractFacade<Prefixcodification> {

	@PersistenceContext(unitName = StaticUtil.UNIT_NAME)
	private EntityManager em;

	@EJB
	private AdminPrefixCodificationFacade prefixFacade;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public AdminPrefixCodificationFacade() {
		super(Prefixcodification.class);
	}

	public Prefixcodification chercherPrefix() {
		List<Prefixcodification> listPrefix = new ArrayList<>();
		listPrefix = prefixFacade.findAllOrderByAttribut("id");
		if (!listPrefix.isEmpty()) {
			return listPrefix.get(0);
		}
		return new Prefixcodification();
	}

}
