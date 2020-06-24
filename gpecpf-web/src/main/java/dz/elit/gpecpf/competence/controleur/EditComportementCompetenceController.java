/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.competence.controleur;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.competence.entity.Competence;
import dz.elit.gpecpf.competence.entity.Comportement;
import dz.elit.gpecpf.competence.service.CompetenceFacade;
import dz.elit.gpecpf.competence.service.ComportementCompetenceFacade;

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
public class EditComportementCompetenceController extends AbstractController implements Serializable {
    
    @EJB
    private ComportementCompetenceFacade ComportementFacade;
        
    @EJB
    private CompetenceFacade compFacade;
        
    private Comportement compo;
    private String oldCode;
        

     private List<Competence> listComp;
     private List <Competence> listcompSelected;
     private List<Competence> listCompetencesComportement ;
     private List<Competence> listCompetenceAdd;
     private List<Competence> listCompetencesDel;
     
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
            listCompetencesComportement =compFacade.competenceForComportement(compo);
            if(listCompetencesComportement!=null && !listCompetencesComportement.isEmpty())
            {
                Collections.sort(listCompetencesComportement);
            }
            listComp.removeAll(listCompetencesComportement);
            if(listComp!=null && !listComp.isEmpty())
            {
                Collections.sort(listComp);
            }
            oldCode=compo.getCode();
        }
    } 
    protected void initEltCompo(){
      compo=new Comportement();
      listComp=new ArrayList<>();     
      listComp=compFacade.findAllOrderByAttribut("code");
      listcompSelected=new ArrayList<>();
      listCompetencesComportement=new ArrayList<>();
      listCompetenceAdd=new ArrayList<>();
      listCompetencesDel=new ArrayList<>();
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
                    oldCode=compo.getCode();
                    listCompetenceAdd.removeAll(compFacade.competenceForComportement(compo));
                    compFacade.editComportemnt(compo, listCompetenceAdd, listCompetencesDel);       
            }
              
            } catch (Exception ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu   
        }     
    }
     public void chercherComp(){
         listComp=compFacade.findByCodeLibelle(codeComp, libComp);
    }

    public void addCompetenceForComprtement() {
	if (!listcompSelected.isEmpty()) {
            listCompetencesComportement.addAll(listcompSelected);
            if(listCompetencesComportement!=null && !listCompetencesComportement.isEmpty())
            {
                Collections.sort(listCompetencesComportement);
            }
            listComp.removeAll(listcompSelected);
            if(listComp!=null && !listComp.isEmpty())
            {
                Collections.sort(listComp);
            }
            listCompetenceAdd.addAll(listcompSelected);
            listCompetencesDel.removeAll(listcompSelected);
            listcompSelected = new ArrayList<>();
	}
    }

	public void removeCompetenceForComportement(Competence competence) {
          
		listCompetencesComportement.remove(competence);
                if(listCompetencesComportement!=null && !listCompetencesComportement.isEmpty())
                {
                    Collections.sort(listCompetencesComportement);
                }
		listComp.add(competence);
                if(listComp!=null && !listComp.isEmpty())
                {
                    Collections.sort(listComp);
                }
		listCompetenceAdd.remove(competence);
		listCompetencesDel.add(competence);
               
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

    public List<Competence> getListcompSelected() {
        return listcompSelected;
    }

    public void setListcompSelected(List<Competence> listcompSelected) {
        this.listcompSelected = listcompSelected;
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

    public List<Competence> getListCompetencesComportement() {
        return listCompetencesComportement;
    }

    public void setListCompetencesComportement(List<Competence> listCompetencesComportement) {
        this.listCompetencesComportement = listCompetencesComportement;
    }

    
    
    
}
