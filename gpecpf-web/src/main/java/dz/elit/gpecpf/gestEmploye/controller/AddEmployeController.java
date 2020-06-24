/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.gestEmploye.controller;

import dz.elit.gpecpf.administration.entity.AdminUtilisateur;
import dz.elit.gpecpf.administration.entity.Prefixcodification;
import dz.elit.gpecpf.administration.service.AdminPrefixCodificationFacade;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import otherEntity.Commune;
import dz.elit.gpecpf.employe.entity.Employe;
import dz.elit.gpecpf.poste.entity.Poste;
import dz.elit.gpecpf.poste.service.PosteFacade;
import java.util.Calendar;
import java.util.Date;
import otherEntity.Historiqueemployeposte;
import otherEntity.Wilaya;

/**
 *
 * @author Kaci Ahmed
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
    @EJB
    private AdminPrefixCodificationFacade prefFacade;
    @EJB
    private AdminUtilisateurFacade userFacade;
    @EJB
    private PosteFacade posteFacade;
    
    private List <AdminUtilisateur> listUser;
     
   
    private  List<Prefixcodification> listPrefix;
    
    private Employe emp;
    
    private List<Formation> listFormations;
    private List<Formation> listFormationsSelected;
    
    private List<Wilaya> listWilayas;
    private Wilaya wilayaSelected;
    
    private List<Commune> listCommunes;
    private Commune communeSelected;
    
    private int idWil=0;
    private int idComune=0;
    
    private Historiqueemployeposte hystEmpPoste;
    private List <Poste> listPostes;
    private Poste posteSelected;
    private Poste posteEmp;


    private  int ageMinimale;
    private int ageMax;
    


    public AddEmployeController() {
    }
    
    @Override//@PostConstruct
    protected void initController() {
        initAddEmploye();
    }
    private void initAddEmploye() {
        emp = new Employe();
        chercherPrefix();
        //partie formation
        listFormationsSelected=new ArrayList<>();
        listFormations=new ArrayList<>();
        listFormations = formationFacade.findAllOrderByAttribut("description"); 
        // partie wilaya
         idWil=0;
        listWilayas = new ArrayList();
        listWilayas=wilayaFacade.findAllOrderByAttribut("nom");
        wilayaSelected =new Wilaya(); 
        // partie commune
        idComune=0;
        communeSelected=new Commune();
        listCommunes =new ArrayList<>();
        //partie user
        listUser=new ArrayList<>();
        
        // partie poste
        listPostes =new ArrayList<>();
        listPostes=posteFacade.findAllOrderByAttribut("code");
        posteSelected=new Poste();
        
        ageMinimale=18;  
        ageMax=70;
    }
    public void chercherPrefix()
    {   
        listPrefix =new ArrayList<>();
        listPrefix=prefFacade.findAllOrderByAttribut("id");
        if(!listPrefix.isEmpty())
        emp.setMatricule(listPrefix.get(0).getEmploye());
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
          Collections.sort(listCommunes);
         }   
     }
     public void obtenirCommune()
     {
         if(idComune!=0){
         communeSelected=communeFacade.find(idComune);
         emp.setIdcommune(communeSelected);
         }       
     }
     private boolean isDateVerifier() {
         
          if(emp.getDate_recrutement().after(new Date()))
         {
               MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_date_recrutement"));
                return false;
         }
         if(emp.getDate_depart()!=null && emp.getDate_depart().before(new Date()))
         {
               
               MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_date_depart"));
                return false;
         }
         if(emp.getDate_depart()!=null)
         {
             if(emp.getDate_recrutement().after(emp.getDate_depart()))
             {
               MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_date_recrutement_superieur_date_depart"));
                return false;
             }
         }
        return true;
    }
     
     private boolean isExisteUser(){
         // le cas ou le champ est vide
         if(emp.getUserName().isEmpty())
         {
             return true;
         }else{
                listUser = userFacade.findByNomPrenomLoginForEmp(emp.getNom(),emp.getPrenom(),emp.getUserName());
                if(!listUser.isEmpty())
                {
                    return true;
                }
                return false;
              }
     }
     
     private boolean userDejaAffecter(){  
      // le cas ou le champ est vide
         if(emp.getUserName().isEmpty())
         {
             return false;
         }else{
                    Employe emp3=null;
                    if(!listUser.isEmpty())
                    {
                       emp3 = empFacade.findByUserName(listUser.get(0).getLogin());
                    }

                   if(emp3 == null) {
                       return false;
                   } else {
                       return true;
                   }
              }
         
     }
    private boolean isExisteMatricule(String matricule) 
    {
        Employe emp2 = empFacade.findByMatricule(matricule);
        if(emp2 == null) {
            return false;
        }else {
            return true;
        }
    }
    private Boolean vrfDateNaissance()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(emp.getDtNaissance());
        int annee_naiss=calendar.get(Calendar.YEAR);
        int mois_naiss=calendar.get(Calendar.MONTH);
        int jour_naiss=calendar.get(Calendar.DATE);

        
        calendar = Calendar.getInstance();
        calendar.setTime(emp.getDate_recrutement());
        int anneRec= calendar.get(Calendar.YEAR);
        int moisRec= calendar.get(Calendar.MONTH);
        int jourRec = calendar.get(Calendar.DATE);
        
        if(annee_naiss+ ageMinimale < anneRec)
            return true;
        else{
             if(annee_naiss+ ageMinimale == anneRec){
                if(mois_naiss < moisRec){
                    return true;
                }else{
                    if(mois_naiss==moisRec){
                        if(jour_naiss <= jourRec){
                            return true;
                        }
                    }
                }
            }   
        }
            return false; 
    }
    private Boolean vrfAgeMax()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(emp.getDtNaissance());
        int annee_naiss=calendar.get(Calendar.YEAR);
        int mois_naiss=calendar.get(Calendar.MONTH);
        int jour_naiss=calendar.get(Calendar.DATE);

        
        calendar = Calendar.getInstance();
        calendar.setTime(emp.getDate_recrutement());
        int anneRec= calendar.get(Calendar.YEAR);
        int moisRec= calendar.get(Calendar.MONTH);
        int jourRec = calendar.get(Calendar.DATE);
        
        if(annee_naiss+ ageMax > anneRec)
            return true;
        else{
            if(annee_naiss+ ageMax == anneRec){
                if(mois_naiss>moisRec){
                    return true;
                }else{
                    if(mois_naiss==moisRec){
                        if(jour_naiss>jourRec){
                            return true;
                        }
                    }
                }
            }  
        }
            return false; 
    }
    private Boolean vrfDtNaiss_DtRec()
    {
        if(emp.getDate_recrutement().before(emp.getDtNaissance()))
        {
            return false;
        }
        return true;
    }
    
   
    private void creerHistoriqueEmployePoste(){
        hystEmpPoste =new Historiqueemployeposte(emp.getId(),posteSelected.getId(),emp.getDate_recrutement()); 
        hystEmpPoste.setEmploye(emp);
        hystEmpPoste.setPoste(posteSelected); 
     //   hystEmpPoste.setDatedeb(emp.getDate_recrutement());
        if(emp.getDate_depart()!=null){
            hystEmpPoste.setDatefin(emp.getDate_depart());
        }
        emp.getListHistoriqueEmployePoste().add(hystEmpPoste);
        posteSelected.getListHistoriqueEmployePoste().add(hystEmpPoste); 
    }
    public void create() {
        try { 
                if(isDateVerifier()){
                   emp.setMatricule(emp.getMatricule().toUpperCase());
                   if (isExisteMatricule(emp.getMatricule())) {
                        MyUtil.addErrorMessage(MyUtil.getBundleCommun(" msg_erreur_existe_matricule")); 
                    }else{
                            if (emp.getListFormation().isEmpty()) {
                                 MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_list_formation_vide"));   
                            }else{
                                    if(!isExisteUser()){
                                      MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_utilisateur_inexistant")); 
                                    }else{  if(userDejaAffecter()){
                                                 MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_utilisateur_deja_affecte"));   
                                             }else{  
                                                    if(! vrfDateNaissance()){
                                                        MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_employe_mineur"));  
                                                    }else{
                                                        if(!vrfAgeMax())
                                                        {
                                                          MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_employe_vieux"));                                                            
                                                        }else{
                                                                if(!vrfAgeMax())
                                                                {
                                                                  MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_employe_vieux"));                                                            
                                                                }else{
                                                                    if(!vrfDtNaiss_DtRec())
                                                                    {
                                                                      MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_dt_naissance_sup_dt_recrutement"));                                                            
                                                                    }else{
                                                                        if(posteSelected== null || posteSelected.getCode()==null)
                                                                        {
                                                                            MyUtil.addErrorMessage(MyUtil.getBundleCommun("  msg_erreur_periode_poste ")); 
                                                                        }else{
                                                                                    empFacade.create(emp);
                                                                                    creerHistoriqueEmployePoste();
                                                                                    empFacade.edit(emp);
                                                                                    MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//Employé crée avec succès
                                                                                    initAddEmploye();
                                                                                    posteEmp=null; 
                                                                                    

                                                                              }
                                                                         }
                                                                      }
                                                                }
                                                            }
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
    
    public void newEmploye() {
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
            emp.removeFormation(frm);
            listFormations.add(frm);
    }
    public void addPosteForEmploye()
    { 
       if(posteSelected==null || posteSelected.getCode()==null)
       {
             MyUtil.addWarnMessage(MyUtil.getBundleCommun("  msg_erreur_periode_poste ")); 
           
       }else{
           posteEmp=posteSelected;
       }
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

    public List<Commune> getListCommunes() {
        return listCommunes;
    }

    public void setListCommunes(List<Commune> listCommunes) {
        this.listCommunes = listCommunes;
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

    public Historiqueemployeposte getHystEmpPoste() {
        return hystEmpPoste;
    }

    public void setHystEmpPoste(Historiqueemployeposte hystEmpPoste) {
        this.hystEmpPoste = hystEmpPoste;
    }

    public List<Poste> getListPostes() {
        return listPostes;
    }

    public void setListPostes(List<Poste> listPostes) {
        this.listPostes = listPostes;
    }

    public Poste getPosteSelected() {
        return posteSelected;
    }

    public void setPosteSelected(Poste posteSelected) {
        this.posteSelected = posteSelected;
    }

    public Poste getPosteEmp() {
        return posteEmp;
    }

    public void setPosteEmp(Poste posteEmp) {
        this.posteEmp = posteEmp;
    }
  
}
