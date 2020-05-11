package dz.elit.gpecpf.administration.controller;

import dz.elit.gpecpf.administration.entity.AdminProfil;
import dz.elit.gpecpf.administration.entity.AdminUniteOrganisationnelle;
import dz.elit.gpecpf.administration.entity.AdminUtilisateur;
import dz.elit.gpecpf.administration.service.AdminProfilFacade;
import dz.elit.gpecpf.administration.service.AdminUniteOrganisationnelleFacade;
import dz.elit.gpecpf.administration.service.AdminUtilisateurFacade;
import dz.elit.gpecpf.commun.controller.MySessionController;
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
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author leghettas.rabah
 */
@ManagedBean
@ViewScoped
public class EditUtilisateurController extends AbstractController implements Serializable {

    @EJB
    private AdminUtilisateurFacade utilisateurFacade;
    @EJB
    private AdminUniteOrganisationnelleFacade uniteOrganisationnelleFacade;
    @EJB
    private AdminProfilFacade profilFacade;

    @ManagedProperty(value = "#{mySessionController}")
    private MySessionController sessionController;
    private AdminUtilisateur utilisateur;

    private List<AdminProfil> listProfils;
    private List<AdminProfil> listProfilsRemoved;
    private List<AdminProfil> listProfilsSelected;

    private int etatCompte;
    private Date dateDuJour;
    private String ancienPwd;
    private String nouveauPwd;
    private String confirmPwd;

    private List<AdminUniteOrganisationnelle> listUniteOrganisationnelle;
    private AdminUniteOrganisationnelle uniteOrganisationnelleSelected;

    private boolean disabled = false;

    /**
     * Creates a new instance of ListUtilisateurController
     */
    public EditUtilisateurController() {
    }

    @Override//@PostConstruct
    protected void initController() {
        dateDuJour = new Date();
        initEditUtilisateur();  
        String compte = MyUtil.getRequestParameter("compte");
        if (compte != null) {
            utilisateur = sessionController.getUtilisateurCourant();
            if (utilisateur.getActive() && utilisateur.getDateExpiration() == null) {
                etatCompte = 1;
            } else if (utilisateur.getActive()) {
                etatCompte = 2;
            } else {
                etatCompte = 3;
            }
        }

        String id = MyUtil.getRequestParameter("id");
        if (id != null) {
            utilisateur = utilisateurFacade.find(Integer.parseInt(id));
            if (utilisateur.getActive() && utilisateur.getDateExpiration() == null) {
                etatCompte = 1;
            } else if (utilisateur.getActive()) {
                etatCompte = 2;
            } else {
                etatCompte = 3;
            }
            listProfils.removeAll(utilisateur.getListAdminProfil());
            uniteOrganisationnelleSelected = utilisateur.getAdminUniteOrganisationnelle();
        }
    }

    private boolean isVerifier() {
        if (utilisateur.getDateActivation() != null && utilisateur.getDateExpiration() != null) {
            if (utilisateur.getDateActivation().after(utilisateur.getDateExpiration())) {
                MyUtil.addWarnMessage(MyUtil.getBundleAdmin("msg_date_activation_doit_superieure_date_expiration"));//"La date d'activation doit être superieure a la date d'expiration ");
                return false;
            }
        }
        return true;
    }

