package dz.elit.gpecpf.employe.controller;

import dz.elit.gpecpf.administration.entity.AdminUtilisateur;
import dz.elit.gpecpf.administration.service.AdminUtilisateurFacade;
import dz.elit.gpecpf.commun.util.AbstractController;
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
public class ListEmployeEvaluationController extends AbstractController implements Serializable {

	@EJB
	EmployeFacade employeFacade;
	@EJB
	private AdminUtilisateurFacade utilisateurFacade;
	@EJB
	private EvaluationFacade evaluationFacade;
	@EJB
	private CompagneEvaluationFacade compagneFacade;

	private AdminUtilisateur utilisateurCourant;
	private Employe employeCourant;

	private Compagneevaluation compagne;
	private List<Evaluation> evaluationsN1;
	private List<Evaluation> evaluationsN2;
	private List<Evaluation> evaluationsN3;

	private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	@Override
	protected void initController() {
		utilisateurCourant = new AdminUtilisateur();
		employeCourant = new Employe();
		initUser();
		if (employeCourant != null) {
			evaluationsN1 = new ArrayList();
			evaluationsN1 = evaluationFacade.evaluationsForSupN1(employeCourant);
			evaluationsN2 = new ArrayList();
			evaluationsN2 = evaluationFacade.evaluationsForSupN2(employeCourant);
			evaluationsN3 = new ArrayList();
			evaluationsN3 = evaluationFacade.evaluationsForSupN3(employeCourant);
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

	public boolean canEval(Evaluation evaluation) {
		if (compagne == null) {
			return false;
		}
		if (!evaluation.getCompagne().equals(compagne)) {
			return false;
		}
		if (evaluation.getSupN1().equals(employeCourant)) {
			return evaluation.getEtat() == -3;
		}
		return false;
	}

	public boolean canEdit(Evaluation evaluation) {
		if (compagne == null) {
			return false;
		}
		if (!evaluation.getCompagne().equals(compagne)) {
			return false;
		}
		if (evaluation.getSupN1().equals(employeCourant)) {
			return evaluation.getEtat() == 1;
		}
		if (evaluation.getSupN2().equals(employeCourant)) {
			return evaluation.getEtat() == 2;
		}
		if (evaluation.getSupN3().equals(employeCourant)) {
			return evaluation.getEtat() == 3;
		}
		return false;
	}

	public boolean canDate(Evaluation evaluation) {
		if (compagne == null) {
			return false;
		}
		if (!evaluation.getCompagne().equals(compagne)) {
			return false;
		}
		if (evaluation.getDateEvaluation() == null) {
			return true;
		}
		return evaluation.getEtat() == -2;
	}

	public String willDate(Evaluation evaluation) {
		String x = "";
		if (evaluation.getDateEvaluation() == null) {
			return "";
		}
		int a = evaluation.getEtat();
		switch (a) {
			case -1:
				x = " (En attente)";
				break;
			case -2:
				x = " (Rejetée)";
				break;
			case -3:
				x = " (Validée)";
				break;
		}
		return formatDate(evaluation.getDateEvaluation()) + x;
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

	public List<Evaluation> getEvaluationsN1() {
		return evaluationsN1;
	}

	public void setEvaluationsN1(List<Evaluation> evaluationsN1) {
		this.evaluationsN1 = evaluationsN1;
	}

	public List<Evaluation> getEvaluationsN2() {
		return evaluationsN2;
	}

	public void setEvaluationsN2(List<Evaluation> evaluationsN2) {
		this.evaluationsN2 = evaluationsN2;
	}

	public List<Evaluation> getEvaluationsN3() {
		return evaluationsN3;
	}

	public void setEvaluationsN3(List<Evaluation> evaluationsN3) {
		this.evaluationsN3 = evaluationsN3;
	}

}
