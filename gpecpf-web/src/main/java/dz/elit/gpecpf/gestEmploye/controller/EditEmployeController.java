/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.gestEmploye.controller;

import dz.elit.gpecpf.administration.entity.AdminUtilisateur;
import dz.elit.gpecpf.administration.service.AdminUtilisateurFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.gestion_employe.service.EmployeFacade;
import dz.elit.gpecpf.poste.entity.Formation;
import dz.elit.gpecpf.poste.service.FormationFacade;
import dz.elit.gpecpf.wilaya.commune.service.CommuneFacade;
import dz.elit.gpecpf.wilaya.commune.service.WilayaFacade;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
 * @author Kaci Ahmed
 */

@ManagedBean
@ViewScoped
public class EditEmployeController extends AbstractController implements Serializable {
    
     //utilisateur =employe  profile =formation unité organisationnel = wilaya et commune
    @EJB
    private EmployeFacade empFacade;
      @EJB
    private FormationFacade formationFacade;
    
    @EJB
    private WilayaFacade wilayaFacade;
    @EJB
    private CommuneFacade communeFacade;
    @EJB
    private AdminUtilisateurFacade userFacade;
    
    private List <AdminUtilisateur> listUser;
    
    private Employe emp;
    
    private List<Formation> listFormations;
    private List<Formation> listFormationsSelected;
    
    private List<Wilaya> listWilayas;
    private Wilaya wilayaSelected;
    private Wilaya wil;   
    
    
    private List<Commune> listCommunes;
    private Commune communeSelected;
    private int idComune;
    
    
    private String dtNaiss;
    private String dtRec;
    private String dtDep;
    
    private String oldMatricule;
    
    SimpleDateFormat formater = null;

    public EditEmployeController() {
    }
      @Override//@PostConstruct
    protected void initController() {
        initEditEmploye();    
        String id = MyUtil.getRequestParameter("id");
        // partie date
        if (id != null) {
            emp =empFacade.find(Integer.parseInt(id));
            formater = new SimpleDateFormat("dd-MM-yyyy");
            dtNaiss=formater.format(emp.getDtNaissance());
            if(emp.getDate_depart()!=null)           
                dtDep =formater.format(emp.getDate_depart());
            else dtDep="  ";
                dtRec=formater.format(emp.getDate_recrutement());
        // partie wilaya commune
           recupWilaya();
           idComune= emp.getIdcommune().getId();
           CommuneParWilaya();
        // partie formation   
        listFormationsSelected= emp.getListFormation();
        listFormations = formationFacade.findAllOrderByAttribut("description"); 
        listFormations.removeAll(listFormationsSelected);
        
        // partie user
        listUser=new ArrayList<>();
        
         oldMatricule =emp.getMatricule();
        }
    }
     private void initEditEmploye() {
        emp = new Employe();  
        listWilayas = new ArrayList();
        listWilayas=wilayaFacade.findAllOrderByAttribut("nom");
        wilayaSelected =new Wilaya();   
         idComune=0;
         listCommunes =new ArrayList<>();
        communeSelected=new Commune();      
        listFormations=new ArrayList<>();
        listFormationsSelected=new ArrayList<>();
    }
     public void recupWilaya(){
         wil =new Wilaya();
         wil = emp.getIdcommune().getIdwilaya();
        
     }
     
     public void CommuneParWilaya()
     {
         if(wil !=null )
         {
           wilayaSelected=wilayaFacade.find(wil.getId());
           listCommunes =new ArrayList<>();
          for(Commune com:wilayaSelected.getCommuneCollection())
          {
              listCommunes.add(com);
          }
         }   
        Collections.sort(listCommunes);
     }
     
     public void obtenirCommune()
     {
        
         if(idComune!=0){
         communeSelected=communeFacade.find(idComune);
         emp.setIdcommune(communeSelected);
         } 
         
     }
     // partie formation
  
    public void addFormationForEmploye() {
        if (!listFormationsSelected.isEmpty()) {
            
            emp.addListFormation(listFormationsSelected);
            listFormations.removeAll(listFormationsSelected);
            listFormationsSelected= new ArrayList<>();
        }
    }
    public void removeFormationForEmploye(Formation frm) 
    {
            emp.removeFormation(frm);
            listFormations.add(frm);
      
    }
     // edit -----------------------
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
    private boolean isExisteMatricule(String matricule) 
    {
        Employe emp2 = empFacade.findByMatricule(matricule);
        if(emp2 == null) {
            return false;
        } else {
            return true;
        }
    }
      private boolean isExisteUser(){
         listUser = userFacade.findByNomPrenomLoginForEmp(emp.getNom(),emp.getPrenom(),emp.getUserName());
         if(!listUser.isEmpty())
         {
             return true;
         }
         return false;
     }
     private boolean userDejaAffecterAvant(){
        
 
         Employe emp3=null;
         if(!listUser.isEmpty())
         {
            emp3 = empFacade.findByUserName(listUser.get(0).getLogin());
            if(emp3!=null && !emp3.getMatricule().equals(emp.getMatricule()))
            {
                return true;
            }
         }     
        return false;
         
     }

        public void edit() {
        try { 
                if(isVerifier()){
                   emp.setMatricule(emp.getMatricule().toUpperCase());
                   if (isExisteMatricule(emp.getMatricule()) && !emp.getMatricule().equals(oldMatricule)) {
                        MyUtil.addErrorMessage(MyUtil.getBundleCommun(" msg_erreur_existe_matricule"));//Erreur inconu   
                    }else{
                            if (emp.getListFormation().isEmpty()) {
                                 MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_list_formation_vide"));//Erreur inconu   
                            }else{
                                    if(!isExisteUser())
                                {
                                  MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_utilisateur_inexistant"));//Erreur inconu   
                                }else{  if(userDejaAffecterAvant())
                                         {
                                             MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_utilisateur_deja_affecte"));//Erreur inconu   
                                         }else{ 
                                                empFacade.edit(emp);
                                                MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//Employé édité avec succès
                                               }
                                      }
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
     
     
     // getter && setter 

    public Employe getEmp() {
        return emp;
    }

    public void setEmp(Employe emp) {
        this.emp = emp;
    }
   

    public String getDtNaiss() {
        return dtNaiss;
    }

    public void setDtNaiss(String dtNaiss) {
        this.dtNaiss = dtNaiss;
    }

    public String getDtRec() {
        return dtRec;
    }

    public void setDtRec(String dtRec) {
        this.dtRec = dtRec;
    }

    public String getDtDep() {
        return dtDep;
    }

    public void setDtDep(String dtDep) {
        this.dtDep = dtDep;
    }

    public List<Wilaya> getListWilayas() {
        return listWilayas;
    }

    public void setListWilayas(List<Wilaya> listWilayas) {
        this.listWilayas = listWilayas;
    }

    public List<Commune> getListCommunes() {
        return listCommunes;
    }

    public void setListCommunes(List<Commune> listCommunes) {
        this.listCommunes = listCommunes;
    }

    public int getIdComune() {
        return idComune;
    }

    public void setIdComune(int idComune) {
        this.idComune = idComune;
    }
// partie formation
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

    public Wilaya getWil() {
        return wil;
    }

    public void setWil(Wilaya wil) {
        this.wil = wil;
    }
     
    
}
