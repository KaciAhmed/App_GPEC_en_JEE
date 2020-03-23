/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.administration.controller;

import dz.elit.gpecpf.administration.entity.AdminDroitVisibilite;
import dz.elit.gpecpf.administration.entity.AdminGroupe;
import dz.elit.gpecpf.administration.entity.AdminModule;
import dz.elit.gpecpf.administration.entity.AdminObjetVisibilite;
import dz.elit.gpecpf.administration.entity.AdminUniteOrganisationnelle;
import dz.elit.gpecpf.administration.entity.AdminUtilisateur;
import dz.elit.gpecpf.administration.service.AdminDroitVisibiliteFacade;
import dz.elit.gpecpf.administration.service.AdminGroupeFacade;
import dz.elit.gpecpf.administration.service.AdminModuleFacade;
import dz.elit.gpecpf.administration.service.AdminUniteOrganisationnelleFacade;
import dz.elit.gpecpf.administration.service.AdminUtilisateurFacade;
import dz.elit.gpecpf.commun.util.MyUtil;
import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author laidani.youcef
 */
@ManagedBean
@ViewScoped
public class DroitVisibiliteController implements Serializable {

    @EJB
    private AdminUniteOrganisationnelleFacade adminUniteOrganisationnelleFacade;

    @EJB
    private AdminUtilisateurFacade adminUtilisateurFacade;

    @EJB
    private AdminDroitVisibiliteFacade adminDroitVisibiliteFacade;

    @EJB
    private AdminGroupeFacade adminGroupeFacade;

    @EJB
    private AdminModuleFacade adminModuleFacade;

    private AdminGroupe groupe;
    private List<AdminGroupe> listeGroupe;

    private List<AdminUniteOrganisationnelle> listeUnite;
    private AdminUniteOrganisationnelle unite;

    private List<AdminUtilisateur> listeUtilisateur;
    private AdminUtilisateur utilisateur;
    private AdminUtilisateur utilisateurAdt;

    private List<AdminObjetVisibilite> listeAdminObjetVisibilite;
    private AdminObjetVisibilite objetVisibilite;

    private TreeNode hierarchieUnite;
    private List<AdminUniteOrganisationnelle> uniteSelectionner;
    private List<String> listeString;

    private String position;
    private boolean positionBool;

    private String choisUtilisateur = "utilisateur";
    private boolean choix = true;

    private AdminModule module;
    private List<AdminModule> listeModule;

    private AdminUtilisateur connecteur;

    @PostConstruct
    public void init() {

        module = new AdminModule();
        listeModule = new ArrayList<>();

        listeModule = adminModuleFacade.findAll();

        listeString = new ArrayList<>();

        uniteSelectionner = new ArrayList<>();

        position = "horizontal";
        positionBool = false;

        groupe = new AdminGroupe();
        listeGroupe = adminGroupeFacade.findAll();

        listeUtilisateur = adminUtilisateurFacade.findAllOrderByUnite();
        utilisateur = new AdminUtilisateur();
        objetVisibilite = new AdminObjetVisibilite();

        //listeAdminObjetVisibilite = adminObjetVisibiliteFacade.findAll();
        Principal principal = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        utilisateur = (AdminUtilisateur) facesContext.getExternalContext().getSessionMap().get(principal.getName());
        utilisateurAdt = new AdminUtilisateur();

        listeUnite = new ArrayList<>();
        listeUnite = adminUniteOrganisationnelleFacade.findAllOrderById();

        creerTree();
    }

    public DroitVisibiliteController() {
    }

    public void remplireObjetVisibilite() {
        listeAdminObjetVisibilite = new ArrayList<>();
        for (int i = 0; i < listeModule.size(); i++) {
            if (listeModule.get(i).getId().equals(module.getId())) {
                listeAdminObjetVisibilite = listeModule.get(i).getListeObjectVisibilite();
                break;
            }
        }
    }

    public void selectUnite(AdminUniteOrganisationnelle uniteNode) {
        this.uniteSelectionner.add(uniteNode);
    }

    public void unselectUnite(AdminUniteOrganisationnelle uniteNode) {
        if (this.uniteSelectionner.contains(uniteNode)) {
            this.uniteSelectionner.remove(uniteNode);
        }
    }

    public void changeChoix() {
        choix = !choix;
        init();
    }

