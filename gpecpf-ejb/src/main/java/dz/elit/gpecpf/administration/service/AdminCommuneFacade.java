


package dz.elit.gpecpf.administration.service;

import dz.elit.gpecpf.administration.entity.AdminCommune;
import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.StaticUtil;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author chekor.samir
 */
@Stateless
public class AdminCommuneFacade extends AbstractFacade<AdminCommune> {
    @PersistenceContext(unitName = StaticUtil.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AdminCommuneFacade() {
        super(AdminCommune.class);
    }
    
}
