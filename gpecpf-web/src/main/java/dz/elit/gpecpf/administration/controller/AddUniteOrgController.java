
package dz.elit.gpecpf.administration.controller;

import dz.elit.gpecpf.administration.entity.AdminCommune;
import dz.elit.gpecpf.administration.entity.AdminUniteOrganisationnelle;
import dz.elit.gpecpf.administration.entity.AdminWilaya;
import dz.elit.gpecpf.administration.service.AdminUniteOrganisationnelleFacade;
import dz.elit.gpecpf.administration.service.AdminWilayaFacade;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author chekor.samir
 */
@ManagedBean
@ViewScoped
public class AddUniteOrgController extends AbstractController implements Serializable {

    @EJB
    private AdminUniteOrganisationnelleFacade societeFacade;
    @EJB
    private AdminWilayaFacade wilayaFacade;

    private AdminUniteOrganisationnelle societe;
    private AdminUniteOrganisationnelle societeSelected;
    
    private List<AdminUniteOrganisationnelle> listUniteOrgs;
    private List<AdminWilaya> listWilayas;
    private List<AdminCommune> listCommunes;
    private Map<String, String> wilayasMap;
    private Map<String, String> communesMap;
    private Map<String, Map<String, String>> dataMap;
    
    private Map<String, String> wilayasRegistreCommerceMap;    
    private Map<String, String> communesRegistreCommerceMap;    
    private Map<String, Map<String, String>> dataRegistreCommerceMap;

    public AddUniteOrgController() {
    }

    @Override
    protected void initController() {
        societe = new AdminUniteOrganisationnelle();
        societeSelected = new AdminUniteOrganisationnelle();
        listUniteOrgs = societeFacade.findAllOrderByTrie();
        listWilayas = wilayaFacade.findAllOrderByAttribut("nomWilaya");
        wilayasMap = new HashMap<>();
        dataMap = new HashMap<>();
         for (AdminWilaya wilaya : listWilayas) {
            wilayasMap.put(wilaya.getNomWilaya(), wilaya.getNomWilaya());
            listCommunes = wilaya.getListCummunes();
            communesMap = new HashMap<>();
            for (AdminCommune commune : listCommunes) {
                communesMap.put(commune.getNomCommune(), commune.getNomCommune());
            }
            dataMap.put(wilaya.getNomWilaya(), communesMap);
        }
         
        wilayasRegistreCommerceMap = new HashMap<>();        
        dataRegistreCommerceMap = new HashMap<>();
         for (AdminWilaya wilaya : listWilayas) {
            wilayasRegistreCommerceMap.put(wilaya.getNomWilaya(), wilaya.getNomWilaya());
            listCommunes = wilaya.getListCummunes();
            communesRegistreCommerceMap = new HashMap<>();
            for (AdminCommune commune : listCommunes) {
                communesRegistreCommerceMap.put(commune.getNomCommune(), commune.getNomCommune());
            }
            dataRegistreCommerceMap.put(wilaya.getNomWilaya(), communesRegistreCommerceMap);
        }
    }

    public void onWilayaChange() {
        if (societe.getWilayaSecuriteSociale() != null && !societe.getWilayaSecuriteSociale().equals("")) {
            communesMap = dataMap.get(societe.getWilayaSecuriteSociale());
        } else {
            communesMap = new HashMap<>();
        }
    }
    public void onWilayaRegistreCommerceChange() {
        if (societe.getWilayaRegistreCommerce() != null && !societe.getWilayaRegistreCommerce().equals("")) {
            communesRegistreCommerceMap = dataRegistreCommerceMap.get(societe.getWilayaRegistreCommerce());
        } else {
            communesRegistreCommerceMap = new HashMap<>();
        }
    }

    public void create() {
        try {
            societe.setTrie((societeSelected != null && societeSelected.getId() != null) ? societeSelected.getTrie() + societe.getCode() : societe.getCode());
            if (societeSelected != null && societeSelected.getId() != null) {
                societe.setUniteParent(societeSelected);
            }
            societe.setNiveau((societe.getUniteParent()==null || societe.getUniteParent().getId()==null) ? 1 : (societe.getUniteParent().getNiveau()+1));       
            societeFacade.create(societe);
            if (societeSelected != null && societeSelected.getId() != null) {
                societeSelected.getAdminUniteOrganisationnelleList().add(societe);
                societeFacade.edit(societeSelected);
            }
            MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//"Unité organisationnelle crée avec succè");
        } catch (Exception ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
        }
    }
    // Getter and setter

    public AdminUniteOrganisationnelle getSociete() {
        return societe;
    }

    public void setSociete(AdminUniteOrganisationnelle societe) {
        this.societe = societe;
    }

    public List<AdminUniteOrganisationnelle> getListUniteOrgs() {
        return listUniteOrgs;
    }

    public void setListUniteOrgs(List<AdminUniteOrganisationnelle> listUniteOrgs) {
        this.listUniteOrgs = listUniteOrgs;
    }

    public AdminUniteOrganisationnelle getSocieteSelected() {
        return societeSelected;
    }

    public void setSocieteSelected(AdminUniteOrganisationnelle societeSelected) {
        this.societeSelected = societeSelected;
    }

    public Map<String, String> getWilayasMap() {
        return wilayasMap;
    }

    public void setWilayasMap(Map<String, String> wilayasMap) {
        this.wilayasMap = wilayasMap;
    }

    public Map<String, String> getCommunesMap() {
        return communesMap;
    }

    public void setCommunesMap(Map<String, String> communesMap) {
        this.communesMap = communesMap;
    }

    public Map<String, String> getWilayasRegistreCommerceMap() {
        return wilayasRegistreCommerceMap;
    }

    public void setWilayasRegistreCommerceMap(Map<String, String> wilayasRegistreCommerceMap) {
        this.wilayasRegistreCommerceMap = wilayasRegistreCommerceMap;
    }

    public Map<String, String> getCommunesRegistreCommerceMap() {
        return communesRegistreCommerceMap;
    }

    public void setCommunesRegistreCommerceMap(Map<String, String> communesRegistreCommerceMap) {
        this.communesRegistreCommerceMap = communesRegistreCommerceMap;
    }

}
