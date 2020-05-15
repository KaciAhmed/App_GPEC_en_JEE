package dz.elit.gpecpf.poste.service;

import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.StaticUtil;
import dz.elit.gpecpf.poste.entity.Condition;
import dz.elit.gpecpf.poste.entity.Emploi;
import dz.elit.gpecpf.poste.entity.Formation;
import dz.elit.gpecpf.poste.entity.Moyen;
import dz.elit.gpecpf.poste.entity.Poste;
import dz.elit.gpecpf.poste.entity.TypePoste;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Nadir Ben Mohand
 */
@Stateless
public class PosteFacade extends AbstractFacade<Poste> {

    @PersistenceContext(unitName = StaticUtil.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PosteFacade() {
        super(Poste.class);
    }
	
	@Override
    public void create(Poste poste) throws MyException, Exception {
        if (isCodePosteExiste(poste)) {
            throw new MyException("Le code existe déjà");
        }
        super.create(poste);
    }
	
	@Override
    public void edit(Poste poste) throws MyException, Exception {
        if (isCodePosteExiste(poste)) {
            throw new MyException("Le code existe déjà");
        }
        super.edit(poste);
    }
	
	private boolean isCodePosteExiste(Poste poste) {
		Query q = em.createNamedQuery("Poste.findByCodeWithoutCurrentId");
        q.setParameter("code", poste.getCode());
        q.setParameter("id", poste.getId());
        return !q.getResultList().isEmpty();
    }
	
	public List<Poste> postesForType(TypePoste typePoste) {
		Query q = em.createNamedQuery("Poste.findByType");
        q.setParameter("typePoste", typePoste);
        return q.getResultList();
	}
	
	public List<Poste> postesForEmploi(Emploi emploi) {
		Query q = em.createNamedQuery("Poste.findByEmploi");
        q.setParameter("emploi", emploi);
        return q.getResultList();
	}
	
	public List<Poste> postesForResponsable(Poste poste) {
		Query q = em.createNamedQuery("Poste.findByResponsable");
        q.setParameter("posteResponsable", poste);
        return q.getResultList();
	}
	
	public List<Poste> postesForCondition(Condition condition) {
		Query q = em.createNamedQuery("Poste.findByCondition");
        q.setParameter("condition", condition);
        return q.getResultList();
	}
	
	public List<Poste> postesForMoyen(Moyen moyen) {
		Query q = em.createNamedQuery("Poste.findByMoyen");
        q.setParameter("moyen", moyen);
        return q.getResultList();
	}
	
	public List<Poste> postesForFormation(Formation formation) {
		Query q = em.createNamedQuery("Poste.findByFormation");
        q.setParameter("formation", formation);
        return q.getResultList();
	}
   public List<Poste> findByCodeDenomination(String code, String Denomination)
    {
        StringBuilder queryStringBuilder = new StringBuilder("SELECT a FROM Poste AS a WHERE 1=1 ");
        if (code != null && !code.equals("")) {
            queryStringBuilder.append(" AND  a.code like :code ");
        }
        if (Denomination != null && !Denomination.equals("")) {
            queryStringBuilder.append(" AND  a.denomination like :denomination ");
        }
        queryStringBuilder.append(" ORDER BY a.code ");

        Query q = em.createQuery(queryStringBuilder.toString());

        if (code != null && !code.equals("")) {
            q.setParameter("code","%"+ code + "%");
        }
        if (Denomination != null && !Denomination.equals("")) {
            q.setParameter("denomination","%"+ Denomination + "%");
        }
        //Implémentation de visibilité
      // JpaHelper.getDatabaseQuery(q).setRedirector(new CustomQueryRedirectors());
    
        return q.getResultList();
    }
      
}