    /**
     * Méthode permer de modifier l'utilisateur sélectionner.
     */
    public void edit() {
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
                //utilisateur.setPwd(StaticUtil.getDefaultEncryptPassword(utilisateur));
             /*   if (uniteOrganisationnelleSelected != null && uniteOrganisationnelleSelected.getId() != null) {
                    utilisateur.setAdminUniteOrganisationnelle(uniteOrganisationnelleSelected);
                }*/
                utilisateurFacade.edit(utilisateur, listProfilsRemoved);
                setDisabled(true);
                MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//"Utilisateur modifié avec succè");
            } catch (MyException ex) {
                MyUtil.addErrorMessage(ex.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
                MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
            }
        }
    }

    public String editPwdUtilisateur() {
        utilisateur = sessionController.getUtilisateurCourant();
        if (!nouveauPwd.equals(confirmPwd)) {
            MyUtil.addErrorMessage(MyUtil.getBundleAdmin("msg_confirmer_votre_pwd"));//"Veuillez confirmer votre mot de passe ");
            return null;
        } else if (!StaticUtil.getEncryptPassword(ancienPwd).equals(utilisateur.getPwd())) {
            MyUtil.addErrorMessage(MyUtil.getBundleAdmin("msg_ancien_pwd_incorrect  "));
            return null;
        } else if (ancienPwd.equals(nouveauPwd)) {
            MyUtil.addErrorMessage(MyUtil.getBundleAdmin("msg_nouveau_pwd_doit_différent_ancien"));
            return null;
        } else if (isMotPasseConforme(nouveauPwd)) {
            utilisateur.setPwd(StaticUtil.getEncryptPassword(nouveauPwd));
            try {
                utilisateurFacade.edit(utilisateur);
                //StaticUtil.addInfoMessage("Mot de passe modifier avec succès");
                return sessionController.logout();
            } catch (Exception ex) {
                ex.printStackTrace();
                MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
                return null;
            }
        }
        return null;
    }

    /**
     * Reteurn true si la chatre de sécurité est respecté sinon false. cette
     * methode vérifier si le mot de passe respecte la charte de sécurité des
     * mots de passe
     *
     * @param motPasse: le mot de passe à verifier
     * @return true si motPasse respecte la chatre de sécurité sinon return
     * false.
     */
    private boolean isMotPasseConforme(String motPasse) {
        if (motPasse.length() < 8) {
            MyUtil.addErrorMessage(MyUtil.getBundleAdmin("msg_longueure_pwd_doit_superieur_8_caracteres"));
            return false;
        } else if (!motPasse.matches(".*[a-z].*") || !motPasse.matches(".*[A-Z].*") || !motPasse.matches(".*[0-9].*")) {
            MyUtil.addErrorMessage(MyUtil.getBundleAdmin("msg_pwd_costraintes"));
//                    + "Le  mot de passe doit contenir un mixte d’au minimum 3 des points suivants :"
//                    + "\n lettres majuscules, lettres minuscules, chiffres. "
//                    + " \n Exemple: Elit2014Harmo");
            return false;
        }
        return true;
    }

    private void initEditUtilisateur() {
        utilisateur = new AdminUtilisateur();
        utilisateur.setDateActivation(new Date());
        etatCompte = 1;
        listProfilsSelected = new ArrayList<>();
        listProfilsRemoved = new ArrayList<>();
        listProfils = profilFacade.findAllOrderByAttribut("libelle");
        listUniteOrganisationnelle = uniteOrganisationnelleFacade.findAllOrderByAttribut("id");
        uniteOrganisationnelleSelected = new AdminUniteOrganisationnelle();
    }

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
            listProfilsRemoved.add(adminProfil);
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

    public void setListProfils(List<AdminProfil> listProfils) {
        this.listProfils = listProfils;
    }

    /**
     * Get the value of etatCompte
     *
     * @return the value of etatCompte
     */
    public int getEtatCompte() {
        return etatCompte;
    }

    /**
     * Set the value of etatCompte
     *
     * @param etatCompte new value of etatCompte
     */
    public void setEtatCompte(int etatCompte) {
        this.etatCompte = etatCompte;
    }

    public Date getDateDuJour() {
        return dateDuJour;
    }

    public List<AdminProfil> getListProfilsSelected() {
        return listProfilsSelected;
    }

    public void setListProfilsSelected(List<AdminProfil> listProfilsSelected) {
        this.listProfilsSelected = listProfilsSelected;
    }

    public void setSessionController(MySessionController sessionController) {
        this.sessionController = sessionController;
    }

    public MySessionController getSessionController() {
        return sessionController;
    }

    public String getAncienPwd() {
        return ancienPwd;
    }

    public void setAncienPwd(String ancienPwd) {
        this.ancienPwd = ancienPwd;
    }

    public String getNouveauPwd() {
        return nouveauPwd;
    }

    public void setNouveauPwd(String nouveauPwd) {
        this.nouveauPwd = nouveauPwd;
    }

    public String getConfirmPwd() {
        return confirmPwd;
    }

    public void setConfirmPwd(String confirmPwd) {
        this.confirmPwd = confirmPwd;
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

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
