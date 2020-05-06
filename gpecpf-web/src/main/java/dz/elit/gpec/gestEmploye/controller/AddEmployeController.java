/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpec.gestEmploye.controller;

import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;

import dz.elit.gpecpf.gestion_employe.service.EmployeFacade;
import dz.elit.gpecpf.poste.entity.Formation;
import dz.elit.gpecpf.poste.service.FormationFacade;
import dz.elit.gpecpf.wilaya.commune.service.CommuneFacade;
import dz.elit.gpecpf.wilaya.commune.service.WilayaFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import otherEntity.Commune;
import otherEntity.Employe;
import otherEntity.Wilaya;

/**
 *
 * @author Dell
 */
@ManagedBean
@ViewScoped
public class AddEmployeController extends AbstractController implements Serializable{
    
    //utilisateur =employe  profile =formation unité organisationnel = wilaya et commune
    @EJB
    private EmployeFacade empFacade;
    @EJB
    private FormationFacade formationFacade;
    
    @EJB
    private WilayaFacade wilayaFacade;
    @EJB
    private CommuneFacade communeFacade;
    
    private Employe emp;
    
    private List<Formation> listFormations;
    private List<Formation> listFormationsSelected;
    
    private List<Wilaya> listWilayas;
    private Wilaya wilayaSelected;
    private List<Commune> listCommunes;
    private Commune communeSelected;
    
  private int idWil=0;
  private int idComune=0;


  
    

    public AddEmployeController() {
    }
    
    @Override//@PostConstruct
    protected void initController() {
        initAddEmploye();
    }
     private void initAddEmploye() {
        emp = new Employe();
        listFormationsSelected=new ArrayList<>();
        listFormations=new ArrayList<>();
        listFormations = formationFacade.findAllOrderByAttribut("description"); 
        communeSelected=new Commune();
        listWilayas = new ArrayList();
        listWilayas=wilayaFacade.findAllOrderByAttribut("code");
        wilayaSelected =new Wilaya(); 
        communeSelected=new Commune();
        listCommunes =new ArrayList<>();
    }
     public void CommuneParWilaya()
     {
         if(idWil !=0 )
         {
           wilayaSelected=wilayaFacade.find(idWil);
           listCommunes =new ArrayList<>();
          for(Commune com:wilayaSelected.getCommuneCollection())
          {
              listCommunes.add(com);
          }
         }   
     }
     public void obtenirCommune()
     {
         if(idComune!=0){
         communeSelected=communeFacade.find(idComune);
         emp.setIdcommune(communeSelected);
         }       
     }
     private boolean isVerifier() {
         if(emp.getDate_depart()!=null)
         {
             if(emp.getDate_recrutement().after(emp.getDate_depart()))
             {
               MyUtil.addWarnMessage(MyUtil.getBundleCommun("msg_date_recrutement_superieur_date_depart"));
                return false;
             }
         }
        return true;
    }
    private boolean isExisteCode(String code) 
    {
        Employe emp2 = empFacade.findByCode(code);
        if(emp2 == null) {
            return false;
        } else {
            return true;
        }
    }
    public void create() {
        try { 
                if(isVerifier()){
                   emp.setCode(emp.getCode().toUpperCase());
                   if (isExisteCode(emp.getCode())) {
                        MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_existe_code"));//Erreur inconu   
                    }else{
                            if (emp.getListFormation().isEmpty()) {
                                 MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_list_formation_vide"));//Erreur inconu   
                            }else{
                                 empFacade.create(emp);
                                 MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//Compétence crée avec succès
                                 initAddEmploye();
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
    public void newEmploye() {
        idWil=0;
        idComune=0;
        initAddEmploye();
    }
  
    public void addFormationForEmploye() {
        if (!listFormationsSelected.isEmpty()) {
            
            emp.addListFormation(listFormationsSelected);
            listFormations.removeAll(listFormationsSelected);
            listFormationsSelected= new ArrayList<>();
        }
    }
    public void removeFormationForEmploye(Formation frm) 
    {
        System.out.println("aaaaa");
            emp.removeFormation(frm);
            listFormations.add(frm);
      
    }
 
    // getter && setter

    public Employe getEmp() {
        return emp;
    }

    public void setEmp(Employe emp) {
        this.emp = emp;
    }

    public List<Formation> getListFormations() {
        return listFormations;
    }

    public void setListFormations(List<Formation> listFormations) {
        this.listFormations = listFormations;
    }

    public List<Formation> getListFormationsSelected() {
        return listFormationsSelected;
    }

    public void setListFormationsSelected(List<Formation> listFormationsSelected) {
        this.listFormationsSelected = listFormationsSelected;
    }

    public List<Wilaya> getListWilayas() {
        return listWilayas;
    }

    public void setListWilayas(List<Wilaya> listWilayas) {
        this.listWilayas = listWilayas;
    }

    public Wilaya getWilayaSelected() {
        return wilayaSelected;
    }

    public void setWilayaSelected(Wilaya wilayaSelected) {
        this.wilayaSelected = wilayaSelected;
    }

    public List<Commune> getListCommunes() {
        return listCommunes;
    }

    public void setListCommunes(List<Commune> listCommunes) {
        this.listCommunes = listCommunes;
    }

    public Commune getCommuneSelected() {
        return communeSelected;
    }

    public void setCommuneSelected(Commune communeSelected) {
        this.communeSelected = communeSelected;
    }
        public int getIdWil() {
        return idWil;
    }

    public void setIdWil(int idWil) {
        this.idWil = idWil;
    }

    public int getIdComune() {
        return idComune;
    }

    public void setIdComune(int idComune) {
        this.idComune = idComune;
    }
    
}
