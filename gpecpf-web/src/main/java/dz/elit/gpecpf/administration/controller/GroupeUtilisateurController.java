/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.administration.controller;

import dz.elit.gpecpf.administration.entity.AdminGroupe;
import dz.elit.gpecpf.administration.entity.AdminUtilisateur;
import dz.elit.gpecpf.administration.service.AdminGroupeFacade;
import dz.elit.gpecpf.administration.service.AdminUtilisateurFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.MyUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author laidani.youcef
 */
@ManagedBean
@ViewScoped
public class GroupeUtilisateurController implements Serializable {

    @EJB
    private AdminGroupeFacade adminGroupeFacade;

    @EJB
    private AdminUtilisateurFacade adminUtilisateurFacade;

    private AdminGroupe groupe;
    private List<AdminGroupe> listeGroupe;

    private List<AdminUtilisateur> listUtilisateurs;
    private List<AdminUtilisateur> listUtilisateursSelected;

    @PostConstruct
    public void init() {
        groupe = new AdminGroupe();
        groupe.setListMembre(new ArrayList<AdminUtilisateur>());
        listeGroupe = new ArrayList<>();
        listeGroupe = adminGroupeFacade.findAll();
        listUtilisateurs = adminUtilisateurFacade.findAllOrderByAttribut("login");

        String idGroupe = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (idGroupe != null) {
            groupe = adminGroupeFacade.findById(Integer.parseInt(idGroupe));
        }
    }

    public GroupeUtilisateurController() {
    }

    public void editGroupe() {
        try {
            adminGroupeFacade.modifier(groupe);
            MyUtil.addInfoMessage("Groupe modifier avec succès");
            init();
        } catch (MyException ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage("Erreur inconu");
        }
    }

    public void retirerModule(AdminUtilisateur membre) {
        if (membre != null) {
            groupe.getListMembre().remove(membre);
        }
    }

    public void deleteGroupe(AdminGroupe grp) {
        try {
            adminGroupeFacade.delete(grp);
            listeGroupe = adminGroupeFacade.findAllOrderById();
            MyUtil.addInfoMessage("Groupe supprimé avec succès");
        } catch (MyException ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage("Erreur inconu");
        }
    }

    public void addGroupe() {
        try {
            adminGroupeFacade.creer(groupe);
            MyUtil.addInfoMessage("Groupe crée avec succès");
            init();
        } catch (Exception ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(ex.getMessage());
        }
    }

    public AdminGroupe getGroupe() {
        return groupe;
    }

    public void setGroupe(AdminGroupe groupe) {
        this.groupe = groupe;
    }

    public List<AdminGroupe> getListeGroupe() {
        return listeGroupe;
    }

    public void setListeGroupe(List<AdminGroupe> listeGroupe) {
        this.listeGroupe = listeGroupe;
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

}
