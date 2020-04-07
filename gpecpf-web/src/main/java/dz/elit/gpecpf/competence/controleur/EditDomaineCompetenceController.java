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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import otherEntity.Competence;
import otherEntity.Domainecompetence;

/**
 *
 * @author Dell
 */
@ManagedBean
@ViewScoped
public class EditDomaineCompetenceController  extends AbstractController implements Serializable{
    
@EJB
private DomaineCompetenceFacade domaineCompFacade;

 
   private Domainecompetence  domaine;
   

    
    @Override//@PostConstruct
    protected void initController() {
  
       String id = MyUtil.getRequestParameter("id");
       
       if (id != null) {
       domaine= domaineCompFacade.find(Integer.parseInt(id));

       }
    }
    
  
    
    
        public void edit() {
        try {
            domaineCompFacade.edit(domaine);
            MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//"Privilège modifié avec succès");
        } catch (Exception ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
        }
    }
        
        // Getter && setter

    public Domainecompetence getDomaine() {
        return domaine;
    }

    public void setDomaine(Domainecompetence domaine) {
        this.domaine = domaine;
    }

 
        
}
