package dz.elit.gpecpf.compagneEvaluation.controller;

import dz.elit.gpecpf.administration.entity.Notification;
import dz.elit.gpecpf.administration.service.AdminPrefixCodificationFacade;
import dz.elit.gpecpf.administration.service.NotificationFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.compagne_evaluation.entity.Compagneevaluation;
import dz.elit.gpecpf.employe.entity.Employe;
import dz.elit.gpecpf.gestion_compagne_evaluation.service.CompagneEvaluationFacade;
import dz.elit.gpecpf.gestion_employe.service.EmployeFacade;
import dz.elit.gpecpf.poste.entity.Poste;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Kaci Ahmed
 */
@ManagedBean
@ViewScoped
public class AddCompagneEvaluationController extends AbstractController implements Serializable {

	@EJB
	private CompagneEvaluationFacade compagneFacade;

	@EJB
	private AdminPrefixCodificationFacade prefixFacade;
	@EJB
	private EmployeFacade employeFacade;
	@EJB
	private NotificationFacade notificationFacade;

	private Compagneevaluation compagne;

	private String codePrefix;

	public AddCompagneEvaluationController() {
	}

	@Override//@PostConstruct
	protected void initController() {
		initAddCompagneEvaluation();
	}

	private void initAddCompagneEvaluation() {
		compagne = new Compagneevaluation();
		try {
			codePrefix = prefixFacade.chercherPrefix().getCompeva();
		} catch (Exception e) {
			codePrefix = "";
		}
		compagne.setCode(codePrefix);
	}

	private boolean isVerifier() {

		if (compagne.getDatedeb().after(compagne.getDatefin())) {
			MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_date_debut_superieur_date_fin"));
			return false;
		}

		return true;
	}

	public void create() {
		try {
			boolean noNew = compagneFacade.activeCompagne();
			if (noNew) {
				throw new MyException("Il ne peut pas y avoir plus d'une compagne active.");
			}
			checkCode();
			if (isVerifier()) {
				compagne.setActive(1);
				compagne.setCode(compagne.getCode().toUpperCase());
				compagneFacade.create(compagne);
				List<Employe> employes = employeFacade.findAllOrderByAttribut("id");
				for (Employe employe : employes) {
					Poste poste = employe.recupPosteEmploye();
					if (poste != null) {
						Notification notif = new Notification();
						notif.setDateNotification(new Date());
						notif.setEmploye(employe);
						notif.setVerifiee(0);
						notif.setLibelle("La compagne d'évaluation viens de commencer");
						notif.setLien("/pages/emp/evaluation/listEval.xhtml");
						try {
							notificationFacade.create(notif);
						} catch (Exception ex) {

						}
					}
				}
				initAddCompagneEvaluation();
				MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//Compagne évaluation enregistré avec succèe
			}

		} catch (MyException ex) {
			MyUtil.addErrorMessage(ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
		}

	}

	public void checkCode() throws MyException {
		if (!codePrefix.equals("")) {
			if (!compagne.getCode().startsWith(codePrefix) || compagne.getCode().equals(codePrefix)) {
				throw new MyException("Le code doit commencer avec le prefix: " + codePrefix + " et suivi d'une chaine.");
			}
		}
	}

	// getter && setter 
	public Compagneevaluation getCompagne() {
		return compagne;
	}

	public void setCompagne(Compagneevaluation compagne) {
		this.compagne = compagne;
	}

	public CompagneEvaluationFacade getCompagneFacade() {
		return compagneFacade;
	}

	public void setCompagneFacade(CompagneEvaluationFacade compagneFacade) {
		this.compagneFacade = compagneFacade;
	}

	public AdminPrefixCodificationFacade getPrefixFacade() {
		return prefixFacade;
	}

	public void setPrefixFacade(AdminPrefixCodificationFacade prefixFacade) {
		this.prefixFacade = prefixFacade;
	}

	public String getCodePrefix() {
		return codePrefix;
	}

	public void setCodePrefix(String codePrefix) {
		this.codePrefix = codePrefix;
	}

}
