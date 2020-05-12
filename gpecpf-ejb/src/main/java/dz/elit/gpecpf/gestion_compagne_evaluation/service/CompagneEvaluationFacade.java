/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.gestion_compagne_evaluation.service;

import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.CustomQueryRedirectors;
import dz.elit.gpecpf.commun.util.StaticUtil;
import dz.elit.gpecpf.compagne_evaluation.entity.Compagneevaluation;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.eclipse.persistence.jpa.JpaHelper;


/**
 *
 * @author Kaci Ahmed
 */
@Stateless
public class CompagneEvaluationFacade extends AbstractFacade<Compagneevaluation> {
    
    @PersistenceContext(unitName = StaticUtil.UNIT_NAME)
    private EntityManager em;

    public CompagneEvaluationFacade() {
        super(Compagneevaluation.class);
    }


    @Override
    protected EntityManager getEntityManager() {
      return em;
    }
    
    public Compagneevaluation findByCode(String code) 
    {
      Query query = em.createNamedQuery("Compagneevaluation.findByCode");
      query.setParameter("code", code);
      List<Compagneevaluation> list = query.getResultList();
      return list.isEmpty() ? null : list.get(0);
    }
    public List<Compagneevaluation> findByCodeDatedebDatefin(String code, Date dateDeb, Date dateFin)
    {
        StringBuilder queryStringBuilder = new StringBuilder("SELECT a FROM Compagneevaluation AS a WHERE 1=1 ");
        if (code != null && !code.equals("")) {
            queryStringBuilder.append(" AND  a.code like :code ");
        }
        if (dateDeb != null && !dateDeb.toString().equals("")) {
            queryStringBuilder.append(" AND  a.datedeb = :datedeb ");
        }
        if (dateFin != null && !dateFin.toString().equals("")) {
            queryStringBuilder.append(" AND  a.datefin = :datefin ");
        }
        queryStringBuilder.append(" ORDER BY a.code ");
        

        Query q = em.createQuery(queryStringBuilder.toString());

        if (code != null && !code.equals("")) {
            q.setParameter("code","%"+ code + "%");
        }
        if (dateDeb != null && !dateDeb.toString().equals("")) {
            q.setParameter("datedeb",dateDeb );
        }
        if (dateFin != null && !dateFin.toString().equals("")) {
            q.setParameter("datefin",dateFin);
        }

        //Implémentation de visibilité
    //   JpaHelper.getDatabaseQuery(q).setRedirector(new CustomQueryRedirectors());
    
        return q.getResultList();
    }
    private boolean isExisteCode(String code) 
    {
        Compagneevaluation compagne = findByCode(code);
        if(compagne == null) {
            return false;
        } else {
            return true;
        }
    }
    @Override
    public void create(Compagneevaluation compagne) throws MyException, Exception 
    {
        if (isExisteCode(compagne.getCode())) {
            throw new MyException("Le code " + compagne.getCode() + " existe déjà ");
        } else {
            super.create(compagne);
        }
    }
    
    public List<Compagneevaluation> findAllOrderByUnite() {
        Query query = em.createNamedQuery("Compagneevaluation.findAll");
        JpaHelper.getDatabaseQuery(query).setRedirector(new CustomQueryRedirectors());
        //query.setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH);
        return query.getResultList();
    }
    
    
}
