package dz.elit.gpecpf.administration.service;

import dz.elit.gpecpf.administration.entity.Notification;
import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.StaticUtil;
import dz.elit.gpecpf.employe.entity.Employe;
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
public class NotificationFacade extends AbstractFacade<Notification> {

	@PersistenceContext(unitName = StaticUtil.UNIT_NAME)
	private EntityManager em;

	public NotificationFacade() {
		super(Notification.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public List<Notification> notificationForEmploye(Employe employe) {
		Query q = em.createNamedQuery("Notification.findByEmploye");
		q.setParameter("employe", employe);
		return q.getResultList();
	}
	
	public int notificationUnseenForEmploye(Employe employe) {
		Query q = em.createNamedQuery("Notification.findByEmployeUnseen");
		q.setParameter("employe", employe);
		return q.getResultList().size();
	}
}
