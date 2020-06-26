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
public class AddAvisController extends AbstractController implements Serializable {

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
	private NotificationFacade notificationFacade;

	private AdminUtilisateur utilisateurCourant;
	private Employe employeCourant;
	private Employe monEmploye;
	private Poste poste;

	private Compagneevaluation compagne;

	private Evaluation evaluation;
	private Avis avis;
	private List<Note> listNotes;
	private List<Avis> listAvis;

	private boolean done = false;
	private boolean error = false;
	private boolean noMore = false;

	private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	@Override
	protected void initController() {
		utilisateurCourant = new AdminUtilisateur();
		employeCourant = new Employe();
		initUser();
		evaluation = new Evaluation();
		compagne = compagneFacade.currentCompagne();
		avis = new Avis();
		if (employeCourant != null) {
			String id = MyUtil.getRequestParameter("id");
			if (id != null) {
				evaluation = evaluationFacade.find(Integer.parseInt(id));
				monEmploye = evaluation.getEmploye();
				poste = monEmploye.recupPosteEmploye();
				int x = evaluation.getEtat();
				List<Evaluation> listEval = null;
				switch (x) {
					case 0:
						listEval = evaluationFacade.evaluationsForEmploye(employeCourant);
						if (!employeCourant.equals(evaluation.getEmploye())) {
							error = true;
						}
						break;
					case 1:
						listEval = evaluationFacade.evaluationsForSupN1(employeCourant);
						if (!employeCourant.equals(evaluation.getSupN1())) {
							error = true;
						}
						if (evaluation.getSupN2() == null) {
							noMore = true;
						}
						break;
					case 2:
						listEval = evaluationFacade.evaluationsForSupN2(employeCourant);
						if (!employeCourant.equals(evaluation.getSupN2())) {
							error = true;
						}
						if (evaluation.getSupN3() == null) {
							noMore = true;
						}
						break;
					case 3:
						listEval = evaluationFacade.evaluationsForSupN3(employeCourant);
						if (!employeCourant.equals(evaluation.getSupN3())) {
							error = true;
						}
						noMore = true;
						break;
					default:
						error = true;
						break;
				}
				if (listEval == null) {
					evaluation = null;
				} else if (!listEval.contains(evaluation)) {
					evaluation = null;
				}
				listNotes = new ArrayList<>();
				listAvis = new ArrayList<>();
				if (evaluation != null) {
					listNotes = noteFacade.notesForEvaluation(evaluation);
					listAvis = avisFacade.avisForEvaluation(evaluation);
				} else {
					error = true;
				}
			} else {
				error = true;
			}
		} else {
			error = true;
		}

	}

