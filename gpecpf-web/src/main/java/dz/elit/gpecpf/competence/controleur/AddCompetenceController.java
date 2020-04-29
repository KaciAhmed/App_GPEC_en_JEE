/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.competence.controleur;

import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.gestion_des_competences.service.CompetenceFacade;
import dz.elit.gpecpf.gestion_des_competences.service.DomaineCompetenceFacade;
import dz.elit.gpecpf.gestion_des_competences.service.TypeCompetenceFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import otherEntity.Competence;
import otherEntity.Domainecompetence;
import otherEntity.Typecompetence;

@ManagedBean
@ViewScoped
public class AddCompetenceController extends AbstractController implements Serializable  {
    @EJB
    private CompetenceFacade compFacade;
    @EJB
   DomaineCompetenceFacade domaineFacade;
    @EJB
    private TypeCompetenceFacade typeCompFacade;
    
     private List<Domainecompetence> listDom;

    private Domainecompetence domaineCompSelected;
    
    private List<Typecompetence> listType;
     private Typecompetence typeCompSelected;
    
    private Competence comp;
    
  // info pour chercher domaine  
    private String codeDom;
    private String libDom;
    
     // info pour chercher type  
    private String codeType;
    private String libType;

    public AddCompetenceController() {
    }
    
         @Override //PostConstruct
    protected void initController() 
    {    
        initAddComp();
        

    }
     protected void initAddComp(){
      comp=new Competence();
     listDom =new ArrayList<>();
     listDom=domaineFacade.findAllOrderByAttribut("code");
     listType=new ArrayList<>();
     listType=typeCompFacade.findAllOrderByAttribut("code");
     domaineCompSelected=new Domainecompetence();
     typeCompSelected=new Typecompetence();
    }
    
     public void chercherDomaine(){
          listDom=domaineFacade.findByCodeLibelle(codeDom, libDom);
    }

    public void chercherTypeComp(){
          listType=typeCompFacade.findByCodeLibelle(codeDom, libDom);
    }
    public void recupDomaine(){
          listDom=domaineFacade.findAll();
    }
     public void recupTypeComp(){
          listType=typeCompFacade.findAll();
    }
    public void addDomaineForComp(){
        
        if(domaineCompSelected.getCode()!=null){
            //comp.setIddomcom(domaineCompSelected);
            comp.addDomComp(domaineCompSelected);
            domaineCompSelected=new Domainecompetence();       
        }

    }
     public void addTypeForComp(){
        if(typeCompSelected.getCode()!=null){
            comp.setIdtypcom(typeCompSelected);
            typeCompSelected=new Typecompetence();
        }

    }
    public void create() {
        try {
               if(comp.getIddomcom()==null)
                {
                    MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_add_competence_dom"));
                }else{  
                        if(comp.getIdtypcom()==null)
                        {
                           MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_add_competence_type"));
                       }else{
                                 compFacade.create(comp);
                                 MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//Compétence crée avec succès
                                 initAddComp();
                             }    
                } 

        } catch (MyException ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
        }
    }
     
    // getter && setter

    public Competence getComp() {
        return comp;
    }

    public void setComp(Competence comp) {
        this.comp = comp;
    }

    public String getCodeDom() {
        return codeDom;
    }

    public void setCodeDom(String codeDom) {
        this.codeDom = codeDom;
    }

    public String getLibDom() {
        return libDom;
    }

    public void setLibDom(String libDom) {
        this.libDom = libDom;
    }
    
    public List<Domainecompetence> getListDom() {
        return listDom;
    }

    public void setListDom(List<Domainecompetence> listDom) {
        this.listDom = listDom;
    }

    public Domainecompetence getDomaineCompSelected() {
        return domaineCompSelected;
    }

    public void setDomaineCompSelected(Domainecompetence domaineCompSelected) {
        this.domaineCompSelected = domaineCompSelected;
    }
 // partie type compétence

    public List<Typecompetence> getListType() {
        return listType;
    }

    public void setListType(List<Typecompetence> listType) {
        this.listType = listType;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getLibType() {
        return libType;
    }

    public void setLibType(String libType) {
        this.libType = libType;
    }

    public Typecompetence getTypeCompSelected() {
        return typeCompSelected;
    }

    public void setTypeCompSelected(Typecompetence typeCompSelected) {
        this.typeCompSelected = typeCompSelected;
    }
    
    
    
}
