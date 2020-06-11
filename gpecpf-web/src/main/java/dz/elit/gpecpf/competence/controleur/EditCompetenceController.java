/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.competence.controleur;

import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.competence.entity.Competence;
import dz.elit.gpecpf.competence.entity.Comportement;
import dz.elit.gpecpf.competence.entity.Domainecompetence;
import dz.elit.gpecpf.competence.entity.Typecompetence;
import dz.elit.gpecpf.competence.service.CompetenceFacade;
import dz.elit.gpecpf.competence.service.ComportementCompetenceFacade;
import dz.elit.gpecpf.competence.service.DomaineCompetenceFacade;
import dz.elit.gpecpf.competence.service.TypeCompetenceFacade;
import dz.elit.gpecpf.poste.entity.Poste;
import dz.elit.gpecpf.poste.service.PosteFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;


/**
 *
 * @author Kaci Ahmed
 */
@ManagedBean
@ViewScoped
public class EditCompetenceController extends AbstractController implements Serializable  {
    @EJB
    private CompetenceFacade compFacade;
    @EJB
    DomaineCompetenceFacade domaineFacade;
    @EJB
    private TypeCompetenceFacade typeCompFacade;
    @EJB
    private ComportementCompetenceFacade comportementFacade;
    @EJB
    private PosteFacade posteFacade;
    

     private Competence comp;
     
     private Domainecompetence domaineComp;
     private List<Domainecompetence> listDom;
     private Domainecompetence domSelected;

     
     private Typecompetence typeComp;
     private List<Typecompetence> listType;
     private Typecompetence typeSelected;
     
     private List <Comportement> listeComportement;
     private List <Comportement> listComportementSelected;
     
     private List<Poste> listPostes;
     private List<Poste> listPostesCompetence;
     private List<Poste> listPostesSelected;
     private List<Poste> listPostesAdd;
     private List<Poste> listPostesDel;
     
     
     String oldCode;
     
     // info pour chercher un domaine
     private String codeDom;
     private String libDom;
    
       // info pour chercher un type
     private String codeType;
     private String libType;
     
      // info por chercher un comportement
      private String codeComportement;
      private String descriptionComportement;
    
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
            listeComportement.removeAll(comp.getListComportement());
            Collections.sort(comp.getListComportement());
            listPostesCompetence =posteFacade.postesForCompetence(comp);
            Collections.sort(listPostesCompetence);
            listPostes.removeAll(listPostesCompetence);
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
     listeComportement =new ArrayList<>();
     listeComportement=comportementFacade.findAllOrderByAttribut("code");
     listComportementSelected=new ArrayList<>();
     
     listPostes = new ArrayList();
     listPostes = posteFacade.findAllOrderByAttribut("code");
     listPostesSelected = new ArrayList<>();
     listPostesCompetence = new ArrayList();
     listPostesAdd = new ArrayList();
     listPostesDel = new ArrayList();
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
                     listPostesAdd.removeAll(posteFacade.postesForCompetence(comp));
                     posteFacade.editCompetence(comp, listPostesAdd, listPostesDel);
                     oldCode=comp.getCode();
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
    public void editDomForComp()
    {
        comp.editDomComp(domSelected);
    }    
    public void editTypeForComp()
    {
      comp.editTypeComp(typeSelected);
    }  
    
    public void addComportementForCompetence() {
	if (!listComportementSelected.isEmpty()) {
            comp.addListComportement(listComportementSelected);
            listeComportement.removeAll(listComportementSelected);
            listComportementSelected= new ArrayList<>();
            Collections.sort(comp.getListComportement());
        }
    }

    public void removeComportementForCompetence(Comportement comportement) {
      comp.removeComportement(comportement);
      listeComportement.add(comportement);
      Collections.sort(listeComportement);
    }
    public void chercherComportement(){
        listeComportement = comportementFacade.findByCodeDescription(codeComportement, descriptionComportement);
    }
    
    public void addPostesForCompetence() {
	if (!listPostesSelected.isEmpty()) {
            listPostesCompetence.addAll(listPostesSelected);
            Collections.sort(listPostesCompetence);
            listPostes.removeAll(listPostesSelected);
            Collections.sort(listPostes);
            listPostesAdd.addAll(listPostesSelected);
            listPostesDel.removeAll(listPostesSelected);
            listPostesSelected = new ArrayList<>();
	}
    }

    public void removePosteForCompetence(Poste poste) {
	listPostesCompetence.remove(poste);
        Collections.sort(listPostesCompetence);
	listPostes.add(poste);
        Collections.sort(listPostes);
	listPostesAdd.remove(poste);
	listPostesDel.add(poste);
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

    public List<Comportement> getListeComportement() {
        return listeComportement;
    }

    public void setListeComportement(List<Comportement> listeComportement) {
        this.listeComportement = listeComportement;
    }

    public List<Comportement> getListComportementSelected() {
        return listComportementSelected;
    }

    public void setListComportementSelected(List<Comportement> listComportementSelected) {
        this.listComportementSelected = listComportementSelected;
    }

    public String getCodeComportement() {
        return codeComportement;
    }

    public void setCodeComportement(String codeComportement) {
        this.codeComportement = codeComportement;
    }

    public String getDescriptionComportement() {
        return descriptionComportement;
    }

    public void setDescriptionComportement(String descriptionComportement) {
        this.descriptionComportement = descriptionComportement;
    }

    public List<Poste> getListPostes() {
        return listPostes;
    }

    public void setListPostes(List<Poste> listPostes) {
        this.listPostes = listPostes;
    }

    public List<Poste> getListPostesCompetence() {
        return listPostesCompetence;
    }

    public void setListPostesCompetence(List<Poste> listPostesCompetence) {
        this.listPostesCompetence = listPostesCompetence;
    }

    public List<Poste> getListPostesSelected() {
        return listPostesSelected;
    }

    public void setListPostesSelected(List<Poste> listPostesSelected) {
        this.listPostesSelected = listPostesSelected;
    }
    
    
}
