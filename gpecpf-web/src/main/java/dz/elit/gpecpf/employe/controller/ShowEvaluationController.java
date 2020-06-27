package dz.elit.gpecpf.employe.controller;

import dz.elit.gpecpf.administration.entity.AdminUtilisateur;
import dz.elit.gpecpf.administration.service.AdminUtilisateurFacade;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.competence.service.CompetenceFacade;
import dz.elit.gpecpf.employe.entity.Employe;
import dz.elit.gpecpf.evaluation.entity.Avis;
import dz.elit.gpecpf.evaluation.entity.Evaluation;
import dz.elit.gpecpf.evaluation.entity.Note;
import dz.elit.gpecpf.evaluation.service.AvisFacade;
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
public class ShowEvaluationController extends AbstractController implements Serializable {

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
	private AvisFacade avisFacade;
	@EJB
	private CompetenceFacade competenceFacade;

	private AdminUtilisateur utilisateurCourant;
	private Employe employeCourant;
	private Employe monEmploye;
	private Poste poste;

	private Evaluation evaluation;
	private List<Note> listNotes;
	private List<Avis> listAvis;

	private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	@Override
	protected void initController() {
		utilisateurCourant = new AdminUtilisateur();
		employeCourant = new Employe();
		initUser();
		evaluation = new Evaluation();
		if (employeCourant != null) {
			String id = MyUtil.getRequestParameter("id");
			if (id != null) {
				evaluation = evaluationFacade.find(Integer.parseInt(id));
				monEmploye = evaluation.getEmploye();
				poste = monEmploye.recupPosteEmploye();
				if (!evaluation.getEmploye().equals(employeCourant) && !evaluation.getSupN1().equals(employeCourant) && !evaluation.getSupN2().equals(employeCourant) && !evaluation.getSupN3().equals(employeCourant)) {
					evaluation = null;
				}
				listNotes = new ArrayList<>();
				listAvis = new ArrayList<>();
				if (evaluation != null) {
					listNotes = noteFacade.notesForEvaluation(evaluation);
					listAvis = avisFacade.avisForEvaluation(evaluation);
				}
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

	public String getNameAvis(int type) {
		switch (type) {
			case 0:
				return evaluation.getEmploye().getNom();
			case 1:
				return evaluation.getSupN1().getNom();
			case 2:
				return evaluation.getSupN2().getNom();
			case 3:
				return evaluation.getSupN3().getNom();
			default:
				return "";
		}
	}

	public String getPrenomAvis(int type) {
		switch (type) {
			case 0:
				return evaluation.getEmploye().getPrenom();
			case 1:
				return evaluation.getSupN1().getPrenom();
			case 2:
				return evaluation.getSupN2().getPrenom();
			case 3:
				return evaluation.getSupN3().getPrenom();
			default:
				return "";
		}
	}

	public String getPositivity(int pos) {
		return (pos == 1) ? "Positif" : "Négatif";
	}

	public String getTypeAvis(int type) {
		switch (type) {
			case 0:
				return "Employé";
			case 1:
				return "N+1";
			case 2:
				return "N+2";
			case 3:
				return "N+3";
			default:
				return "";
		}
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

	public NoteFacade getNoteFacade() {
		return noteFacade;
	}

	public void setNoteFacade(NoteFacade noteFacade) {
		this.noteFacade = noteFacade;
	}

	public AvisFacade getAvisFacade() {
		return avisFacade;
	}

	public void setAvisFacade(AvisFacade avisFacade) {
		this.avisFacade = avisFacade;
	}

	public List<Avis> getListAvis() {
		return listAvis;
	}

	public void setListAvis(List<Avis> listAvis) {
		this.listAvis = listAvis;
	}

	public CompetenceFacade getCompetenceFacade() {
		return competenceFacade;
	}

	public void setCompetenceFacade(CompetenceFacade competenceFacade) {
		this.competenceFacade = competenceFacade;
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
