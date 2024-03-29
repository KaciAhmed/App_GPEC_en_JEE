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
public class AddCompetenceController extends AbstractController implements Serializable {

	@EJB
	private CompetenceFacade compFacade;
	@EJB
	DomaineCompetenceFacade domaineFacade;
	@EJB
	private TypeCompetenceFacade typeCompFacade;
	@EJB
	private AdminPrefixCodificationFacade prefixFacade;
	@EJB
	private ComportementCompetenceFacade comportementFacade;
	@EJB
	private PosteFacade posteFacade;

	private List<Domainecompetence> listDom;

	private Domainecompetence domaineCompSelected;

	private List<Typecompetence> listType;
	private Typecompetence typeCompSelected;

	private Competence comp;

	private List<Comportement> listComportement;
	private List<Comportement> listComportementSelected;

	private List<Poste> listPostes;
	private List<Poste> listPostesCompetence;
	private List<Poste> listPostesSelected;

	// info pour chercher domaine  
	private String codeDom;
	private String libDom;

	// info pour chercher type  
	private String codeType;
	private String libType;

	// info pour chercher un comportement
	private String codeComportement;
	private String descriptionComportement;

	private String codePrefix;

	public AddCompetenceController() {
	}

	@Override //PostConstruct
	protected void initController() {
		initAddComp();
	}

	protected void initAddComp() {
		comp = new Competence();
		try {
			codePrefix = prefixFacade.chercherPrefix().getComp();
		} catch (Exception e) {
			codePrefix = "";
		}
		comp.setCode(codePrefix);
		listDom = new ArrayList<>();
		listDom = domaineFacade.findAllOrderByAttribut("code");
		listType = new ArrayList<>();
		listType = typeCompFacade.findAllOrderByAttribut("code");
		domaineCompSelected = new Domainecompetence();
		typeCompSelected = new Typecompetence();
		listComportement = new ArrayList<>();
		listComportement = comportementFacade.findAllOrderByAttribut("code");
		listComportementSelected = new ArrayList<>();

		listPostes = new ArrayList();
		listPostes = posteFacade.findAllOrderByAttribut("code");
		listPostesSelected = new ArrayList<>();
		listPostesCompetence = new ArrayList();
	}

	public void chercherDomaine() {
		listDom = domaineFacade.findByCodeLibelle(codeDom, libDom);
	}

	public void chercherTypeComp() {
		listType = typeCompFacade.findByCodeLibelle(codeDom, libDom);
	}

	public void recupDomaine() {
		listDom = domaineFacade.findAll();
	}

	public void recupTypeComp() {
		listType = typeCompFacade.findAll();
	}

	public void addDomaineForComp() {
		if (domaineCompSelected.getCode() != null) {
			comp.addDomComp(domaineCompSelected);
		}
	}

	public void addTypeForComp() {
		if (typeCompSelected.getCode() != null) {
			comp.setIdtypcom(typeCompSelected);
		}
	}

	public void addComportementForCompetence() {
		if (!listComportementSelected.isEmpty()) {
			comp.addListComportement(listComportementSelected);
			listComportement.removeAll(listComportementSelected);
			listComportementSelected = new ArrayList<>();
		}
	}

	public void removeComportementForcompetence(Comportement comportement) {
		comp.removeComportement(comportement);
		listComportement.add(comportement);
		Collections.sort(listComportement);
	}

	public void chercherComportement() {
		listComportement = comportementFacade.findByCodeDescription(codeComportement, descriptionComportement);
	}

	private boolean isExisteCode(String code) {
		Competence Compo2 = compFacade.findByCode(code);
		if (Compo2 == null) {
			return false;
		} else {
			return true;
		}
	}

	public void addPostesForCompetence() {
		if (!listPostesSelected.isEmpty()) {
			listPostesCompetence.addAll(listPostesSelected);
			Collections.sort(listPostesCompetence);
			listPostes.removeAll(listPostesSelected);
			listPostesSelected = new ArrayList<>();
		}
	}

	public void removePosteForCompetence(Poste poste) {
		listPostesCompetence.remove(poste);
		listPostes.add(poste);
		Collections.sort(listPostes);
	}

	public void create() {
		try {
			checkCode();
			if (comp.getIddomcom() == null) {
				MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_add_competence_dom"));
			} else if (comp.getIdtypcom() == null) {
				MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_add_competence_type"));
			} else {
				comp.setCode(comp.getCode().toUpperCase());
				if (isExisteCode(comp.getCode())) {
					MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_existe_code"));//Erreur inconu   
				} else {
					compFacade.create(comp);
					domaineFacade.edit(comp.getIddomcom());
					typeCompFacade.edit(comp.getIdtypcom());
					posteFacade.editCompetence(comp, listPostesCompetence, new ArrayList());
					MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//Compétence crée avec succès
					initAddComp();
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

	// getter && setter
	public Competence getComp() {
		return comp;
	}

	public void setComp(Competence comp) {
		this.comp = comp;
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

	public List<Domainecompetence> getListDom() {
		return listDom;
	}

	public void setListDom(List<Domainecompetence> listDom) {
		this.listDom = listDom;
	}

	public Domainecompetence getDomaineCompSelected() {
		return domaineCompSelected;
	}

	public void setDomaineCompSelected(Domainecompetence domaineCompSelected) {
		this.domaineCompSelected = domaineCompSelected;
	}
	// partie type compétence

	public List<Typecompetence> getListType() {
		return listType;
	}

	public void setListType(List<Typecompetence> listType) {
		this.listType = listType;
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

	public Typecompetence getTypeCompSelected() {
		return typeCompSelected;
	}

	public void setTypeCompSelected(Typecompetence typeCompSelected) {
		this.typeCompSelected = typeCompSelected;
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

	public String getCodePrefix() {
		return codePrefix;
	}

	public void setCodePrefix(String codePrefix) {
		this.codePrefix = codePrefix;
	}

	public ComportementCompetenceFacade getComportementFacade() {
		return comportementFacade;
	}

	public void setComportementFacade(ComportementCompetenceFacade comportementFacade) {
		this.comportementFacade = comportementFacade;
	}

	public PosteFacade getPosteFacade() {
		return posteFacade;
	}

	public void setPosteFacade(PosteFacade posteFacade) {
		this.posteFacade = posteFacade;
	}

	public List<Comportement> getListComportement() {
		return listComportement;
	}

	public void setListComportement(List<Comportement> listComportement) {
		this.listComportement = listComportement;
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
