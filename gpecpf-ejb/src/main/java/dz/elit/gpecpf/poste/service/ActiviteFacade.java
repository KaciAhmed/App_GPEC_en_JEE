package dz.elit.gpecpf.poste.service;

import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.StaticUtil;
import dz.elit.gpecpf.poste.entity.Activite;
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
        if (isCodeActiviteExiste(activite)) {
            throw new MyException("Le code existe déjà");
        }
        super.create(activite);
    }
	
	private boolean isCodeActiviteExiste(Activite activite) {
		Query q = em.createNamedQuery("Activite.findByCodeWithoutCurrentId");
        q.setParameter("code", activite.getCode());
        q.setParameter("id", activite.getId());
        return !q.getResultList().isEmpty();
    }
}
