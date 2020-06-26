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
import dz.elit.gpecpf.evaluation.entity.Note;
import dz.elit.gpecpf.evaluation.service.EvaluationFacade;
import dz.elit.gpecpf.evaluation.service.NoteFacade;
import dz.elit.gpecpf.gestion_compagne_evaluation.service.CompagneEvaluationFacade;
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
public class AddEmployeEvaluationController extends AbstractController implements Serializable {

	@EJB
	EmployeFacade employeFacade;
	@EJB
	private AdminUtilisateurFacade utilisateurFacade;
	@EJB
	private EvaluationFacade evaluationFacade;
	@EJB
	private CompagneEvaluationFacade compagneFacade;
	@EJB
	private NoteFacade noteFacade;
	@EJB
	private NotificationFacade notificationFacade;

	private AdminUtilisateur utilisateurCourant;
	private Employe employeCourant;
	private Employe monEmploye;
	private Poste poste;

	private Compagneevaluation compagne;

	private Evaluation evaluation;
	private List<Note> listNotes;

	private boolean done = false;

	private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	@Override
	protected void initController() {
		utilisateurCourant = new AdminUtilisateur();
		employeCourant = new Employe();
		initUser();
		evaluation = new Evaluation();
		compagne = compagneFacade.currentCompagne();
		if (employeCourant != null) {
			String id = MyUtil.getRequestParameter("id");
			if (id != null) {
				evaluation = evaluationFacade.find(Integer.parseInt(id));
				monEmploye = evaluation.getEmploye();
				poste = monEmploye.recupPosteEmploye();
				List<Evaluation> listEval = evaluationFacade.evaluationsForSupN1(employeCourant);
				if (!listEval.contains(evaluation)) {
					evaluation = null;
				}
				listNotes = new ArrayList<>();
				if (evaluation != null) {
					listNotes = noteFacade.notesForEvaluation(evaluation);
				}
			}
		}

	}

	public void create() {
		try {
			boolean developpementMode = true;
			if (evaluation.getEtat() != -3) {
				throw new MyException("Evaluation impossible");
			}
			if (!developpementMode && evaluation.getDateEvaluation().after(new Date())) {
				throw new MyException("Evaluation impossible");
			}
			for (Note note : listNotes) {
				if (note.getNoteSuperieure() < 0 || note.getNoteSuperieure() > 5) {
					throw new MyException("La note doit être comprise entre 0 et 5.");
				}
			}
			Notification notif = new Notification();
			notif.setDateNotification(new Date());
			notif.setEmploye(evaluation.getEmploye());
			notif.setVerifiee(0);
			notif.setLibelle("Votre fiche d'évaluation viens d'être remplis.");
			notif.setLien("/pages/emp/evaluation/listEval.xhtml");
			notificationFacade.create(notif);
			evaluation.setEtat(0);
			evaluationFacade.edit(evaluation);
			for (Note note : listNotes) {
				noteFacade.edit(note);
			}
			MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));
			done = true;
		} catch (MyException ex) {
			MyUtil.addErrorMessage(ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));
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
		if (monEmploye == null) {
			return null;
		}
		Historiqueemployeposte hist2 = new Historiqueemployeposte();
		List<Historiqueemployeposte> listHistEmpOrdone = new ArrayList<>();
		for (Historiqueemployeposte hist : monEmploye.getListHistoriqueEmployePoste()) {
			if (hist.getDatefin() != null && (hist.getHistoriqueemployepostePK().getDatedeb().equals(hist.getDatefin()) || hist.getHistoriqueemployepostePK().getDatedeb().after(hist.getDatefin()))) {
				// rien faire
			} else {
				if (hist.getDatefin() == null || hist.getDatefin().equals(monEmploye.getDate_depart())) {
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

	public Evaluation getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(Evaluation evaluation) {
		this.evaluation = evaluation;
	}

	public List<Note> getListNotes() {
		return listNotes;
	}

	public void setListNotes(List<Note> listNotes) {
		this.listNotes = listNotes;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public NoteFacade getNoteFacade() {
		return noteFacade;
	}

	public void setNoteFacade(NoteFacade noteFacade) {
		this.noteFacade = noteFacade;
	}

	public Employe getMonEmploye() {
		return monEmploye;
	}

	public void setMonEmploye(Employe monEmploye) {
		this.monEmploye = monEmploye;
	}

	public Poste getPoste() {
		return poste;
	}

	public void setPoste(Poste poste) {
		this.poste = poste;
	}

}
