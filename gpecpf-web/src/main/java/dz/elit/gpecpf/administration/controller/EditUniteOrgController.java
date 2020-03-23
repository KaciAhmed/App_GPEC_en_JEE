

package dz.elit.gpecpf.administration.controller;

import dz.elit.gpecpf.administration.entity.AdminCommune;
import dz.elit.gpecpf.administration.entity.AdminUniteOrganisationnelle;
import dz.elit.gpecpf.administration.entity.AdminWilaya;
import dz.elit.gpecpf.administration.service.AdminUniteOrganisationnelleFacade;
import dz.elit.gpecpf.administration.service.AdminWilayaFacade;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author chekor.samir
 */
@ManagedBean
@ViewScoped
public class EditUniteOrgController extends AbstractController implements Serializable {

    @EJB
    private AdminUniteOrganisationnelleFacade societeFacade;
    @EJB
    private AdminWilayaFacade wilayaFacade;

    private AdminUniteOrganisationnelle societe;
    private AdminUniteOrganisationnelle societeSelected;
    private List<AdminUniteOrganisationnelle> listUniteOrgs;
    private List<AdminWilaya> listWilayas;
    private List<AdminCommune> listCommunes;
    private TreeMap<String, String> wilayasMap;
    private TreeMap<String, String> communesMap;
    private TreeMap<String, Map<String, String>> dataMap;

    private TreeMap<String, String> wilayasRegistreCommerceMap;
    private TreeMap<String, String> communesRegistreCommerceMap;
    private TreeMap<String, Map<String, String>> dataRegistreCommerceMap;

    public EditUniteOrgController() {
    }

    @Override
    protected void initController() {
        societe = new AdminUniteOrganisationnelle();
        societeSelected = new AdminUniteOrganisationnelle();
        listUniteOrgs = societeFacade.findAllOrderByTrie();
        listWilayas = wilayaFacade.findAllOrderByAttribut("nomWilaya");
        wilayasMap = new TreeMap<>();
        dataMap = new TreeMap<>();
        for (AdminWilaya wilaya : listWilayas) {
            wilayasMap.put(wilaya.getNomWilaya(), wilaya.getNomWilaya());
            listCommunes = wilaya.getListCummunes();
            communesMap = new TreeMap<>();
            for (AdminCommune commune : listCommunes) {
                communesMap.put(commune.getNomCommune(), commune.getNomCommune());
            }
            dataMap.put(wilaya.getNomWilaya(), communesMap);
        }

        wilayasRegistreCommerceMap = new TreeMap<>();
        dataRegistreCommerceMap = new TreeMap<>();
        for (AdminWilaya wilaya : listWilayas) {
            wilayasRegistreCommerceMap.put(wilaya.getNomWilaya(), wilaya.getNomWilaya());
            listCommunes = wilaya.getListCummunes();
            communesRegistreCommerceMap = new TreeMap<>();
            for (AdminCommune commune : listCommunes) {
                communesRegistreCommerceMap.put(commune.getNomCommune(), commune.getNomCommune());
            }
            dataRegistreCommerceMap.put(wilaya.getNomWilaya(), communesRegistreCommerceMap);
        }
        String id = MyUtil.getRequestParameter("id");
        if (id != null) {
            societe = societeFacade.find(Integer.parseInt(id));
            societeSelected = societe.getUniteParent() != null ? societe.getUniteParent() : new AdminUniteOrganisationnelle();
        }

    }

    public void onWilayaChange() {
        if (societe.getWilayaSecuriteSociale() != null && !societe.getWilayaSecuriteSociale().equals("")) {
            communesMap = (TreeMap<String, String>) dataMap.get(societe.getWilayaSecuriteSociale());
        } else {
            communesMap = new TreeMap<>();
        }
    }

    public void onWilayaRegistreCommerceChange() {
        if (societe.getWilayaRegistreCommerce() != null && !societe.getWilayaRegistreCommerce().equals("")) {
            communesRegistreCommerceMap = (TreeMap<String, String>) dataRegistreCommerceMap.get(societe.getWilayaRegistreCommerce());
        } else {
            communesRegistreCommerceMap = new TreeMap<>();
        }
    }

    public void edit() {
        try {
            societe.setTrie((societeSelected != null && societeSelected.getId() != null) ? societeSelected.getTrie() + societe.getCode() : societe.getCode());
            if (societeSelected != null && societeSelected.getId() != null) {
                societe.setUniteParent(societeSelected);
            }
            societe.setNiveau((societe.getUniteParent() == null || societe.getUniteParent().getId() == null) ? 1 : (societe.getUniteParent().getNiveau() + 1));
            societeFacade.edit(societe);
            if (societeSelected != null && societeSelected.getId() != null) {
                societeSelected.getAdminUniteOrganisationnelleList().add(societe);
                societeFacade.edit(societeSelected);
            }
            MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//"Unité organisationnelle modifié avec succè");
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

   

    public List<AdminWilaya> getListWilayas() {
        return listWilayas;
    }

    public void setListWilayas(List<AdminWilaya> listWilayas) {
        this.listWilayas = listWilayas;
    }

    public List<AdminCommune> getListCommunes() {
        return listCommunes;
    }

    public void setListCommunes(List<AdminCommune> listCommunes) {
        this.listCommunes = listCommunes;
    }

    public TreeMap<String, String> getWilayasMap() {
        return wilayasMap;
    }

    public void setWilayasMap(TreeMap<String, String> wilayasMap) {
        this.wilayasMap = wilayasMap;
    }

    public TreeMap<String, String> getCommunesMap() {
        return communesMap;
    }

    public void setCommunesMap(TreeMap<String, String> communesMap) {
        this.communesMap = communesMap;
    }

    public TreeMap<String, String> getWilayasRegistreCommerceMap() {
        return wilayasRegistreCommerceMap;
    }

    public void setWilayasRegistreCommerceMap(TreeMap<String, String> wilayasRegistreCommerceMap) {
        this.wilayasRegistreCommerceMap = wilayasRegistreCommerceMap;
    }

    public TreeMap<String, String> getCommunesRegistreCommerceMap() {
        return communesRegistreCommerceMap;
    }

    public void setCommunesRegistreCommerceMap(TreeMap<String, String> communesRegistreCommerceMap) {
        this.communesRegistreCommerceMap = communesRegistreCommerceMap;
    }

    
}
