/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.commun.util;

import dz.elit.gpecpf.administration.entity.AdminDroitVisibilite;
import dz.elit.gpecpf.administration.entity.AdminGroupe;
import dz.elit.gpecpf.administration.entity.AdminObjetVisibilite;
import dz.elit.gpecpf.administration.entity.AdminUniteOrganisationnelle;
import dz.elit.gpecpf.administration.entity.AdminUtilisateur;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author laidani.youcef
 */
public class UniteOrganisationnelleObjetVisibilite {

    private String code;
    private boolean selected;
    private AdminObjetVisibilite entite;
    private AdminUniteOrganisationnelle unite;

    public UniteOrganisationnelleObjetVisibilite(String code, boolean selected, AdminObjetVisibilite entite, AdminUniteOrganisationnelle unite) {
        this.code = code;
        this.selected = selected;
        this.unite = unite;
        this.entite = entite;
    }

    public Map<String, List<UniteOrganisationnelleObjetVisibilite>> construireListUOV(List<AdminUniteOrganisationnelle> listeUO,
            List<AdminObjetVisibilite> listeOV) {
        Map<String, List<UniteOrganisationnelleObjetVisibilite>> listePiece = new HashMap<>();

        for (AdminObjetVisibilite ov : listeOV) {
            List<UniteOrganisationnelleObjetVisibilite> liste = new ArrayList<>();
            for (AdminUniteOrganisationnelle uo : listeUO) {
                UniteOrganisationnelleObjetVisibilite piece = new UniteOrganisationnelleObjetVisibilite(ov.getId() + "_" + uo.getId(),
                        false, ov, uo);
                liste.add(piece);
            }
            listePiece.put(ov.getLibelle(), liste);
        }
        return listePiece;
    }

    public Map<String, List<UniteOrganisationnelleObjetVisibilite>> construireListUOVGroupe(List<AdminUniteOrganisationnelle> listeUO,
            List<AdminObjetVisibilite> listeOV, AdminGroupe groupe) {
        Map<String, List<UniteOrganisationnelleObjetVisibilite>> listePiece = new HashMap<>();

        for (AdminObjetVisibilite ov : listeOV) {
            List<UniteOrganisationnelleObjetVisibilite> liste = new ArrayList<>();
            for (AdminUniteOrganisationnelle uo : listeUO) {
                UniteOrganisationnelleObjetVisibilite piece = new UniteOrganisationnelleObjetVisibilite(ov.getId() + "_" + uo.getId(),
                        testDroitExist(uo, ov, groupe.getAdminDroitVisibiliteList()), ov, uo);
                liste.add(piece);
            }
            listePiece.put(ov.getLibelle(), liste);
        }
        return listePiece;
    }

    public Map<String, List<UniteOrganisationnelleObjetVisibilite>> construireListUOVUtilisateur(List<AdminUniteOrganisationnelle> listeUO,
            List<AdminObjetVisibilite> listeOV, AdminUtilisateur utilisateur) {
        Map<String, List<UniteOrganisationnelleObjetVisibilite>> listePiece = new HashMap<>();
        for (AdminObjetVisibilite ov : listeOV) {
            List<UniteOrganisationnelleObjetVisibilite> liste = new ArrayList<>();
            for (AdminUniteOrganisationnelle uo : listeUO) {
                UniteOrganisationnelleObjetVisibilite piece = new UniteOrganisationnelleObjetVisibilite(ov.getId() + "_" + uo.getId(),
                        testDroitExist(uo, ov, utilisateur.getAdminDroitVisibiliteList()), ov, uo);
                liste.add(piece);
            }
            listePiece.put(ov.getLibelle(), liste);
        }
        return listePiece;
    }

    private boolean testDroitExist(AdminUniteOrganisationnelle uo, AdminObjetVisibilite ov, List<AdminDroitVisibilite> listeDV) {
        boolean exist = false;
        for (AdminDroitVisibilite droit : listeDV) {
            if (droit.getIdObjetVisibilite().getId().equals(ov.getId()) && droit.getIdUniteOrganisationnelle().getId().equals(uo.getId())) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    public UniteOrganisationnelleObjetVisibilite() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public AdminObjetVisibilite getEntite() {
        return entite;
    }

    public void setEntite(AdminObjetVisibilite entite) {
        this.entite = entite;
    }

    public AdminUniteOrganisationnelle getUnite() {
        return unite;
    }

    public void setUnite(AdminUniteOrganisationnelle unite) {
        this.unite = unite;
    }

    @Override
    public String toString() {
        return "UniteOrganisationnelleObjetVisibilite{" + "code=" + code + ", selected=" + selected + ", entite=" + entite + ", unite=" + unite + '}';
    }

}
