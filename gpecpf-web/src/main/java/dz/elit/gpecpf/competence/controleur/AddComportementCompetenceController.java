package dz.elit.gpecpf.competence.controleur;

import dz.elit.gpecpf.administration.service.AdminPrefixCodificationFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.competence.entity.Competence;
import dz.elit.gpecpf.competence.entity.Comportement;
import dz.elit.gpecpf.competence.service.CompetenceFacade;
import dz.elit.gpecpf.competence.service.ComportementCompetenceFacade;
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
public class AddComportementCompetenceController extends AbstractController implements Serializable {

	@EJB
	private ComportementCompetenceFacade ComportementFacade;

	@EJB
	private CompetenceFacade compFacade;

	@EJB
	private AdminPrefixCodificationFacade prefixFacade;

	private List<Competence> listComp;
	private List<Competence> listCmtCmp;
	private List<Competence> listcompSelected;

	private Comportement compo;

	// info pour chercher compétence 
	private String codeComp;
	private String libComp;

	private String codePrefix;

	public AddComportementCompetenceController() {
	}

	@Override //PostConstruct
	protected void initController() {
		initAddComp();
	}

	protected void initAddComp() {
		compo = new Comportement();
		try {
			codePrefix = prefixFacade.chercherPrefix().getComport();
		} catch (Exception e) {
			codePrefix = "";
		}
		compo.setCode(codePrefix);
		listcompSelected = new ArrayList<>();
		listCmtCmp = new ArrayList<>();
		listComp = new ArrayList<>();
		listComp = compFacade.findAllOrderByAttribut("code");
	}

	public void chercherCompetence() {
		listComp = compFacade.findByCodeLibelle(codeComp, libComp);
	}

	public void addCompetenceForComportement() {
		if (!listcompSelected.isEmpty()) {
			listCmtCmp.addAll(listcompSelected);
			listComp.removeAll(listcompSelected);
			listcompSelected = new ArrayList<>();
		}
	}

	public void removeCompetenceForComportement(Competence cmt) {
		listCmtCmp.remove(cmt);
		listComp.add(cmt);
		Collections.sort(listComp);

	}

	private boolean isExisteCode(String code) {
		Comportement Compo2 = ComportementFacade.findByCode(code);
		if (Compo2 == null) {
			return false;
		} else {
			return true;
		}
	}

	public void checkCode() throws MyException {
		if (!codePrefix.equals("")) {
			if (!compo.getCode().startsWith(codePrefix) || compo.getCode().equals(codePrefix)) {
				throw new MyException("Le code doit commencer avec le prefix: " + codePrefix + " et suivi d'une chaine.");
			}
		}
	}

	public void create() {
		try {
			checkCode();
			compo.setCode(compo.getCode().toUpperCase());
			if (isExisteCode(compo.getCode())) {
				MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_existe_code"));//Erreur inconu   
			} else {
				ComportementFacade.create(compo);
				MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//comportement crée avec succès
				initAddComp();
			}
		} catch (MyException ex) {
			ex.printStackTrace();
			MyUtil.addErrorMessage(ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
		}
	}
	// getter  & setter

	public List<Competence> getListComp() {
		return listComp;
	}

	public void setListComp(List<Competence> listComp) {
		this.listComp = listComp;
	}

	public Comportement getCompo() {
		return compo;
	}

	public void setCompo(Comportement compo) {
		this.compo = compo;
	}

	public String getCodeComp() {
		return codeComp;
	}

	public void setCodeComp(String codeComp) {
		this.codeComp = codeComp;
	}

	public String getLibComp() {
		return libComp;
	}

	public void setLibComp(String libComp) {
		this.libComp = libComp;
	}

	public ComportementCompetenceFacade getComportementFacade() {
		return ComportementFacade;
	}

	public void setComportementFacade(ComportementCompetenceFacade ComportementFacade) {
		this.ComportementFacade = ComportementFacade;
	}

	public CompetenceFacade getCompFacade() {
		return compFacade;
	}

	public void setCompFacade(CompetenceFacade compFacade) {
		this.compFacade = compFacade;
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

	public List<Competence> getListCmtCmp() {
		return listCmtCmp;
	}

	public void setListCmtCmp(List<Competence> listCmtCmp) {
		this.listCmtCmp = listCmtCmp;
	}

	public List<Competence> getListcompSelected() {
		return listcompSelected;
	}

	public void setListcompSelected(List<Competence> listcompSelected) {
		this.listcompSelected = listcompSelected;
	}

}
