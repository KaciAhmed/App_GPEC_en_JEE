/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.competence.controleur;

import dz.elit.gpecpf.commun.controller.Imprimer;
import dz.elit.gpecpf.commun.reporting.engine.Reporting;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.competence.entity.Typecompetence;
import dz.elit.gpecpf.competence.service.TypeCompetenceFacade;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
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
 * @author Kaci Ahmed
 */
@ManagedBean
@ViewScoped
public class ListTypeCompetenceController  extends AbstractController implements Serializable {
    
    @EJB
    private TypeCompetenceFacade typeCompFacade;
    
     @ManagedProperty(value = "#{imprimer}")
    private Imprimer ctrImprimer;
     
     private List<Typecompetence> listTypeCompetences;
    
    private Typecompetence typeComp;

    public ListTypeCompetenceController() {
    }
    
    
      @Override//@PostConstruct
    protected void initController() {
  
     findList()  ; 

       }
        
       public void findList() {
      
        listTypeCompetences= typeCompFacade.findAllOrderByAttribut("code");
        
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
        JRBeanCollectionDataSource data = new JRBeanCollectionDataSource(listTypeCompetences);
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
                param, new JRBeanCollectionDataSource(listTypeCompetences));
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
        JRBeanCollectionDataSource data = new JRBeanCollectionDataSource(listTypeCompetences);
        parametres.put("rapportNom", rapportNom);
        parametres.put("entreprisFr", entreprisFr);
        parametres.put("entreprisAr", entreprisAr);
        parametres.put("iSoRapport", iSoRapport);
        parametres.put("iMgDir", urlLogo);
        parametres.put("SUBREPORT_DIR", SUBREPORT_DIR);

        Reporting.downloadReportPdf(rapport, data, parametres);

    }
           
           // getter && setter 

    public TypeCompetenceFacade getTypeCompFacade() {
        return typeCompFacade;
    }

    public void setTypeCompFacade(TypeCompetenceFacade typeCompFacade) {
        this.typeCompFacade = typeCompFacade;
    }

    public List<Typecompetence> getListTypeCompetences() {
        return listTypeCompetences;
    }

    public void setListTypeCompetences(List<Typecompetence> listTypeCompetences) {
        this.listTypeCompetences = listTypeCompetences;
    }

    public Typecompetence getTypeComp() {
        return typeComp;
    }

    public void setTypeComp(Typecompetence typeComp) {
        this.typeComp = typeComp;
    }

    public Imprimer getCtrImprimer() {
        return ctrImprimer;
    }

    public void setCtrImprimer(Imprimer ctrImprimer) {
        this.ctrImprimer = ctrImprimer;
    }
           
    
}
