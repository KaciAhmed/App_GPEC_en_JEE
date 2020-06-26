package dz.elit.gpecpf.competence.controleur;

import dz.elit.gpecpf.administration.service.AdminPrefixCodificationFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.competence.entity.Domainecompetence;
import dz.elit.gpecpf.competence.service.DomaineCompetenceFacade;

import java.io.Serializable;
import java.util.ArrayList;
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
public class EditDomaineCompetenceController extends AbstractController implements Serializable {

	@EJB
	private DomaineCompetenceFacade domaineCompFacade;
	@EJB
	private AdminPrefixCodificationFacade prefixFacade;

	private Domainecompetence domaine;
	private List<Domainecompetence> lstDomPere;
	private Domainecompetence domPereSelected;
	private Domainecompetence oldDomPere;
	private List<Domainecompetence> listeSousDomaine;
	private Domainecompetence oldDomPereForEdit;
	private Domainecompetence newDomPereForEdit;

	String oldCode;

	private String codePere;
	private String libPere;

	private String codePrefix;

	@Override//@PostConstruct
	protected void initController() {
		lstDomPere = new ArrayList<>();
		lstDomPere = domaineCompFacade.findAllOrderByAttribut("code");
		try {
			codePrefix = prefixFacade.chercherPrefix().getDomcomp();
		} catch (Exception e) {
			codePrefix = "";
		}
		String id = MyUtil.getRequestParameter("id");
		if (id != null) {
			domaine = domaineCompFacade.find(Integer.parseInt(id));
			initEdition();
			if (domaine.getDomainecompetenceCollection() != null && !domaine.getDomainecompetenceCollection().isEmpty()) {
				for (Domainecompetence d : domaine.getDomainecompetenceCollection()) {
					listeSousDomaine.add(d);
				}
			}
		}
	}

	public void checkCode() throws MyException {
		if (!codePrefix.equals("")) {
			if (!domaine.getCode().startsWith(codePrefix) || domaine.getCode().equals(codePrefix)) {
				throw new MyException("Le code doit commencer avec le prefix: " + codePrefix + " et suivi d'une chaine.");
			}
		}
	}

	private void initEdition() {
		domPereSelected = domaine.getIddommere();
		oldDomPere = domaine.getIddommere();
		lstDomPere.remove(domaine);
		oldCode = domaine.getCode();
		listeSousDomaine = new ArrayList<>();
		oldDomPereForEdit = null;
		newDomPereForEdit = null;
	}

	private boolean isExisteCode(String code) {
		Domainecompetence domaine2 = domaineCompFacade.findByCode(code);
		if (domaine2 == null) {
			return false;
		} else {
			return true;
		}
	}

	public void edit() {
		try {
			checkCode();
			domaine.setCode(domaine.getCode().toUpperCase());
			if (isExisteCode(domaine.getCode()) && !domaine.getCode().equals(oldCode)) {
				MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_existe_code"));//Erreur inconu   
			} else {
				domaineCompFacade.edit(domaine);
				MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//"Domaine modifié avec succès");
				if (oldDomPereForEdit != null) {
					domaineCompFacade.edit(oldDomPereForEdit);
				}
				if (newDomPereForEdit != null) {
					domaineCompFacade.edit(newDomPereForEdit);
				}
				initEdition();
			}
		} catch (MyException ex) {
			MyUtil.addErrorMessage(ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu

		}
	}

	public void chercherDomPere() {
		lstDomPere = domaineCompFacade.findByCodeLibelle(codePere, libPere);
	}

	public void editDomPereForDom() {
		if (domPereSelected == null && oldDomPere != null) {
			oldDomPere.getDomainecompetenceCollection().remove(domaine);
			domaine.setIddommere(null);
			oldDomPereForEdit = oldDomPere;
			oldDomPere = domPereSelected;
		} else {
			if (oldDomPere == null && domPereSelected != null) {
				domaine.addDomPere(domPereSelected);
				newDomPereForEdit = domPereSelected;
				oldDomPere = domPereSelected;
			} else {
				if (!domPereSelected.getCode().equals(oldDomPere.getCode())) {
					domaine.editDomPere(domPereSelected, oldDomPere);
					oldDomPereForEdit = oldDomPere;
					newDomPereForEdit = domPereSelected;
					oldDomPere = domPereSelected;
				}
			}
		}
	}

	public void viderDompere() {
		domPereSelected = null;
	}

	// Getter && setter
	public Domainecompetence getDomaine() {
		return domaine;
	}

	public void setDomaine(Domainecompetence domaine) {
		this.domaine = domaine;
	}

	public String getCodePere() {
		return codePere;
	}

	public void setCodePere(String codePere) {
		this.codePere = codePere;
	}

	public String getLibPere() {
		return libPere;
	}

	public void setLibPere(String libPere) {
		this.libPere = libPere;
	}

	public List<Domainecompetence> getLstDomPere() {
		return lstDomPere;
	}

	public void setLstDomPere(List<Domainecompetence> lstDomPere) {
		this.lstDomPere = lstDomPere;
	}

	public Domainecompetence getDomPereSelected() {
		return domPereSelected;
	}

	public void setDomPereSelected(Domainecompetence domPereSelected) {
		this.domPereSelected = domPereSelected;
	}

	public DomaineCompetenceFacade getDomaineCompFacade() {
		return domaineCompFacade;
	}

	public void setDomaineCompFacade(DomaineCompetenceFacade domaineCompFacade) {
		this.domaineCompFacade = domaineCompFacade;
	}

	public AdminPrefixCodificationFacade getPrefixFacade() {
		return prefixFacade;
	}

	public void setPrefixFacade(AdminPrefixCodificationFacade prefixFacade) {
		this.prefixFacade = prefixFacade;
	}

	public Domainecompetence getOldDomPere() {
		return oldDomPere;
	}

	public void setOldDomPere(Domainecompetence oldDomPere) {
		this.oldDomPere = oldDomPere;
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

	public List<Domainecompetence> getListeSousDomaine() {
		return listeSousDomaine;
	}

	public void setListeSousDomaine(List<Domainecompetence> listeSousDomaine) {
		this.listeSousDomaine = listeSousDomaine;
	}

	public Domainecompetence getOldDomPereForEdit() {
		return oldDomPereForEdit;
	}

	public void setOldDomPereForEdit(Domainecompetence oldDomPereForEdit) {
		this.oldDomPereForEdit = oldDomPereForEdit;
	}

	public Domainecompetence getNewDomPereForEdit() {
		return newDomPereForEdit;
	}

	public void setNewDomPereForEdit(Domainecompetence newDomPereForEdit) {
		this.newDomPereForEdit = newDomPereForEdit;
	}

}
