package dz.elit.gpecpf.administration.controller;

import dz.elit.gpecpf.administration.entity.AdminProfil;
import dz.elit.gpecpf.administration.entity.AdminUniteOrganisationnelle;
import dz.elit.gpecpf.administration.entity.AdminUtilisateur;
import dz.elit.gpecpf.administration.service.AdminProfilFacade;
import dz.elit.gpecpf.administration.service.AdminUniteOrganisationnelleFacade;
import dz.elit.gpecpf.administration.service.AdminUtilisateurFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.commun.util.StaticUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
public class AddUtilisateurController extends AbstractController implements Serializable {

    @EJB
    private AdminUtilisateurFacade utilisateurFacade;
    @EJB
    private AdminProfilFacade profilFacade;
    @EJB
    private AdminUniteOrganisationnelleFacade uniteOrganisationnelleFacade;

    private AdminUtilisateur utilisateur;

    private List<AdminProfil> listProfils;
    private List<AdminProfil> listProfilsSelected;
    private List<AdminUniteOrganisationnelle> listUniteOrganisationnelle;
    private AdminUniteOrganisationnelle uniteOrganisationnelleSelected;

    private int etatCompte;
    private Date dateDuJour;
    private boolean disabled = false;

    /**
     * Creates a new instance of ListUtilisateurController
     */
    public AddUtilisateurController() {
    }

    @Override//@PostConstruct
    protected void initController() {
        dateDuJour = new Date();
        initAddUtilisateur();
    }

    private boolean isVerifier() {
        if (utilisateur.getDateActivation() != null && utilisateur.getDateExpiration() != null) {
            if (utilisateur.getDateActivation().after(utilisateur.getDateExpiration())) {
                MyUtil.addWarnMessage(MyUtil.getBundleAdmin("msg_date_activation_doit_superieure_date_expiration"));
                return false;
            }
        }
        return true;
    }

    public void create() {
        if (isVerifier()) {
            try {
                switch (etatCompte) {
                    case 1:
                        utilisateur.setActive(true);
                        utilisateur.setDateExpiration(null);
                        break;
                    case 2:
                        utilisateur.setActive(true);
                        break;
                    case 3:
                        utilisateur.setActive(false);
                        utilisateur.setDateExpiration(null);
                        break;
                }
                utilisateur.setPwd(StaticUtil.getDefaultEncryptPassword());
                utilisateur.setLogin(utilisateur.getNom().toLowerCase() + "." + utilisateur.getPrenom().toLowerCase());
                if (uniteOrganisationnelleSelected != null && uniteOrganisationnelleSelected.getId() != null) {
                    utilisateur.setAdminUniteOrganisationnelle(uniteOrganisationnelleSelected);
                }
                utilisateurFacade.create(utilisateur);
                setDisabled(true);
                initAddUtilisateur();
                MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//"Utilisateur enregistré avec succè");
            } catch (MyException ex) {
                MyUtil.addErrorMessage(ex.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
                MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
            }
        }
    }

    public void newUtilisateur() {
        initAddUtilisateur();
        setDisabled(false);
    }

    private void initAddUtilisateur() {
        utilisateur = new AdminUtilisateur();
        utilisateur.setDateActivation(new Date());
        etatCompte = 1;
        listProfilsSelected = new ArrayList<>();
        listProfils = profilFacade.findAllOrderByAttribut("libelle");
        listUniteOrganisationnelle = uniteOrganisationnelleFacade.findAllOrderByAttribut("id");
        uniteOrganisationnelleSelected = new AdminUniteOrganisationnelle();
    }
// ajout est suppression de profile
    public void addProfilsForUtilisateur() {
        if (!listProfilsSelected.isEmpty()) {
            utilisateur.addListProfils(listProfilsSelected);
            listProfils.removeAll(listProfilsSelected);
            listProfilsSelected = new ArrayList<>();
        }
    }

    public void removeProfilsForUtilisateur(AdminProfil adminProfil) {

        if (adminProfil != null) {

            utilisateur.removeProfil(adminProfil);
            listProfils.add(adminProfil);
        }
    }

    // Getter and setter
    public AdminUtilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(AdminUtilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public List<AdminProfil> getListProfils() {
        return listProfils;
    }

    public List<AdminProfil> getListProfilsSelected() {
        return listProfilsSelected;
    }

    public void setListProfilsSelected(List<AdminProfil> listProfilsSelected) {
        this.listProfilsSelected = listProfilsSelected;
    }

    public List<AdminUniteOrganisationnelle> getListUniteOrganisationnelle() {
        return listUniteOrganisationnelle;
    }

    public void setListUniteOrganisationnelle(List<AdminUniteOrganisationnelle> listUniteOrganisationnelle) {
        this.listUniteOrganisationnelle = listUniteOrganisationnelle;
    }

    public AdminUniteOrganisationnelle getUniteOrganisationnelleSelected() {
        return uniteOrganisationnelleSelected;
    }

    public void setUniteOrganisationnelleSelected(AdminUniteOrganisationnelle uniteOrganisationnelleSelected) {
        this.uniteOrganisationnelleSelected = uniteOrganisationnelleSelected;
    }

    public int getEtatCompte() {
        return etatCompte;
    }

    public void setEtatCompte(int etatCompte) {
        this.etatCompte = etatCompte;
    }

    public Date getDateDuJour() {
        return dateDuJour;
    }

    public void setDateDuJour(Date dateDuJour) {
        this.dateDuJour = dateDuJour;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

}
