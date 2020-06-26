package dz.elit.gpecpf.referentiel.controller;

import dz.elit.gpecpf.poste.service.TypePosteFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.poste.entity.Poste;
import dz.elit.gpecpf.poste.entity.TypePoste;
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
public class EditTypePosteController extends AbstractController implements Serializable {

	@EJB
	private TypePosteFacade typePosteFacade;

	@EJB
	private PosteFacade posteFacade;

	private List<Poste> listPostes;

	private TypePoste typePoste;

	private String code;
	private String libelle;

	/**
	 * Creates a new instance of AddProfilController
	 */
	public EditTypePosteController() {
	}

	@Override//@PostConstruct
	protected void initController() {
		initAddTypePoste();
		typePoste = new TypePoste();
		String id = MyUtil.getRequestParameter("id");
		if (id != null) {
			typePoste = typePosteFacade.find(Integer.parseInt(id));
			listPostes = posteFacade.postesForType(typePoste);
		}
	}

	public void edit() {
		try {
			typePosteFacade.edit(typePoste);
			MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));
			initAddTypePoste();
		} catch (MyException ex) {
			ex.printStackTrace();
			MyUtil.addErrorMessage(ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
		}
	}

	private void initAddTypePoste() {
		typePoste = new TypePoste();
		listPostes = new ArrayList<>();
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public void setTypePoste(TypePoste typePoste) {
		this.typePoste = typePoste;
	}

	public void setTypePosteFacade(TypePosteFacade typePosteFacade) {
		this.typePosteFacade = typePosteFacade;
	}

	public String getCode() {
		return code;
	}

	public String getLibelle() {
		return libelle;
	}

	public TypePoste getTypePoste() {
		return typePoste;
	}

	public TypePosteFacade getTypePosteFacade() {
		return typePosteFacade;
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

}