	public void create(int type) {
		try {
			if (error) {
				throw new MyException("Avis impossible");
			}
			avis.setType(evaluation.getEtat());
			if (type == 1) {
				if (noMore) {
					evaluation.setEtat(-6);
				} else {
					evaluation.setEtat(evaluation.getEtat() + 1);
				}
				avis.setPositif(1);
			} else if (type == 0) {
				switch (evaluation.getEtat()) {
					case 0:
						evaluation.setEtat(evaluation.getEtat() + 1);
						break;
					case 1:
						throw new MyException("Erreur Inconnu");
					case 2:
						evaluation.setEtat(-4);
						evaluation.setDateEvaluation(null);
						break;
					case 3:
						evaluation.setEtat(-5);
						evaluation.setDateEvaluation(null);
						break;
				}
				avis.setPositif(0);
			}
			Notification notif = new Notification();
			switch (avis.getType()) {
				case 0:
					notif.setDateNotification(new Date());
					notif.setEmploye(evaluation.getSupN1());
					notif.setVerifiee(0);
					notif.setLibelle("L'employé " + evaluation.getEmploye().getNom() + " " + evaluation.getEmploye().getPrenom() + " a rajouté son avis.");
					notif.setLien("/pages/emp/evaluateur/listEval.xhtml");
					notificationFacade.create(notif);
					break;
				case 1:
					if (noMore) {
						notif = new Notification();
						notif.setDateNotification(new Date());
						notif.setEmploye(evaluation.getEmploye());
						notif.setVerifiee(0);
						notif.setLibelle("L'évaluation est terminé.");
						notif.setLien("/pages/emp/evaluation/listEval.xhtml");
						notificationFacade.create(notif);
					} else {
						notif = new Notification();
						notif.setDateNotification(new Date());
						notif.setEmploye(evaluation.getSupN2());
						notif.setVerifiee(0);
						notif.setLibelle("Attente d'une vérification pour l'évaluation de l'employé " + evaluation.getEmploye().getNom() + " " + evaluation.getEmploye().getPrenom() + ".");
						notif.setLien("/pages/emp/verification/listEval.xhtml");
						notificationFacade.create(notif);
					}
					break;
				case 2:
					if (type == 0) {
						notif = new Notification();
						notif.setDateNotification(new Date());
						notif.setVerifiee(0);
						notif.setEmploye(evaluation.getEmploye());
						notif.setLien("/pages/emp/evaluation/listEval.xhtml");
						notif.setLibelle("L'évaluation va se refaire.");
						notificationFacade.create(notif);

						notif = new Notification();
						notif.setDateNotification(new Date());
						notif.setVerifiee(0);
						notif.setEmploye(evaluation.getSupN1());
						notif.setLien("/pages/emp/evaluateur/listEval.xhtml");
						notif.setLibelle("L'évaluation de l'employé " + evaluation.getEmploye().getNom() + " " + evaluation.getEmploye().getPrenom() + " est à refaire.");
						notificationFacade.create(notif);
					} else if (noMore) {
						notif = new Notification();
						notif.setDateNotification(new Date());
						notif.setEmploye(evaluation.getEmploye());
						notif.setVerifiee(0);
						notif.setLibelle("L'évaluation est terminé.");
						notif.setLien("/pages/emp/evaluation/listEval.xhtml");
						notificationFacade.create(notif);
					} else {
						notif = new Notification();
						notif.setDateNotification(new Date());
						notif.setEmploye(evaluation.getSupN3());
						notif.setVerifiee(0);
						notif.setLibelle("Attente d'une vérification pour l'évaluation de l'employé " + evaluation.getEmploye().getNom() + " " + evaluation.getEmploye().getPrenom() + ".");
						notif.setLien("/pages/emp/verification/listEval.xhtml");
						notificationFacade.create(notif);
					}
					break;
				case 3:
					if (type == 0) {
						notif = new Notification();
						notif.setDateNotification(new Date());
						notif.setVerifiee(0);
						notif.setEmploye(evaluation.getEmploye());
						notif.setLien("/pages/emp/evaluateur/listEval.xhtml");
						notif.setLibelle("L'évaluation va se refaire.");
						notificationFacade.create(notif);

						notif = new Notification();
						notif.setDateNotification(new Date());
						notif.setVerifiee(0);
						notif.setEmploye(evaluation.getSupN1());
						notif.setLien("/pages/emp/evaluation/listEval.xhtml");
						notif.setLibelle("L'évaluation de l'employé " + evaluation.getEmploye().getNom() + " " + evaluation.getEmploye().getPrenom() + " est à refaire.");
						notificationFacade.create(notif);
					} else {
						notif = new Notification();
						notif.setDateNotification(new Date());
						notif.setEmploye(evaluation.getEmploye());
						notif.setVerifiee(0);
						notif.setLibelle("L'évaluation est terminé.");
						notif.setLien("/pages/emp/evaluation/listEval.xhtml");
						notificationFacade.create(notif);
					}
					break;
			}
			avis.setEvaluation(evaluation);
			evaluationFacade.edit(evaluation);
			avisFacade.edit(avis);
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

	public boolean onlyYes() {
		if (employeCourant == null || evaluation == null) {
			return false;
		}
		return !employeCourant.equals(evaluation.getSupN1());
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

	public Avis getAvis() {
		return avis;
	}

	public void setAvis(Avis avis) {
		this.avis = avis;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public boolean isNoMore() {
		return noMore;
	}

	public void setNoMore(boolean noMore) {
		this.noMore = noMore;
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
