/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.gestion_des_competences.service;

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
import otherEntity.Domainecompetence;

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
    
  /*  public List<Competence> findListCompetenceByidDomaine (Integer idDomCom)
    {
      
        StringBuilder queryStringBuilder = new StringBuilder("SELECT a FROM Competence AS a WHERE 1=1 ");
        if (idDomCom!= null && idDomCom!=0) {
            queryStringBuilder.append(" AND  a.iddomcom = :iddomcom ");
        }
        Query q = em.createQuery(queryStringBuilder.toString());
        if (idDomCom != null && idDomCom!=0) {
            q.setParameter("iddomcom",idDomCom);
        }
        //Implémentation de visibilité
       JpaHelper.getDatabaseQuery(q).setRedirector(new CustomQueryRedirectors());
    
        return q.getResultList();
    
    }
*/
    
  /*   public List<Competence> findListCompetenceByidDomaine (Integer iddomcom){
         Query q = em.createNamedQuery("Competence.findByDomaine");
         q.setParameter("iddomcom",iddomcom);
         return q.getResultList();
         
     }
*/

    
}
