/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.administration.controller;

import dz.elit.gpecpf.administration.entity.AdminConnecxionHistorique;
import dz.elit.gpecpf.administration.entity.AdminUtilisateur;
import dz.elit.gpecpf.administration.service.AdminUtilisateurFacade;
import dz.elit.gpecpf.commun.controller.Imprimer;
import dz.elit.gpecpf.commun.reporting.engine.Reporting;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.commun.util.StaticUtil;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.context.RequestContext;

/**
 *
 * @author leghettas.rabah
 */
@ManagedBean
@ViewScoped
public class ListUtilisateurController extends AbstractController implements Serializable {

    @EJB
    private AdminUtilisateurFacade utilisateurFacade;

    @ManagedProperty(value = "#{imprimer}")
    private Imprimer ctrImprimer;

    private List<AdminUtilisateur> listUtilisateurs;
    private List<AdminConnecxionHistorique> listConnecxionHistorique = new ArrayList<AdminConnecxionHistorique>();

    //Les variables de recherche
    private String nom;
    private String prenom;
    private String login;

    /**
     * Creates a new instance of ListUtilisateurController
     */
    public ListUtilisateurController() {

    }

    @Override //PostConstruct
    protected void initController() {
        findList();
    }

    public void remove(AdminUtilisateur utilisateur) {
        String loginCurrent = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();
        if (loginCurrent.equals(utilisateur.getLogin())) {
            MyUtil.addWarnMessage(MyUtil.getBundleAdmin("msg_vous_pouvez_pas_supprimer_votre_compte"));
        } else {
            try {
                utilisateurFacade.remove(utilisateur);
                MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//"Utilisateur supprimé");
                findList();
            } catch (Exception ex) {
                ex.printStackTrace();
                MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
            }
        }
    }

    public void initialisePwdUtilisateur(AdminUtilisateur utilisateur) {
        try {
            utilisateur.setPwd(StaticUtil.getDefaultEncryptPassword());
            utilisateurFacade.edit(utilisateur);
            MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//"Mot de passe réinitialisé avec succés");
        } catch (Exception ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
        }
    }

    public void rechercher() {
        listUtilisateurs = utilisateurFacade.findByNomPrenomLogin(nom, prenom, login);
        if (listUtilisateurs.isEmpty() || listUtilisateurs.size() < 1) {
            MyUtil.addInfoMessage(MyUtil.getBundleAdmin("msg_resultat_recherche_null"));
        }
    }

    private void findList() {
        listUtilisateurs = utilisateurFacade.findAllOrderByAttribut("login");
        rechercher();
    }

    public void download() throws SQLException, IOException {
        String rapportLien = "/dz/elit/harmo/commun/reporting/source/listUtilisateur.jasper";
        InputStream rapport = getClass().getResourceAsStream(rapportLien);
        String SUBREPORT_DIR = getClass().getResource("/dz/elit/harmo/commun/reporting/source/Entete/").getFile();
        String rapportNom = "rapportNom";
        String entreprisFr = "entreprisFr";
        String entreprisAr = "entreprisAr";
        String iSoRapport = "iSoRapport";
        InputStream urlLogo = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/images/images-login/logo.png");
        Map parametres = new HashMap();
        JRBeanCollectionDataSource data = new JRBeanCollectionDataSource(listUtilisateurs);
        parametres.put("rapportNom", rapportNom);
        parametres.put("entreprisFr", entreprisFr);
        parametres.put("entreprisAr", entreprisAr);
        parametres.put("iSoRapport", iSoRapport);
        parametres.put("iMgDir", urlLogo);
        parametres.put("SUBREPORT_DIR", SUBREPORT_DIR);
        ctrImprimer.setData(data);
        ctrImprimer.setParametres(parametres);
        ctrImprimer.setRapport(rapport);
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("width", 350);
        options.put("contentHeight", 90);
        options.put("contentWidth", 310);
        RequestContext.getCurrentInstance().openDialog("/pages/commun/download.xhtml", options, null);
    }

    public String telecharger() throws IOException, JRException {

        Map<String, String> param = new HashMap<>();
        param.put("rapportNom", "Test");

        Reporting.printEtat(getClass().getResourceAsStream("/dz/elit/gpecpf/reporting/source/test.jasper"),
                param, new JRBeanCollectionDataSource(listUtilisateurs));
        return "";

    }

    public void creerRapportUnique() throws JRException, FileNotFoundException {

        String rapportLien = "/reporting/source/listUtilisateur.jasper";
        InputStream rapport = getClass().getResourceAsStream(rapportLien);
        String rapportNom = "Test";
        String entreprisFr = "Elit";
        String entreprisAr = "شركة";
        String iSoRapport = "iSoRapport";
        String SUBREPORT_DIR = getClass().getResource("/dz/elit/harmo/commun/reporting/source/Entete/").getFile();
        InputStream urlLogo = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/images/images-login/logo.png");
        Map parametres = new HashMap();
        JRBeanCollectionDataSource data = new JRBeanCollectionDataSource(listUtilisateurs);
        parametres.put("rapportNom", rapportNom);
        parametres.put("entreprisFr", entreprisFr);
        parametres.put("entreprisAr", entreprisAr);
        parametres.put("iSoRapport", iSoRapport);
        parametres.put("iMgDir", urlLogo);
        parametres.put("SUBREPORT_DIR", SUBREPORT_DIR);

        Reporting.downloadReportPdf(rapport, data, parametres);

    }

    // getter && setter
    public List<AdminUtilisateur> getListUtilisateurs() {
        return listUtilisateurs;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Imprimer getCtrImprimer() {
        return ctrImprimer;
    }

    public void setCtrImprimer(Imprimer ctrImprimer) {
        this.ctrImprimer = ctrImprimer;
    }

    public List<AdminConnecxionHistorique> getListConnecxionHistorique() {
        return listConnecxionHistorique;
    }

    public void setListConnecxionHistorique(List<AdminConnecxionHistorique> listConnecxionHistorique) {
        this.listConnecxionHistorique = listConnecxionHistorique;
    }
}
