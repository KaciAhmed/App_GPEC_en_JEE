package dz.elit.gpecpf.referentiel.controller;

import dz.elit.gpecpf.administration.service.AdminPrefixCodificationFacade;
import dz.elit.gpecpf.poste.service.ConditionFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.poste.entity.Condition;
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
public class EditConditionController extends AbstractController implements Serializable {

	@EJB
	private ConditionFacade conditionFacade;
	@EJB
	private PosteFacade posteFacade;
	@EJB
	private AdminPrefixCodificationFacade prefixFacade;

	private List<Poste> listPostes;
	private List<Poste> listPostesCondition;
	private List<Poste> listPostesSelected;
	private List<Poste> listPostesAdd;
	private List<Poste> listPostesDel;

	private Condition condition;

	private String code;
	private String description;

	private String codePrefix;

	public EditConditionController() {
	}

	@Override//@PostConstruct
	protected void initController() {
		try {
			codePrefix = prefixFacade.chercherPrefix().getCond();
		} catch (Exception e) {
			codePrefix = "";
		}
		initAddCondition();
		String id = MyUtil.getRequestParameter("id");
		if (id != null) {
			condition = conditionFacade.find(Integer.parseInt(id));
			listPostesCondition = posteFacade.postesForCondition(condition);
		}
		listPostes.removeAll(listPostesCondition);
	}

	public void edit() {
		try {
			checkCode();
			conditionFacade.edit(condition);
			MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));
			listPostesAdd.removeAll(posteFacade.postesForCondition(condition));
			posteFacade.editCondition(condition, listPostesAdd, listPostesDel);
			initAddCondition();
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
			if (!condition.getCode().startsWith(codePrefix) || condition.getCode().equals(codePrefix)) {
				throw new MyException("Le code doit commencer avec le prefix: " + codePrefix + " et suivi d'une chaine.");
			}
		}
	}

	public void addPostesForCondition() {
		if (!listPostesSelected.isEmpty()) {
			listPostesCondition.addAll(listPostesSelected);
			listPostes.removeAll(listPostesSelected);
			listPostesAdd.addAll(listPostesSelected);
			listPostesDel.removeAll(listPostesSelected);
			listPostesSelected = new ArrayList<>();
		}
	}

	public void removePosteForCondition(Poste poste) {
		listPostesCondition.remove(poste);
		listPostes.add(poste);
		listPostesAdd.remove(poste);
		listPostesDel.add(poste);
	}

	private void initAddCondition() {
		condition = new Condition();
		listPostes = new ArrayList();
		listPostes = posteFacade.findAllOrderByAttribut("code");
		listPostesSelected = new ArrayList<>();
		listPostesCondition = new ArrayList();
		listPostesAdd = new ArrayList();
		listPostesDel = new ArrayList();
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public void setConditionFacade(ConditionFacade conditionFacade) {
		this.conditionFacade = conditionFacade;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public Condition getCondition() {
		return condition;
	}

	public ConditionFacade getConditionFacade() {
		return conditionFacade;
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

	public List<Poste> getListPostesCondition() {
		return listPostesCondition;
	}

	public void setListPostesCondition(List<Poste> listPostesCondition) {
		this.listPostesCondition = listPostesCondition;
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
