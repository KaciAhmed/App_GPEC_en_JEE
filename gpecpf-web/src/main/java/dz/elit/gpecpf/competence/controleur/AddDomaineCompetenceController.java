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

public class AddDomaineCompetenceController extends AbstractController implements Serializable {
   
   @EJB
   DomaineCompetenceFacade domaineFacade;
    
    private Domainecompetence domaine;
    
     public AddDomaineCompetenceController() {
    }

     @Override//@PostConstruct
    protected void initController() {
            initAddDomaineCompetence();
    }
        public void create() {
        try {           
                domaineFacade.create(domaine);
               // initAddDomaineCompetence();
                MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//"Domaine enregistré avec succè");
            } catch (MyException ex) {
                MyUtil.addErrorMessage(ex.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
                MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
            }
        
    }
    
       private void initAddDomaineCompetence() {
        domaine=new Domainecompetence();
    }
       // getter est setter

    public Domainecompetence getDomaine() {
        return domaine;
    }

    public void setDomaine(Domainecompetence domaine) {
        this.domaine = domaine;
    }

}
