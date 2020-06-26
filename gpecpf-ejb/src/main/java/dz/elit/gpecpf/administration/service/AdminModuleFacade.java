package dz.elit.gpecpf.administration.service;

import dz.elit.gpecpf.administration.entity.AdminModule;
import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.StaticUtil;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author leghettas.rabah
 */
@Stateless
public class AdminModuleFacade extends AbstractFacade<AdminModule> {

	@PersistenceContext(unitName = StaticUtil.UNIT_NAME)
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public AdminModuleFacade() {
		super(AdminModule.class);
	}

	public List<AdminModule> getListModule(String login) {
		Query q = em.createNamedQuery("AdminPrivilege.findListModuleByLogin");
		q.setParameter("login", login);
		return q.getResultList();
	}

	public void moveToTop(AdminModule module) throws Exception {
		AdminModule moduleTop = getModule(module, -1);
		moduleTop.setOrdre(module.getOrdre());
		module.setOrdre(module.getOrdre() - 1);
		edit(module);
		edit(moduleTop);
	}

	public void moveToBottom(AdminModule module) throws Exception {
		AdminModule moduleBottom = getModule(module, 1);
		moduleBottom.setOrdre(module.getOrdre());
		module.setOrdre(module.getOrdre() + 1);
		edit(module);
		edit(moduleBottom);
	}

	private AdminModule getModule(AdminModule module, int type) {
		Query q = em.createNamedQuery("AdminModule.findByOrdre");
		q.setParameter("ordre", module.getOrdre() + type);
		return q.getResultList().isEmpty() ? null : (AdminModule) q.getResultList().get(0);
	}
}
