/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.compagneEvaluation.controller;

import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.compagne_evaluation.entity.Compagneevaluation;
import dz.elit.gpecpf.gestion_compagne_evaluation.service.CompagneEvaluationFacade;
import java.io.Serializable;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Kaci Ahmed
 */
@ManagedBean
@ViewScoped
public class EditCompagneEvaluationController extends AbstractController implements Serializable{
    
    @EJB
    private CompagneEvaluationFacade compagneFacade;
    
    private Compagneevaluation compagne;
    
    String oldCode;
    
    
    @Override//@PostConstruct
    protected void initController() {
      
        String id = MyUtil.getRequestParameter("id");
       if (id != null) {
           compagne = compagneFacade.find(Integer.parseInt(id));
           oldCode =compagne.getCode();
       }
   }
     private boolean isExisteCode(String code) 
    {
        Compagneevaluation compagne2 = compagneFacade.findByCode(code);
        if(compagne2 == null) {
            return false;
        } else {
            return true;
        }
    }
    private boolean isVerifier() {
         
             if(compagne.getDatedeb().after(compagne.getDatefin()))
             {
               MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_date_debut_superieur_date_fin"));
                return false;
             }
         
        return true;
    }
    public void edit() {
        try {
            compagne.setCode(compagne.getCode().toUpperCase());
            if (isExisteCode(compagne.getCode()) && !compagne.getCode().equals(oldCode)) {
            MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_existe_code"));//Erreur inconu   
            }else{
                    if(isVerifier())
                    {
                        compagneFacade.edit(compagne);
                        MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//"Compagne modifié avec succès");
                    }
                 }
            } catch (Exception ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
            
        }
    }
    
    // getter && setter 

    public Compagneevaluation getCompagne() {
        return compagne;
    }

    public void setCompagne(Compagneevaluation compagne) {
        this.compagne = compagne;
    }
    
       
}
