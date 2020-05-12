package dz.elit.gpecpf.poste.service;

import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.StaticUtil;
import dz.elit.gpecpf.poste.entity.Condition;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


/**
 *
 * @author Nadir Ben Mohand
 */
@Stateless
public class ConditionFacade extends AbstractFacade<Condition> {

    @PersistenceContext(unitName = StaticUtil.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConditionFacade() {
        super(Condition.class);
    }
	
	@Override
    public void create(Condition condition) throws MyException, Exception {
        if (isCodeConditionExiste(condition)) {
            throw new MyException("Le code existe déjà");
        }
        super.create(condition);
    }
	
	@Override
    public void edit(Condition condition) throws MyException, Exception {
        if (isCodeConditionExiste(condition)) {
            throw new MyException("Le code existe déjà");
        }
        super.edit(condition);
    }
	
	private boolean isCodeConditionExiste(Condition condition) {
		Query q = em.createNamedQuery("Condition.findByCodeWithoutCurrentId");
        q.setParameter("code", condition.getCode());
        q.setParameter("id", condition.getId());
        return !q.getResultList().isEmpty();
    }
	
}
