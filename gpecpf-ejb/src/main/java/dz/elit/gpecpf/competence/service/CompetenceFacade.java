package dz.elit.gpecpf.competence.service;

import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.CustomQueryRedirectors;
import dz.elit.gpecpf.commun.util.StaticUtil;
import dz.elit.gpecpf.competence.entity.Competence;
import dz.elit.gpecpf.competence.entity.Comportement;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.eclipse.persistence.jpa.JpaHelper;

/**
 *
 * @author Kaci Ahmed
 */
@Stateless
public class CompetenceFacade extends AbstractFacade<Competence> {

	@PersistenceContext(unitName = StaticUtil.UNIT_NAME)
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public CompetenceFacade() {
		super(Competence.class);
	}

	public Competence findByCode(String code) {
		Query query = em.createNamedQuery("Competence.findByCode");
		query.setParameter("code", code);
		List<Competence> list = query.getResultList();
		return list.isEmpty() ? null : list.get(0);
	}

	public List<Competence> findByCodeLibelleDescription(String code, String libelle, String description) {
		StringBuilder queryStringBuilder = new StringBuilder("SELECT a FROM Competence AS a WHERE 1=1 ");
		if (code != null && !code.equals("")) {
			queryStringBuilder.append(" AND  a.code like :code ");
		}
		if (libelle != null && !libelle.equals("")) {
			queryStringBuilder.append(" AND  a.libelle like :libelle ");
		}
		if (description != null && !description.equals("")) {
			queryStringBuilder.append(" AND  a.description like :description ");
		}
		queryStringBuilder.append(" ORDER BY a.code ");

		Query q = em.createQuery(queryStringBuilder.toString());

		if (code != null && !code.equals("")) {
			q.setParameter("code", "%" + code + "%");
		}
		if (libelle != null && !libelle.equals("")) {
			q.setParameter("libelle", "%" + libelle + "%");
		}
		if (description != null && !description.equals("")) {
			q.setParameter("description", "%" + description + "%");
		}

		//Implémentation de visibilité
		//  JpaHelper.getDatabaseQuery(q).setRedirector(new CustomQueryRedirectors());
		return q.getResultList();
	}

	public List<Competence> findByCodeLibelle(String code, String libelle) {
		StringBuilder queryStringBuilder = new StringBuilder("SELECT a FROM Competence AS a WHERE 1=1 ");
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

	private boolean isExisteCode(String code) {
		Competence Comp = findByCode(code);
		if (Comp == null) {
			return false;
		} else {
			return true;
		}
	}

	public void editComportemnt(Comportement comportement, List<Competence> competencesAdd, List<Competence> competencesRemove) throws Exception {
		for (Competence competence : competencesAdd) {
			competence.addComprtement(comportement);
			edit(competence);
		}
		for (Competence competence : competencesRemove) {
			competence.removeComportement(comportement);
			edit(competence);
		}
	}

	public List<Competence> competenceForComportement(Comportement comportement) {
		Query q = em.createNamedQuery("Competence.findByComportement");
		q.setParameter("comportement", comportement);
		return q.getResultList();
	}

	@Override
	public void create(Competence Comp) throws MyException, Exception {
		if (isExisteCode(Comp.getCode())) {
			throw new MyException("Le code " + Comp.getCode() + " existe déjà ");
		} else {
			super.create(Comp);
		}
	}

	public List<Competence> findAllOrderByUnite() {
		Query query = em.createNamedQuery("Competence.findAll");
		JpaHelper.getDatabaseQuery(query).setRedirector(new CustomQueryRedirectors());
		//query.setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH);
		return query.getResultList();
	}

}
