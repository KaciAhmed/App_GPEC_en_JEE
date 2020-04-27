package dz.elit.gpecpf.poste.service;

import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.StaticUtil;
import dz.elit.gpecpf.poste.entity.Tache;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


/**
 *
 * @author Nadir Ben Mohand
 */
@Stateless
public class TacheFacade extends AbstractFacade<Tache> {

    @PersistenceContext(unitName = StaticUtil.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TacheFacade() {
        super(Tache.class);
    }
	
	@Override
    public void create(Tache tache) throws MyException, Exception {
        if (isCodeTacheExiste(tache)) {
            throw new MyException("Le code existe déjà");
        }
        super.create(tache);
    }
	
	private boolean isCodeTacheExiste(Tache tache) {
		Query q = em.createNamedQuery("Tache.findByCodeWithoutCurrentId");
        q.setParameter("code", tache.getCode());
        q.setParameter("id", tache.getId());
        return !q.getResultList().isEmpty();
    }
	
}
