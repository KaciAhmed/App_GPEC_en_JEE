/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.commun.controller;

import dz.elit.gpecpf.commun.util.AbstractController;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author AYADI hakim && CHEKOR Samir && LEGHATTAS rabah && LAIDANI Youcef 
 * ManagedBean avec un scope application pour les informations static
 */

/* «Eager = true» indique que le bean géré est créé avant d'être demandé pour la première fois 
ce bean contient un name donc il poura etre injecter dans un autre bean 
(on pourra apeler les méthode de ce bean via un attribut de l'autre bean)*/
@ManagedBean(name = "myApplicationController", eager = true)
@ApplicationScoped
public class MyApplicationController extends AbstractController implements Serializable {

    /**
     * Map of themes
     */
    private Map<String, String> themes;

//    private List<AdminConnecxionHistorique> listUtilisateurEncourConnecter = new ArrayList<>();

    /**
     * Creates a new instance of RolesController
     */
    public MyApplicationController() {
    }

    @Override
    protected void initController() {
        initThemes();
    }

//    public void addConnexion(AdminConnecxionHistorique connecxion) {
//        listUtilisateurEncourConnecter.add(connecxion);
//    }

//    public void removeConnexion(AdminConnecxionHistorique connecxion) {
//        listUtilisateurEncourConnecter.remove(connecxion);
//    }

    private void initThemes() {
        themes = new TreeMap<>();
        themes.put("Metro", "elit-metro");
        themes.put("Blue", "elit-blue");
    }

    public Map<String, String> getThemes() {
        return themes;
    }

//    public List<AdminConnecxionHistorique> getListUtilisateurEncourConnecter() {
//        return listUtilisateurEncourConnecter;
//    }
//
//    public void setListUtilisateurEncourConnecter(List<AdminConnecxionHistorique> listUtilisateurEncourConnecter) {
//        this.listUtilisateurEncourConnecter = listUtilisateurEncourConnecter;
//    }

}
