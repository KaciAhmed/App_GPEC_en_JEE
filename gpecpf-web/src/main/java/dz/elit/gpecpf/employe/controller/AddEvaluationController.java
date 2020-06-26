package dz.elit.gpecpf.employe.controller;

import dz.elit.gpecpf.administration.entity.AdminUtilisateur;
import dz.elit.gpecpf.administration.entity.Notification;
import dz.elit.gpecpf.administration.service.AdminUtilisateurFacade;
import dz.elit.gpecpf.administration.service.NotificationFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.compagne_evaluation.entity.Compagneevaluation;
import dz.elit.gpecpf.competence.entity.Competence;
import dz.elit.gpecpf.employe.entity.Employe;
import dz.elit.gpecpf.evaluation.entity.Evaluation;
import dz.elit.gpecpf.evaluation.entity.Note;
import dz.elit.gpecpf.evaluation.entity.NoteID;
import dz.elit.gpecpf.evaluation.service.EvaluationFacade;
import dz.elit.gpecpf.evaluation.service.NoteFacade;
import dz.elit.gpecpf.gestion_compagne_evaluation.service.CompagneEvaluationFacade;
import dz.elit.gpecpf.gestion_employe.service.EmployeFacade;
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
public class AddEvaluationController extends AbstractController implements Serializable {

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
	private Employe boss;

	private Compagneevaluation compagne;
	private Poste poste;

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
			poste = employeCourant.recupPosteEmploye();
			boss = employeFacade.boss(employeCourant);
			if (poste != null) {
				listNotes = new ArrayList<>();
				for (Competence comp : poste.getListCompetences()) {
					Note note = new Note();
					note.setCompetence(comp);
					note.setEvaluation(evaluation);
					listNotes.add(note);
				}
			}
		}

	}

	public void create() {
		try {
			for (Note note : listNotes) {
				if (note.getNoteEmploye() < 0 || note.getNoteEmploye() > 5) {
					throw new MyException("La note doit être comprise entre 0 et 5.");
				}
			}
			if (canEvaluate()) {
				throw new MyException("Evaluation impossible");
			}
			initEvalData();
			evaluationFacade.create(evaluation);
			Notification notif = new Notification();
			notif.setDateNotification(new Date());
			notif.setEmploye(evaluation.getSupN1());
			notif.setVerifiee(0);
			notif.setLibelle("L'employé " + evaluation.getEmploye().getNom() + " " + evaluation.getEmploye().getPrenom() + " viens de s'auto-évaluer.");
			notif.setLien("/pages/emp/evaluateur/listEval.xhtml");
			notificationFacade.create(notif);
			for (Note note : listNotes) {
				note.setId(new NoteID(note.getCompetence().getId(), evaluation.getId()));
				noteFacade.create(note);
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

	private void initEvalData() {
		evaluation.setCompagne(compagne);
		evaluation.setEmploye(employeCourant);
		evaluation.setEtat(-1);
		if (boss != null) {
			evaluation.setSupN1(boss);
			Employe boss2 = employeFacade.boss(boss);
			if (boss2 != null) {
				evaluation.setSupN2(boss2);
				evaluation.setSupN3(employeFacade.boss(boss2));
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

	public boolean canEvaluate() {
		if (boss == null) {
			return true;
		}
		if (compagne == null) {
			return true;
		}
		return evaluationFacade.evaluationCompagneEmploye(compagne, employeCourant) != null;
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

	public Poste getPoste() {
		return poste;
	}

	public void setPoste(Poste poste) {
		this.poste = poste;
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

	public Employe getBoss() {
		return boss;
	}

	public void setBoss(Employe boss) {
		this.boss = boss;
	}

}
