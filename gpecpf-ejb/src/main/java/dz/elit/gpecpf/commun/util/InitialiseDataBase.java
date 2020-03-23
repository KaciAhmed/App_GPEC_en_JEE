/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.commun.util;

import dz.elit.gpecpf.administration.entity.AdminModule;
import dz.elit.gpecpf.administration.entity.AdminPrivilege;
import dz.elit.gpecpf.administration.entity.AdminProfil;
import dz.elit.gpecpf.administration.entity.AdminUtilisateur;
import java.util.Arrays;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ayadi
 */
@Singleton
@Startup
public class InitialiseDataBase {

    @PersistenceContext(unitName = StaticUtil.UNIT_NAME)
    private EntityManager em;

    @PostConstruct
    public void initDataBase() {
        //em.createNativeQuery("CREATE DATABASE bd_gpecpf").executeUpdate();
        //em.flush();
        //em.createNativeQuery("CREATE SCHEMA sch_admin ").executeUpdate();
        //em.flush();
        if (!isBaseVide()) {
            return;
        }
        // *** creation de VIEW pour la gestion des droit d'acces ************************
        em.createNativeQuery(" CREATE OR REPLACE VIEW " + StaticUtil.ADMINISTRATION_SCHEMA + ".user_groupe_security AS "
                + " SELECT pr.code AS user_privilege, u.login AS login "
                + "   FROM " + StaticUtil.ADMINISTRATION_SCHEMA + ".admin_utilisateur u "
                + "     JOIN " + StaticUtil.ADMINISTRATION_SCHEMA + ".admin_utilisateur_profil up ON up.id_utilisateur = u.id "
                + "     JOIN " + StaticUtil.ADMINISTRATION_SCHEMA + ".admin_profil p ON up.id_profil = p.id "
                + "     JOIN " + StaticUtil.ADMINISTRATION_SCHEMA + ".admin_profil_privilege ppr ON ppr.id_profil = p.id "
                + "     JOIN " + StaticUtil.ADMINISTRATION_SCHEMA + ".admin_privilege pr ON ppr.id_privilege = pr.id "
                + "  WHERE u.active = true AND (u.date_expiration IS NULL OR u.date_expiration > date(now())) AND u.date_activation <= date(now()) "
                + " UNION "
                + " SELECT 'INACTIF'::character varying(20) AS user_privilege, u.login AS login "
                + "   FROM " + StaticUtil.ADMINISTRATION_SCHEMA + ".admin_utilisateur u  WHERE u.active <> true "
                + " UNION "
                + " SELECT 'EXPIRE'::character varying(20) AS user_privilege, u.login AS login "
                + "   FROM " + StaticUtil.ADMINISTRATION_SCHEMA + ".admin_utilisateur u  WHERE u.active = true AND u.date_expiration < date(now()) "
                + " UNION "
                + " SELECT 'PAS_ENCORS_ACTIF'::character varying(20) AS user_privilege, u.login AS login "
                + "       FROM " + StaticUtil.ADMINISTRATION_SCHEMA + ".admin_utilisateur u "
                + "  WHERE u.active = true AND u.date_activation > date(now()) ;  "
        ).executeUpdate();

        //*************** Initialisation des modules **********************************
        // Module Administration
        AdminModule adminModule = new AdminModule("ADMIN", "Administration", "/pages/administration/indexAdmin.xhtml", 1);
        em.persist(adminModule);
        em.flush();

        //***************** Initialisation des privilèges *********************
        //Admin_001	Gestion des sessions
        //Admin_002	Gestion des utilisateurs
        //Admin_003	Gestion des profils
        //Admin_004	Gestion des privilèges
        //Admin_005	Gestion de structures organisationnelles
        //Admin_006	Gestion des modules
        // gestion utilisateur
        em.persist(new AdminPrivilege("ADMIN_002_001", "Céer un utilisateur ", adminModule));
        em.persist(new AdminPrivilege("ADMIN_002_002", "Modifier un utilisateur ", adminModule));
        em.persist(new AdminPrivilege("ADMIN_002_003", "Supprimer un utilisateur ", adminModule));
        em.persist(new AdminPrivilege("ADMIN_002_004", "Rechercher un utilisateur ", adminModule));
        em.persist(new AdminPrivilege("ADMIN_002_005", "Consulter un utilisateur ", adminModule));
        em.persist(new AdminPrivilege("ADMIN_002_006", "Lister un utilisateur ", adminModule));
        em.persist(new AdminPrivilege("ADMIN_002_007", "Réinitialiser le mot de passe d'un utilisateur ", adminModule));
        em.persist(new AdminPrivilege("ADMIN_002_008", "Activer et Désactiver un utilisateur ", adminModule));

        // gestion profil  
        em.persist(new AdminPrivilege("ADMIN_003_001", "Céer un profil ", adminModule));
        em.persist(new AdminPrivilege("ADMIN_003_002", "Modifier un profil ", adminModule));
        em.persist(new AdminPrivilege("ADMIN_003_003", "Supprimer un profil ", adminModule));
        em.persist(new AdminPrivilege("ADMIN_003_004", "Rechercher un profil ", adminModule));
        em.persist(new AdminPrivilege("ADMIN_003_005", "Consulter un profil ", adminModule));
        em.persist(new AdminPrivilege("ADMIN_003_006", "Lister un profil ", adminModule));

        //gestion privilège
        em.persist(new AdminPrivilege("ADMIN_004_003", "Modifier un privilège ", adminModule));
        em.persist(new AdminPrivilege("ADMIN_004_005", "Consulter un privilège ", adminModule));
        em.persist(new AdminPrivilege("ADMIN_004_006", "Lister un privilège ", adminModule));

        // gestion unité organisationnelle  
        em.persist(new AdminPrivilege("ADMIN_005_001", "Céer une unité organisationnelle  ", adminModule));
        em.persist(new AdminPrivilege("ADMIN_005_002", "Modifier une unité organisationnelle ", adminModule));
        em.persist(new AdminPrivilege("ADMIN_005_003", "Supprimer une unité organisationnelle ", adminModule));
        em.persist(new AdminPrivilege("ADMIN_005_004", "Consulter une unité organisationnelle ", adminModule));
        em.persist(new AdminPrivilege("ADMIN_005_005", "Lister une unité organisationnelle ", adminModule));

        // gestion des modules  
        em.persist(new AdminPrivilege("ADMIN_006_001", "Lister les modules  ", adminModule));
        em.persist(new AdminPrivilege("ADMIN_006_002", "Ordonner les modules ", adminModule));

        //***************** Fin Initialisation Module Administration *************************************************************************
        // Initialisation Autres modules
        // Utiliser le meme principe
        //***** Creation d'un profil Administrateur pour l'utilisateur admin **************************************
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(AdminPrivilege.class));
        AdminProfil adminProfil = new AdminProfil("Administrateur", "Administrateur", em.createQuery(cq).getResultList());
        em.persist(adminProfil);
        em.flush();
        AdminUtilisateur utilisateur = new AdminUtilisateur("Nom", "Prenom", "admin", StaticUtil.getDefaultEncryptPassword(), new Date(), Arrays.asList(adminProfil));
        utilisateur.setActive(true);
        em.persist(utilisateur);

        adminProfil.getListAdminUtilisateurs().add(utilisateur);
        em.persist(adminProfil);
    }

    public boolean isBaseVide() {
        try {
            Query q = em.createQuery("SELECT COUNT(a) FROM AdminModule a");
            Long l = (Long) q.getSingleResult();
            return l == 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

//    @Schedule(second = "0", minute = "0", hour = "1", dayOfMonth = "*", month = "*", year = "*")
//    public void startTrace() {
//        try {
//
//            List<AdminHistorique> adminHistoriques = em.createQuery("select admhist from AdminHistorique admhist").getResultList();
//            if (adminHistoriques != null) {
//                for (AdminHistorique adminHistorique : adminHistoriques) {
//                    Gson h = new Gson();
//                    String s = h.toJson(adminHistorique);
//                    Client.WsAdminHistoriqueFacade.createHistoriqueByString(s);
//                    em.remove(adminHistorique);
//
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("---------no Service---------");
//        }
//    }
}
