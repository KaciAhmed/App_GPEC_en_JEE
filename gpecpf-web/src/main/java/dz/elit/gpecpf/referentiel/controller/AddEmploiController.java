package dz.elit.gpecpf.referentiel.controller;

import dz.elit.gpecpf.administration.service.AdminPrefixCodificationFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.poste.entity.Emploi;
import dz.elit.gpecpf.poste.service.EmploiFacade;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Nadir Ben Mohand
 */
@ManagedBean
@ViewScoped
public class AddEmploiController extends AbstractController implements Serializable {

	@EJB
	private EmploiFacade emploiFacade;
	@EJB
	private AdminPrefixCodificationFacade prefixFacade;

	private Emploi emploi;

	private String code;
	private String libelle;
	private String description;

	private String codePrefix;

	public AddEmploiController() {
	}

	@Override//@PostConstruct
	protected void initController() {
		initAddEmploi();
	}

	public void create() {
		try {
			checkCode();
			emploiFacade.create(emploi);
			MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));
			initAddEmploi();
		} catch (MyException ex) {
			ex.printStackTrace();
			MyUtil.addErrorMessage(ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
		}
	}

	public void checkCode() throws MyException {
		if (!codePrefix.equals("")) {
			if (!emploi.getCode().startsWith(codePrefix) || emploi.getCode().equals(codePrefix)) {
				throw new MyException("Le code doit commencer avec le prefix: " + codePrefix + " et suivi d'une chaine.");
			}
		}
	}

	private void initAddEmploi() {
		emploi = new Emploi();
		try {
			codePrefix = prefixFacade.chercherPrefix().getEmploi();
		} catch (Exception e) {
			codePrefix = "";
		}
		emploi.setCode(codePrefix);
	}

	public String getCode() {
		return code;
	}

	public String getLibelle() {
		return libelle;
	}

	public String getDescription() {
		return description;
	}

	public Emploi getEmploi() {
		return emploi;
	}

	public EmploiFacade getEmploiFacade() {
		return emploiFacade;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setEmploi(Emploi emploi) {
		this.emploi = emploi;
	}

	public void setEmploiFacade(EmploiFacade emploiFacade) {
		this.emploiFacade = emploiFacade;
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
