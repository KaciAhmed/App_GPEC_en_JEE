package dz.elit.gpecpf.competence.controleur;

import dz.elit.gpecpf.administration.service.AdminPrefixCodificationFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.competence.entity.Competence;
import dz.elit.gpecpf.competence.entity.Comportement;
import dz.elit.gpecpf.competence.entity.Domainecompetence;
import dz.elit.gpecpf.competence.entity.Typecompetence;
import dz.elit.gpecpf.competence.service.CompetenceFacade;
import dz.elit.gpecpf.competence.service.ComportementCompetenceFacade;
import dz.elit.gpecpf.competence.service.DomaineCompetenceFacade;
import dz.elit.gpecpf.competence.service.TypeCompetenceFacade;
import dz.elit.gpecpf.poste.entity.Poste;
import dz.elit.gpecpf.poste.service.PosteFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
public class EditCompetenceController extends AbstractController implements Serializable {

	@EJB
	private CompetenceFacade compFacade;
	@EJB
	DomaineCompetenceFacade domaineFacade;
	@EJB
	private TypeCompetenceFacade typeCompFacade;
	@EJB
	private AdminPrefixCodificationFacade prefixFacade;
	@EJB
	private PosteFacade posteFacade;
	@EJB
	private ComportementCompetenceFacade comportementFacade;

	private Competence comp;

	private Domainecompetence domaineComp;
	private List<Domainecompetence> listDom;
	private Domainecompetence domSelected;

	private Typecompetence typeComp;
	private List<Typecompetence> listType;
	private Typecompetence typeSelected;

	private List<Comportement> listeComportement;
	private List<Comportement> listComportementSelected;

	private List<Poste> listPostes;
	private List<Poste> listPostesCompetence;
	private List<Poste> listPostesSelected;
	private List<Poste> listPostesAdd;
	private List<Poste> listPostesDel;

	private Domainecompetence oldDomaine;
	private Boolean bdom = false;

	private Typecompetence oldType;
	private Boolean btype = false;

	String oldCode;

	// info pour chercher un domaine
	private String codeDom;
	private String libDom;

	// info pour chercher un type
	private String codeType;
	private String libType;

	// info por chercher un comportement
	private String codeComportement;
	private String descriptionComportement;

	private String codePrefix;

	public EditCompetenceController() {
	}

	@Override //PostConstruct
	protected void initController() {
		try {
			codePrefix = prefixFacade.chercherPrefix().getComp();
		} catch (Exception e) {
			codePrefix = "";
		}
		initEditComp();
		String id = MyUtil.getRequestParameter("id");
		if (id != null) {
			comp = compFacade.find(Integer.parseInt(id));
			domSelected = comp.getIddomcom();
			typeSelected = comp.getIdtypcom();
			oldCode = comp.getCode();
			listeComportement.removeAll(comp.getListComportement());
			if (listeComportement != null && !listeComportement.isEmpty()) {
				Collections.sort(comp.getListComportement());
			}
			listPostesCompetence = posteFacade.postesForCompetence(comp);
			if (listPostesCompetence != null && !listPostesCompetence.isEmpty()) {
				Collections.sort(listPostesCompetence);
				listPostes.removeAll(listPostesCompetence);
			}
		}
	}

	protected void initEditComp() {
		comp = new Competence();
		listDom = new ArrayList<>();
		listDom = domaineFacade.findAllOrderByAttribut("code");
		listType = new ArrayList<>();
		listType = typeCompFacade.findAllOrderByAttribut("code");
		domaineComp = new Domainecompetence();
		typeComp = new Typecompetence();
		listeComportement = new ArrayList<>();
		listeComportement = comportementFacade.findAllOrderByAttribut("code");
		listComportementSelected = new ArrayList<>();

		listPostes = new ArrayList();
		listPostes = posteFacade.findAllOrderByAttribut("code");
		listPostesSelected = new ArrayList<>();
		listPostesCompetence = new ArrayList();
		listPostesAdd = new ArrayList();
		listPostesDel = new ArrayList();
		oldDomaine = new Domainecompetence();
		oldType = new Typecompetence();
		bdom = false;
		btype = false;
	}

