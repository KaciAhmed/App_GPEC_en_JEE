/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.administration.controller;

import dz.elit.gpecpf.administration.entity.Prefixcodification;
import dz.elit.gpecpf.administration.service.AdminPrefixCodificationFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import java.io.Serializable;
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
public class AcceuilPrefixCodificationController extends AbstractController implements Serializable {
      @EJB
      private AdminPrefixCodificationFacade codifFacade;
      
      private Prefixcodification codif;
      
      private List <Prefixcodification> listCodif;
      
      private boolean nouveau ;

    public AcceuilPrefixCodificationController() {
    }
     
    @Override//@PostConstruct
    protected void initController() {
       codif =new Prefixcodification();
       nouveau =true;
       listCodif =codifFacade.findAll();
       if(!listCodif.isEmpty())
       {
           codif = listCodif.get(0);
           nouveau =false;
       }
    }
    
      public void valider() {
        try { 
               if(nouveau==true)
               {
                   codifFacade.create(codif);
                   MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));// préfixe codification créé avec succès
               }else{
                   codifFacade.edit(codif);
                   MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//préfixe codification mofifié avec succès
               }                        
        } catch (MyException ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
        }
    }
      
      // getter & setter 

    public Prefixcodification getCodif() {
        return codif;
    }

    public void setCodif(Prefixcodification codif) {
        this.codif = codif;
    }

}
