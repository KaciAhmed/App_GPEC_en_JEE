/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.competence.controleur;

import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.competence.entity.Typecompetence;
import dz.elit.gpecpf.competence.service.TypeCompetenceFacade;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;


/**
 *
 * @author Kaci Ahmed
 */
@ManagedBean
@ViewScoped
public class EditTypeCompetenceController extends AbstractController implements Serializable {
    
    @EJB
    private TypeCompetenceFacade typeCompFacade;
    
    private Typecompetence typecomp;
    
       @Override//@PostConstruct
    protected void initController() {
  
       String id = MyUtil.getRequestParameter("id");
       if (id != null) {
       typecomp= typeCompFacade.find(Integer.parseInt(id));
       }
    }
    
    // getter && setter

    public Typecompetence getTypecomp() {
        return typecomp;
    }

    public void setTypecomp(Typecompetence typecomp) {
        this.typecomp = typecomp;
    }
    
}