	private boolean isExisteCode(String code) {
		Competence Compo2 = compFacade.findByCode(code);
		if (Compo2 == null) {
			return false;
		} else {
			return true;
		}
	}

	public void edit() {
		try {
			checkCode();
			comp.setCode(comp.getCode().toUpperCase());
			if (isExisteCode(comp.getCode()) && !comp.getCode().equals(oldCode)) {
				MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_existe_code"));//Erreur inconu   
			} else {
				compFacade.edit(comp);
				MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//"compétence modifié avec succès");
				listPostesAdd.removeAll(posteFacade.postesForCompetence(comp));
				posteFacade.editCompetence(comp, listPostesAdd, listPostesDel);
				oldCode = comp.getCode();
				if (bdom) {
					domaineFacade.edit(domSelected);
					domaineFacade.edit(oldDomaine);
				}
				if (btype) {
					typeCompFacade.edit(oldType);
					typeCompFacade.edit(typeSelected);
				}
			}
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
			if (!comp.getCode().startsWith(codePrefix) || comp.getCode().equals(codePrefix)) {
				throw new MyException("Le code doit commencer avec le prefix: " + codePrefix + " et suivi d'une chaine.");
			}
		}
	}

	public void chercherDomComp() {
		listDom = domaineFacade.findByCodeLibelle(codeDom, libDom);
	}

	public void chercherTypeComp() {
		listType = typeCompFacade.findByCodeLibelle(codeType, libType);
	}

	public void editDomForComp() {
		oldDomaine = comp.getIddomcom();
		comp.editDomComp(domSelected);
		bdom = true;
	}

	public void editTypeForComp() {
		oldType = comp.getIdtypcom();
		comp.editTypeComp(typeSelected);
		btype = true;
	}

	public void addComportementForCompetence() {
		if (!listComportementSelected.isEmpty()) {
			comp.addListComportement(listComportementSelected);
			listeComportement.removeAll(listComportementSelected);
			listComportementSelected = new ArrayList<>();
			if (comp.getListComportement() != null && !comp.getListComportement().isEmpty()) {
				Collections.sort(comp.getListComportement());
			}
		}
	}

	public void removeComportementForCompetence(Comportement comportement) {
		comp.removeComportement(comportement);
		listeComportement.add(comportement);
		if (listeComportement != null && !listeComportement.isEmpty()) {
			Collections.sort(listeComportement);
		}
	}

	public void chercherComportement() {
		listeComportement = comportementFacade.findByCodeDescription(codeComportement, descriptionComportement);
	}

	public void addPostesForCompetence() {
		if (!listPostesSelected.isEmpty()) {
			listPostesCompetence.addAll(listPostesSelected);
			Collections.sort(listPostesCompetence);
			listPostes.removeAll(listPostesSelected);
			if (listPostes != null && !listPostes.isEmpty()) {
				Collections.sort(listPostes);
			}
			listPostesAdd.addAll(listPostesSelected);
			listPostesDel.removeAll(listPostesSelected);
			listPostesSelected = new ArrayList<>();
		}
	}

	public void removePosteForCompetence(Poste poste) {
		listPostesCompetence.remove(poste);
		if (listPostesCompetence != null && !listPostesCompetence.isEmpty()) {
			Collections.sort(listPostesCompetence);
		}
		listPostes.add(poste);
		if (listPostes != null && !listPostes.isEmpty()) {
			Collections.sort(listPostes);
		}
		listPostesAdd.remove(poste);
		listPostesDel.add(poste);
	}

	// getter && setter 
	public Competence getComp() {
		return comp;
	}

	public void setComp(Competence comp) {
		this.comp = comp;
	}
	// getter && setter du domaine

	public List<Domainecompetence> getListDom() {
		return listDom;
	}

	public void setListDom(List<Domainecompetence> listDom) {
		this.listDom = listDom;
	}

	public Domainecompetence getDomSelected() {
		return domSelected;
	}

	public void setDomSelected(Domainecompetence domSelected) {
		this.domSelected = domSelected;
	}

	public String getCodeDom() {
		return codeDom;
	}