    public void creerTree() {

        uniteSelectionner = new ArrayList<>();
        List<TreeNode> listeTree = new ArrayList<>();
        for (int i = 0; i < listeUnite.size(); i++) {
            listeTree.add(new DefaultTreeNode(listeUnite.get(i)));
        }
        boolean root = false;
        for (int i = 0; i < listeTree.size(); i++) {
            for (int j = 0; j < listeTree.size(); j++) {
                if (((AdminUniteOrganisationnelle) listeTree.get(j).getData()).getUniteParent() != null
                        && ((AdminUniteOrganisationnelle) listeTree.get(j).getData()).getUniteParent().getId().equals(
                        ((AdminUniteOrganisationnelle) listeTree.get(i).getData()).getId())) {
                    /*Ajouter le fils au pere*/
                    TreeNode tre = listeTree.get(j);

                    if (((AdminUniteOrganisationnelle) tre.getData()).getCreerPar().getId().equals(utilisateur.getId())) {
                        tre.setSelectable(true);
                    } else if (!isDroitVisibilite(tre, utilisateur)) {
                        tre.setSelectable(false);
                    } else {
                        tre.setSelectable(true);
                    }
                    if ( isDroitVisibilite(tre, utilisateurAdt) || 
                            ((AdminUniteOrganisationnelle) tre.getData()).getCreerPar().getId().equals(utilisateurAdt.getId())) {
                        uniteSelectionner.add((AdminUniteOrganisationnelle) tre.getData());
                        listeString.add(((AdminUniteOrganisationnelle) tre.getData()).getCode());
                        tre.setSelected(true);
                    } else {
                        tre.setSelected(false);
                    }

                    listeTree.get(i).getChildren().add(tre);

                } else if (((AdminUniteOrganisationnelle) listeTree.get(j).getData()).getUniteParent() == null && root == false) {
                    root = true;
                    TreeNode tre = listeTree.get(j);
                    if (!isDroitVisibilite(tre, utilisateur)) {
                        tre.setSelectable(false);
                    } else {
                        tre.setSelectable(true);
                    }
                    if (isDroitVisibilite(tre, utilisateurAdt)) {
                        uniteSelectionner.add((AdminUniteOrganisationnelle) tre.getData());
                        listeString.add(((AdminUniteOrganisationnelle) tre.getData()).getCode());
                        tre.setSelected(true);
                    } else {
                        tre.setSelected(false);
                    }

                }
            }
        }
        if (listeUnite.size() > 0) {
            int indice = 0;
            for (int i = 0; i < listeTree.size(); i++) {
                if (indice < count(listeTree.get(i))) {
                    indice = i;
                    break;
                }
            }
            hierarchieUnite = listeTree.get(indice);
            collapsingORexpanding(hierarchieUnite, true);
        }
    }

    private int count(TreeNode node) {
        int count = 1;
        for (TreeNode child : node.getChildren()) {
            count += count(child);
        }
        return count;
    }

    private boolean isDroitVisibilite(TreeNode adminUniteOrganisationnelle, AdminUtilisateur util) {
        boolean isDroit = false;
        if (choix) {
            if (util.getAdminDroitVisibiliteList() != null) {
                for (AdminDroitVisibilite uniteOrg : util.getAdminDroitVisibiliteList()) {
                    if (uniteOrg.getIdUniteOrganisationnelle().getId().equals(((AdminUniteOrganisationnelle) adminUniteOrganisationnelle.getData()).getId())
                            && uniteOrg.getIdObjetVisibilite().getId().equals(objetVisibilite.getId())) {
                        isDroit = true;
                        break;
                    }
                }
            }
        } else if (!choix) {
            if (groupe.getAdminDroitVisibiliteList() != null) {
                for (AdminDroitVisibilite uniteOrg : groupe.getAdminDroitVisibiliteList()) {
                    if (uniteOrg.getIdUniteOrganisationnelle().getId().equals(((AdminUniteOrganisationnelle) adminUniteOrganisationnelle.getData()).getId())
                            && uniteOrg.getIdObjetVisibilite().getId().equals(objetVisibilite.getId())) {
                        isDroit = true;
                        break;
                    }
                }
            }
        }
        return isDroit;
    }

