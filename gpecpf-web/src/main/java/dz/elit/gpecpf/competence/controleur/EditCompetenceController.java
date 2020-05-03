/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.competence.controleur;

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
public class EditCompetenceController extends AbstractController implements Serializable  {
    @EJB
    private CompetenceFacade compFacade;
    @EJB
   DomaineCompetenceFacade domaineFacade;
    @EJB
    private TypeCompetenceFacade typeCompFacade;

     private Competence comp;
     
     private Domainecompetence domaineComp;
     private List<Domainecompetence> listDom;
     private Domainecompetence domSelected;

     
     private Typecompetence typeComp;
     private List<Typecompetence> listType;
     private Typecompetence typeSelected;
     
     String oldCode;
     
     // info pour chercher un domaine
     private String codeDom;
     private String libDom;
    
       // info pour chercher un type
     private String codeType;
     private String libType;
    public EditCompetenceController() {
    }
    
     @Override //PostConstruct
    protected void initController() 
    {    
        initEditComp();
        String id = MyUtil.getRequestParameter("id");
        if (id != null) {
            comp = compFacade.find(Integer.parseInt(id));
            domSelected=comp.getIddomcom();   
            typeSelected=comp.getIdtypcom();
            oldCode=comp.getCode();
        }
    }
    
    protected void initEditComp(){
      comp=new Competence();
      listDom=new ArrayList<>();
      listDom=domaineFacade.findAllOrderByAttribut("code");
      listType=new ArrayList<>();
      listType=typeCompFacade.findAllOrderByAttribut("code");
     domaineComp=new Domainecompetence();
     typeComp=new Typecompetence();
    }
    private boolean isExisteCode(String code) 
    {
        Competence Compo2 = compFacade.findByCode(code);
        if(Compo2 == null) {
            return false;
        } else {
            return true;
        }
    }
         
    public void edit() {
        try {
            comp.setCode(comp.getCode().toUpperCase());
            if (isExisteCode(comp.getCode()) && !comp.getCode().equals(oldCode)) {
            MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_existe_code"));//Erreur inconu   
            }else{
                     compFacade.edit(comp);
                     MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//"compétence modifié avec succès");
                }
            } catch (Exception ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu   
        }     
    }
    public void chercherDomComp(){
        listDom=domaineFacade.findByCodeLibelle(codeDom,libDom);
    }
      public void chercherTypeComp(){
        listType=typeCompFacade.findByCodeLibelle(codeType,libType);
    }
    public void editDomForComp(){
       // comp.setIddomcom(domSelected);
        comp.editDomComp(domaineComp);
      //  domSelected=new Domainecompetence();
    }    
    public void editTypeForComp(){
      //  comp.setIdtypcom(typeSelected);
      comp.addTypeComp(typeSelected);
       // typeSelected=new Typecompetence();

    }  
         
         // getter && setter 

    public Competence getComp() {
        return comp;
    }

    public void setComp(Competence comp) {
        this.comp = comp;
    }
    // getter && setter du domaine
    public List<Domainecompetence> getListDom() {
        return listDom;
    }

    public void setListDom(List<Domainecompetence> listDom) {
        this.listDom = listDom;
    }

    public Domainecompetence getDomSelected() {
        return domSelected;
    }

    public void setDomSelected(Domainecompetence domSelected) {
        this.domSelected = domSelected;
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
        // getter && setter du type

    public List<Typecompetence> getListType() {
        return listType;
    }

    public void setListType(List<Typecompetence> listType) {
        this.listType = listType;
    }

    public Typecompetence getTypeSelected() {
        return typeSelected;
    }

    public void setTypeSelected(Typecompetence typeSelected) {
        this.typeSelected = typeSelected;
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
    
}
