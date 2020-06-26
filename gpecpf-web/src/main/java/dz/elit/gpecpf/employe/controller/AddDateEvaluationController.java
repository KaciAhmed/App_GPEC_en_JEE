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
public class AddDateEvaluationController extends AbstractController implements Serializable {

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

	private Compagneevaluation compagne;
	private List<Evaluation> evaluations;

	private Evaluation evaluation;
	private Date oldDate;

	private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	@Override
	protected void initController() {
		utilisateurCourant = new AdminUtilisateur();
		employeCourant = new Employe();
		initUser();
		if (employeCourant != null) {
			evaluations = new ArrayList();
			evaluations = evaluationFacade.evaluationsForSupN1(employeCourant);
		}
		compagne = compagneFacade.currentCompagne();
		String id = MyUtil.getRequestParameter("id");
		if (id != null) {
			evaluation = evaluationFacade.find(Integer.parseInt(id));
			if (evaluation.getEtat() == -2) {
				oldDate = evaluation.getDateEvaluation();
			}
			if (!evaluations.contains(evaluation)) {
				evaluation = null;
			}
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

	public void create() {
		try {
			if (evaluation.getEtat() == -2) {
				if (formatDate(evaluation.getDateEvaluation()).equals(formatDate(oldDate))) {
					throw new MyException("La nouvelle date doit être différente de l'ancienne date " + formatDate(oldDate));
				}
			}
			if (evaluation.getDateEvaluation().before(new Date())) {
				evaluation.setEtat(-2);
				throw new MyException("La date doit etre supérieure à celle d'ajourd'hui");
			} else if (evaluation.getDateEvaluation().before(compagne.getDatedeb())) {
				evaluation.setEtat(-2);
				throw new MyException("La date doit etre supérieure à la date début de compagne");
			} else if (evaluation.getDateEvaluation().after(compagne.getDatefin())) {
				evaluation.setEtat(-2);
				throw new MyException("La date doit etre inférieure à la date fin de compagne");
			}
			Notification notif = new Notification();
			notif.setDateNotification(new Date());
			notif.setEmploye(evaluation.getEmploye());
			notif.setVerifiee(0);
			notif.setLibelle("Veillez vérifier la date d'évaluation.");
			notif.setLien("/pages/emp/evaluation/listEval.xhtml");
			notificationFacade.create(notif);
			evaluation.setEtat(-1);
			evaluationFacade.edit(evaluation);
			MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));
		} catch (MyException ex) {
			MyUtil.addErrorMessage(ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));
		}
	}

	public boolean canDate() {
		if (evaluation == null) {
			return true;
		}
		if (compagne == null) {
			return true;
		}
		if (!evaluation.getCompagne().equals(compagne)) {
			return true;
		}
		if (evaluation.getDateEvaluation() == null) {
			return false;
		} else {
			return evaluation.getEtat() != -2;
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

	public Evaluation getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(Evaluation evaluation) {
		this.evaluation = evaluation;
	}

}
