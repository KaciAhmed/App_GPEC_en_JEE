package dz.elit.gpecpf.administration.controller;

import dz.elit.gpecpf.administration.entity.AdminDroitVisibilite;
import dz.elit.gpecpf.administration.entity.AdminGroupe;
import dz.elit.gpecpf.administration.entity.AdminObjetVisibilite;
import dz.elit.gpecpf.administration.entity.AdminUniteOrganisationnelle;
import dz.elit.gpecpf.administration.entity.AdminUtilisateur;
import dz.elit.gpecpf.administration.service.AdminGroupeFacade;
import dz.elit.gpecpf.administration.service.AdminObjetVisibiliteFacade;
import dz.elit.gpecpf.administration.service.AdminUniteOrganisationnelleFacade;
import dz.elit.gpecpf.administration.service.AdminUtilisateurFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.commun.util.UniteOrganisationnelleObjetVisibilite;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author laidani.youcef
 */
@ManagedBean
@ViewScoped
public class EditGroupeVisibiliteController {

	@EJB
	private AdminGroupeFacade adminGroupeFacade;
	@EJB
	private AdminUtilisateurFacade utilisateurFacade;
	@EJB
	private AdminUniteOrganisationnelleFacade uniteOrganisationnelleFacade;
	@EJB
	private AdminObjetVisibiliteFacade adminObjetVisibiliteFacade;

	List<AdminUniteOrganisationnelle> listeUnite;
	List<AdminObjetVisibilite> listeObjet;

	private List<AdminUtilisateur> listUtilisateurs;
	private List<AdminUtilisateur> listUtilisateursSelected;
	private AdminUtilisateur utilisateur;

	private List<AdminGroupe> listeGroupe;
	private AdminGroupe groupe;

	private Map<String, List<UniteOrganisationnelleObjetVisibilite>> listeDroitVisibilite;

	@PostConstruct
	public void init() {
		groupe = new AdminGroupe();
		listUtilisateurs = utilisateurFacade.findAllOrderByAttribut("login");
		listUtilisateursSelected = new ArrayList<>();
		//Find unité que j'ai le droit
		listeUnite = uniteOrganisationnelleFacade.findAllOrderByTrie();
		listeObjet = adminObjetVisibiliteFacade.findAll();

		//Construire une liste qui contien la liste des unite organization et liste de objet
		UniteOrganisationnelleObjetVisibilite construire = new UniteOrganisationnelleObjetVisibilite();

		String id = MyUtil.getRequestParameter("id");
		if (id != null) {
			groupe = adminGroupeFacade.find(Integer.parseInt(id));
			listeDroitVisibilite = construire.construireListUOVGroupe(listeUnite, listeObjet, groupe);
		}

	}

	public void removeUtilisateur(AdminUtilisateur utilisateur) {
		if (utilisateur != null) {
			groupe.getListMembre().remove(utilisateur);
		}
	}

	public void edit() {
		List<AdminDroitVisibilite> listeDroits = new ArrayList<>();
		for (Map.Entry<String, List<UniteOrganisationnelleObjetVisibilite>> block : listeDroitVisibilite.entrySet()) {
			for (UniteOrganisationnelleObjetVisibilite piece : block.getValue()) {
				if (piece.isSelected()) {
					//Construire une liste des droit de visibilité
					AdminDroitVisibilite droit = new AdminDroitVisibilite(piece.getEntite(), piece.getUnite(), groupe);
					listeDroits.add(droit);
				}
			}
		}

		groupe.setAdminDroitVisibiliteList(listeDroits);
		try {
			adminGroupeFacade.modifier(groupe);
			MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));
		} catch (MyException ex) {
			MyUtil.addErrorMessage(ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
		}
	}

	public EditGroupeVisibiliteController() {
	}

	public List<AdminUniteOrganisationnelle> getListeUnite() {
		return listeUnite;
	}

	public void setListeUnite(List<AdminUniteOrganisationnelle> listeUnite) {
		this.listeUnite = listeUnite;
	}

	public List<AdminObjetVisibilite> getListeObjet() {
		return listeObjet;
	}

	public void setListeObjet(List<AdminObjetVisibilite> listeObjet) {
		this.listeObjet = listeObjet;
	}

	public List<AdminUtilisateur> getListUtilisateurs() {
		return listUtilisateurs;
	}

	public void setListUtilisateurs(List<AdminUtilisateur> listUtilisateurs) {
		this.listUtilisateurs = listUtilisateurs;
	}

	public List<AdminUtilisateur> getListUtilisateursSelected() {
		return listUtilisateursSelected;
	}

	public void setListUtilisateursSelected(List<AdminUtilisateur> listUtilisateursSelected) {
		this.listUtilisateursSelected = listUtilisateursSelected;
	}

	public AdminUtilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(AdminUtilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public List<AdminGroupe> getListeGroupe() {
		return listeGroupe;
	}

	public void setListeGroupe(List<AdminGroupe> listeGroupe) {
		this.listeGroupe = listeGroupe;
	}

	public AdminGroupe getGroupe() {
		return groupe;
	}

	public void setGroupe(AdminGroupe groupe) {
		this.groupe = groupe;
	}

	public Map<String, List<UniteOrganisationnelleObjetVisibilite>> getListeDroitVisibilite() {
		return listeDroitVisibilite;
	}

	public void setListeDroitVisibilite(Map<String, List<UniteOrganisationnelleObjetVisibilite>> listeDroitVisibilite) {
		this.listeDroitVisibilite = listeDroitVisibilite;
	}

}
