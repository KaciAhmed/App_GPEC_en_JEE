

package dz.elit.gpecpf.administration.service;

import dz.elit.gpecpf.administration.entity.AdminHistorique;
import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.StaticUtil;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author abdechakour.amine
 */
@Stateless
public class AdminHistoriqueFacade extends AbstractFacade<AdminHistorique> {


    @PersistenceContext(unitName = StaticUtil.UNIT_NAME)
    private EntityManager em;

  @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AdminHistoriqueFacade() {
        super(AdminHistorique.class);
    }
    
 }
