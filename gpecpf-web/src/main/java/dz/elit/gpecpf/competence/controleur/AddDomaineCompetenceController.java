/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.competence.controleur;

import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.gestion_des_competences.service.DomaineCompetenceFacade;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import otherEntity.Domainecompetence;

/**
 *
 * @author Dell
 */
@ManagedBean
@ViewScoped
public class AddDomaineCompetenceController extends AbstractController implements Serializable {
   
   @EJB
   DomaineCompetenceFacade domaineFacade;
    
    private Domainecompetence domaine;
    
    private List<Domainecompetence> lstDomPere;
    private Domainecompetence domPereSelected;
    
    private String codePere;
    private String libPere;
    
     public AddDomaineCompetenceController() {
    }

    public void addDomPereConst()
    {  
    }
    
    @Override//@PostConstruct
    protected void initController() {
            initAddDomaineCompetence();      
    }
    private void initAddDomaineCompetence() {
      domaine=new Domainecompetence();
      domPereSelected=new Domainecompetence(); 
      lstDomPere=new ArrayList<>();
      lstDomPere=domaineFacade.findAllOrderByAttribut("code");
    }
    
    public void create() {
        try {      
              domaineFacade.create(domaine);
               initAddDomaineCompetence();
                MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//"Domaine enregistré avec succè");
            } catch (MyException ex) {
                MyUtil.addErrorMessage(ex.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
                MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
            }
        
    }
  
    public void chercherDomPere(){
        lstDomPere=domaineFacade.findByCodeLibelle(codePere, libPere);
    }
    
    public void addDomPereForDom(){
        if(domPereSelected.getCode()!=null){
        
            domaine.addDomPere(domPereSelected);
            lstDomPere.removeAll((Collection<?>) domPereSelected);
            domPereSelected=new Domainecompetence(); 
        }
    }
            
       // getter est setter

    public Domainecompetence getDomaine() {
        return domaine;
    }

    public void setDomaine(Domainecompetence domaine) {
        this.domaine = domaine;
    }

    public DomaineCompetenceFacade getDomaineFacade() {
        return domaineFacade;
    }

    public void setDomaineFacade(DomaineCompetenceFacade domaineFacade) {
        this.domaineFacade = domaineFacade;
    }

    public List<Domainecompetence> getLstDomPere() {
        return lstDomPere;
    }

    public void setLstDomPere(List<Domainecompetence> lstDomPere) {
        this.lstDomPere = lstDomPere;
    }

    public Domainecompetence getDomPereSelected() {
        return domPereSelected;
    }

    public void setDomPereSelected(Domainecompetence domPereSelected) {
        this.domPereSelected = domPereSelected;
    }
    
    public String getCodePere() {
        return codePere;
    }

    public void setCodePere(String codePere) {
        this.codePere = codePere;
    }

    public String getLibPere() {
        return libPere;
    }

    public void setLibPere(String libPere) {
        this.libPere = libPere;
    }
    

}
