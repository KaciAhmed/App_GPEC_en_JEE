/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.administration.controller;

import dz.elit.gpecpf.administration.entity.AdminConnexionEncours;
import dz.elit.gpecpf.administration.service.AdminConnexionEncoursFacade;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author laidani.youcef
 */
@ManagedBean
@ViewScoped
public class ListConnexionEnCours {

    @EJB
    private AdminConnexionEncoursFacade connexionEncoursFacade;
    
    private List<AdminConnexionEncours> listeConnexionEncours;
    
    @PostConstruct
    public void init(){
        listeConnexionEncours = connexionEncoursFacade.findAll();
    }
    
    public ListConnexionEnCours() {
    }

    public List<AdminConnexionEncours> getListeConnexionEncours() {
        return listeConnexionEncours;
    }

    public void setListeConnexionEncours(List<AdminConnexionEncours> listeConnexionEncours) {
        this.listeConnexionEncours = listeConnexionEncours;
    }
}
