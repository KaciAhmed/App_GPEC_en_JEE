package dz.elit.gpecpf.referentiel.controller;

import dz.elit.gpecpf.administration.service.AdminPrefixCodificationFacade;
import dz.elit.gpecpf.poste.service.MoyenFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.poste.entity.Moyen;
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
public class EditMoyenController extends AbstractController implements Serializable {

	@EJB
	private MoyenFacade moyenFacade;
	@EJB
	private PosteFacade posteFacade;
	@EJB
	private AdminPrefixCodificationFacade prefixFacade;

	private List<Poste> listPostes;
	private List<Poste> listPostesMoyen;
	private List<Poste> listPostesSelected;
	private List<Poste> listPostesAdd;
	private List<Poste> listPostesDel;

	private Moyen moyen;

	private String code;
	private String description;

	private String codePrefix;

	public EditMoyenController() {
	}

	@Override//@PostConstruct
	protected void initController() {
		try {
			codePrefix = prefixFacade.chercherPrefix().getMoy();
		} catch (Exception e) {
			codePrefix = "";
		}
		initAddMoyen();
		String id = MyUtil.getRequestParameter("id");
		if (id != null) {
			moyen = moyenFacade.find(Integer.parseInt(id));
			listPostesMoyen = posteFacade.postesForMoyen(moyen);
		}
		listPostes.removeAll(listPostesMoyen);
	}

	public void checkCode() throws MyException {
		if (!codePrefix.equals("")) {
			if (!moyen.getCode().startsWith(codePrefix) || moyen.getCode().equals(codePrefix)) {
				throw new MyException("Le code doit commencer avec le prefix: " + codePrefix + " et suivi d'une chaine.");
			}
		}
	}

	public void edit() {
		try {
			checkCode();
			moyenFacade.edit(moyen);
			MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));
			listPostesAdd.removeAll(posteFacade.postesForMoyen(moyen));
			posteFacade.editMoyen(moyen, listPostesAdd, listPostesDel);
			initAddMoyen();
		} catch (MyException ex) {
			ex.printStackTrace();
			MyUtil.addErrorMessage(ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
		}
	}

	public void addPostesForMoyen() {
		if (!listPostesSelected.isEmpty()) {
			listPostesMoyen.addAll(listPostesSelected);
			listPostes.removeAll(listPostesSelected);
			listPostesAdd.addAll(listPostesSelected);
			listPostesDel.removeAll(listPostesSelected);
			listPostesSelected = new ArrayList<>();
		}
	}

	public void removePosteForMoyen(Poste poste) {
		listPostesMoyen.remove(poste);
		listPostes.add(poste);
		listPostesAdd.remove(poste);
		listPostesDel.add(poste);
	}

	private void initAddMoyen() {
		moyen = new Moyen();
		listPostes = new ArrayList();
		listPostes = posteFacade.findAllOrderByAttribut("code");
		listPostesSelected = new ArrayList<>();
		listPostesMoyen = new ArrayList();
		listPostesAdd = new ArrayList();
		listPostesDel = new ArrayList();
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setMoyen(Moyen moyen) {
		this.moyen = moyen;
	}

	public void setMoyenFacade(MoyenFacade moyenFacade) {
		this.moyenFacade = moyenFacade;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public Moyen getMoyen() {
		return moyen;
	}

	public MoyenFacade getMoyenFacade() {
		return moyenFacade;
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

	public List<Poste> getListPostesMoyen() {
		return listPostesMoyen;
	}

	public void setListPostesMoyen(List<Poste> listPostesMoyen) {
		this.listPostesMoyen = listPostesMoyen;
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
