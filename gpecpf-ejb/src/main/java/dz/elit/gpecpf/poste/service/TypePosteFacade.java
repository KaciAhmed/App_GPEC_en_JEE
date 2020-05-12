package dz.elit.gpecpf.poste.service;

import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.StaticUtil;
import dz.elit.gpecpf.poste.entity.TypePoste;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


/**
 *
 * @author Nadir Ben Mohand
 */
@Stateless
public class TypePosteFacade extends AbstractFacade<TypePoste> {

    @PersistenceContext(unitName = StaticUtil.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TypePosteFacade() {
        super(TypePoste.class);
    }
	
	@Override
    public void create(TypePoste typePoste) throws MyException, Exception {
        if (isCodeTypePosteExiste(typePoste)) {
            throw new MyException("Le code existe déjà");
        }
        super.create(typePoste);
    }
	
	private boolean isCodeTypePosteExiste(TypePoste typePoste) {
		Query q = em.createNamedQuery("TypePoste.findByCodeWithoutCurrentId");
        q.setParameter("code", typePoste.getCode());
        q.setParameter("id", typePoste.getId());
        return !q.getResultList().isEmpty();
    }
	
}
