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
import otherEntity.Competence;


/**
 *
 * @author Dell
 */
@Stateless
public class CompetenceFacade extends AbstractFacade<Competence> {

    @PersistenceContext(unitName = StaticUtil.UNIT_NAME)
    private EntityManager em;
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public CompetenceFacade()
    {
        super(Competence.class);
    }
    
   public Competence findByCode(String code) 
    {
      Query query = em.createNamedQuery("Competence.findByCode");
      query.setParameter("code", code);
      List<Competence> list = query.getResultList();
      return list.isEmpty() ? null : list.get(0);
    }
    public List<Competence> findByCodeLibelleDescription(String code, String libelle, String description)
    {
        StringBuilder queryStringBuilder = new StringBuilder("SELECT a FROM Competence AS a WHERE 1=1 ");
        if (code != null && !code.equals("")) {
            queryStringBuilder.append(" AND  a.code like :code ");
        }
        if (libelle != null && !libelle.equals("")) {
            queryStringBuilder.append(" AND  a.libelle like :libelle ");
        }
        if (description != null && !description.equals("")) {
            queryStringBuilder.append(" AND  a.description like :description ");
        }
        queryStringBuilder.append(" ORDER BY a.code ");

        Query q = em.createQuery(queryStringBuilder.toString());

        if (code != null && !code.equals("")) {
            q.setParameter("code","%"+ code + "%");
        }
        if (libelle != null && !libelle.equals("")) {
            q.setParameter("libelle","%"+ libelle + "%");
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
        Competence Comp = findByCode(code);
        if(Comp == null) {
            return false;
        } else {
            return true;
        }
    }
        
    @Override
    public void create(Competence Comp) throws MyException, Exception 
    {
        if (isExisteCode(Comp.getCode())) {
            throw new MyException("Le code " + Comp.getCode() + " existe déjà ");
        } else {
            super.create(Comp);
        }
    }
     
     public List<Competence> findAllOrderByUnite() {
        Query query = em.createNamedQuery("Competence.findAll");
        JpaHelper.getDatabaseQuery(query).setRedirector(new CustomQueryRedirectors());
        //query.setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH);
        return query.getResultList();
    }
    
}
