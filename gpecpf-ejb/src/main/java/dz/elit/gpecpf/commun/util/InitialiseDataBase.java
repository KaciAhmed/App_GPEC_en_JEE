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
import dz.elit.gpecpf.administration.service.AdminPrivilegeFacade;

import java.util.Arrays;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
	@EJB
    private AdminPrivilegeFacade adminPrivilegeFacade;

    @PostConstruct
    public void initDataBase() {
        // em.createNativeQuery("CREATE DATABASE bd_gpecpf").executeUpdate();
        // em.flush();
        // em.createNativeQuery("CREATE SCHEMA sch_admin ").executeUpdate();
        // em.flush();
        if (!isBaseVide()) {
            return;
        }
        // *** creation de VIEW pour la gestion des droit d'acces
        // ************************
        em.createNativeQuery(" CREATE OR REPLACE VIEW " + StaticUtil.ADMINISTRATION_SCHEMA + ".user_groupe_security AS "
                + " SELECT pr.code AS user_privilege, u.login AS login " + "   FROM " + StaticUtil.ADMINISTRATION_SCHEMA
                + ".admin_utilisateur u " + "     JOIN " + StaticUtil.ADMINISTRATION_SCHEMA
                + ".admin_utilisateur_profil up ON up.id_utilisateur = u.id " + "     JOIN "
                + StaticUtil.ADMINISTRATION_SCHEMA + ".admin_profil p ON up.id_profil = p.id " + "     JOIN "
                + StaticUtil.ADMINISTRATION_SCHEMA + ".admin_profil_privilege ppr ON ppr.id_profil = p.id "
                + "     JOIN " + StaticUtil.ADMINISTRATION_SCHEMA + ".admin_privilege pr ON ppr.id_privilege = pr.id "
                + "  WHERE u.active = true AND (u.date_expiration IS NULL OR u.date_expiration > date(now())) AND u.date_activation <= date(now()) "
                + " UNION " + " SELECT 'INACTIF'::character varying(20) AS user_privilege, u.login AS login "
                + "   FROM " + StaticUtil.ADMINISTRATION_SCHEMA + ".admin_utilisateur u  WHERE u.active <> true "
                + " UNION " + " SELECT 'EXPIRE'::character varying(20) AS user_privilege, u.login AS login "
                + "   FROM " + StaticUtil.ADMINISTRATION_SCHEMA
                + ".admin_utilisateur u  WHERE u.active = true AND u.date_expiration < date(now()) " + " UNION "
                + " SELECT 'PAS_ENCORS_ACTIF'::character varying(20) AS user_privilege, u.login AS login "
                + "       FROM " + StaticUtil.ADMINISTRATION_SCHEMA + ".admin_utilisateur u "
                + "  WHERE u.active = true AND u.date_activation > date(now()) ;  ").executeUpdate();

        // *************** Initialisation des modules **********************************
        // Module Administration
        AdminModule adminModule = new AdminModule("ADMIN", "Administration", "/pages/administration/indexAdmin.xhtml",
                1);
        em.persist(adminModule);
        em.flush();

        // ***************** Initialisation des privilèges *********************
        // Admin_001 Gestion des sessions
        // Admin_002 Gestion des utilisateurs
        // Admin_003 Gestion des profils
        // Admin_004 Gestion des privilèges
        // Admin_005 Gestion de structures organisationnelles
        // Admin_006 Gestion des modules
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

        // gestion privilège
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
        em.flush();

        // ***************** Fin Initialisation Module Administration
        // *************************************************************************
        // ***** Creation d'un profil Administrateur pour l'utilisateur admin
        // **************************************
        AdminProfil adminProfil = new AdminProfil("Administrateur", "Administrateur",
                adminPrivilegeFacade.getListPrivilegeByModule(adminModule.getId()));
        em.persist(adminProfil);
        em.flush();
        AdminUtilisateur utilisateur = new AdminUtilisateur("Nom", "Prenom", "admin",
                StaticUtil.getDefaultEncryptPassword(), new Date(), Arrays.asList(adminProfil));
        utilisateur.setActive(true);
        em.persist(utilisateur);

        adminProfil.getListAdminUtilisateurs().add(utilisateur);
        em.persist(adminProfil);
        // Initialisation des modules du referentiel
        adminModule = new AdminModule("REFER", "Gestionnaire du Referentiel",
                "/pages/referentiel/indexReferentiel.xhtml", 2);
        em.persist(adminModule);
        em.flush();
        // Type de Compétence
        em.persist(new AdminPrivilege("REFER_001_005", "Consulter un type de compétence ", adminModule));
        em.persist(new AdminPrivilege("REFER_001_006", "Lister un type de compétence ", adminModule));
        // Domaine de compétence
        em.persist(new AdminPrivilege("REFER_002_001", "Créer un domaine de compétence ", adminModule));
        em.persist(new AdminPrivilege("REFER_002_002", "Modifier un domaine de compétence ", adminModule));
        em.persist(new AdminPrivilege("REFER_002_003", "Supprimer un domaine de compétence ", adminModule));
        em.persist(new AdminPrivilege("REFER_002_004", "Rechercher un domaine de compétence ", adminModule));
        em.persist(new AdminPrivilege("REFER_002_005", "Consulter un domaine de compétence ", adminModule));
        em.persist(new AdminPrivilege("REFER_002_006", "Lister un domaine de compétence ", adminModule));
        // Competénce
        em.persist(new AdminPrivilege("REFER_003_001", "Créer une compétence ", adminModule));
        em.persist(new AdminPrivilege("REFER_003_002", "Modifier une compétence ", adminModule));
        em.persist(new AdminPrivilege("REFER_003_003", "Supprimer une compétence ", adminModule));
        em.persist(new AdminPrivilege("REFER_003_004", "Rechercher une compétence ", adminModule));
        em.persist(new AdminPrivilege("REFER_003_005", "Consulter une compétence ", adminModule));
        em.persist(new AdminPrivilege("REFER_003_006", "Lister une compétence ", adminModule));
        // Tache
        em.persist(new AdminPrivilege("REFER_004_001", "Créer une tâche ", adminModule));
        em.persist(new AdminPrivilege("REFER_004_002", "Modifier une tâche ", adminModule));
        em.persist(new AdminPrivilege("REFER_004_003", "Supprimer une tâche ", adminModule));
        em.persist(new AdminPrivilege("REFER_004_004", "Rechercher une tâche ", adminModule));
        em.persist(new AdminPrivilege("REFER_004_005", "Consulter une tâche ", adminModule));
        em.persist(new AdminPrivilege("REFER_004_006", "Lister une tâche ", adminModule));
        // Activité
        em.persist(new AdminPrivilege("REFER_005_001", "Créer une activité ", adminModule));
        em.persist(new AdminPrivilege("REFER_005_002", "Modifier une activité ", adminModule));
        em.persist(new AdminPrivilege("REFER_005_003", "Supprimer une activité ", adminModule));
        em.persist(new AdminPrivilege("REFER_005_004", "Rechercher une activité ", adminModule));
        em.persist(new AdminPrivilege("REFER_005_005", "Consulter une activité ", adminModule));
        em.persist(new AdminPrivilege("REFER_005_006", "Lister une activité ", adminModule));
        // Mission
        em.persist(new AdminPrivilege("REFER_006_001", "Créer une mission ", adminModule));
        em.persist(new AdminPrivilege("REFER_006_002", "Modifier une mission ", adminModule));
        em.persist(new AdminPrivilege("REFER_006_003", "Supprimer une mission ", adminModule));
        em.persist(new AdminPrivilege("REFER_006_004", "Rechercher une mission ", adminModule));
        em.persist(new AdminPrivilege("REFER_006_005", "Consulter une mission ", adminModule));
        em.persist(new AdminPrivilege("REFER_006_006", "Lister une mission ", adminModule));
        // Emploi
        em.persist(new AdminPrivilege("REFER_007_001", "Créer un emploi ", adminModule));
        em.persist(new AdminPrivilege("REFER_007_002", "Modifier un emploi ", adminModule));
        em.persist(new AdminPrivilege("REFER_007_003", "Supprimer un emploi ", adminModule));
        em.persist(new AdminPrivilege("REFER_007_004", "Rechercher un emploi ", adminModule));
        em.persist(new AdminPrivilege("REFER_007_005", "Consulter un emploi ", adminModule));
        em.persist(new AdminPrivilege("REFER_007_006", "Lister un emploi ", adminModule));
        // Poste
        em.persist(new AdminPrivilege("REFER_008_001", "Créer un poste ", adminModule));
        em.persist(new AdminPrivilege("REFER_008_002", "Modifier un poste ", adminModule));
        em.persist(new AdminPrivilege("REFER_008_003", "Supprimer un poste ", adminModule));
        em.persist(new AdminPrivilege("REFER_008_004", "Rechercher un poste ", adminModule));
        em.persist(new AdminPrivilege("REFER_008_005", "Consulter un poste ", adminModule));
        em.persist(new AdminPrivilege("REFER_008_006", "Lister un poste ", adminModule));
		// Comportement
        em.persist(new AdminPrivilege("REFER_009_001", "Créer un comportement de compétence ", adminModule));
        em.persist(new AdminPrivilege("REFER_009_002", "Modifier un comportement de compétence ", adminModule));
        em.persist(new AdminPrivilege("REFER_009_003", "Supprimer un comportement de compétence ", adminModule));
        em.persist(new AdminPrivilege("REFER_009_004", "Rechercher un comportement de compétence ", adminModule));
        em.persist(new AdminPrivilege("REFER_009_005", "Consulter un comportement de compétence ", adminModule));
        em.persist(new AdminPrivilege("REFER_009_006", "Lister un comportement de compétence ", adminModule));
		// Condition
        em.persist(new AdminPrivilege("REFER_010_001", "Créer une condition ", adminModule));
        em.persist(new AdminPrivilege("REFER_010_002", "Modifier une condition ", adminModule));
        em.persist(new AdminPrivilege("REFER_010_003", "Supprimer une condition ", adminModule));
        em.persist(new AdminPrivilege("REFER_010_004", "Rechercher une condition ", adminModule));
        em.persist(new AdminPrivilege("REFER_010_005", "Consulter une condition ", adminModule));
        em.persist(new AdminPrivilege("REFER_010_006", "Lister une condition ", adminModule));
		// Moyen
        em.persist(new AdminPrivilege("REFER_011_001", "Créer un moyen ", adminModule));
        em.persist(new AdminPrivilege("REFER_011_002", "Modifier un moyen ", adminModule));
        em.persist(new AdminPrivilege("REFER_011_003", "Supprimer un moyen ", adminModule));
        em.persist(new AdminPrivilege("REFER_011_004", "Rechercher un moyen ", adminModule));
        em.persist(new AdminPrivilege("REFER_011_005", "Consulter un moyen ", adminModule));
        em.persist(new AdminPrivilege("REFER_011_006", "Lister un moyen ", adminModule));
		// Formation
        em.persist(new AdminPrivilege("REFER_012_001", "Créer une formation ", adminModule));
        em.persist(new AdminPrivilege("REFER_012_002", "Modifier une formation ", adminModule));
        em.persist(new AdminPrivilege("REFER_012_003", "Supprimer une formation ", adminModule));
        em.persist(new AdminPrivilege("REFER_012_004", "Rechercher une formation ", adminModule));
        em.persist(new AdminPrivilege("REFER_012_005", "Consulter une formation ", adminModule));
        em.persist(new AdminPrivilege("REFER_012_006", "Lister une formation ", adminModule));
        em.flush();
        // Fin Module du referentiel
        adminProfil = new AdminProfil("Referentiel", "Referentiel",
                adminPrivilegeFacade.getListPrivilegeByModule(adminModule.getId()));
        em.persist(adminProfil);
        em.flush();
        // Gestionnaire des employés
        adminModule = new AdminModule("GESEMP", "Gestionnaire Employés", "/pages/gesemp/indexGesemp.xhtml", 3);
        em.persist(adminModule);
        em.flush();
        em.persist(new AdminPrivilege("GESEMP_001_001", "Céer un employé ", adminModule));
        em.persist(new AdminPrivilege("GESEMP_001_002", "Modifier un employé ", adminModule));
        em.persist(new AdminPrivilege("GESEMP_001_003", "Supprimer un employé ", adminModule));
        em.persist(new AdminPrivilege("GESEMP_001_004", "Rechercher un employé ", adminModule));
        em.persist(new AdminPrivilege("GESEMP_001_005", "Consulter un employé ", adminModule));
        em.persist(new AdminPrivilege("GESEMP_001_006", "Lister un employé ", adminModule));
        em.flush();
        // Fin du module gestionnaire des employés
        adminProfil = new AdminProfil("GestionnaireEmployés", "GestionnaireEmployés",
                adminPrivilegeFacade.getListPrivilegeByModule(adminModule.getId()));
        em.persist(adminProfil);
        em.flush();
        // DRH
        adminModule = new AdminModule("DRH", "Direction Ressources Humaines", "/pages/drh/indexDrh.xhtml", 4);
        em.persist(adminModule);
        em.flush();
        // Employé
        em.persist(new AdminPrivilege("DRH_001_004", "Rechercher un employé ", adminModule));
        em.persist(new AdminPrivilege("DRH_001_005", "Consulter un employé ", adminModule));
        em.persist(new AdminPrivilege("DRH_001_006", "Lister un employé ", adminModule));
        // Poste
        em.persist(new AdminPrivilege("DRH_002_004", "Rechercher un poste ", adminModule));
        em.persist(new AdminPrivilege("DRH_002_005", "Consulter un poste ", adminModule));
        em.persist(new AdminPrivilege("DRH_002_006", "Lister un poste ", adminModule));
        // Compagne
        em.persist(new AdminPrivilege("DRH_003_001", "Créer une compagne ", adminModule));
        em.persist(new AdminPrivilege("DRH_003_002", "Modifier une compagne ", adminModule));
        em.persist(new AdminPrivilege("DRH_003_003", "Supprimer une compagne ", adminModule));
        em.persist(new AdminPrivilege("DRH_003_004", "Rechercher une compagne ", adminModule));
        em.persist(new AdminPrivilege("DRH_003_005", "Consulter une compagne ", adminModule));
        em.persist(new AdminPrivilege("DRH_003_006", "Lister une compagne ", adminModule));
        // Reporting
        em.persist(new AdminPrivilege("DRH_004_001", "Créer un reporting ", adminModule));
        em.persist(new AdminPrivilege("DRH_004_002", "Modifier un reporting ", adminModule));
        em.persist(new AdminPrivilege("DRH_004_003", "Supprimer un reporting ", adminModule));
        em.persist(new AdminPrivilege("DRH_004_004", "Rechercher un reporting ", adminModule));
        em.persist(new AdminPrivilege("DRH_004_005", "Consulter un reporting ", adminModule));
        em.persist(new AdminPrivilege("DRH_004_006", "Lister un reporting ", adminModule));
        em.flush();
        // Fin du module DRH
        adminProfil = new AdminProfil("DRH", "DRH", adminPrivilegeFacade.getListPrivilegeByModule(adminModule.getId()));
        em.persist(adminProfil);
        em.flush();
        // Employé
        adminModule = new AdminModule("EMP", "Employé", "/pages/emp/indexEmp.xhtml", 5);
        em.persist(adminModule);
        em.flush();
        // Poste
        em.persist(new AdminPrivilege("EMP_001_005", "Consulter un poste ", adminModule));
        // Employé
        em.persist(new AdminPrivilege("EMP_002_005", "Consulter un employé ", adminModule));
        // Evaluation
        em.persist(new AdminPrivilege("EMP_003_001", "Créer une évaluation ", adminModule));
        em.persist(new AdminPrivilege("EMP_003_002", "Modifier une évaluation ", adminModule));
        em.persist(new AdminPrivilege("EMP_003_003", "Supprimer une évaluation ", adminModule));
        em.persist(new AdminPrivilege("EMP_003_004", "Rechercher une évaluation ", adminModule));
        em.persist(new AdminPrivilege("EMP_003_005", "Consulter une évaluation ", adminModule));
        em.persist(new AdminPrivilege("EMP_003_006", "Lister une évaluation ", adminModule));
        em.flush();
        // Fin module Employé
        adminProfil = new AdminProfil("Employé", "Employé",
                adminPrivilegeFacade.getListPrivilegeByModule(adminModule.getId()));
        em.persist(adminProfil);
        em.flush();

        // Initialisation Autres modules
        // Utiliser le meme principe

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

    // @Schedule(second = "0", minute = "0", hour = "1", dayOfMonth = "*", month =
    // "*", year = "*")
    // public void startTrace() {
    // try {
    //
    // List<AdminHistorique> adminHistoriques = em.createQuery("select admhist from
    // AdminHistorique admhist").getResultList();
    // if (adminHistoriques != null) {
    // for (AdminHistorique adminHistorique : adminHistoriques) {
    // Gson h = new Gson();
    // String s = h.toJson(adminHistorique);
    // Client.WsAdminHistoriqueFacade.createHistoriqueByString(s);
    // em.remove(adminHistorique);
    //
    // }
    // }
    // } catch (Exception e) {
    // System.out.println("---------no Service---------");
    // }
    // }
}
