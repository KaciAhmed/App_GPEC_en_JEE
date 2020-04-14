/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
package dz.elit.gpecpf.competence.controleur;


import dz.elit.gpecpf.gestion_des_competences.service.DomaineCompetenceFacade;

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
import otherEntity.Domainecompetence;
/**
 *
 * @author Dell
 *//*
@ManagedBean
@ViewScoped
public class ListDomaineCompetenceController extends AbstractController implements Serializable {

    @EJB
    private DomaineCompetenceFacade domaineCompFacade;
    
    @ManagedProperty(value = "#{imprimer}")
    private Imprimer ctrImprimer;
      
    private List<Domainecompetence> listDomaineCompetences;
    
        //Les variables de recherche
    private String code;
    private String libelle;
    private String description;
    
    public ListDomaineCompetenceController(){
        
    }
    
      
    @Override //PostConstruct
    protected void initController() 
    {    
        findList()  ;  
    }

    public ListDomaineCompetenceController(String code, String libelle, String description) {
        this.code = code;
        this.libelle = libelle;
        this.description = description;
    }
    
     private void findList() {
       listDomaineCompetences = domaineCompFacade.findAllOrderByAttribut("code");
        rechercher();
    }
       public void rechercher() {
        listDomaineCompetences= domaineCompFacade.findByCodeLibelleDescription(code, libelle, description);
        if (listDomaineCompetences.isEmpty() || listDomaineCompetences.size() < 1) {
            MyUtil.addInfoMessage(MyUtil.getBundleAdmin("msg_resultat_recherche_null"));
        }
    }
       
    public void remove(Domainecompetence domComp) 
    {
     try {
            domaineCompFacade.remove(domComp);
              MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//"Utilisateur supprimé");
             findList();
         } catch (Exception ex) 
           {
             ex.printStackTrace();
             MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
           }   
    }
    
    public void download() throws SQLException, IOException 
    {
        String rapportLien = "/dz/elit/harmo/commun/reporting/source/listUtilisateur.jasper";
        InputStream rapport = getClass().getResourceAsStream(rapportLien);
        String SUBREPORT_DIR = getClass().getResource("/dz/elit/harmo/commun/reporting/source/Entete/").getFile();
        String rapportNom = "rapportNom";
        String entreprisFr = "entreprisFr";
        String entreprisAr = "entreprisAr";
        String iSoRapport = "iSoRapport";
        InputStream urlLogo = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/images/images-login/logo.png");
        Map parametres = new HashMap();
        JRBeanCollectionDataSource data = new JRBeanCollectionDataSource(listDomaineCompetences);
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
                param, new JRBeanCollectionDataSource(listDomaineCompetences));
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
        JRBeanCollectionDataSource data = new JRBeanCollectionDataSource(listDomaineCompetences);
        parametres.put("rapportNom", rapportNom);
        parametres.put("entreprisFr", entreprisFr);
        parametres.put("entreprisAr", entreprisAr);
        parametres.put("iSoRapport", iSoRapport);
        parametres.put("iMgDir", urlLogo);
        parametres.put("SUBREPORT_DIR", SUBREPORT_DIR);

        Reporting.downloadReportPdf(rapport, data, parametres);

    }
        
   // getter and setter

    public DomaineCompetenceFacade getDomaineCompFacade() {
        return domaineCompFacade;
    }

    public void setDomaineCompFacade(DomaineCompetenceFacade domaineCompFacade) {
        this.domaineCompFacade = domaineCompFacade;
    }

    public Imprimer getCtrImprimer() {
        return ctrImprimer;
    }

    public void setCtrImprimer(Imprimer ctrImprimer) {
        this.ctrImprimer = ctrImprimer;
    }

    public List<Domainecompetence> getListDomaineCompetences() {
        return listDomaineCompetences;
    }

    public void setListDomaineCompetences(List<Domainecompetence> listDomaineCompetences) {
        this.listDomaineCompetences = listDomaineCompetences;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
       
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
        
    
}
*/