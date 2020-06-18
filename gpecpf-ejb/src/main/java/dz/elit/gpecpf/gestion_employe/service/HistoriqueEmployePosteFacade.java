/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.gestion_employe.service;

import dz.elit.gpecpf.commun.util.StaticUtil;
import dz.elit.gpecpf.employe.entity.Employe;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import otherEntity.Historiqueemployeposte;

/**
 *
 * @author Kaci Ahmed
 */
@Stateless
public class HistoriqueEmployePosteFacade {
    
    @PersistenceContext(unitName = StaticUtil.UNIT_NAME)
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public HistoriqueEmployePosteFacade() {
    }
    
     public Historiqueemployeposte chercherHistorique(Employe employe){
         int id=employe.getId();
         String req= "SELECT h FROM Historiqueemployeposte h WHERE h.historiqueemployepostePK.idemploye = :idemploye";
          Query q= em.createQuery(req);
          q.setParameter("idemploye", id);
         Historiqueemployeposte hist = (Historiqueemployeposte)q.getSingleResult();
         
         return hist;
        //String req= "SELECT idposte FROM historiqueemployeposte WHERE idemploye ="+id;   
    }
     public void supprimerHistorique(int idEmp, int idPoste){
        
         String req= "Delete from sch_admin.historiqueemployeposte where idemploye = "+idEmp+" and idposte = "+idPoste;
          Query q= em.createNamedQuery(req);
          q.executeUpdate();

    }
     public void suppHistorique(Historiqueemployeposte hist){
     
         em.remove(hist);

        //String req= "SELECT idposte FROM historiqueemployeposte WHERE idemploye ="+id;   
    }

}
