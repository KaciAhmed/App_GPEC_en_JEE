/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.competence.controleur;

import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.competence.entity.Domainecompetence;
import dz.elit.gpecpf.competence.service.DomaineCompetenceFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
public class EditDomaineCompetenceController  extends AbstractController implements Serializable{
    
@EJB
private DomaineCompetenceFacade domaineCompFacade;
 
   private Domainecompetence  domaine;
   private List<Domainecompetence> lstDomPere;
   private Domainecompetence domPereSelected;
   private Domainecompetence oldDomPere;
   
   String oldCode;
   
    private String codePere;
    private String libPere;
    
    @Override//@PostConstruct
    protected void initController() {
        lstDomPere=new ArrayList<>();
        lstDomPere=domaineCompFacade.findAllOrderByAttribut("code");
        
       String id = MyUtil.getRequestParameter("id");
       if (id != null) {
           domaine= domaineCompFacade.find(Integer.parseInt(id));
           initEdition();
       }
    }
    private void initEdition()
    {
       domPereSelected=domaine.getIddommere();
       oldDomPere=domaine.getIddommere();
       lstDomPere.remove(domaine);
       oldCode=domaine.getCode();
        
    }
    private boolean isExisteCode(String code) 
    {
        Domainecompetence domaine2 = domaineCompFacade.findByCode(code);
        if(domaine2 == null) {
            return false;
        } else {
            return true;
        }
    }
    public void edit() {
        try {
            domaine.setCode(domaine.getCode().toUpperCase());
            if (isExisteCode(domaine.getCode()) && !domaine.getCode().equals(oldCode)) {
            MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_existe_code"));//Erreur inconu   
            }else{
                     domaineCompFacade.edit(domaine);
                     MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//"Domaine modifié avec succès");
                     initEdition();
            }
            } catch (Exception ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
            
        }
    }
          public void chercherDomPere(){
        lstDomPere=domaineCompFacade.findByCodeLibelle(codePere, libPere);
    }
    public void editDomPereForDom(){
          if(domPereSelected==null && oldDomPere!=null)
          {
              oldDomPere.getDomainecompetenceCollection().remove(domaine);
              domaine.setIddommere(null);
              oldDomPere=domPereSelected;
          }else{
                if(oldDomPere==null && domPereSelected!=null ){
                     domaine.addDomPere(domPereSelected);
                     oldDomPere=domPereSelected;
                }else{

                    if(!domPereSelected.getCode().equals(oldDomPere.getCode()) ){

                        domaine.editDomPere(domPereSelected,oldDomPere);
                        oldDomPere=domPereSelected;
                    }
                }
               }
        //  System.out.println("-------------------------"+domPereSelected.getCode());
    }
    public void viderDompere(){
        domPereSelected=null;
    }
        
        // Getter && setter

    public Domainecompetence getDomaine() {
        return domaine;
    }

    public void setDomaine(Domainecompetence domaine) {
        this.domaine = domaine;
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
    
     
}