package dz.elit.gpecpf.administration.service;

import dz.elit.gpecpf.administration.entity.AdminModule;
import dz.elit.gpecpf.administration.entity.AdminPrivilege;
import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.StaticUtil;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ayadi
 */
@Stateless
public class AdminPrivilegeFacade extends AbstractFacade<AdminPrivilege> {

	@PersistenceContext(unitName = StaticUtil.UNIT_NAME)
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public AdminPrivilegeFacade() {
		super(AdminPrivilege.class);
	}

	public List<AdminPrivilege> getListAdminPrivilege(String login) {
		Query q = em.createNamedQuery("AdminPrivilege.findByLogin");
		q.setParameter("login", login);
		return q.getResultList();
	}

	public List<AdminPrivilege> getListPrivilegeByModule(Integer idModule) {
		Query q = em.createNamedQuery("AdminPrivilege.findByModule");
		q.setParameter("idModule", idModule);
		return q.getResultList();
	}

	public List<AdminPrivilege> findByCodeDescModule(String code, String description, AdminModule module) {
		StringBuilder queryStringBuilder = new StringBuilder("SELECT a FROM AdminPrivilege AS a WHERE 1=1 ");
		if (code != null && !code.equals("")) {
			queryStringBuilder.append(" AND  a.code like :code ");
		}
		if (description != null && !description.equals("")) {
			queryStringBuilder.append(" AND  a.description like :description ");
		}
		if (module != null && module.getId() != null) {
			queryStringBuilder.append(" AND  a.adminModule  =:module ");
		}
		queryStringBuilder.append(" ORDER BY a.code ");

		Query q = em.createQuery(queryStringBuilder.toString());

		if (code != null && !code.equals("")) {
			q.setParameter("code", code + "%");
		}
		if (description != null && !description.equals("")) {
			q.setParameter("description", "%" + description + "%");
		}
		if (module != null && module.getId() != null) {
			q.setParameter("module", module);
		}

		return q.getResultList();
	}
}
