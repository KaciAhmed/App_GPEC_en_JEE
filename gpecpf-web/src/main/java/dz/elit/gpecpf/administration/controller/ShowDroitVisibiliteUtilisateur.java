package dz.elit.gpecpf.administration.controller;

import dz.elit.gpecpf.administration.entity.AdminDroitVisibilite;
import dz.elit.gpecpf.administration.entity.AdminGroupe;
import dz.elit.gpecpf.administration.entity.AdminObjetVisibilite;
import dz.elit.gpecpf.administration.entity.AdminUniteOrganisationnelle;
import dz.elit.gpecpf.administration.entity.AdminUtilisateur;
import dz.elit.gpecpf.administration.service.AdminObjetVisibiliteFacade;
import dz.elit.gpecpf.administration.service.AdminUniteOrganisationnelleFacade;
import dz.elit.gpecpf.administration.service.AdminUtilisateurFacade;
import dz.elit.gpecpf.commun.util.UniteOrganisationnelleObjetVisibilite;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author laidani.youcef
 */
@ManagedBean
@ViewScoped
public class ShowDroitVisibiliteUtilisateur implements Serializable {

	@EJB
	private AdminUtilisateurFacade utilisateurFacade;
	@EJB
	private AdminUniteOrganisationnelleFacade uniteOrganisationnelleFacade;
	@EJB
	private AdminObjetVisibiliteFacade adminObjetVisibiliteFacade;

	private AdminUtilisateur utilisateur;

	private List<AdminUtilisateur> listeUtilisateur;
	private Map<String, List<UniteOrganisationnelleObjetVisibilite>> listeDroitVisibilite;

	List<AdminUniteOrganisationnelle> listeUnite;
	List<AdminObjetVisibilite> listeObjet;

	@PostConstruct
	public void init() {
		listeUtilisateur = utilisateurFacade.findAll();
		utilisateur = new AdminUtilisateur();
		listeDroitVisibilite = new HashMap<>();
		listeUnite = uniteOrganisationnelleFacade.findAllOrderByTrie();
		listeObjet = adminObjetVisibiliteFacade.findAll();
		listeDroitVisibilite = new HashMap<>();
		UniteOrganisationnelleObjetVisibilite construire = new UniteOrganisationnelleObjetVisibilite();
		listeDroitVisibilite = construire.construireListUOV(listeUnite, listeObjet);
	}

	public void construireListeDroit() {
		utilisateur.setAdminDroitVisibiliteList(construireListeDroit(this.utilisateur));
		//listeDroitVisibilite = new HashMap<>();
		UniteOrganisationnelleObjetVisibilite construire = new UniteOrganisationnelleObjetVisibilite();
		listeDroitVisibilite = construire.construireListUOVUtilisateur(listeUnite, listeObjet, utilisateur);
	}

	private List<AdminDroitVisibilite> construireListeDroit(AdminUtilisateur user) {
		int maxSize = 0;
		int counter = 0;

		for (AdminGroupe groupe : user.getListAdminGroupe()) {
			maxSize += groupe.getAdminDroitVisibiliteList().size();
		}
		AdminDroitVisibilite[] accumulator = new AdminDroitVisibilite[maxSize];

		for (AdminGroupe array : user.getListAdminGroupe()) {
			for (AdminDroitVisibilite i : array.getAdminDroitVisibiliteList()) {
				if (!isDuplicated(accumulator, counter, i)) {
					accumulator[counter++] = i;
				}
			}
		}

		List<AdminDroitVisibilite> result = new ArrayList<>();
		for (int i = 0; i < counter; i++) {
			result.add(accumulator[i]);
		}

		return result;
	}

	public static boolean isDuplicated(AdminDroitVisibilite[] array, int counter, AdminDroitVisibilite value) {
		for (int i = 0; i < counter; i++) {
			if (Objects.equals(array[i].getIdObjetVisibilite().getId(), value.getIdObjetVisibilite().getId())
					&& Objects.equals(array[i].getIdUniteOrganisationnelle().getId(), value.getIdUniteOrganisationnelle().getId())) {
				return true;
			}
		}
		return false;
	}

	public ShowDroitVisibiliteUtilisateur() {
	}

	public AdminUtilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(AdminUtilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public List<AdminUtilisateur> getListeUtilisateur() {
		return listeUtilisateur;
	}

	public void setListeUtilisateur(List<AdminUtilisateur> listeUtilisateur) {
		this.listeUtilisateur = listeUtilisateur;
	}

	public Map<String, List<UniteOrganisationnelleObjetVisibilite>> getListeDroitVisibilite() {
		return listeDroitVisibilite;
	}

	public void setListeDroitVisibilite(Map<String, List<UniteOrganisationnelleObjetVisibilite>> listeDroitVisibilite) {
		this.listeDroitVisibilite = listeDroitVisibilite;
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

}
