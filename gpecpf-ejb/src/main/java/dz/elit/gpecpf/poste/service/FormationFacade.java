package dz.elit.gpecpf.poste.service;

import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.StaticUtil;
import dz.elit.gpecpf.poste.entity.Formation;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


/**
 *
 * @author Nadir Ben Mohand
 */
@Stateless
public class FormationFacade extends AbstractFacade<Formation> {

    @PersistenceContext(unitName = StaticUtil.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FormationFacade() {
        super(Formation.class);
    }
	
	@Override
    public void create(Formation formation) throws MyException, Exception {
        if (isCodeFormationExiste(formation)) {
            throw new MyException("Le code existe déjà");
        }
        super.create(formation);
    }
	
	private boolean isCodeFormationExiste(Formation formation) {
		Query q = em.createNamedQuery("Formation.findByCodeWithoutCurrentId");
        q.setParameter("code", formation.getCode());
        q.setParameter("id", formation.getId());
        return !q.getResultList().isEmpty();
    }
	
}
