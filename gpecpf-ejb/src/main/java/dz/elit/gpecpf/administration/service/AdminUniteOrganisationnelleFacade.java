/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.administration.service;


import dz.elit.gpecpf.administration.entity.AdminUniteOrganisationnelle;
import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.StaticUtil;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author leghettas.rabah
 */
@Stateless
public class AdminUniteOrganisationnelleFacade  extends AbstractFacade<AdminUniteOrganisationnelle> {

    @PersistenceContext(unitName = StaticUtil.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public AdminUniteOrganisationnelleFacade() {
        super(AdminUniteOrganisationnelle.class);
    }  
    
    @Override
    public void create(AdminUniteOrganisationnelle uniteOrg) throws Exception{
        super.create(uniteOrg);
    }
    public AdminUniteOrganisationnelle findByCode(String code) {
        Query query = em.createNamedQuery("AdminUniteOrganisationnelle.findByCode");
        query.setParameter("code", code);
        List<AdminUniteOrganisationnelle> list = query.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    public List<AdminUniteOrganisationnelle> findAllOrderByTrie() {
        return this.findAllOrderByAttribut("trie");
    }

}