	public void setCodeDom(String codeDom) {
		this.codeDom = codeDom;
	}

	public String getLibDom() {
		return libDom;
	}

	public void setLibDom(String libDom) {
		this.libDom = libDom;
	}
	// getter && setter du type

	public List<Typecompetence> getListType() {
		return listType;
	}

	public void setListType(List<Typecompetence> listType) {
		this.listType = listType;
	}

	public Typecompetence getTypeSelected() {
		return typeSelected;
	}

	public void setTypeSelected(Typecompetence typeSelected) {
		this.typeSelected = typeSelected;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getLibType() {
		return libType;
	}

	public void setLibType(String libType) {
		this.libType = libType;
	}

	public CompetenceFacade getCompFacade() {
		return compFacade;
	}

	public void setCompFacade(CompetenceFacade compFacade) {
		this.compFacade = compFacade;
	}

	public DomaineCompetenceFacade getDomaineFacade() {
		return domaineFacade;
	}

	public void setDomaineFacade(DomaineCompetenceFacade domaineFacade) {
		this.domaineFacade = domaineFacade;
	}

	public TypeCompetenceFacade getTypeCompFacade() {
		return typeCompFacade;
	}

	public void setTypeCompFacade(TypeCompetenceFacade typeCompFacade) {
		this.typeCompFacade = typeCompFacade;
	}

	public AdminPrefixCodificationFacade getPrefixFacade() {
		return prefixFacade;
	}

	public void setPrefixFacade(AdminPrefixCodificationFacade prefixFacade) {
		this.prefixFacade = prefixFacade;
	}

	public Domainecompetence getDomaineComp() {
		return domaineComp;
	}

	public void setDomaineComp(Domainecompetence domaineComp) {
		this.domaineComp = domaineComp;
	}

	public Typecompetence getTypeComp() {
		return typeComp;
	}

	public void setTypeComp(Typecompetence typeComp) {
		this.typeComp = typeComp;
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

	public PosteFacade getPosteFacade() {
		return posteFacade;
	}

	public void setPosteFacade(PosteFacade posteFacade) {
		this.posteFacade = posteFacade;
	}

	public ComportementCompetenceFacade getComportementFacade() {
		return comportementFacade;
	}

	public void setComportementFacade(ComportementCompetenceFacade comportementFacade) {
		this.comportementFacade = comportementFacade;
	}

	public List<Comportement> getListeComportement() {
		return listeComportement;
	}

	public void setListeComportement(List<Comportement> listeComportement) {
		this.listeComportement = listeComportement;
	}

	public List<Comportement> getListComportementSelected() {
		return listComportementSelected;
	}

	public void setListComportementSelected(List<Comportement> listComportementSelected) {
		this.listComportementSelected = listComportementSelected;
	}

	public List<Poste> getListPostes() {
		return listPostes;
	}

	public void setListPostes(List<Poste> listPostes) {
		this.listPostes = listPostes;
	}

	public List<Poste> getListPostesCompetence() {
		return listPostesCompetence;
	}

	public void setListPostesCompetence(List<Poste> listPostesCompetence) {
		this.listPostesCompetence = listPostesCompetence;
	}

	public List<Poste> getListPostesSelected() {
		return listPostesSelected;
	}

	public void setListPostesSelected(List<Poste> listPostesSelected) {
		this.listPostesSelected = listPostesSelected;
	}

	public List<Poste> getListPostesAdd() {
		return listPostesAdd;
	}

	public void setListPostesAdd(List<Poste> listPostesAdd) {
		this.listPostesAdd = listPostesAdd;
	}

	public List<Poste> getListPostesDel() {
		return listPostesDel;
	}

	public void setListPostesDel(List<Poste> listPostesDel) {
		this.listPostesDel = listPostesDel;
	}

	public String getCodeComportement() {
		return codeComportement;
	}

	public void setCodeComportement(String codeComportement) {
		this.codeComportement = codeComportement;
	}

	public String getDescriptionComportement() {
		return descriptionComportement;
	}

	public void setDescriptionComportement(String descriptionComportement) {
		this.descriptionComportement = descriptionComportement;
	}

}
