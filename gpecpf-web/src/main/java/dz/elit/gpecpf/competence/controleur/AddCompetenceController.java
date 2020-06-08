/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.competence.controleur;

import dz.elit.gpecpf.administration.entity.Prefixcodification;
import dz.elit.gpecpf.administration.service.AdminPrefixCodificationFacade;
import dz.elit.gpecpf.commun.exception.MyException;
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
public class AddCompetenceController extends AbstractController implements Serializable  {
    @EJB
    private CompetenceFacade compFacade;
    @EJB
   DomaineCompetenceFacade domaineFacade;
    @EJB
    private TypeCompetenceFacade typeCompFacade; 
    @EJB
    private AdminPrefixCodificationFacade prefFacade;
    @EJB
    private ComportementCompetenceFacade comportementFacade;
    
    private  List<Prefixcodification> listPrefix;
    
    private List<Domainecompetence> listDom;

    private Domainecompetence domaineCompSelected;
    
    private List<Typecompetence> listType;
    private Typecompetence typeCompSelected;
    
    private Competence comp;
    
    private List<Comportement> listComportement;
    private List<Comportement> listComportementSelected;
    
  // info pour chercher domaine  
    private String codeDom;
    private String libDom;
    
     // info pour chercher type  
    private String codeType;
    private String libType;
    
    // info pour chercher un comportement
    private String codeComportement;
    private String descriptionComportement;

    public AddCompetenceController() {
    }
    
         @Override //PostConstruct
    protected void initController() 
    {    
        initAddComp();
        

    }
     protected void initAddComp(){
        comp=new Competence();
        chercherPrefix();
       listDom =new ArrayList<>();
       listDom=domaineFacade.findAllOrderByAttribut("code");
       listType=new ArrayList<>();
       listType=typeCompFacade.findAllOrderByAttribut("code");
       domaineCompSelected=new Domainecompetence();
       typeCompSelected=new Typecompetence();
       listComportement =new ArrayList<>();
       listComportement=comportementFacade.findAllOrderByAttribut("code");
       listComportementSelected=new ArrayList<>();
     }
     
    public void chercherPrefix()
    {
        listPrefix =new ArrayList<>();
        listPrefix=prefFacade.findAllOrderByAttribut("id");
        if(!listPrefix.isEmpty())
        comp.setCode(listPrefix.get(0).getComp());
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
            comp.addDomComp(domaineCompSelected);  
        }

    }
     public void addTypeForComp(){
        if(typeCompSelected.getCode()!=null){
            comp.setIdtypcom(typeCompSelected);
        }
    }
    public void addComportementForCompetence() {
		if (!listComportementSelected.isEmpty()) {
                    comp.addListComportement(listComportementSelected);
                    listComportement.removeAll(listComportementSelected);
                    listComportementSelected = new ArrayList<>();
		}
	}

    public void removeComportementForcompetence(Comportement comportement) {
        comp.removeComportement(comportement);
        listComportement.add(comportement);
        Collections.sort(listComportement);
    }
    public void chercherComportement(){
        listComportement = comportementFacade.findByCodeDescription(codeComportement, descriptionComportement);
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
                                 comp.setCode(comp.getCode().toUpperCase());
                                if (isExisteCode(comp.getCode())) {
                                     MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_existe_code"));//Erreur inconu   
                                 }else{
                                        compFacade.create(comp);
                                        MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//Compétence crée avec succès
                                        initAddComp();
                                }
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

    public List<Comportement> getListComportement() {
        return listComportement;
    }

    public void setListComportement(List<Comportement> listComportement) {
        this.listComportement = listComportement;
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
    
}
