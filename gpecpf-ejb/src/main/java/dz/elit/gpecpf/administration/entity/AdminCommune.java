/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.administration.entity;

import dz.elit.gpecpf.commun.util.StaticUtil;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author chekor.samir
 */
@Entity
@Table(name = "admin_commune" ,schema = StaticUtil.ADMINISTRATION_SCHEMA)
public class AdminCommune implements Serializable  {

    private static final long serialVersionUID = 1L;  
    @Id
    @Column(name = "code_commune",nullable = false)
    private String codeCommune;
    @Column(name = "nom_commune",nullable = false)
    private String nomCommune;
    @Column(name = "code_postal_commune")
    private String codePostalCommune;
    @JoinColumn(name = "code_wil",referencedColumnName = "code_wilaya")
    @ManyToOne
    private AdminWilaya codeWil;

    public AdminCommune() {
    }

    public AdminCommune(String codeCommune, String nomCommune, String codePostalCommune, AdminWilaya codeWil) {
        this.codeCommune = codeCommune;
        this.nomCommune = nomCommune;
        this.codePostalCommune = codePostalCommune;
        this.codeWil = codeWil;
    } 

    public String getCodeCommune() {
        return codeCommune;
    }

    public void setCodeCommune(String codeCommune) {
        this.codeCommune = codeCommune;
    }

    public String getNomCommune() {
        return nomCommune;
    }

    public void setNomCommune(String nomCommune) {
        this.nomCommune = nomCommune;
    }

    public String getCodePostalCommune() {
        return codePostalCommune;
    }

    public void setCodePostalCommune(String codePostalCommune) {
        this.codePostalCommune = codePostalCommune;
    }

    public AdminWilaya getCodeWil() {
        return codeWil;
    }

    public void setCodeWil(AdminWilaya codeWil) {
        this.codeWil = codeWil;
    }

    
    
    /* methode de hachage pour hacher le code commune*/

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codeCommune != null ? codeCommune.hashCode() : 0);
        return hash;
    }

    /* méthode qui vérifie que le code commune de l'objet actuel est 
        le meme que celui passé en parametre */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the code_wilaya fields are not set
        if (!(object instanceof AdminCommune)) {
            return false;
        }
        AdminCommune other = (AdminCommune) object;
        if ((this.codeCommune == null && other.codeCommune != null) || (this.codeCommune != null && !this.codeCommune.equals(other.codeCommune))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dz.elit.gpecpf.administration.entity.AdminWilaya[ id=" + codeCommune + " ]";
    }

}
