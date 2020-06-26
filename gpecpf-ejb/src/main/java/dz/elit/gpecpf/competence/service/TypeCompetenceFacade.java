package dz.elit.gpecpf.competence.service;

import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.CustomQueryRedirectors;
import dz.elit.gpecpf.commun.util.StaticUtil;
import dz.elit.gpecpf.competence.entity.Typecompetence;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.eclipse.persistence.jpa.JpaHelper;

/**
 *
 * @author Dell
 */
@Stateless
public class TypeCompetenceFacade extends AbstractFacade<Typecompetence> {

	@PersistenceContext(unitName = StaticUtil.UNIT_NAME)
	private EntityManager em;

	public TypeCompetenceFacade() {
		super(Typecompetence.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public Typecompetence findByCode(String code) {
		Query query = em.createNamedQuery("Typecompetence.findByCode");
		query.setParameter("code", code);
		List<Typecompetence> list = query.getResultList();
		return list.isEmpty() ? null : list.get(0);
	}

	public List<Typecompetence> findByCodeLibelle(String code, String libelle) {
		StringBuilder queryStringBuilder = new StringBuilder("SELECT a FROM Typecompetence AS a WHERE 1=1 ");
		if (code != null && !code.equals("")) {
			queryStringBuilder.append(" AND  a.code like :code ");
		}
		if (libelle != null && !libelle.equals("")) {
			queryStringBuilder.append(" AND  a.libelle like :libelle ");
		}
		queryStringBuilder.append(" ORDER BY a.code ");

		Query q = em.createQuery(queryStringBuilder.toString());

		if (code != null && !code.equals("")) {
			q.setParameter("code", "%" + code + "%");
		}
		if (libelle != null && !libelle.equals("")) {
			q.setParameter("libelle", "%" + libelle + "%");
		}
		//Implémentation de visibilité
		JpaHelper.getDatabaseQuery(q).setRedirector(new CustomQueryRedirectors());

		return q.getResultList();
	}

}
