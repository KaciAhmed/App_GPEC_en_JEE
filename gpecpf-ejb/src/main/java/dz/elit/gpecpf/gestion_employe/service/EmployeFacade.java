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
import dz.elit.gpecpf.employe.entity.Employe;
import dz.elit.gpecpf.poste.entity.Poste;
import java.util.ArrayList;

/**
 *
 * @author Kaci Ahmed
 */
@Stateless
public class EmployeFacade extends AbstractFacade<Employe> {

	@PersistenceContext(unitName = StaticUtil.UNIT_NAME)
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public EmployeFacade() {
		super(Employe.class);
	}

	public Employe findByMatricule(String matricule) {
		Query query = em.createNamedQuery("Employe.findByMatricule");
		query.setParameter("matricule", matricule);
		List<Employe> list = query.getResultList();
		return list.isEmpty() ? null : list.get(0);
	}

	public Employe findByUserName(String userName) {
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
			q.setParameter("nom", "%" + nom + "%");
		}
		if (prenom != null && !prenom.equals("")) {
			q.setParameter("prenom", "%" + prenom + "%");
		}
		if (matricule != null && !matricule.equals("")) {
			q.setParameter("matricule", "%" + matricule + "%");
		}
		//Implémentation de visibilité
		//  JpaHelper.getDatabaseQuery(q).setRedirector(new CustomQueryRedirectors());

		return q.getResultList();
	}

	private boolean isExisteMatricule(String matricule) {
		Employe emp = findByMatricule(matricule);
		if (emp == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void create(Employe emp) throws MyException, Exception {
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

	public List<Poste> postesFromEmployes() {
		List<Employe> listEmployes = new ArrayList<>();
		listEmployes = findAll();
		List<Poste> listPostes = new ArrayList<>();
		for (Employe employe : listEmployes) {
			Poste poste = new Poste();
			poste = employe.recupPosteEmploye();
			if (poste != null) {
				listPostes.add(poste);
			}
		}
		return listPostes;
	}

	public Employe boss(Employe employe) {
		if (employe == null) {
			return null;
		}
		Poste posteSup = new Poste();
		Poste posteEmp = new Poste();
		posteEmp = employe.recupPosteEmploye();
		if (posteEmp != null) {
			posteSup = posteEmp.getPosteSuperieur();
			if (posteSup != null) {
				List<Employe> listEmployes = new ArrayList();
				listEmployes = findAll();
				for (Employe emp : listEmployes) {
					if (emp.recupPosteEmploye().equals(posteSup)) {
						return emp;
					}
				}
			}
		}
		return null;
	}

	public List<Employe> workers(Employe boss) {
		List<Employe> listWorkers = new ArrayList();
		Poste posteBoss = new Poste();
		posteBoss = boss.recupPosteEmploye();
		if (posteBoss != null) {
			List<Employe> listEmployes = new ArrayList();
			listEmployes = findAll();
			for (Employe employe : listEmployes) {
				Employe otherBoss = boss(employe);
				if (otherBoss != null) {
					if (otherBoss.equals(boss)) {
						listWorkers.add(employe);
					}
				}
			}
			return listWorkers;
		}
		return null;
	}

	public List<Employe> subWorkers(List<Employe> bosses) {
		if (bosses == null) {
			return null;
		}
		List<Employe> listSubWorkers = new ArrayList();
		for (Employe boss : bosses) {
			listSubWorkers.addAll(workers(boss));
		}
		return listSubWorkers;
	}

	public int calculerTotalEmp() {
		List<Employe> lstEmp = findAll();
		if (lstEmp.isEmpty()) {
			return 0;
		} else {
			String req = "Select max(id) from sch_admin.employe";
			Query q = em.createNativeQuery(req);
			int nbr = (Integer) q.getSingleResult();
			return nbr;
		}

	}

}