    public void addDroit() throws Exception {
        if (!utilisateur.equals(utilisateurAdt)) {
            try {

                if (choisUtilisateur.equals("utilisateur")) {
                    if (utilisateurAdt.getId() != null && objetVisibilite.getId() != 0) {
                        this.utilisateurAdt.setAdminDroitVisibiliteList(new ArrayList<AdminDroitVisibilite>());
                        List<AdminDroitVisibilite> listeDroits = new ArrayList<>();
                        for (AdminUniteOrganisationnelle unt : uniteSelectionner) {
                            listeDroits.add(new AdminDroitVisibilite(objetVisibilite, unt, utilisateurAdt));
                        }
                        utilisateurAdt.setAdminDroitVisibiliteList(listeDroits);
                        adminDroitVisibiliteFacade.createListeDroit(utilisateurAdt, objetVisibilite);

                        MyUtil.addInfoMessage("Droits de visibilité ajouter avec succès");
                    } else {
                        MyUtil.addWarnMessage("Merci de sélectioner un utilisateur et une entité");
                    }
                } else if (choisUtilisateur.equals("groupe")) {
                    if (groupe.getId() != null && objetVisibilite.getId() != 0) {
                        this.groupe.setAdminDroitVisibiliteList(new ArrayList<AdminDroitVisibilite>());
                        List<AdminDroitVisibilite> listeDroits = new ArrayList<>();
                        for (AdminUniteOrganisationnelle unt : uniteSelectionner) {
                            listeDroits.add(new AdminDroitVisibilite(objetVisibilite, unt, groupe));
                        }
                        groupe.setAdminDroitVisibiliteList(listeDroits);
                        adminDroitVisibiliteFacade.createListeDroitGroupe(groupe, objetVisibilite);

                        MyUtil.addInfoMessage("Droits de visibilité ajouter avec succès");
                    } else {
                        MyUtil.addWarnMessage("Merci de sélectioner un utilisateur et une entité");
                    }
                }
            } catch (Exception e) {
                System.out.println("Exception ajouter Droit de visibilité: " + e);
                MyUtil.addErrorMessage("Echec d'ajout des droits de visibilités");
            }
            init();
        } else {
            MyUtil.addErrorMessage("Echec d'ajouter des drois pour vous même, merci de contacter votre super administrateur");
        }

    }

    public void collapsingORexpanding(TreeNode node, boolean option) {
        if (node.getChildren().isEmpty()) {
            //node.setSelected(false);
        } else {
            for (TreeNode s : node.getChildren()) {
                collapsingORexpanding(s, option);
            }
            node.setExpanded(option);
            //node.setSelected(false);
        }
    }

    public void changerPosition() {
        if (positionBool) {
            position = "vertical";
        } else {
            position = "horizontal";
        }
    }

    public List<AdminUniteOrganisationnelle> getListeUnite() {
        return listeUnite;
    }

    public void setListeUnite(List<AdminUniteOrganisationnelle> listeUnite) {
        this.listeUnite = listeUnite;
    }

    public AdminUniteOrganisationnelle getUnite() {
        return unite;
    }

    public void setUnite(AdminUniteOrganisationnelle unite) {
        this.unite = unite;
    }

    public List<AdminUtilisateur> getListeUtilisateur() {
        return listeUtilisateur;
    }

    public void setListeUtilisateur(List<AdminUtilisateur> listeUtilisateur) {
        this.listeUtilisateur = listeUtilisateur;
    }

    public TreeNode getHierarchieUnite() {
        return hierarchieUnite;
    }

    public void setHierarchieUnite(TreeNode hierarchieUnite) {
        this.hierarchieUnite = hierarchieUnite;
    }

    public List<AdminUniteOrganisationnelle> getUniteSelectionner() {
        return uniteSelectionner;
    }

    public void setUniteSelectionner(List<AdminUniteOrganisationnelle> uniteSelectionner) {
        this.uniteSelectionner = uniteSelectionner;
    }

    public AdminUtilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(AdminUtilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public List<AdminObjetVisibilite> getListeAdminObjetVisibilite() {
        return listeAdminObjetVisibilite;
    }

    public void setListeAdminObjetVisibilite(List<AdminObjetVisibilite> listeAdminObjetVisibilite) {
        this.listeAdminObjetVisibilite = listeAdminObjetVisibilite;
    }

    public AdminObjetVisibilite getObjetVisibilite() {
        return objetVisibilite;
    }

    public void setObjetVisibilite(AdminObjetVisibilite objetVisibilite) {
        this.objetVisibilite = objetVisibilite;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean isPositionBool() {
        return positionBool;
    }

    public void setPositionBool(boolean positionBool) {
        this.positionBool = positionBool;
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

    public String getChoisUtilisateur() {
        return choisUtilisateur;
    }

    public void setChoisUtilisateur(String choisUtilisateur) {
        this.choisUtilisateur = choisUtilisateur;
    }

    public boolean isChoix() {
        return choix;
    }

    public void setChoix(boolean choix) {
        this.choix = choix;
    }

    public List<String> getListeString() {
        return listeString;
    }

    public void setListeString(List<String> listeString) {
        this.listeString = listeString;
    }

    public AdminUtilisateur getUtilisateurAdt() {
        return utilisateurAdt;
    }

    public void setUtilisateurAdt(AdminUtilisateur utilisateurAdt) {
        this.utilisateurAdt = utilisateurAdt;
    }

    public AdminModule getModule() {
        return module;
    }

    public void setModule(AdminModule module) {
        this.module = module;
    }

    public List<AdminModule> getListeModule() {
        return listeModule;
    }

    public void setListeModule(List<AdminModule> listeModule) {
        this.listeModule = listeModule;
    }

    public AdminUtilisateur getConnecteur() {
        return connecteur;
    }

    public void setConnecteur(AdminUtilisateur connecteur) {
        this.connecteur = connecteur;
    }

}
