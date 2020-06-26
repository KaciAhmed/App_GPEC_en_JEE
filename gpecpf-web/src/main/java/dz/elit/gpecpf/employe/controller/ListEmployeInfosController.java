package dz.elit.gpecpf.employe.controller;

import dz.elit.gpecpf.administration.entity.AdminUtilisateur;
import dz.elit.gpecpf.administration.service.AdminUtilisateurFacade;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.employe.entity.Employe;
import dz.elit.gpecpf.gestion_employe.service.EmployeFacade;
import dz.elit.gpecpf.other.entity.Historiqueemployeposte;
import dz.elit.gpecpf.poste.entity.Poste;
import java.io.Serializable;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Nadir Ben Mohand
 */
@ManagedBean
@ViewScoped
public class ListEmployeInfosController extends AbstractController implements Serializable {

	@EJB
	EmployeFacade employeFacade;
	@EJB
	private AdminUtilisateurFacade utilisateurFacade;

	private AdminUtilisateur utilisateurCourant;
	private Employe employeCourant;

	private Poste poste;

	private Employe boss;
	private List<Employe> workers;

	private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	@Override
	protected void initController() {
		utilisateurCourant = new AdminUtilisateur();
		employeCourant = new Employe();
		boss = new Employe();
		workers = new ArrayList();
		initUser();
		if (employeCourant != null) {
			boss = employeFacade.boss(employeCourant);
			workers = employeFacade.workers(employeCourant);
			poste = employeCourant.recupPosteEmploye();
		}
	}

	public void initUser() {
		Principal principal = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
		if (principal != null) {
			utilisateurCourant = utilisateurFacade.findByLogin(principal.getName());
			employeCourant = employeFacade.findByUserName(utilisateurCourant.getLogin());
		} else {
			utilisateurCourant = null;
			employeCourant = null;
		}
	}

	public String formatDate(Date x) {
		if (x != null) {
			return formatter.format(x);
		}
		return "";
	}

	public List<Historiqueemployeposte> ordonerListHistEmp() {
		if (employeCourant == null) {
			return null;
		}
		Historiqueemployeposte hist2 = new Historiqueemployeposte();
		List<Historiqueemployeposte> listHistEmpOrdone = new ArrayList<>();
		for (Historiqueemployeposte hist : employeCourant.getListHistoriqueEmployePoste()) {
			if (hist.getDatefin() != null && (hist.getHistoriqueemployepostePK().getDatedeb().equals(hist.getDatefin()) || hist.getHistoriqueemployepostePK().getDatedeb().after(hist.getDatefin()))) {
				// rien faire
			} else {
				if (hist.getDatefin() == null || hist.getDatefin().equals(employeCourant.getDate_depart())) {
					hist2 = hist;
				} else {
					listHistEmpOrdone.add(hist);
				}
			}
		}
		listHistEmpOrdone.add(hist2);
		return listHistEmpOrdone;
	}

	public EmployeFacade getEmployeFacade() {
		return employeFacade;
	}

	public void setEmployeFacade(EmployeFacade employeFacade) {
		this.employeFacade = employeFacade;
	}

	public AdminUtilisateurFacade getUtilisateurFacade() {
		return utilisateurFacade;
	}

	public void setUtilisateurFacade(AdminUtilisateurFacade utilisateurFacade) {
		this.utilisateurFacade = utilisateurFacade;
	}

	public AdminUtilisateur getUtilisateurCourant() {
		return utilisateurCourant;
	}

	public void setUtilisateurCourant(AdminUtilisateur utilisateurCourant) {
		this.utilisateurCourant = utilisateurCourant;
	}

	public Employe getEmployeCourant() {
		return employeCourant;
	}

	public void setEmployeCourant(Employe employeCourant) {
		this.employeCourant = employeCourant;
	}

	public Employe getBoss() {
		return boss;
	}

	public void setBoss(Employe boss) {
		this.boss = boss;
	}

	public List<Employe> getWorkers() {
		return workers;
	}

	public void setWorkers(List<Employe> workers) {
		this.workers = workers;
	}

	public Poste getPoste() {
		return poste;
	}

	public void setPoste(Poste poste) {
		this.poste = poste;
	}

	public SimpleDateFormat getFormatter() {
		return formatter;
	}

}