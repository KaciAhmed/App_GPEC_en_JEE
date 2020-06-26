package dz.elit.gpecpf.poste.service;

import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.StaticUtil;
import dz.elit.gpecpf.poste.entity.Activite;
import dz.elit.gpecpf.poste.entity.Mission;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Nadir Ben Mohand
 */
@Stateless
public class MissionFacade extends AbstractFacade<Mission> {

	@PersistenceContext(unitName = StaticUtil.UNIT_NAME)
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@EJB
	private ActiviteFacade activiteFacade;

	public MissionFacade() {
		super(Mission.class);
	}

	@Override
	public void create(Mission mission) throws MyException, Exception {
		if (isCodeMissionExiste(mission)) {
			throw new MyException("Le code existe déjà");
		}
		super.create(mission);
		for (Activite activite : mission.getListActivites()) {
			activiteFacade.edit(activite);
		}
	}

	@Override
	public void edit(Mission mission) throws MyException, Exception {
		if (isCodeMissionExiste(mission)) {
			throw new MyException("Le code existe déjà");
		}
		super.edit(mission);
	}

	@Override
	public void remove(Mission mission) throws Exception {
		if (isMissionAffectForPoste(mission)) {
			throw new MyException("Il y a des poste ayant cette mission, vous ne peuvez pas la supprimer ");
		}
		super.remove(mission);
	}

	public void editActivite(Activite activite, List<Mission> missionsAdd, List<Mission> missionsRemove) throws Exception {
		for (Mission mission : missionsAdd) {
			mission.addActivite(activite);
			edit(mission);
		}
		for (Mission mission : missionsRemove) {
			mission.removeActivite(activite);
			edit(mission);
		}
	}

	private boolean isCodeMissionExiste(Mission mission) {
		Query q = em.createNamedQuery("Mission.findByCodeWithoutCurrentId");
		q.setParameter("code", mission.getCode());
		q.setParameter("id", mission.getId());
		return !q.getResultList().isEmpty();
	}

	public List<Mission> missionsForActivite(Activite activite) {
		Query q = em.createNamedQuery("Mission.findByActivite");
		q.setParameter("activite", activite);
		return q.getResultList();
	}

	private boolean isMissionAffectForPoste(Mission mission) {
		Query q = em.createNamedQuery("Poste.findByMission");
		q.setParameter("mission", mission);
		List<Activite> list = q.getResultList();
		return !list.isEmpty() && list.size() >= 1;
	}
}
