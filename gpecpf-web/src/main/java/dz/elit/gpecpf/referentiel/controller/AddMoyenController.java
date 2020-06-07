package dz.elit.gpecpf.referentiel.controller;

import dz.elit.gpecpf.administration.service.AdminPrefixCodificationFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.poste.entity.Moyen;
import dz.elit.gpecpf.poste.entity.Poste;
import dz.elit.gpecpf.poste.service.MoyenFacade;
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
public class AddMoyenController extends AbstractController implements Serializable {

	@EJB
	private MoyenFacade moyenFacade;
	@EJB
	private PosteFacade posteFacade;
	@EJB
	private AdminPrefixCodificationFacade prefixFacade;
	private Moyen moyen;

	private List<Poste> listPostes;
	private List<Poste> listPostesMoyen;
	private List<Poste> listPostesSelected;

	private String code;
	private String description;

	private String codePrefix;

	public AddMoyenController() {
	}

	@Override//@PostConstruct
	protected void initController() {
		initAddMoyen();
	}

	public void create() {
		try {
			checkCode();
			moyenFacade.create(moyen);
			posteFacade.editMoyen(moyen, listPostesMoyen, new ArrayList());
			MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));
			initAddMoyen();
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
			if (!moyen.getCode().startsWith(codePrefix) || moyen.getCode().equals(codePrefix)) {
				throw new MyException("Le code doit commencer avec le prefix: " + codePrefix + " et suivi d'une chaine.");
			}
		}
	}

	private void initAddMoyen() {
		moyen = new Moyen();
		try {
			codePrefix = prefixFacade.chercherPrefix().getMoy();
		} catch (Exception e) {
			codePrefix = "";
		}
		moyen.setCode(codePrefix);
		listPostes = new ArrayList();
		listPostes = posteFacade.findAllOrderByAttribut("code");
		listPostesSelected = new ArrayList<>();
		listPostesMoyen = new ArrayList();
	}

	public void addPostesForMoyen() {
		if (!listPostesSelected.isEmpty()) {
			listPostesMoyen.addAll(listPostesSelected);
			listPostes.removeAll(listPostesSelected);
			listPostesSelected = new ArrayList<>();
		}
	}

	public void removePosteForMoyen(Poste poste) {
		listPostesMoyen.remove(poste);
		listPostes.add(poste);
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

}
