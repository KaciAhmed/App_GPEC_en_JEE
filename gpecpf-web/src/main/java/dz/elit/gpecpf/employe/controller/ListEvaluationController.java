package dz.elit.gpecpf.employe.controller;

import dz.elit.gpecpf.administration.entity.AdminUtilisateur;
import dz.elit.gpecpf.administration.entity.Notification;
import dz.elit.gpecpf.administration.service.AdminUtilisateurFacade;
import dz.elit.gpecpf.administration.service.NotificationFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.compagne_evaluation.entity.Compagneevaluation;
import dz.elit.gpecpf.employe.entity.Employe;
import dz.elit.gpecpf.evaluation.entity.Evaluation;
import dz.elit.gpecpf.evaluation.service.EvaluationFacade;
import dz.elit.gpecpf.gestion_compagne_evaluation.service.CompagneEvaluationFacade;
import dz.elit.gpecpf.gestion_employe.service.EmployeFacade;
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
public class ListEvaluationController extends AbstractController implements Serializable {

	@EJB
	EmployeFacade employeFacade;
	@EJB
	private AdminUtilisateurFacade utilisateurFacade;
	@EJB
	private EvaluationFacade evaluationFacade;
	@EJB
	private CompagneEvaluationFacade compagneFacade;
	@EJB
	private NotificationFacade notificationFacade;

	private AdminUtilisateur utilisateurCourant;
	private Employe employeCourant;

	private List<Evaluation> evaluations;
	private Compagneevaluation compagne;

	private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	@Override
	protected void initController() {
		utilisateurCourant = new AdminUtilisateur();
		employeCourant = new Employe();
		initUser();
		if (employeCourant != null) {
			evaluations = new ArrayList();
			evaluations = evaluationFacade.evaluationsForEmploye(employeCourant);
		}
		compagne = compagneFacade.currentCompagne();
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

	public boolean canAvis(Evaluation evaluation) {
		if (!evaluation.getCompagne().equals(compagne)) {
			return false;
		}
		return evaluation.getEtat() == 0;
	}

	public boolean canEdit(Boolean bool, Evaluation evaluation) {
		if (!bool) {
			return false;
		}
		if (!evaluation.getCompagne().equals(compagne)) {
			return false;
		}
		return evaluation.getEtat() == -1 && bool;
	}

	public boolean canEvaluate() {
		if (employeFacade.boss(employeCourant) == null) {
			return true;
		}
		if (compagne == null) {
			return true;
		}
		return evaluationFacade.evaluationCompagneEmploye(compagne, employeCourant) != null;
	}

	public String showDate(Evaluation evaluation) {
		if (evaluation.getEtat() == -2) {
			return "";
		}
		return formatDate(evaluation.getDateEvaluation());
	}

	public boolean canConfirm(Evaluation evaluation) {
		if (evaluation.getCompagne().equals(compagne)) {
			if (evaluation.getDateEvaluation() == null) {
				return false;
			}
			return evaluation.getEtat() == -1;
		}
		return false;
	}

	public String whatEtat(int x) {
		switch (x) {
			case 0:
				return "Avis Employé";
			case 1:
				return "Avis Supérieur 1";
			case 2:
				return "Vérification Supérieur 2";
			case 3:
				return "Vérification Supérieur 3";
			case -1:
				return "Attente date d'entretien";
			case -2:
				return "Date d'entretien à revoir";
			case -3:
				return "Attente de l'évaluation";
			case -4:
				return "Refusé par le Supérieur 2";
			case -5:
				return "Refusé par le Supérieur 3";
			case -6:
				return "Evaluation achevé";
		}
		return "";
	}

	public void confirmation(Evaluation eval, int x) throws Exception {
		try {
			Notification notif = new Notification();
			notif.setDateNotification(new Date());
			notif.setEmploye(eval.getSupN1());
			notif.setVerifiee(0);
			switch (x) {
				case -3:
					notif.setLibelle("L'employé " + eval.getEmploye().getNom() + " " + eval.getEmploye().getPrenom() + " viens de valider la date de l'entretien.");
					eval.setEtat(x);
					evaluationFacade.edit(eval);
					MyUtil.addInfoMessage(MyUtil.getBundleCommun("La date a été validée"));
					break;
				case -2:
					notif.setLibelle("L'employé " + eval.getEmploye().getNom() + " " + eval.getEmploye().getPrenom() + " demande une autre date.");
					eval.setEtat(x);
					evaluationFacade.edit(eval);
					MyUtil.addInfoMessage(MyUtil.getBundleCommun("La date à été refusée"));
					break;
				default:
					throw new MyException("Vous avez entré une donnée invalide");
			}
			notif.setLien("/pages/emp/evaluateur/listEval.xhtml");
			notificationFacade.create(notif);
		} catch (MyException e) {
			MyUtil.addErrorMessage(e.getMessage());
		} catch (Exception e) {
			MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));
		}
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

	public SimpleDateFormat getFormatter() {
		return formatter;
	}

	public EvaluationFacade getEvaluationFacade() {
		return evaluationFacade;
	}

	public void setEvaluationFacade(EvaluationFacade evaluationFacade) {
		this.evaluationFacade = evaluationFacade;
	}

	public List<Evaluation> getEvaluations() {
		return evaluations;
	}

	public void setEvaluations(List<Evaluation> evaluations) {
		this.evaluations = evaluations;
	}

	public CompagneEvaluationFacade getCompagneFacade() {
		return compagneFacade;
	}

	public void setCompagneFacade(CompagneEvaluationFacade compagneFacade) {
		this.compagneFacade = compagneFacade;
	}

	public Compagneevaluation getCompagne() {
		return compagne;
	}

	public void setCompagne(Compagneevaluation compagne) {
		this.compagne = compagne;
	}

}
