/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.wilaya.commune.service;

import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.StaticUtil;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import otherEntity.Wilaya;

/**
 *
 * @author Dell
 */
@Stateless
public class WilayaFacade extends  AbstractFacade<Wilaya>{
    
     @PersistenceContext(unitName = StaticUtil.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    public WilayaFacade()
    {
        super(Wilaya.class);
    }
}
