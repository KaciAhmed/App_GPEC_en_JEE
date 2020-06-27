package dz.elit.gpecpf.poste.service;

import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.StaticUtil;
import dz.elit.gpecpf.poste.entity.Activite;
import dz.elit.gpecpf.poste.entity.Mission;
import dz.elit.gpecpf.poste.entity.Tache;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Nadir Ben Mohand
 */
@Stateless
public class ActiviteFacade extends AbstractFacade<Activite> {

	@PersistenceContext(unitName = StaticUtil.UNIT_NAME)
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public ActiviteFacade() {
		super(Activite.class);
	}

	@Override
	public void create(Activite activite) throws MyException, Exception {
		if (isCodeActiviteExiste2(activite)) {
			throw new MyException("Le code existe déjà");
		}
		if (isLibelleActiviteExiste2(activite)) {
			throw new MyException("La libelle existe déjà");
		}
		super.create(activite);
	}

	public void edit(Activite activite) throws MyException, Exception {
		if (isCodeActiviteExiste(activite)) {
			throw new MyException("Le code existe déjà");
		}
		if (isLibelleActiviteExiste(activite)) {
			throw new MyException("La libelle existe déjà");
		}
		super.edit(activite);
	}

	@Override
	public void remove(Activite activite) throws MyException, Exception {
		if (isActiviteAffectForMission(activite)) {
			throw new MyException("Il y a des activités ayant cette tache, vous ne peuvez pas la supprimer ");
		}
		super.remove(activite);
	}

	public void editTache(Tache tache, List<Activite> activitesAdd, List<Activite> activitesRemove) throws Exception {
		for (Activite activite : activitesAdd) {
			activite.addTache(tache);
			edit(activite);
		}
		for (Activite activite : activitesRemove) {
			activite.removeTache(tache);
			edit(activite);
		}
	}

	private boolean isCodeActiviteExiste(Activite activite) {
		Query q = em.createNamedQuery("Activite.findByCodeWithoutCurrentId");
		q.setParameter("code", activite.getCode());
		q.setParameter("id", activite.getId());
		return !q.getResultList().isEmpty();
	}
	
	private boolean isCodeActiviteExiste2(Activite activite) {
		Query q = em.createNamedQuery("Activite.findByCode");
		q.setParameter("code", activite.getCode());
		return !q.getResultList().isEmpty();
	}
	
	private boolean isLibelleActiviteExiste(Activite activite) {
		Query q = em.createNamedQuery("Activite.findByLibelleWithoutCurrentId");
		q.setParameter("libelle", activite.getLibelle());
		q.setParameter("id", activite.getId());
		return !q.getResultList().isEmpty();
	}
	
	private boolean isLibelleActiviteExiste2(Activite activite) {
		Query q = em.createNamedQuery("Activite.findByLibelle");
		q.setParameter("libelle", activite.getLibelle());
		return !q.getResultList().isEmpty();
	}

	private boolean isActiviteAffectForMission(Activite activite) {
		Query q = em.createNamedQuery("Activite.findByActivite");
		q.setParameter("activite", activite);
		List<Mission> list = q.getResultList();
		return !list.isEmpty() && list.size() >= 1;
	}

	public List<Activite> activitesForTache(Tache tache) {
		Query q = em.createNamedQuery("Activite.findByTache");
		q.setParameter("tache", tache);
		return q.getResultList();
	}
}
