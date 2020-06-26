package dz.elit.gpecpf.compagneEvaluation.controller;

import dz.elit.gpecpf.administration.service.AdminPrefixCodificationFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.compagne_evaluation.entity.Compagneevaluation;
import dz.elit.gpecpf.gestion_compagne_evaluation.service.CompagneEvaluationFacade;
import java.io.Serializable;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Kaci Ahmed
 */
@ManagedBean
@ViewScoped
public class EditCompagneEvaluationController extends AbstractController implements Serializable {
	
	@EJB
	private CompagneEvaluationFacade compagneFacade;
	@EJB
	private AdminPrefixCodificationFacade prefixFacade;
	
	private Compagneevaluation compagne;
	
	String oldCode;
	
	private String codePrefix;
	
	@Override//@PostConstruct
	protected void initController() {
		try {
			codePrefix = prefixFacade.chercherPrefix().getCompeva();
		} catch (Exception e) {
			codePrefix = "";
		}
		String id = MyUtil.getRequestParameter("id");
		if (id != null) {
			compagne = compagneFacade.find(Integer.parseInt(id));
			oldCode = compagne.getCode();
		}
	}
	
	private boolean isExisteCode(String code) {
		Compagneevaluation compagne2 = compagneFacade.findByCode(code);
		if (compagne2 == null) {
			return false;
		} else {
			return true;
		}
	}
	
	private boolean isVerifier() {
		
		if (compagne.getDatedeb().after(compagne.getDatefin())) {
			MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_date_debut_superieur_date_fin"));
			return false;
		}
		
		return true;
	}
	
	public void edit() {
		try {
			boolean update = compagneFacade.activeCompagne();
			Compagneevaluation x = compagneFacade.currentCompagne();
			if (compagne.getDatefin().after(new Date())) {
				compagne.setActive(1);
			}
			if (compagne.getDatefin().before(new Date())) {
				compagne.setActive(0);
			}
			if (update && compagne.getActive() == 1 && !compagne.equals(x)) {
				throw new MyException("Il ne peut pas y avoir plus d'une compagne active.");
			}
			checkCode();
			compagne.setCode(compagne.getCode().toUpperCase());
			if (isExisteCode(compagne.getCode()) && !compagne.getCode().equals(oldCode)) {
				MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_existe_code"));//Erreur inconu   
			} else if (isVerifier()) {
				compagneFacade.edit(compagne);
				oldCode = compagne.getCode();
				MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//"Compagne modifié avec succès");
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
	
	public String getOldCode() {
		return oldCode;
	}
	
	public void setOldCode(String oldCode) {
		this.oldCode = oldCode;
	}
	
	public String getCodePrefix() {
		return codePrefix;
	}
	
	public void setCodePrefix(String codePrefix) {
		this.codePrefix = codePrefix;
	}
	
}
