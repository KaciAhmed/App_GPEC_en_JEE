package dz.elit.gpecpf.referentiel.controller;

import dz.elit.gpecpf.administration.service.AdminPrefixCodificationFacade;
import dz.elit.gpecpf.poste.service.FormationFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.poste.entity.Formation;
import dz.elit.gpecpf.poste.entity.Poste;
import dz.elit.gpecpf.poste.service.PosteFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Nadir Ben Mohand
 */
@ManagedBean
@ViewScoped
public class EditFormationController extends AbstractController implements Serializable {

	@EJB
	private FormationFacade formationFacade;
	@EJB
	private PosteFacade posteFacade;
	@EJB
	private AdminPrefixCodificationFacade prefixFacade;

	private List<Poste> listPostes;
	private List<Poste> listPostesFormation;
	private List<Poste> listPostesSelected;
	private List<Poste> listPostesAdd;
	private List<Poste> listPostesDel;

	private Formation formation;

	private String code;
	private String description;
	private String type;
	private String exigence;

	private String codePrefix;

	public EditFormationController() {
	}

	@Override//@PostConstruct
	protected void initController() {
		try {
			codePrefix = prefixFacade.chercherPrefix().getForm();
		} catch (Exception e) {
			codePrefix = "";
		}
		initAddFormation();
		String id = MyUtil.getRequestParameter("id");
		if (id != null) {
			formation = formationFacade.find(Integer.parseInt(id));
			listPostesFormation = posteFacade.postesForFormation(formation);
		}
		listPostes.removeAll(listPostesFormation);
	}

	public void checkCode() throws MyException {
		if (!codePrefix.equals("")) {
			if (!formation.getCode().startsWith(codePrefix) || formation.getCode().equals(codePrefix)) {
				throw new MyException("Le code doit commencer avec le prefix: " + codePrefix + " et suivi d'une chaine.");
			}
		}
	}

	public void edit() {
		try {
			checkCode();
			formationFacade.edit(formation);
			MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));
			listPostesAdd.removeAll(posteFacade.postesForFormation(formation));
			posteFacade.editFormation(formation, listPostesAdd, listPostesDel);
			initAddFormation();
		} catch (MyException ex) {
			ex.printStackTrace();
			MyUtil.addErrorMessage(ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
		}
	}

	public void addPostesForFormation() {
		if (!listPostesSelected.isEmpty()) {
			listPostesFormation.addAll(listPostesSelected);
			listPostes.removeAll(listPostesSelected);
			listPostesAdd.addAll(listPostesSelected);
			listPostesDel.removeAll(listPostesSelected);
			listPostesSelected = new ArrayList<>();
		}
	}

	public void removePosteForFormation(Poste poste) {
		listPostesFormation.remove(poste);
		listPostes.add(poste);
		listPostesAdd.remove(poste);
		listPostesDel.add(poste);
	}

	private void initAddFormation() {
		formation = new Formation();
		listPostes = new ArrayList();
		listPostes = posteFacade.findAllOrderByAttribut("code");
		listPostesSelected = new ArrayList<>();
		listPostesFormation = new ArrayList();
		listPostesAdd = new ArrayList();
		listPostesDel = new ArrayList();
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setFormation(Formation formation) {
		this.formation = formation;
	}

	public void setFormationFacade(FormationFacade formationFacade) {
		this.formationFacade = formationFacade;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setExigence(String exigence) {
		this.exigence = exigence;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public Formation getFormation() {
		return formation;
	}

	public FormationFacade getFormationFacade() {
		return formationFacade;
	}

	public String getType() {
		return type;
	}

	public String getExigence() {
		return exigence;
	}

	public PosteFacade getPosteFacade() {
		return posteFacade;
	}

	public void setPosteFacade(PosteFacade posteFacade) {
		this.posteFacade = posteFacade;
	}

	public List<Poste> getListPostes() {
		return listPostes;
	}

	public void setListPostes(List<Poste> listPostes) {
		this.listPostes = listPostes;
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

	public List<Poste> getListPostesFormation() {
		return listPostesFormation;
	}

	public void setListPostesFormation(List<Poste> listPostesFormation) {
		this.listPostesFormation = listPostesFormation;
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

}
