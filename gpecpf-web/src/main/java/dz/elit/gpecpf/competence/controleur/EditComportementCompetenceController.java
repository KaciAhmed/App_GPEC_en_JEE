/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.competence.controleur;

import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.gestion_des_competences.service.CompetenceFacade;
import dz.elit.gpecpf.gestion_des_competences.service.ComportementCompetenceFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import otherEntity.Competence;
import otherEntity.Comportement;

/**
 *
 * @author Dell
 */
@ManagedBean
@ViewScoped
public class EditComportementCompetenceController extends AbstractController implements Serializable {
    
        @EJB
      private ComportementCompetenceFacade ComportementFacade;
        
        @EJB
    private CompetenceFacade compFacade;
        
    private Comportement compo;
    private String oldCode;
        
     private Competence competence;
     private List<Competence> listComp;
     private Competence compSelected;
     
     // info pour chercher une compétence
     private String codeComp;
     private String libComp;

    public EditComportementCompetenceController() {
    }
    @Override //PostConstruct
    protected void initController() 
    {    
       initEltCompo();        

            String id = MyUtil.getRequestParameter("id");
        if (id != null) {
            compo = ComportementFacade.find(Integer.parseInt(id));
            compSelected=compo.getIdcomp();
            oldCode=compo.getCode();
        }
    } 
    protected void initEltCompo(){
      compo=new Comportement();
      competence=new Competence();
      listComp=new ArrayList<>();     
      listComp=compFacade.findAllOrderByAttribut("code");
    }
    
    private boolean isExisteCode(String code) 
    {
        Comportement Compo2 = ComportementFacade.findByCode(code);
        if(Compo2 == null) {
            return false;
        } else {
            return true;
        }
    }
    public void edit() {
        try {
            compo.setCode(compo.getCode().toUpperCase());
            if (isExisteCode(compo.getCode()) && !compo.getCode().equals(oldCode)) {
            MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_existe_code"));//Erreur inconu   
            }else{
                  ComportementFacade.edit(compo);
                MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//"comportement modifié avec succès"); 
            }
              
            } catch (Exception ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu   
        }     
    }
     public void chercherComp(){
         listComp=compFacade.findByCodeLibelle(codeComp, libComp);
    }

    public void editCompForComportement(){
        compo.editTypeComp(compSelected);
       // compo.setIdcomp(compSelected);
        compSelected=new Competence();
    }
      // getter && setter 

    public Comportement getCompo() {
        return compo;
    }

    public void setCompo(Comportement compo) {
        this.compo = compo;
    }

    public List<Competence> getListComp() {
        return listComp;
    }

    public void setListComp(List<Competence> listComp) {
        this.listComp = listComp;
    }

    public Competence getCompSelected() {
        return compSelected;
    }

    public void setCompSelected(Competence compSelected) {
        this.compSelected = compSelected;
    }

    public String getCodeComp() {
        return codeComp;
    }

    public void setCodeComp(String codeComp) {
        this.codeComp = codeComp;
    }

    public String getLibComp() {
        return libComp;
    }

    public void setLibComp(String libComp) {
        this.libComp = libComp;
    }

    
    
    
}
