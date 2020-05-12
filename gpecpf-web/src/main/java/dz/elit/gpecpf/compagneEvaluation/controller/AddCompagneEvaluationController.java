/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.compagneEvaluation.controller;

import dz.elit.gpecpf.administration.entity.Prefixcodification;
import dz.elit.gpecpf.administration.service.AdminPrefixCodificationFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.compagne_evaluation.entity.Compagneevaluation;
import dz.elit.gpecpf.gestion_compagne_evaluation.service.CompagneEvaluationFacade;
import java.io.Serializable;
import java.util.ArrayList;
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
public class AddCompagneEvaluationController extends AbstractController implements Serializable {
    
    @EJB
    private CompagneEvaluationFacade compagneFacade;
    
    @EJB
    private AdminPrefixCodificationFacade prefFacade;
    
    private  List<Prefixcodification> listPrefix;
    
    private Compagneevaluation compagne;

    public AddCompagneEvaluationController() {
    }
    
    @Override//@PostConstruct
    protected void initController() {
            initAddCompagneEvaluation();      
    }
    private void initAddCompagneEvaluation() {
      compagne=new Compagneevaluation();
      chercherPrefix();
      
    }
    public void chercherPrefix()
    {
        listPrefix =new ArrayList<>();
        listPrefix=prefFacade.findAllOrderByAttribut("id");
        if(!listPrefix.isEmpty())
        compagne.setCode(listPrefix.get(0).getCompeva());
    }
    private boolean isVerifier() {
         
             if(compagne.getDatedeb().after(compagne.getDatefin()))
             {
               MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_date_debut_superieur_date_fin"));
                return false;
             }
         
        return true;
    }
    
      public void create() {
        try {  
                if(isVerifier())
                {
                    compagne.setCode(compagne.getCode().toUpperCase());
                    compagneFacade.create(compagne);
                    initAddCompagneEvaluation();
                    MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//Compagne évaluation enregistré avec succèe
                }
                
            } catch (MyException ex) {
                MyUtil.addErrorMessage(ex.getMessage());
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
