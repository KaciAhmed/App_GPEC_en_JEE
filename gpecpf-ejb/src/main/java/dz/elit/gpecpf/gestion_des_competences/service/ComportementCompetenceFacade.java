/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.gestion_des_competences.service;

import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.CustomQueryRedirectors;
import dz.elit.gpecpf.commun.util.StaticUtil;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.eclipse.persistence.jpa.JpaHelper;
import otherEntity.Comportement;

/**
 *
 * @author Dell
 */
@Stateless
public class ComportementCompetenceFacade  extends AbstractFacade<Comportement>{
    
     @PersistenceContext(unitName = StaticUtil.UNIT_NAME)
    private EntityManager em;
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ComportementCompetenceFacade() {
        super(Comportement.class);
    }
    
    public Comportement findByCode(String code) 
    {
      Query query = em.createNamedQuery("Comportement.findByCode");
      query.setParameter("code", code);
      List<Comportement> list = query.getResultList();
      return list.isEmpty() ? null : list.get(0);
    }
        public List<Comportement> findByCodeDescription(String code, String description)
    {
        StringBuilder queryStringBuilder = new StringBuilder("SELECT a FROM Comportement AS a WHERE 1=1 ");
        if (code != null && !code.equals("")) {
            queryStringBuilder.append(" AND  a.code like :code ");
        }
        if (description != null && !description.equals("")) {
            queryStringBuilder.append(" AND  a.description like :description ");
        }
        queryStringBuilder.append(" ORDER BY a.code ");

        Query q = em.createQuery(queryStringBuilder.toString());

        if (code != null && !code.equals("")) {
            q.setParameter("code","%"+ code + "%");
        }
        if (description != null && !description.equals("")) {
            q.setParameter("description","%"+ description + "%");
        }

        //Implémentation de visibilité
       JpaHelper.getDatabaseQuery(q).setRedirector(new CustomQueryRedirectors());
    
        return q.getResultList();
    }
     private boolean isExisteCode(String code) 
    {
        Comportement Compo = findByCode(code);
        if(Compo == null) {
            return false;
        } else {
            return true;
        }
    }
        @Override
    public void create(Comportement Comp) throws MyException, Exception 
    {
        if (isExisteCode(Comp.getCode())) {
            throw new MyException("Le code " + Comp.getCode() + " existe déjà ");
        } else {
            super.create(Comp);
        }
    }

    
    public List<Comportement> findAllOrderByUnite() {
        Query query = em.createNamedQuery("Comportement.findAll");
        JpaHelper.getDatabaseQuery(query).setRedirector(new CustomQueryRedirectors());
        //query.setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH);
        return query.getResultList();
    }
    
}
