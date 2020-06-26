package dz.elit.gpecpf.administration.controller;

import dz.elit.gpecpf.administration.entity.AdminModule;
import dz.elit.gpecpf.administration.entity.AdminPrivilege;
import dz.elit.gpecpf.administration.entity.AdminProfil;
import dz.elit.gpecpf.administration.entity.AdminUtilisateur;
import dz.elit.gpecpf.administration.service.AdminModuleFacade;
import dz.elit.gpecpf.administration.service.AdminPrivilegeFacade;
import dz.elit.gpecpf.administration.service.AdminProfilFacade;
import dz.elit.gpecpf.administration.service.AdminUtilisateurFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author leghettas.rabah
 */
@ManagedBean
@ViewScoped
public class AddProfilController extends AbstractController implements Serializable {

	@EJB
	private AdminProfilFacade profilFacade;
	@EJB
	private AdminUtilisateurFacade utilisateurFacade;
	@EJB
	private AdminPrivilegeFacade privilegeFacade;
	@EJB
	private AdminModuleFacade moduleFacade;

	private AdminProfil profil;
	private AdminModule module;

	private List<AdminUtilisateur> listUtilisateurs;
	private List<AdminPrivilege> listPrivileges;
	private List<AdminPrivilege> listPrivilegesSelected;
	private List<AdminUtilisateur> listUtilisateursSelected;
	private List<AdminModule> listModules;

	private String code;
	private String description;

	/**
	 * Creates a new instance of AddProfilController
	 */
	public AddProfilController() {
	}

	@Override//@PostConstruct
	protected void initController() {
		initAddProfil();
		module = new AdminModule();
		listUtilisateurs = utilisateurFacade.findAllOrderByAttribut("login");
		listModules = moduleFacade.findAllOrderByAttribut("code");
		//charger la liste des privileges
		rechercher();
	}

	public void rechercher() {
		listPrivileges = privilegeFacade.findByCodeDescModule(code, description, module);
		if (profil != null && !profil.getListAdminPrivilege().isEmpty()) {
			listPrivileges.removeAll(profil.getListAdminPrivilege());
		}
	}

	public void create() {
		try {
			profilFacade.create(profil);
			MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//Profil crée avec succès
			initAddProfil();
		} catch (MyException ex) {
			ex.printStackTrace();
			MyUtil.addErrorMessage(ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
		}
	}

	public void addPrivilegesForProfil() {
		if (!listPrivilegesSelected.isEmpty()) {
			//profil.getListAdminPrivilege().addAll(listPrivilegesSelected);
			profil.addListPrivileges(listPrivilegesSelected);
			listPrivileges.removeAll(listPrivilegesSelected);
			listPrivilegesSelected = new ArrayList<>();
		}
	}

	public void addUtilisateursForProfil() {
		if (!listUtilisateursSelected.isEmpty()) {
			// profil.getListAdminUtilisateurs().addAll(listUtilisateursSelected);
			profil.addListUtilisateurs(listUtilisateursSelected);
			listUtilisateurs.removeAll(listUtilisateursSelected);
			listUtilisateursSelected = new ArrayList<>();
		}
	}

	private void initAddProfil() {
		profil = new AdminProfil();
		listPrivilegesSelected = new ArrayList<>();
		listUtilisateursSelected = new ArrayList<>();
		listPrivileges = new ArrayList();
		listUtilisateurs = new ArrayList();
	}

	public void removePrivilegesForProfil(AdminPrivilege privilege) {
		if (privilege != null) {
			profil.removePrivilege(privilege);
			listPrivileges.add(privilege);
		}
	}

	public void removeUtilisateurForProfil(AdminUtilisateur utilisateur) {
		if (utilisateur != null) {
			profil.removeUtilisateur(utilisateur);
			listUtilisateurs.add(utilisateur);
		}
	}

	// Getter and setter
	public AdminProfil getProfil() {
		return profil;
	}

	public void setProfil(AdminProfil profil) {
		this.profil = profil;
	}

	public List<AdminUtilisateur> getListUtilisateurs() {
		return listUtilisateurs;
	}

	public void setListUtilisateurs(List<AdminUtilisateur> listUtilisateurs) {
		this.listUtilisateurs = listUtilisateurs;
	}

	public List<AdminPrivilege> getListPrivileges() {
		return listPrivileges;
	}

	public void setListPrivileges(List<AdminPrivilege> listPrivileges) {
		this.listPrivileges = listPrivileges;
	}

	public List<AdminPrivilege> getListPrivilegesSelected() {
		return listPrivilegesSelected;
	}

	public void setListPrivilegesSelected(List<AdminPrivilege> listPrivilegesSelected) {
		this.listPrivilegesSelected = listPrivilegesSelected;
	}

	public List<AdminUtilisateur> getListUtilisateursSelected() {
		return listUtilisateursSelected;
	}

	public void setListUtilisateursSelected(List<AdminUtilisateur> listUtilisateursSelected) {
		this.listUtilisateursSelected = listUtilisateursSelected;
	}

	public List<AdminModule> getListModules() {
		return listModules;
	}

	public void setListModules(List<AdminModule> listModules) {
		this.listModules = listModules;
	}

	public AdminModule getModule() {
		return module;
	}

	public void setModule(AdminModule module) {
		this.module = module;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
