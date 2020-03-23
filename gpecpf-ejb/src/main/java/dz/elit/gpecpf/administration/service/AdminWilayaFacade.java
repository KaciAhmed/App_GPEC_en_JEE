

package dz.elit.gpecpf.administration.service;

import dz.elit.gpecpf.administration.entity.AdminWilaya;
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
public class AdminWilayaFacade extends  AbstractFacade<AdminWilaya> {
    @PersistenceContext(unitName = StaticUtil.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AdminWilayaFacade() {
        super(AdminWilaya.class);
    }
    
}
