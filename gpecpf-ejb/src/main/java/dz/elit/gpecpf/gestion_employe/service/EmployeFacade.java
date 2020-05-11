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
 * @author Kaci Ahmed
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
    
    public Employe findByMatricule(String matricule) 
    {
      Query query = em.createNamedQuery("Employe.findByMatricule");
      query.setParameter("matricule", matricule);
      List<Employe> list = query.getResultList();
      return list.isEmpty() ? null : list.get(0);
    }
    public Employe findByUserName(String userName) 
    {
      Query query = em.createNamedQuery("Employe.findByUserName");
      query.setParameter("userName", userName);
      List<Employe> list = query.getResultList();
      return list.isEmpty() ? null : list.get(0);
    }
    
    public List<Employe> findByNomPrenomMatricule(String nom, String prenom, String matricule) {
        StringBuilder queryStringBuilder = new StringBuilder("SELECT a FROM Employe AS a WHERE 1=1 ");
        if (nom != null && !nom.equals("")) {
            queryStringBuilder.append(" AND  a.nom like :nom ");
        }
        if (prenom != null && !prenom.equals("")) {
            queryStringBuilder.append(" AND  a.prenom like :prenom ");
        }
        if (matricule != null && !matricule.equals("")) {
            queryStringBuilder.append(" AND  a.matricule like :matricule ");
        }
        queryStringBuilder.append(" ORDER BY a.matricule ");

        Query q = em.createQuery(queryStringBuilder.toString());

        if (nom != null && !nom.equals("")) {
            q.setParameter("nom","%"+ nom + "%");
        }
        if (prenom != null && !prenom.equals("")) {
            q.setParameter("prenom","%"+ prenom + "%");
        }
        if (matricule != null && !matricule.equals("")) {
            q.setParameter("matricule","%"+ matricule+ "%");
        }
        //Implémentation de visibilité
      //  JpaHelper.getDatabaseQuery(q).setRedirector(new CustomQueryRedirectors());

        return q.getResultList();
    }
    private boolean isExisteMatricule(String matricule) 
    {
        Employe emp = findByMatricule(matricule);
        if(emp == null) {
            return false;
        } else {
            return true;
        }
    }
            @Override
    public void create(Employe emp) throws MyException, Exception 
    {
        if (isExisteMatricule(emp.getMatricule())) {
            throw new MyException("Le matricule " + emp.getMatricule() + " existe déjà ");
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
