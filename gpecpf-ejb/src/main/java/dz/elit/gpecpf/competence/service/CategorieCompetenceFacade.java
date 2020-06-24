/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
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
import otherEntity.Categoriecompetence;

/**
 *
 * @author Dell
 *//*
@Stateless
public class CategorieCompetenceFacade extends AbstractFacade<Categoriecompetence> {
    
    @PersistenceContext(unitName = StaticUtil.UNIT_NAME)
    private EntityManager em;

    public CategorieCompetenceFacade() {
        super(Categoriecompetence.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
      return em;
    }
    
    public Categoriecompetence findByCode(String code) 
    {
      Query query = em.createNamedQuery("Categoriecompetence.findByCode");
      query.setParameter("code", code);
      List<Categoriecompetence> list = query.getResultList();
      return list.isEmpty() ? null : list.get(0);
    }
    
    public List<Categoriecompetence> findByCodeLibelle(String code, String libelle)
    {
        StringBuilder queryStringBuilder = new StringBuilder("SELECT a FROM Categoriecompetence AS a WHERE 1=1 ");
        if (code != null && !code.equals("")) {
            queryStringBuilder.append(" AND  a.code like :code ");
        }
        if (libelle != null && !libelle.equals("")) {
            queryStringBuilder.append(" AND  a.libelle like :libelle ");
        }
        queryStringBuilder.append(" ORDER BY a.code ");

        Query q = em.createQuery(queryStringBuilder.toString());

        if (code != null && !code.equals("")) {
            q.setParameter("code","%"+ code + "%");
        }
        if (libelle != null && !libelle.equals("")) {
            q.setParameter("libelle","%"+ libelle + "%");
        }

        //Implémentation de visibilité
       JpaHelper.getDatabaseQuery(q).setRedirector(new CustomQueryRedirectors());
    
        return q.getResultList();
    }
    
     private boolean isExisteCode(String code) 
    {
        Categoriecompetence catComp = findByCode(code);
        if(catComp == null) {
            return false;
        } else {
            return true;
        }
    }
     
       @Override
    public void create(Categoriecompetence catComp) throws MyException, Exception 
    {
        if (isExisteCode(catComp.getCode())) {
            throw new MyException("Le code " + catComp.getCode() + " existe déjà ");
        } else {
            super.create(catComp);
        }
    }
    
     public List<Categoriecompetence> findAllOrderByUnite() {
        Query query = em.createNamedQuery("Categoriecompetence.findAll");
        JpaHelper.getDatabaseQuery(query).setRedirector(new CustomQueryRedirectors());
        //query.setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH);
        return query.getResultList();
    }
    
}
*/