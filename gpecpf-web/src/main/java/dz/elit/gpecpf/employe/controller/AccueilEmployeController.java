package dz.elit.gpecpf.employe.controller;

import dz.elit.gpecpf.administration.entity.AdminUtilisateur;
import dz.elit.gpecpf.administration.entity.Notification;
import dz.elit.gpecpf.administration.service.AdminUtilisateurFacade;
import dz.elit.gpecpf.administration.service.NotificationFacade;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.employe.entity.Employe;
import dz.elit.gpecpf.evaluation.entity.Evaluation;
import dz.elit.gpecpf.evaluation.service.EvaluationFacade;
import dz.elit.gpecpf.gestion_employe.service.EmployeFacade;
import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class AccueilEmployeController extends AbstractController implements Serializable {

	@EJB
	EmployeFacade employeFacade;
	@EJB
	private AdminUtilisateurFacade utilisateurFacade;
	@EJB
	private EvaluationFacade evaluationFacade;
	@EJB
	private NotificationFacade notificationFacade;

	private AdminUtilisateur utilisateurCourant;
	private Employe employeCourant;
	private List<Notification> notifications;
	private int nbrUnseen;

	private boolean isEmploye = false;

	@Override
	protected void initController() {
		utilisateurCourant = new AdminUtilisateur();
		employeCourant = new Employe();
		initUser();
		notifications = new ArrayList<>();
		if (employeCourant != null) {
			isEmploye = true;
			notifications = notificationFacade.notificationForEmploye(employeCourant);
			nbrUnseen = notificationFacade.notificationUnseenForEmploye(employeCourant);
		}
	}

	public boolean roleEmploye(boolean access) {
		return employeCourant != null && access;
	}

	public boolean willDoEvals() {
		if (employeCourant == null) {
			return false;
		}
		List<Evaluation> listEvals = evaluationFacade.evaluationsForSupN1(employeCourant);
		return listEvals != null && !listEvals.isEmpty();
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

	public boolean isIsEmploye() {
		return isEmploye;
	}

	public String oldTime(Date date) {
		Date date2 = new Date();
		long diff = date2.getTime() - date.getTime();
		int diffSeconds = (int) (diff / 1000 % 60);
		int diffMinutes = (int) (diff / (60 * 1000) % 60);
		int diffHours = (int) (diff / (60 * 60 * 1000));
		int diffInDays = (int) (diff / (1000 * 60 * 60 * 24));
		if (diffInDays > 0) {
			return "" + diffInDays + " Jours";
		}
		if (diffHours > 0) {
			return "" + diffHours + " h";
		}
		if (diffMinutes > 0) {
			return "" + diffMinutes + " min";
		}
		if (diffSeconds > 0) {
			return "" + diffSeconds + " sec";
		}
		return "";
	}

	public String newNotif(Notification notif) {
		return (notif.getVerifiee() == 0) ? "en-cours" : "fini";
	}

	public String seen(Notification notif) {
		if (notif.getVerifiee() == 0) {
			notif.setVerifiee(1);
			try {
				notificationFacade.edit(notif);
			} catch (Exception ex) {

			}
		}
		return notif.getLien();
	}

	public void setIsEmploye(boolean isEmploye) {
		this.isEmploye = isEmploye;
	}

	public List<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}

	public int getNbrUnseen() {
		return nbrUnseen;
	}

	public void setNbrUnseen(int nbrUnseen) {
		this.nbrUnseen = nbrUnseen;
	}

}
