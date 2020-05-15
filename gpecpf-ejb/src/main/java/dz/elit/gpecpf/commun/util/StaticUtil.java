/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.commun.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author ayadi
 */
public abstract class StaticUtil {
    /* shéma utilisé dans  la bdd*/
    public static final String ADMINISTRATION_SCHEMA = "sch_admin";
    public static final String COMPETENCE_SCHEMA = "sch_competence";
    public static final String POSTE_SCHEMA = "sch_poste";
    public static final String EVALUATION_SCHEMA = "sch_evaluation";
    /* nom de l'unité de persistance de type "nom de la bdd-ejbPU" PU pour 
    persistance unit*/
     public static final String UNIT_NAME = "gpecpf-ejbPU";
    
   // mot de passe par défeault
    public static String getDefaultEncryptPassword() {
        return getEncryptPassword("Sonelgaz.1");
    }

    /**
     * *************************************************************************
     * @param password
     * @return hashString
     * methode pour hasher un mot de passe
     *************************************************************************
     */
    public static String getEncryptPassword(String password) {

        byte[] uniqueKey = password.getBytes();
        byte[] hash = null;

        try {
            hash = MessageDigest.getInstance("SHA-256").digest(uniqueKey);
        } catch (NoSuchAlgorithmException e) {
            throw new Error("No SHA-256 support in this VM.");
        }

        StringBuilder hashString = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(hash[i]);
            if (hex.length() == 1) {
                hashString.append('0');
                hashString.append(hex.charAt(hex.length() - 1));
            } else {
                hashString.append(hex.substring(hex.length() - 2));
            }
        }
        return hashString.toString();
    }
}
