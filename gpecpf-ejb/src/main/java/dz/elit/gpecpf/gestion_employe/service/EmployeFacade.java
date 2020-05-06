/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.gestion_employe.service;

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
import otherEntity.Employe;

/**
 *
 * @author Dell
 */
@Stateless
public class EmployeFacade extends AbstractFacade<Employe>{

    @PersistenceContext(unitName = StaticUtil.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmployeFacade() {
        super(Employe.class);
    }
    
    public Employe findByCode(String code) 
    {
      Query query = em.createNamedQuery("Employe.findBycode");
      query.setParameter("code", code);
      List<Employe> list = query.getResultList();
      return list.isEmpty() ? null : list.get(0);
    }
    
    public List<Employe> findByNomPrenomCode(String nom, String prenom, String code) {
        StringBuilder queryStringBuilder = new StringBuilder("SELECT a FROM Employe AS a WHERE 1=1 ");
        if (nom != null && !nom.equals("")) {
            queryStringBuilder.append(" AND  a.nom like :nom ");
        }
        if (prenom != null && !prenom.equals("")) {
            queryStringBuilder.append(" AND  a.prenom like :prenom ");
        }
        if (code != null && !code.equals("")) {
            queryStringBuilder.append(" AND  a.code like :code ");
        }
        queryStringBuilder.append(" ORDER BY a.code ");

        Query q = em.createQuery(queryStringBuilder.toString());

        if (nom != null && !nom.equals("")) {
            q.setParameter("nom","%"+ nom + "%");
        }
        if (prenom != null && !prenom.equals("")) {
            q.setParameter("prenom","%"+ prenom + "%");
        }
        if (code != null && !code.equals("")) {
            q.setParameter("code","%"+ code+ "%");
        }
        //Implémentation de visibilité
      //  JpaHelper.getDatabaseQuery(q).setRedirector(new CustomQueryRedirectors());

        return q.getResultList();
    }
    private boolean isExisteCode(String code) 
    {
        Employe emp = findByCode(code);
        if(emp == null) {
            return false;
        } else {
            return true;
        }
    }
            @Override
    public void create(Employe emp) throws MyException, Exception 
    {
        if (isExisteCode(emp.getCode())) {
            throw new MyException("Le code " + emp.getCode() + " existe déjà ");
        } else {
            super.create(emp);
        }
    }
    
       public List<Employe> findAllOrderByUnite() {
        Query query = em.createNamedQuery("Employe.findAll");
        JpaHelper.getDatabaseQuery(query).setRedirector(new CustomQueryRedirectors());
        //query.setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH);
        return query.getResultList();
    }
    
    
}
