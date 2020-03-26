package dz.elit.gpecpf.administration.service;

import dz.elit.gpecpf.administration.entity.AdminConnecxionHistorique;
import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.StaticUtil;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Leghettas rabah & Chekor.samir
 */
@Stateless
public class AdminConnecxionHistoriqueFacade extends AbstractFacade<AdminConnecxionHistorique> {

    @PersistenceContext(unitName = StaticUtil.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AdminConnecxionHistoriqueFacade() {
        super(AdminConnecxionHistorique.class);
    }
/* récupérer les ligne qui coréspondent aux connéxions d'un utilisateur
    avec une addresse ip et à une date de connexion données
*/ 
    public List<AdminConnecxionHistorique> findByParams
        (int first, int pageSize, String utilisateur, 
                String adresseIp, Date dateConnexion) {
        Query q = setSelectQuery(utilisateur, adresseIp, dateConnexion);        
        q.setFirstResult(first);
        q.setMaxResults(pageSize);
        return q.getResultList();
    }
  // utiliser en haut
    private Query setSelectQuery(String utilisateur, String adresseIp, 
            Date dateConnexion) {
        StringBuilder queryStringBuilder = new StringBuilder("SELECT a FROM AdminConnecxionHistorique AS a WHERE 1=1 ");
        Query q = setCondition(queryStringBuilder, utilisateur, adresseIp, dateConnexion);
        return q;
    }
  // utiliser en haut
    private Query setCondition(StringBuilder queryStringBuilder, 
            String utilisateur, String adresseIp, Date dateConnexion) {
        if (utilisateur != null && !utilisateur.equals("")) {
            queryStringBuilder.append(" AND  a.utilisateur like :utilisateur ");
        }
        if (adresseIp != null && !adresseIp.equals("")) {
            queryStringBuilder.append(" AND  a.adresseIp like :adresseIp ");
        }
        if (dateConnexion != null) {
            queryStringBuilder.append(" AND  a.dateConnexion = :dateConnexion ");
        }
        //queryStringBuilder.append(" ORDER BY a.champ ");
        Query q = em.createQuery(queryStringBuilder.toString());
        if (utilisateur != null && !utilisateur.equals("")) {
            q.setParameter("utilisateur", utilisateur + "%");
        }
        if (adresseIp != null && !adresseIp.equals("")) {
            q.setParameter("adresseIp", adresseIp + "%");
        }
        if (dateConnexion != null) {
            q.setParameter("dateConnexion", dateConnexion);
        }
        return q;
    }
 
  
      /* récupérer le nombre connéxions d'un utilisateur
    avec une addresse ip et à une date donné
*/ 
   public int count(String utilisateur, String adresseIp, Date dateConnexion){
       Query q = setCountQuery(utilisateur, adresseIp, dateConnexion); 
       return ((Long) q.getSingleResult()).intValue();
   } 
   // utiliser en haut
     public Query setCountQuery(String utilisateur, String adresseIp, Date dateConnexion) {
        StringBuilder queryStringBuilder = new StringBuilder("SELECT COUNT(a) FROM AdminConnecxionHistorique AS a WHERE 1=1 ");
        Query q = setCondition(queryStringBuilder, utilisateur, adresseIp, dateConnexion);
        return q;
    }

}
