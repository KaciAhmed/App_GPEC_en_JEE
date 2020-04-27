package dz.elit.gpecpf.poste.service;

import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.StaticUtil;
import dz.elit.gpecpf.poste.entity.Mission;
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

    public MissionFacade() {
        super(Mission.class);
    }
	
	@Override
    public void create(Mission mission) throws MyException, Exception {
        if (isCodeMissionExiste(mission)) {
            throw new MyException("Le code existe déjà");
        }
        super.create(mission);
    }
	
	private boolean isCodeMissionExiste(Mission mission) {
		Query q = em.createNamedQuery("Mission.findByCodeWithoutCurrentId");
        q.setParameter("code", mission.getCode());
        q.setParameter("id", mission.getId());
        return !q.getResultList().isEmpty();
    }
}
