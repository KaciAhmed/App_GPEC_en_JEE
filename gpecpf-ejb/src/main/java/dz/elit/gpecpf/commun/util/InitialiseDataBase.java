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
import otherEntity.Commune;
import otherEntity.Typecompetence;
import otherEntity.Wilaya;

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
//-------------------------------------------------------------------------------------------------------------------
 // Initialiser les types de compétence 	
        em.persist(new Typecompetence("TYPE_COMP_001", "SAVOIR", "Les connaissances théoriques acquises pendant le parcours scolaire et lors des différentes expériences professionnelle"));
        em.persist(new Typecompetence("TYPE_COMP_002", "SAVOIR-FAIRE", "ensemble de connaissances pratiques, la maîtrise d’un poste, d’un marché ou d’un produit spécifique"));
        em.persist(new Typecompetence("TYPE_COMP_003", "SAVOIR-ÊTRE PROFESSIONNEL", "représente un ensemble de manières d’agir et de capacités relationnelles utiles pour interagir dans un contexte professionnel"));
        em.flush();
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Initialisation des wilaya
       /* em.persist(new Wilaya("1", "Adrar"));
        em.persist(new Wilaya("2", "Chlef"));
        em.persist(new Wilaya("3", "Oum El Bouaghi"));
        em.persist(new Wilaya("4", "Laghouat"));
        em.persist(new Wilaya("5", "Batna"));
        em.persist(new Wilaya("6", "Béjaïa"));
        em.persist(new Wilaya("7", "Biskra"));
        em.persist(new Wilaya("8", "Béchar"));
        em.persist(new Wilaya("9", "Blida"));
        em.persist(new Wilaya("10", "Bouira"));
        em.persist(new Wilaya("11", "Tamanrasset"));
        em.persist(new Wilaya("12", "Tébessa"));
        em.persist(new Wilaya("13", "Tlemcen"));
        em.persist(new Wilaya("14", "Tiaret"));
        em.persist(new Wilaya("15", "Tizi Ouzou"));
        em.persist(new Wilaya("16", "Alger"));
        em.persist(new Wilaya("17", "Djelfa"));
        em.persist(new Wilaya("18", "Jijel"));
        em.persist(new Wilaya("19", "Sétif"));
        em.persist(new Wilaya("20", "Saïda"));
        em.persist(new Wilaya("21", "Skikda"));
        em.persist(new Wilaya("22", "Sidi Bel Abbès"));
        em.persist(new Wilaya("23", "Annaba"));
        em.persist(new Wilaya("24", "Guelma"));
        em.persist(new Wilaya("25", "Constantine"));
        em.persist(new Wilaya("26", "Médéa"));
        em.persist(new Wilaya("27", "Mostaganem"));
        em.persist(new Wilaya("28", "M''Sila"));
        em.persist(new Wilaya("29", "Mascara"));
        em.persist(new Wilaya("30", "Ouargla"));
        em.persist(new Wilaya("31", "Oran"));
        em.persist(new Wilaya("32", "El Bayadh"));
        em.persist(new Wilaya("33", "Illizi"));
        em.persist(new Wilaya("34", "Bordj Bou Arreridj"));
        em.persist(new Wilaya("35", "Boumerdès"));
        em.persist(new Wilaya("36", "El Tarf"));
        em.persist(new Wilaya("37", "Tindouf"));
        em.persist(new Wilaya("38", "Tissemsilt"));
        em.persist(new Wilaya("39", "El Oued"));
        em.persist(new Wilaya("40", "Khenchela"));
        em.persist(new Wilaya("41", "Souk Ahras"));
        em.persist(new Wilaya("42", "Tipaza"));
        em.persist(new Wilaya("43", "Mila"));
        em.persist(new Wilaya("44", "Aïn Defla"));
        em.persist(new Wilaya("45", "Naâma"));
        em.persist(new Wilaya("46", "Aïn Témouchent"));
        em.persist(new Wilaya("47", "Ghardaïa"));
        em.persist(new Wilaya("48", "Relizane"));*/
      em.createNativeQuery("INSERT INTO sch_admin.wilaya (id, code, nom) VALUES\n" +
"(1, 1, 'Adrar'),\n" +
"(2, 2, 'Chlef'),\n" +
"(3, 3, 'Laghouat'),\n" +
"(4, 4, 'Oum El Bouaghi'),\n" +
"(5, 5, 'Batna'),\n" +
"(6, 6, 'Béjaïa'),\n" +
"(7, 7, 'Biskra'),\n" +
"(8, 8, 'Béchar'),\n" +
"(9, 9, 'Blida'),\n" +
"(10, 10, 'Bouira'),\n" +
"(11, 11, 'Tamanrasset'),\n" +
"(12, 12, 'Tébessa'),\n" +
"(13, 13, 'Tlemcen'),\n" +
"(14, 14, 'Tiaret'),\n" +
"(15, 15, 'Tizi Ouzou'),\n" +
"(16, 16, 'Alger'),\n" +
"(17, 17, 'Djelfa'),\n" +
"(18, 18, 'Jijel'),\n" +
"(19, 19, 'Sétif'),\n" +
"(20, 20, 'Saïda'),\n" +
"(21, 21, 'Skikda'),\n" +
"(22, 22, 'Sidi Bel Abbès'),\n" +
"(23, 23, 'Annaba'),\n" +
"(24, 24, 'Guelma'),\n" +
"(25, 25, 'Constantine'),\n" +
"(26, 26, 'Médéa'),\n" +
"(27, 27, 'Mostaganem'),\n" +
"(28, 28, 'M''Sila'),\n" +
"(29, 29, 'Mascara'),\n" +
"(30, 30, 'Ouargla'),\n" +
"(31, 31, 'Oran'),\n" +
"(32, 32, 'El Bayadh'),\n" +
"(33, 33, 'Illizi'),\n" +
"(34, 34, 'Bordj Bou Arreridj'),\n" +
"(35, 35, 'Boumerdès'),\n" +
"(36, 36, 'El Tarf'),\n" +
"(37, 37, 'Tindouf'),\n" +
"(38, 38, 'Tissemsilt'),\n" +
"(39, 39, 'El Oued'),\n" +
"(40, 40, 'Khenchela'),\n" +
"(41, 41, 'Souk Ahras'),\n" +
"(42, 42, 'Tipaza'),\n" +
"(43, 43, 'Mila'),\n" +
"(44, 44, 'Aïn Defla'),\n" +
"(45, 45, 'Naâma'),\n" +
"(46, 46, 'Aïn Témouchent'),\n" +
"(47, 47, 'Ghardaïa'),\n" +
"(48, 48, 'Relizane');").executeUpdate();
//------------------------------------------------------------------------------------------------------------------------------------------------
// Initialisation des Comune
        em.createNativeQuery("INSERT INTO sch_admin.commune (id, code, nom, idwilaya) VALUES\n" +
"(1, '01001', 'Adrar', 1),\n" +
"(2, '01002', 'Tamest', 1),\n" +
"(3, '01003', 'Charouine', 1),\n" +
"(4, '01004', 'Reggane', 1),\n" +
"(5, '01005', 'Inozghmir', 1),\n" +
"(6, '01006', 'Tit', 1),\n" +
"(7, '01007', 'Ksar Kaddour', 1),\n" +
"(8, '01008', 'Tsabit', 1),\n" +
"(9, '01009', 'Timimoun', 1),\n" +
"(10, '01010', 'Ouled Said', 1),\n" +
"(11, '01011', 'Zaouiet Kounta', 1),\n" +
"(12, '01012', 'Aoulef', 1),\n" +
"(13, '01013', 'Timokten', 1),\n" +
"(14, '01014', 'Tamentit', 1),\n" +
"(15, '01015', 'Fenoughil', 1),\n" +
"(16, '01016', 'Tinerkouk', 1),\n" +
"(17, '01017', 'Deldoul', 1),\n" +
"(18, '01018', 'Sali', 1),\n" +
"(19, '01019', 'Akabli', 1),\n" +
"(20, '01020', 'Metarfa', 1),\n" +
"(21, '01021', 'O Ahmed Timmi', 1),\n" +
"(22, '01022', 'Bouda', 1),\n" +
"(23, '01023', 'Aougrout', 1),\n" +
"(24, '01024', 'Talmine', 1),\n" +
"(25, '01025', 'B Badji Mokhtar', 1),\n" +
"(26, '01026', 'Sbaa', 1),\n" +
"(27, '01027', 'Ouled Aissa', 1),\n" +
"(28, '01028', 'Timiaouine', 1),\n" +
"(29, '02001', 'Chlef', 2),\n" +
"(30, '02002', 'Tenes', 2),\n" +
"(31, '02003', 'Benairia', 2),\n" +
"(32, '02004', 'El Karimia', 2),\n" +
"(33, '02005', 'Tadjna', 2),\n" +
"(34, '02006', 'Taougrite', 2),\n" +
"(35, '02007', 'Beni Haoua', 2),\n" +
"(36, '02008', 'Sobha', 2),\n" +
"(37, '02009', 'Harchoun', 2),\n" +
"(38, '02010', 'Ouled Fares', 2),\n" +
"(39, '02011', 'Sidi Akacha', 2),\n" +
"(40, '02012', 'Boukadir', 2),\n" +
"(41, '02013', 'Beni Rached', 2),\n" +
"(42, '02014', 'Talassa', 2),\n" +
"(43, '02015', 'Herenfa', 2),\n" +
"(44, '02016', 'Oued Goussine', 2),\n" +
"(45, '02017', 'Dahra', 2),\n" +
"(46, '02018', 'Ouled Abbes', 2),\n" +
"(47, '02019', 'Sendjas', 2),\n" +
"(48, '02020', 'Zeboudja', 2),\n" +
"(49, '02021', 'Oued Sly', 2),\n" +
"(50, '02022', 'Abou El Hassen', 2),\n" +
"(51, '02023', 'El Marsa', 2),\n" +
"(52, '02024', 'Chettia', 2),\n" +
"(53, '02025', 'Sidi Abderrahmane', 2),\n" +
"(54, '02026', 'Moussadek', 2),\n" +
"(55, '02027', 'El Hadjadj', 2),\n" +
"(56, '02028', 'Labiod Medjadja', 2),\n" +
"(57, '02029', 'Oued Fodda', 2),\n" +
"(58, '02030', 'Ouled Ben Abdelkader', 2),\n" +
"(59, '02031', 'Bouzghaia', 2),\n" +
"(60, '02032', 'Ain Merane', 2),\n" +
"(61, '02033', 'Oum Drou', 2),\n" +
"(62, '02034', 'Breira', 2),\n" +
"(63, '02035', 'Ben Boutaleb', 2),\n" +
"(64, '03001', 'Laghouat', 3),\n" +
"(65, '03002', 'Ksar El Hirane', 3),\n" +
"(66, '03003', 'Benacer Ben Chohra', 3),\n" +
"(67, '03004', 'Sidi Makhlouf', 3),\n" +
"(68, '03005', 'Hassi Delaa', 3),\n" +
"(69, '03006', 'Hassi R Mel', 3),\n" +
"(70, '03007', 'Ain Mahdi', 3),\n" +
"(71, '03008', 'Tadjmout', 3),\n" +
"(72, '03009', 'Kheneg', 3),\n" +
"(73, '03010', 'Gueltat Sidi Saad', 3),\n" +
"(74, '03011', 'Ain Sidi Ali', 3),\n" +
"(75, '03012', 'Beidha', 3),\n" +
"(76, '03013', 'Brida', 3),\n" +
"(77, '03014', 'El Ghicha', 3),\n" +
"(78, '03015', 'Hadj Mechri', 3),\n" +
"(79, '03016', 'Sebgag', 3),\n" +
"(80, '03017', 'Taouiala', 3),\n" +
"(81, '03018', 'Tadjrouna', 3),\n" +
"(82, '03019', 'Aflou', 3),\n" +
"(83, '03020', 'El Assafia', 3),\n" +
"(84, '03021', 'Oued Morra', 3),\n" +
"(85, '03022', 'Oued M Zi', 3),\n" +
"(86, '03023', 'El Haouaita', 3),\n" +
"(87, '03024', 'Sidi Bouzid', 3),\n" +
"(88, '04001', 'Oum El Bouaghi', 4),\n" +
"(89, '04002', 'Ain Beida', 4),\n" +
"(90, '04003', 'Ainmlila', 4),\n" +
"(91, '04004', 'Behir Chergui', 4),\n" +
"(92, '04005', 'El Amiria', 4),\n" +
"(93, '04006', 'Sigus', 4),\n" +
"(94, '04007', 'El Belala', 4),\n" +
"(95, '04008', 'Ain Babouche', 4),\n" +
"(96, '04009', 'Berriche', 4),\n" +
"(97, '04010', 'Ouled Hamla', 4),\n" +
"(98, '04011', 'Dhala', 4),\n" +
"(99, '04012', 'Ain Kercha', 4),\n" +
"(100, '04013', 'Hanchir Toumghani', 4),\n" +
"(101, '04014', 'El Djazia', 4),\n" +
"(102, '04015', 'Ain Diss', 4),\n" +
"(103, '04016', 'Fkirina', 4),\n" +
"(104, '04017', 'Souk Naamane', 4),\n" +
"(105, '04018', 'Zorg', 4),\n" +
"(106, '04019', 'El Fedjoudj Boughrar', 4),\n" +
"(107, '04020', 'Ouled Zouai', 4),\n" +
"(108, '04021', 'Bir Chouhada', 4),\n" +
"(109, '04022', 'Ksar Sbahi', 4),\n" +
"(110, '04023', 'Oued Nini', 4),\n" +
"(111, '04024', 'Meskiana', 4),\n" +
"(112, '04025', 'Ain Fekroune', 4),\n" +
"(113, '04026', 'Rahia', 4),\n" +
"(114, '04027', 'Ain Zitoun', 4),\n" +
"(115, '04028', 'Ouled Gacem', 4),\n" +
"(116, '04029', 'El Harmilia', 4),\n" +
"(117, '05001', 'Batna', 5),\n" +
"(118, '05002', 'Ghassira', 5),\n" +
"(119, '05003', 'Maafa', 5),\n" +
"(120, '05004', 'Merouana', 5),\n" +
"(121, '05005', 'Seriana', 5),\n" +
"(122, '05006', 'Menaa', 5),\n" +
"(123, '05007', 'El Madher', 5),\n" +
"(124, '05008', 'Tazoult', 5),\n" +
"(125, '05009', 'Ngaous', 5),\n" +
"(126, '05010', 'Guigba', 5),\n" +
"(127, '05011', 'Inoughissen', 5),\n" +
"(128, '05012', 'Ouyoun El Assafir', 5),\n" +
"(129, '05013', 'Djerma', 5),\n" +
"(130, '05014', 'Bitam', 5),\n" +
"(131, '05015', 'Metkaouak', 5),\n" +
"(132, '05016', 'Arris', 5),\n" +
"(133, '05017', 'Kimmel', 5),\n" +
"(134, '05018', 'Tilatou', 5),\n" +
"(135, '05019', 'Ain Djasser', 5),\n" +
"(136, '05020', 'Ouled Selam', 5),\n" +
"(137, '05021', 'Tigherghar', 5),\n" +
"(138, '05022', 'Ain Yagout', 5),\n" +
"(139, '05023', 'Fesdis', 5),\n" +
"(140, '05024', 'Sefiane', 5),\n" +
"(141, '05025', 'Rahbat', 5),\n" +
"(142, '05026', 'Tighanimine', 5),\n" +
"(143, '05027', 'Lemsane', 5),\n" +
"(144, '05028', 'Ksar Belezma', 5),\n" +
"(145, '05029', 'Seggana', 5),\n" +
"(146, '05030', 'Ichmoul', 5),\n" +
"(147, '05031', 'Foum Toub', 5),\n" +
"(148, '05032', 'Beni Foudhala El Hakania', 5),\n" +
"(149, '05033', 'Oued El Ma', 5),\n" +
"(150, '05034', 'Talkhamt', 5),\n" +
"(151, '05035', 'Bouzina', 5),\n" +
"(152, '05036', 'Chemora', 5),\n" +
"(153, '05037', 'Oued Chaaba', 5),\n" +
"(154, '05038', 'Taxlent', 5),\n" +
"(155, '05039', 'Gosbat', 5),\n" +
"(156, '05040', 'Ouled Aouf', 5),\n" +
"(157, '05041', 'Boumagueur', 5),\n" +
"(158, '05042', 'Barika', 5),\n" +
"(159, '05043', 'Djezzar', 5),\n" +
"(160, '05044', 'Tkout', 5),\n" +
"(161, '05045', 'Ain Touta', 5),\n" +
"(162, '05046', 'Hidoussa', 5),\n" +
"(163, '05047', 'Teniet El Abed', 5),\n" +
"(164, '05048', 'Oued Taga', 5),\n" +
"(165, '05049', 'Ouled Fadel', 5),\n" +
"(166, '05050', 'Timgad', 5),\n" +
"(167, '05051', 'Ras El Aioun', 5),\n" +
"(168, '05052', 'Chir', 5),\n" +
"(169, '05053', 'Ouled Si Slimane', 5),\n" +
"(170, '05054', 'Zanat El Beida', 5),\n" +
"(171, '05055', 'Amdoukal', 5),\n" +
"(172, '05056', 'Ouled Ammar', 5),\n" +
"(173, '05057', 'El Hassi', 5),\n" +
"(174, '05058', 'Lazrou', 5),\n" +
"(175, '05059', 'Boumia', 5),\n" +
"(176, '05060', 'Boulhilat', 5),\n" +
"(177, '05061', 'Larbaa', 5),\n" +
"(178, '06001', 'Bejaia', 6),\n" +
"(179, '06002', 'Amizour', 6),\n" +
"(180, '06003', 'Ferraoun', 6),\n" +
"(181, '06004', 'Taourirt Ighil', 6),\n" +
"(182, '06005', 'Chelata', 6),\n" +
"(183, '06006', 'Tamokra', 6),\n" +
"(184, '06007', 'Timzrit', 6),\n" +
"(185, '06008', 'Souk El Thenine', 6),\n" +
"(186, '06009', 'Mcisna', 6),\n" +
"(187, '06010', 'Thinabdher', 6),\n" +
"(188, '06011', 'Tichi', 6),\n" +
"(189, '06012', 'Semaoun', 6),\n" +
"(190, '06013', 'Kendira', 6),\n" +
"(191, '06014', 'Tifra', 6),\n" +
"(192, '06015', 'Ighram', 6),\n" +
"(193, '06016', 'Amalou', 6),\n" +
"(194, '06017', 'Ighil Ali', 6),\n" +
"(195, '06018', 'Ifelain Ilmathen', 6),\n" +
"(196, '06019', 'Toudja', 6),\n" +
"(197, '06020', 'Darguina', 6),\n" +
"(198, '06021', 'Sidi Ayad', 6),\n" +
"(199, '06022', 'Aokas', 6),\n" +
"(200, '06023', 'Beni Djellil', 6),\n" +
"(201, '06024', 'Adekar', 6),\n" +
"(202, '06025', 'Akbou', 6),\n" +
"(203, '06026', 'Seddouk', 6),\n" +
"(204, '06027', 'Tazmalt', 6),\n" +
"(205, '06028', 'Ait Rizine', 6),\n" +
"(206, '06029', 'Chemini', 6),\n" +
"(207, '06030', 'Souk Oufella', 6),\n" +
"(208, '06031', 'Taskriout', 6),\n" +
"(209, '06032', 'Tibane', 6),\n" +
"(210, '06033', 'Tala Hamza', 6),\n" +
"(211, '06034', 'Barbacha', 6),\n" +
"(212, '06035', 'Beni Ksila', 6),\n" +
"(213, '06036', 'Ouzallaguen', 6),\n" +
"(214, '06037', 'Bouhamza', 6),\n" +
"(215, '06038', 'Beni Melikeche', 6),\n" +
"(216, '06039', 'Sidi Aich', 6),\n" +
"(217, '06040', 'El Kseur', 6),\n" +
"(218, '06041', 'Melbou', 6),\n" +
"(219, '06042', 'Akfadou', 6),\n" +
"(220, '06043', 'Leflaye', 6),\n" +
"(221, '06044', 'Kherrata', 6),\n" +
"(222, '06045', 'Draa Kaid', 6),\n" +
"(223, '06046', 'Tamridjet', 6),\n" +
"(224, '06047', 'Ait Smail', 6),\n" +
"(225, '06048', 'Boukhelifa', 6),\n" +
"(226, '06049', 'Tizi Nberber', 6),\n" +
"(227, '06050', 'Beni Maouch', 6),\n" +
"(228, '06051', 'Oued Ghir', 6),\n" +
"(229, '06052', 'Boudjellil', 6),\n" +
"(230, '07001', 'Biskra', 7),\n" +
"(231, '07002', 'Oumache', 7),\n" +
"(232, '07003', 'Branis', 7),\n" +
"(233, '07004', 'Chetma', 7),\n" +
"(234, '07005', 'Ouled Djellal', 7),\n" +
"(235, '07006', 'Ras El Miaad', 7),\n" +
"(236, '07007', 'Besbes', 7),\n" +
"(237, '07008', 'Sidi Khaled', 7),\n" +
"(238, '07009', 'Doucen', 7),\n" +
"(239, '07010', 'Ech Chaiba', 7),\n" +
"(240, '07011', 'Sidi Okba', 7),\n" +
"(241, '07012', 'Mchouneche', 7),\n" +
"(242, '07013', 'El Haouch', 7),\n" +
"(243, '07014', 'Ain Naga', 7),\n" +
"(244, '07015', 'Zeribet El Oued', 7),\n" +
"(245, '07016', 'El Feidh', 7),\n" +
"(246, '07017', 'El Kantara', 7),\n" +
"(247, '07018', 'Ain Zaatout', 7),\n" +
"(248, '07019', 'El Outaya', 7),\n" +
"(249, '07020', 'Djemorah', 7),\n" +
"(250, '07021', 'Tolga', 7),\n" +
"(251, '07022', 'Lioua', 7),\n" +
"(252, '07023', 'Lichana', 7),\n" +
"(253, '07024', 'Ourlal', 7),\n" +
"(254, '07025', 'Mlili', 7),\n" +
"(255, '07026', 'Foughala', 7),\n" +
"(256, '07027', 'Bordj Ben Azzouz', 7),\n" +
"(257, '07028', 'Meziraa', 7),\n" +
"(258, '07029', 'Bouchagroun', 7),\n" +
"(259, '07030', 'Mekhadma', 7),\n" +
"(260, '07031', 'El Ghrous', 7),\n" +
"(261, '07032', 'El Hadjab', 7),\n" +
"(262, '07033', 'Khanguet Sidinadji', 7),\n" +
"(263, '08001', 'Bechar', 8),\n" +
"(264, '08002', 'Erg Ferradj', 8),\n" +
"(265, '08003', 'Ouled Khoudir', 8),\n" +
"(266, '08004', 'Meridja', 8),\n" +
"(267, '08005', 'Timoudi', 8),\n" +
"(268, '08006', 'Lahmar', 8),\n" +
"(269, '08007', 'Beni Abbes', 8),\n" +
"(270, '08008', 'Beni Ikhlef', 8),\n" +
"(271, '08009', 'Mechraa Houari B', 8),\n" +
"(272, '08010', 'Kenedsa', 8),\n" +
"(273, '08011', 'Igli', 8),\n" +
"(274, '08012', 'Tabalbala', 8),\n" +
"(275, '08013', 'Taghit', 8),\n" +
"(276, '08014', 'El Ouata', 8),\n" +
"(277, '08015', 'Boukais', 8),\n" +
"(278, '08016', 'Mogheul', 8),\n" +
"(279, '08017', 'Abadla', 8),\n" +
"(280, '08018', 'Kerzaz', 8),\n" +
"(281, '08019', 'Ksabi', 8),\n" +
"(282, '08020', 'Tamtert', 8),\n" +
"(283, '08021', 'Beni Ounif', 8),\n" +
"(284, '09001', 'Blida', 9),\n" +
"(285, '09002', 'Chebli', 9),\n" +
"(286, '09003', 'Bouinan', 9),\n" +
"(287, '09004', 'Oued El Alleug', 9),\n" +
"(288, '09007', 'Ouled Yaich', 9),\n" +
"(289, '09008', 'Chrea', 9),\n" +
"(290, '09010', 'El Affroun', 9),\n" +
"(291, '09011', 'Chiffa', 9),\n" +
"(292, '09012', 'Hammam Melouane', 9),\n" +
"(293, '09013', 'Ben Khlil', 9),\n" +
"(294, '09014', 'Soumaa', 9),\n" +
"(295, '09016', 'Mouzaia', 9),\n" +
"(296, '09017', 'Souhane', 9),\n" +
"(297, '09018', 'Meftah', 9),\n" +
"(298, '09019', 'Ouled Selama', 9),\n" +
"(299, '09020', 'Boufarik', 9),\n" +
"(300, '09021', 'Larbaa', 9),\n" +
"(301, '09022', 'Oued Djer', 9),\n" +
"(302, '09023', 'Beni Tamou', 9),\n" +
"(303, '09024', 'Bouarfa', 9),\n" +
"(304, '09025', 'Beni Mered', 9),\n" +
"(305, '09026', 'Bougara', 9),\n" +
"(306, '09027', 'Guerrouaou', 9),\n" +
"(307, '09028', 'Ain Romana', 9),\n" +
"(308, '09029', 'Djebabra', 9),\n" +
"(309, '10001', 'Bouira', 10),\n" +
"(310, '10002', 'El Asnam', 10),\n" +
"(311, '10003', 'Guerrouma', 10),\n" +
"(312, '10004', 'Souk El Khemis', 10),\n" +
"(313, '10005', 'Kadiria', 10),\n" +
"(314, '10006', 'Hanif', 10),\n" +
"(315, '10007', 'Dirah', 10),\n" +
"(316, '10008', 'Ait Laaziz', 10),\n" +
"(317, '10009', 'Taghzout', 10),\n" +
"(318, '10010', 'Raouraoua', 10),\n" +
"(319, '10011', 'Mezdour', 10),\n" +
"(320, '10012', 'Haizer', 10),\n" +
"(321, '10013', 'Lakhdaria', 10),\n" +
"(322, '10014', 'Maala', 10),\n" +
"(323, '10015', 'El Hachimia', 10),\n" +
"(324, '10016', 'Aomar', 10),\n" +
"(325, '10017', 'Chorfa', 10),\n" +
"(326, '10018', 'Bordj Oukhriss', 10),\n" +
"(327, '10019', 'El Adjiba', 10),\n" +
"(328, '10020', 'El Hakimia', 10),\n" +
"(329, '10021', 'El Khebouzia', 10),\n" +
"(330, '10022', 'Ahl El Ksar', 10),\n" +
"(331, '10023', 'Bouderbala', 10),\n" +
"(332, '10024', 'Zbarbar', 10),\n" +
"(333, '10025', 'Ain El Hadjar', 10),\n" +
"(334, '10026', 'Djebahia', 10),\n" +
"(335, '10027', 'Aghbalou', 10),\n" +
"(336, '10028', 'Taguedit', 10),\n" +
"(337, '10029', 'Ain Turk', 10),\n" +
"(338, '10030', 'Saharidj', 10),\n" +
"(339, '10031', 'Dechmia', 10),\n" +
"(340, '10032', 'Ridane', 10),\n" +
"(341, '10033', 'Bechloul', 10),\n" +
"(342, '10034', 'Boukram', 10),\n" +
"(343, '10035', 'Ain Bessam', 10),\n" +
"(344, '10036', 'Bir Ghbalou', 10),\n" +
"(345, '10037', 'Mchedallah', 10),\n" +
"(346, '10038', 'Sour El Ghozlane', 10),\n" +
"(347, '10039', 'Maamora', 10),\n" +
"(348, '10040', 'Ouled Rached', 10),\n" +
"(349, '10041', 'Ain Laloui', 10),\n" +
"(350, '10042', 'Hadjera Zerga', 10),\n" +
"(351, '10043', 'Ath Mansour', 10),\n" +
"(352, '10044', 'El Mokrani', 10),\n" +
"(353, '10045', 'Oued El Berdi', 10),\n" +
"(354, '11001', 'Tamanghasset', 11),\n" +
"(355, '11002', 'Abalessa', 11),\n" +
"(356, '11003', 'In Ghar', 11),\n" +
"(357, '11004', 'In Guezzam', 11),\n" +
"(358, '11005', 'Idles', 11),\n" +
"(359, '11006', 'Tazouk', 11),\n" +
"(360, '11007', 'Tinzaouatine', 11),\n" +
"(361, '11008', 'In Salah', 11),\n" +
"(362, '11009', 'In Amguel', 11),\n" +
"(363, '11010', 'Foggaret Ezzaouia', 11),\n" +
"(364, '12001', 'Tebessa', 12),\n" +
"(365, '12002', 'Bir El Ater', 12),\n" +
"(366, '12003', 'Cheria', 12),\n" +
"(367, '12004', 'Stah Guentis', 12),\n" +
"(368, '12005', 'El Aouinet', 12),\n" +
"(369, '12006', 'Lahouidjbet', 12),\n" +
"(370, '12007', 'Safsaf El Ouesra', 12),\n" +
"(371, '12008', 'Hammamet', 12),\n" +
"(372, '12009', 'Negrine', 12),\n" +
"(373, '12010', 'Bir El Mokadem', 12),\n" +
"(374, '12011', 'El Kouif', 12),\n" +
"(375, '12012', 'Morsott', 12),\n" +
"(376, '12013', 'El Ogla', 12),\n" +
"(377, '12014', 'Bir Dheheb', 12),\n" +
"(378, '12015', 'El Ogla El Malha', 12),\n" +
"(379, '12016', 'Gorriguer', 12),\n" +
"(380, '12017', 'Bekkaria', 12),\n" +
"(381, '12018', 'Boukhadra', 12),\n" +
"(382, '12019', 'Ouenza', 12),\n" +
"(383, '12020', 'El Ma El Biodh', 12),\n" +
"(384, '12021', 'Oum Ali', 12),\n" +
"(385, '12022', 'Thlidjene', 12),\n" +
"(386, '12023', 'Ain Zerga', 12),\n" +
"(387, '12024', 'El Meridj', 12),\n" +
"(388, '12025', 'Boulhaf Dyr', 12),\n" +
"(389, '12026', 'Bedjene', 12),\n" +
"(390, '12027', 'El Mazeraa', 12),\n" +
"(391, '12028', 'Ferkane', 12),\n" +
"(392, '13001', 'Tlemcen', 13),\n" +
"(393, '13002', 'Beni Mester', 13),\n" +
"(394, '13003', 'Ain Tallout', 13),\n" +
"(395, '13004', 'Remchi', 13),\n" +
"(396, '13005', 'El Fehoul', 13),\n" +
"(397, '13006', 'Sabra', 13),\n" +
"(398, '13007', 'Ghazaouet', 13),\n" +
"(399, '13008', 'Souani', 13),\n" +
"(400, '13009', 'Djebala', 13),\n" +
"(401, '13010', 'El Gor', 13),\n" +
"(402, '13011', 'Oued Chouly', 13),\n" +
"(403, '13012', 'Ain Fezza', 13),\n" +
"(404, '13013', 'Ouled Mimoun', 13),\n" +
"(405, '13014', 'Amieur', 13),\n" +
"(406, '13015', 'Ain Youcef', 13),\n" +
"(407, '13016', 'Zenata', 13),\n" +
"(408, '13017', 'Beni Snous', 13),\n" +
"(409, '13018', 'Bab El Assa', 13),\n" +
"(410, '13019', 'Dar Yaghmouracene', 13),\n" +
"(411, '13020', 'Fellaoucene', 13),\n" +
"(412, '13021', 'Azails', 13),\n" +
"(413, '13022', 'Sebbaa Chioukh', 13),\n" +
"(414, '13023', 'Terni Beni Hediel', 13),\n" +
"(415, '13024', 'Bensekrane', 13),\n" +
"(416, '13025', 'Ain Nehala', 13),\n" +
"(417, '13026', 'Hennaya', 13),\n" +
"(418, '13027', 'Maghnia', 13),\n" +
"(419, '13028', 'Hammam Boughrara', 13),\n" +
"(420, '13029', 'Souahlia', 13),\n" +
"(421, '13030', 'Msirda Fouaga', 13),\n" +
"(422, '13031', 'Ain Fetah', 13),\n" +
"(423, '13032', 'El Aricha', 13),\n" +
"(424, '13033', 'Souk Thlata', 13),\n" +
"(425, '13034', 'Sidi Abdelli', 13),\n" +
"(426, '13035', 'Sebdou', 13),\n" +
"(427, '13036', 'Beni Ouarsous', 13),\n" +
"(428, '13037', 'Sidi Medjahed', 13),\n" +
"(429, '13038', 'Beni Boussaid', 13),\n" +
"(430, '13039', 'Marsa Ben Mhidi', 13),\n" +
"(431, '13040', 'Nedroma', 13),\n" +
"(432, '13041', 'Sidi Djillali', 13),\n" +
"(433, '13042', 'Beni Bahdel', 13),\n" +
"(434, '13043', 'El Bouihi', 13),\n" +
"(435, '13044', 'Honaine', 13),\n" +
"(436, '13045', 'Tianet', 13),\n" +
"(437, '13046', 'Ouled Riyah', 13),\n" +
"(438, '13047', 'Bouhlou', 13),\n" +
"(439, '13048', 'Souk El Khemis', 13),\n" +
"(440, '13049', 'Ain Ghoraba', 13),\n" +
"(441, '13050', 'Chetouane', 13),\n" +
"(442, '13051', 'Mansourah', 13),\n" +
"(443, '13052', 'Beni Semiel', 13),\n" +
"(444, '13053', 'Ain Kebira', 13),\n" +
"(445, '14001', 'Tiaret', 14),\n" +
"(446, '14002', 'Medroussa', 14),\n" +
"(447, '14003', 'Ain Bouchekif', 14),\n" +
"(448, '14004', 'Sidi Ali Mellal', 14),\n" +
"(449, '14005', 'Ain Zarit', 14),\n" +
"(450, '14006', 'Ain Deheb', 14),\n" +
"(451, '14007', 'Sidi Bakhti', 14),\n" +
"(452, '14008', 'Medrissa', 14),\n" +
"(453, '14009', 'Zmalet El Emir Aek', 14),\n" +
"(454, '14010', 'Madna', 14),\n" +
"(455, '14011', 'Sebt', 14),\n" +
"(456, '14012', 'Mellakou', 14),\n" +
"(457, '14013', 'Dahmouni', 14),\n" +
"(458, '14014', 'Rahouia', 14),\n" +
"(459, '14015', 'Mahdia', 14),\n" +
"(460, '14016', 'Sougueur', 14),\n" +
"(461, '14017', 'Sidi Abdelghani', 14),\n" +
"(462, '14018', 'Ain El Hadid', 14),\n" +
"(463, '14019', 'Ouled Djerad', 14),\n" +
"(464, '14020', 'Naima', 14),\n" +
"(465, '14021', 'Meghila', 14),\n" +
"(466, '14022', 'Guertoufa', 14),\n" +
"(467, '14023', 'Sidi Hosni', 14),\n" +
"(468, '14024', 'Djillali Ben Amar', 14),\n" +
"(469, '14025', 'Sebaine', 14),\n" +
"(470, '14026', 'Tousnina', 14),\n" +
"(471, '14027', 'Frenda', 14),\n" +
"(472, '14028', 'Ain Kermes', 14),\n" +
"(473, '14029', 'Ksar Chellala', 14),\n" +
"(474, '14030', 'Rechaiga', 14),\n" +
"(475, '14031', 'Nadorah', 14),\n" +
"(476, '14032', 'Tagdemt', 14),\n" +
"(477, '14033', 'Oued Lilli', 14),\n" +
"(478, '14034', 'Mechraa Safa', 14),\n" +
"(479, '14035', 'Hamadia', 14),\n" +
"(480, '14036', 'Chehaima', 14),\n" +
"(481, '14037', 'Takhemaret', 14),\n" +
"(482, '14038', 'Sidi Abderrahmane', 14),\n" +
"(483, '14039', 'Serghine', 14),\n" +
"(484, '14040', 'Bougara', 14),\n" +
"(485, '14041', 'Faidja', 14),\n" +
"(486, '14042', 'Tidda', 14),\n" +
"(487, '15001', 'Tizi Ouzou', 15),\n" +
"(488, '15002', 'Ain El Hammam', 15),\n" +
"(489, '15003', 'Akbil', 15),\n" +
"(490, '15004', 'Freha', 15),\n" +
"(491, '15005', 'Souamaa', 15),\n" +
"(492, '15006', 'Mechtrass', 15),\n" +
"(493, '15007', 'Irdjen', 15),\n" +
"(494, '15008', 'Timizart', 15),\n" +
"(495, '15009', 'Makouda', 15),\n" +
"(496, '15010', 'Draa El Mizan', 15),\n" +
"(497, '15011', 'Tizi Ghenif', 15),\n" +
"(498, '15012', 'Bounouh', 15),\n" +
"(499, '15013', 'Ait Chaffaa', 15),\n" +
"(500, '15014', 'Frikat', 15),\n" +
"(501, '15015', 'Beni Aissi', 15),\n" +
"(502, '15016', 'Beni Zmenzer', 15),\n" +
"(503, '15017', 'Iferhounene', 15),\n" +
"(504, '15018', 'Azazga', 15),\n" +
"(505, '15019', 'Iloula Oumalou', 15),\n" +
"(506, '15020', 'Yakouren', 15),\n" +
"(507, '15021', 'Larba Nait Irathen', 15),\n" +
"(508, '15022', 'Tizi Rached', 15),\n" +
"(509, '15023', 'Zekri', 15),\n" +
"(510, '15024', 'Ouaguenoun', 15),\n" +
"(511, '15025', 'Ain Zaouia', 15),\n" +
"(512, '15026', 'Mkira', 15),\n" +
"(513, '15027', 'Ait Yahia', 15),\n" +
"(514, '15028', 'Ait Mahmoud', 15),\n" +
"(515, '15029', 'Maatka', 15),\n" +
"(516, '15030', 'Ait Boumehdi', 15),\n" +
"(517, '15031', 'Abi Youcef', 15),\n" +
"(518, '15032', 'Beni Douala', 15),\n" +
"(519, '15033', 'Illilten', 15),\n" +
"(520, '15034', 'Bouzguen', 15),\n" +
"(521, '15035', 'Ait Aggouacha', 15),\n" +
"(522, '15036', 'Ouadhia', 15),\n" +
"(523, '15037', 'Azzefoun', 15),\n" +
"(524, '15038', 'Tigzirt', 15),\n" +
"(525, '15039', 'Ait Aissa Mimoun', 15),\n" +
"(526, '15040', 'Boghni', 15),\n" +
"(527, '15041', 'Ifigha', 15),\n" +
"(528, '15042', 'Ait Oumalou', 15),\n" +
"(529, '15043', 'Tirmitine', 15),\n" +
"(530, '15044', 'Akerrou', 15),\n" +
"(531, '15045', 'Yatafen', 15),\n" +
"(532, '15046', 'Beni Ziki', 15),\n" +
"(533, '15047', 'Draa Ben Khedda', 15),\n" +
"(534, '15048', 'Ouacif', 15),\n" +
"(535, '15049', 'Idjeur', 15),\n" +
"(536, '15050', 'Mekla', 15),\n" +
"(537, '15051', 'Tizi Nthlata', 15),\n" +
"(538, '15052', 'Beni Yenni', 15),\n" +
"(539, '15053', 'Aghrib', 15),\n" +
"(540, '15054', 'Iflissen', 15),\n" +
"(541, '15055', 'Boudjima', 15),\n" +
"(542, '15056', 'Ait Yahia Moussa', 15),\n" +
"(543, '15057', 'Souk El Thenine', 15),\n" +
"(544, '15058', 'Ait Khelil', 15),\n" +
"(545, '15059', 'Sidi Naamane', 15),\n" +
"(546, '15060', 'Iboudraren', 15),\n" +
"(547, '15061', 'Aghni Goughran', 15),\n" +
"(548, '15062', 'Mizrana', 15),\n" +
"(549, '15063', 'Imsouhal', 15),\n" +
"(550, '15064', 'Tadmait', 15),\n" +
"(551, '15065', 'Ait Bouadou', 15),\n" +
"(552, '15066', 'Assi Youcef', 15),\n" +
"(553, '15067', 'Ait Toudert', 15),\n" +
"(554, '16001', 'Alger Centre', 16),\n" +
"(555, '16002', 'Sidi Mhamed', 16),\n" +
"(556, '16003', 'El Madania', 16),\n" +
"(557, '16004', 'Hamma Anassers', 16),\n" +
"(558, '16005', 'Bab El Oued', 16),\n" +
"(559, '16006', 'Bologhine Ibn Ziri', 16),\n" +
"(560, '16007', 'Casbah', 16),\n" +
"(561, '16008', 'Oued Koriche', 16),\n" +
"(562, '16009', 'Bir Mourad Rais', 16),\n" +
"(563, '16010', 'El Biar', 16),\n" +
"(564, '16011', 'Bouzareah', 16),\n" +
"(565, '16012', 'Birkhadem', 16),\n" +
"(566, '16013', 'El Harrach', 16),\n" +
"(567, '16014', 'Baraki', 16),\n" +
"(568, '16015', 'Oued Smar', 16),\n" +
"(569, '16016', 'Bourouba', 16),\n" +
"(570, '16017', 'Hussein Dey', 16),\n" +
"(571, '16018', 'Kouba', 16),\n" +
"(572, '16019', 'Bachedjerah', 16),\n" +
"(573, '16020', 'Dar El Beida', 16),\n" +
"(574, '16021', 'Bab Azzouar', 16),\n" +
"(575, '16022', 'Ben Aknoun', 16),\n" +
"(576, '16023', 'Dely Ibrahim', 16),\n" +
"(577, '16024', 'Bains Romains', 16),\n" +
"(578, '16025', 'Rais Hamidou', 16),\n" +
"(579, '16026', 'Djasr Kasentina', 16),\n" +
"(580, '16027', 'El Mouradia', 16),\n" +
"(581, '16028', 'Hydra', 16),\n" +
"(582, '16029', 'Mohammadia', 16),\n" +
"(583, '16030', 'Bordj El Kiffan', 16),\n" +
"(584, '16031', 'El Magharia', 16),\n" +
"(585, '16032', 'Beni Messous', 16),\n" +
"(586, '16033', 'Les Eucalyptus', 16),\n" +
"(587, '16034', 'Birtouta', 16),\n" +
"(588, '16035', 'Tassala El Merdja', 16),\n" +
"(589, '16036', 'Ouled Chebel', 16),\n" +
"(590, '16037', 'Sidi Moussa', 16),\n" +
"(591, '16038', 'Ain Taya', 16),\n" +
"(592, '16039', 'Bordj El Bahri', 16),\n" +
"(593, '16040', 'Marsa', 16),\n" +
"(594, '16041', 'Haraoua', 16),\n" +
"(595, '16042', 'Rouiba', 16),\n" +
"(596, '16043', 'Reghaia', 16),\n" +
"(597, '16044', 'Ain Benian', 16),\n" +
"(598, '16045', 'Staoueli', 16),\n" +
"(599, '16046', 'Zeralda', 16),\n" +
"(600, '16047', 'Mahelma', 16),\n" +
"(601, '16048', 'Rahmania', 16),\n" +
"(602, '16049', 'Souidania', 16),\n" +
"(603, '16050', 'Cheraga', 16),\n" +
"(604, '16051', 'Ouled Fayet', 16),\n" +
"(605, '16052', 'El Achour', 16),\n" +
"(606, '16053', 'Draria', 16),\n" +
"(607, '16054', 'Douera', 16),\n" +
"(608, '16055', 'Baba Hassen', 16),\n" +
"(609, '16056', 'Khracia', 16),\n" +
"(610, '16057', 'Saoula', 16),\n" +
"(611, '17001', 'Djelfa', 17),\n" +
"(612, '17002', 'Moudjebara', 17),\n" +
"(613, '17003', 'El Guedid', 17),\n" +
"(614, '17004', 'Hassi Bahbah', 17),\n" +
"(615, '17005', 'Ain Maabed', 17),\n" +
"(616, '17006', 'Sed Rahal', 17),\n" +
"(617, '17007', 'Feidh El Botma', 17),\n" +
"(618, '17008', 'Birine', 17),\n" +
"(619, '17009', 'Bouira Lahdeb', 17),\n" +
"(620, '17010', 'Zaccar', 17),\n" +
"(621, '17011', 'El Khemis', 17),\n" +
"(622, '17012', 'Sidi Baizid', 17),\n" +
"(623, '17013', 'Mliliha', 17),\n" +
"(624, '17014', 'El Idrissia', 17),\n" +
"(625, '17015', 'Douis', 17),\n" +
"(626, '17016', 'Hassi El Euch', 17),\n" +
"(627, '17017', 'Messaad', 17),\n" +
"(628, '17018', 'Guettara', 17),\n" +
"(629, '17019', 'Sidi Ladjel', 17),\n" +
"(630, '17020', 'Had Sahary', 17),\n" +
"(631, '17021', 'Guernini', 17),\n" +
"(632, '17022', 'Selmana', 17),\n" +
"(633, '17023', 'Ain Chouhada', 17),\n" +
"(634, '17024', 'Oum Laadham', 17),\n" +
"(635, '17025', 'Dar Chouikh', 17),\n" +
"(636, '17026', 'Charef', 17),\n" +
"(637, '17027', 'Beni Yacoub', 17),\n" +
"(638, '17028', 'Zaafrane', 17),\n" +
"(639, '17029', 'Deldoul', 17),\n" +
"(640, '17030', 'Ain El Ibel', 17),\n" +
"(641, '17031', 'Ain Oussera', 17),\n" +
"(642, '17032', 'Benhar', 17),\n" +
"(643, '17033', 'Hassi Fedoul', 17),\n" +
"(644, '17034', 'Amourah', 17),\n" +
"(645, '17035', 'Ain Fekka', 17),\n" +
"(646, '17036', 'Tadmit', 17),\n" +
"(647, '18001', 'Jijel', 18),\n" +
"(648, '18002', 'Erraguene', 18),\n" +
"(649, '18003', 'El Aouana', 18),\n" +
"(650, '18004', 'Ziamma Mansouriah', 18),\n" +
"(651, '18005', 'Taher', 18),\n" +
"(652, '18006', 'Emir Abdelkader', 18),\n" +
"(653, '18007', 'Chekfa', 18),\n" +
"(654, '18008', 'Chahna', 18),\n" +
"(655, '18009', 'El Milia', 18),\n" +
"(656, '18010', 'Sidi Maarouf', 18),\n" +
"(657, '18011', 'Settara', 18),\n" +
"(658, '18012', 'El Ancer', 18),\n" +
"(659, '18013', 'Sidi Abdelaziz', 18),\n" +
"(660, '18014', 'Kaous', 18),\n" +
"(661, '18015', 'Ghebala', 18),\n" +
"(662, '18016', 'Bouraoui Belhadef', 18),\n" +
"(663, '18017', 'Djmila', 18),\n" +
"(664, '18018', 'Selma Benziada', 18),\n" +
"(665, '18019', 'Boussif Ouled Askeur', 18),\n" +
"(666, '18020', 'El Kennar Nouchfi', 18),\n" +
"(667, '18021', 'Ouled Yahia Khadrouch', 18),\n" +
"(668, '18022', 'Boudria Beni Yadjis', 18),\n" +
"(669, '18023', 'Kemir Oued Adjoul', 18),\n" +
"(670, '18024', 'Texena', 18),\n" +
"(671, '18025', 'Djemaa Beni Habibi', 18),\n" +
"(672, '18026', 'Bordj Taher', 18),\n" +
"(673, '18027', 'Ouled Rabah', 18),\n" +
"(674, '18028', 'Ouadjana', 18),\n" +
"(675, '19001', 'Setif', 19),\n" +
"(676, '19002', 'Ain El Kebira', 19),\n" +
"(677, '19003', 'Beni Aziz', 19),\n" +
"(678, '19004', 'Ouled Sidi Ahmed', 19),\n" +
"(679, '19005', 'Boutaleb', 19),\n" +
"(680, '19006', 'Ain Roua', 19),\n" +
"(681, '19007', 'Draa Kebila', 19),\n" +
"(682, '19008', 'Bir El Arch', 19),\n" +
"(683, '19009', 'Beni Chebana', 19),\n" +
"(684, '19010', 'Ouled Tebben', 19),\n" +
"(685, '19011', 'Hamma', 19),\n" +
"(686, '19012', 'Maaouia', 19),\n" +
"(687, '19013', 'Ain Legraj', 19),\n" +
"(688, '19014', 'Ain Abessa', 19),\n" +
"(689, '19015', 'Dehamcha', 19),\n" +
"(690, '19016', 'Babor', 19),\n" +
"(691, '19017', 'Guidjel', 19),\n" +
"(692, '19018', 'Ain Lahdjar', 19),\n" +
"(693, '19019', 'Bousselam', 19),\n" +
"(694, '19020', 'El Eulma', 19),\n" +
"(695, '19021', 'Djemila', 19),\n" +
"(696, '19022', 'Beni Ouartilane', 19),\n" +
"(697, '19023', 'Rosfa', 19),\n" +
"(698, '19024', 'Ouled Addouane', 19),\n" +
"(699, '19025', 'Belaa', 19),\n" +
"(700, '19026', 'Ain Arnat', 19),\n" +
"(701, '19027', 'Amoucha', 19),\n" +
"(702, '19028', 'Ain Oulmane', 19),\n" +
"(703, '19029', 'Beidha Bordj', 19),\n" +
"(704, '19030', 'Bouandas', 19),\n" +
"(705, '19031', 'Bazer Sakhra', 19),\n" +
"(706, '19032', 'Hammam Essokhna', 19),\n" +
"(707, '19033', 'Mezloug', 19),\n" +
"(708, '19034', 'Bir Haddada', 19),\n" +
"(709, '19035', 'Serdj El Ghoul', 19),\n" +
"(710, '19036', 'Harbil', 19),\n" +
"(711, '19037', 'El Ouricia', 19),\n" +
"(712, '19038', 'Tizi Nbechar', 19),\n" +
"(713, '19039', 'Salah Bey', 19),\n" +
"(714, '19040', 'Ain Azal', 19),\n" +
"(715, '19041', 'Guenzet', 19),\n" +
"(716, '19042', 'Talaifacene', 19),\n" +
"(717, '19043', 'Bougaa', 19),\n" +
"(718, '19044', 'Beni Fouda', 19),\n" +
"(719, '19045', 'Tachouda', 19),\n" +
"(720, '19046', 'Beni Mouhli', 19),\n" +
"(721, '19047', 'Ouled Sabor', 19),\n" +
"(722, '19048', 'Guellal', 19),\n" +
"(723, '19049', 'Ain Sebt', 19),\n" +
"(724, '19050', 'Hammam Guergour', 19),\n" +
"(725, '19051', 'Ait Naoual Mezada', 19),\n" +
"(726, '19052', 'Ksar El Abtal', 19),\n" +
"(727, '19053', 'Beni Hocine', 19),\n" +
"(728, '19054', 'Ait Tizi', 19),\n" +
"(729, '19055', 'Maouklane', 19),\n" +
"(730, '19056', 'Guelta Zerka', 19),\n" +
"(731, '19057', 'Oued El Barad', 19),\n" +
"(732, '19058', 'Taya', 19),\n" +
"(733, '19059', 'El Ouldja', 19),\n" +
"(734, '19060', 'Tella', 19),\n" +
"(735, '20001', 'Saida', 20),\n" +
"(736, '20002', 'Doui Thabet', 20),\n" +
"(737, '20003', 'Ain El Hadjar', 20),\n" +
"(738, '20004', 'Ouled Khaled', 20),\n" +
"(739, '20005', 'Moulay Larbi', 20),\n" +
"(740, '20006', 'Youb', 20),\n" +
"(741, '20007', 'Hounet', 20),\n" +
"(742, '20008', 'Sidi Amar', 20),\n" +
"(743, '20009', 'Sidi Boubekeur', 20),\n" +
"(744, '20010', 'El Hassasna', 20),\n" +
"(745, '20011', 'Maamora', 20),\n" +
"(746, '20012', 'Sidi Ahmed', 20),\n" +
"(747, '20013', 'Ain Sekhouna', 20),\n" +
"(748, '20014', 'Ouled Brahim', 20),\n" +
"(749, '20015', 'Tircine', 20),\n" +
"(750, '20016', 'Ain Soltane', 20),\n" +
"(751, '21001', 'Skikda', 21),\n" +
"(752, '21002', 'Ain Zouit', 21),\n" +
"(753, '21003', 'El Hadaik', 21),\n" +
"(754, '21004', 'Azzaba', 21),\n" +
"(755, '21005', 'Djendel Saadi Mohamed', 21),\n" +
"(756, '21006', 'Ain Cherchar', 21),\n" +
"(757, '21007', 'Bekkouche Lakhdar', 21),\n" +
"(758, '21008', 'Benazouz', 21),\n" +
"(759, '21009', 'Es Sebt', 21),\n" +
"(760, '21010', 'Collo', 21),\n" +
"(761, '21011', 'Beni Zid', 21),\n" +
"(762, '21012', 'Kerkera', 21),\n" +
"(763, '21013', 'Ouled Attia', 21),\n" +
"(764, '21014', 'Oued Zehour', 21),\n" +
"(765, '21015', 'Zitouna', 21),\n" +
"(766, '21016', 'El Harrouch', 21),\n" +
"(767, '21017', 'Zerdazas', 21),\n" +
"(768, '21018', 'Ouled Hebaba', 21),\n" +
"(769, '21019', 'Sidi Mezghiche', 21),\n" +
"(770, '21020', 'Emdjez Edchich', 21),\n" +
"(771, '21021', 'Beni Oulbane', 21),\n" +
"(772, '21022', 'Ain Bouziane', 21),\n" +
"(773, '21023', 'Ramdane Djamel', 21),\n" +
"(774, '21024', 'Beni Bachir', 21),\n" +
"(775, '21025', 'Salah Bouchaour', 21),\n" +
"(776, '21026', 'Tamalous', 21),\n" +
"(777, '21027', 'Ain Kechra', 21),\n" +
"(778, '21028', 'Oum Toub', 21),\n" +
"(779, '21029', 'Bein El Ouiden', 21),\n" +
"(780, '21030', 'Fil Fila', 21),\n" +
"(781, '21031', 'Cheraia', 21),\n" +
"(782, '21032', 'Kanoua', 21),\n" +
"(783, '21033', 'El Ghedir', 21),\n" +
"(784, '21034', 'Bouchtata', 21),\n" +
"(785, '21035', 'Ouldja Boulbalout', 21),\n" +
"(786, '21036', 'Kheneg Mayoum', 21),\n" +
"(787, '21037', 'Hamadi Krouma', 21),\n" +
"(788, '21038', 'El Marsa', 21),\n" +
"(789, '22001', 'Sidi Bel Abbes', 22),\n" +
"(790, '22002', 'Tessala', 22),\n" +
"(791, '22003', 'Sidi Brahim', 22),\n" +
"(792, '22004', 'Mostefa Ben Brahim', 22),\n" +
"(793, '22005', 'Telagh', 22),\n" +
"(794, '22006', 'Mezaourou', 22),\n" +
"(795, '22007', 'Boukhanafis', 22),\n" +
"(796, '22008', 'Sidi Ali Boussidi', 22),\n" +
"(797, '22009', 'Badredine El Mokrani', 22),\n" +
"(798, '22010', 'Marhoum', 22),\n" +
"(799, '22011', 'Tafissour', 22),\n" +
"(800, '22012', 'Amarnas', 22),\n" +
"(801, '22013', 'Tilmouni', 22),\n" +
"(802, '22014', 'Sidi Lahcene', 22),\n" +
"(803, '22015', 'Ain Thrid', 22),\n" +
"(804, '22016', 'Makedra', 22),\n" +
"(805, '22017', 'Tenira', 22),\n" +
"(806, '22018', 'Moulay Slissen', 22),\n" +
"(807, '22019', 'El Hacaiba', 22),\n" +
"(808, '22020', 'Hassi Zehana', 22),\n" +
"(809, '22021', 'Tabia', 22),\n" +
"(810, '22022', 'Merine', 22),\n" +
"(811, '22023', 'Ras El Ma', 22),\n" +
"(812, '22024', 'Ain Tindamine', 22),\n" +
"(813, '22025', 'Ain Kada', 22),\n" +
"(814, '22026', 'Mcid', 22),\n" +
"(815, '22027', 'Sidi Khaled', 22),\n" +
"(816, '22028', 'Ain El Berd', 22),\n" +
"(817, '22029', 'Sfissef', 22),\n" +
"(818, '22030', 'Ain Adden', 22),\n" +
"(819, '22031', 'Oued Taourira', 22),\n" +
"(820, '22032', 'Dhaya', 22),\n" +
"(821, '22033', 'Zerouala', 22),\n" +
"(822, '22034', 'Lamtar', 22),\n" +
"(823, '22035', 'Sidi Chaib', 22),\n" +
"(824, '22036', 'Sidi Dahou Dezairs', 22),\n" +
"(825, '22037', 'Oued Sbaa', 22),\n" +
"(826, '22038', 'Boudjebaa El Bordj', 22),\n" +
"(827, '22039', 'Sehala Thaoura', 22),\n" +
"(828, '22040', 'Sidi Yacoub', 22),\n" +
"(829, '22041', 'Sidi Hamadouche', 22),\n" +
"(830, '22042', 'Belarbi', 22),\n" +
"(831, '22043', 'Oued Sefioun', 22),\n" +
"(832, '22044', 'Teghalimet', 22),\n" +
"(833, '22045', 'Ben Badis', 22),\n" +
"(834, '22046', 'Sidi Ali Benyoub', 22),\n" +
"(835, '22047', 'Chetouane Belaila', 22),\n" +
"(836, '22048', 'Bir El Hammam', 22),\n" +
"(837, '22049', 'Taoudmout', 22),\n" +
"(838, '22050', 'Redjem Demouche', 22),\n" +
"(839, '22051', 'Benachiba Chelia', 22),\n" +
"(840, '22052', 'Hassi Dahou', 22),\n" +
"(841, '23001', 'Annaba', 23),\n" +
"(842, '23002', 'Berrahel', 23),\n" +
"(843, '23003', 'El Hadjar', 23),\n" +
"(844, '23004', 'Eulma', 23),\n" +
"(845, '23005', 'El Bouni', 23),\n" +
"(846, '23006', 'Oued El Aneb', 23),\n" +
"(847, '23007', 'Cheurfa', 23),\n" +
"(848, '23008', 'Seraidi', 23),\n" +
"(849, '23009', 'Ain Berda', 23),\n" +
"(850, '23010', 'Chetaibi', 23),\n" +
"(851, '23011', 'Sidi Amer', 23),\n" +
"(852, '23012', 'Treat', 23),\n" +
"(853, '24001', 'Guelma', 24),\n" +
"(854, '24002', 'Nechmaya', 24),\n" +
"(855, '24003', 'Bouati Mahmoud', 24),\n" +
"(856, '24004', 'Oued Zenati', 24),\n" +
"(857, '24005', 'Tamlouka', 24),\n" +
"(858, '24006', 'Oued Fragha', 24),\n" +
"(859, '24007', 'Ain Sandel', 24),\n" +
"(860, '24008', 'Ras El Agba', 24),\n" +
"(861, '24009', 'Dahouara', 24),\n" +
"(862, '24010', 'Belkhir', 24),\n" +
"(863, '24011', 'Ben Djarah', 24),\n" +
"(864, '24012', 'Bou Hamdane', 24),\n" +
"(865, '24013', 'Ain Makhlouf', 24),\n" +
"(866, '24014', 'Ain Ben Beida', 24),\n" +
"(867, '24015', 'Khezara', 24),\n" +
"(868, '24016', 'Beni Mezline', 24),\n" +
"(869, '24017', 'Bou Hachana', 24),\n" +
"(870, '24018', 'Guelaat Bou Sbaa', 24),\n" +
"(871, '24019', 'Hammam Maskhoutine', 24),\n" +
"(872, '24020', 'El Fedjoudj', 24),\n" +
"(873, '24021', 'Bordj Sabat', 24),\n" +
"(874, '24022', 'Hamman Nbail', 24),\n" +
"(875, '24023', 'Ain Larbi', 24),\n" +
"(876, '24024', 'Medjez Amar', 24),\n" +
"(877, '24025', 'Bouchegouf', 24),\n" +
"(878, '24026', 'Heliopolis', 24),\n" +
"(879, '24027', 'Ain Hessania', 24),\n" +
"(880, '24028', 'Roknia', 24),\n" +
"(881, '24029', 'Salaoua Announa', 24),\n" +
"(882, '24030', 'Medjez Sfa', 24),\n" +
"(883, '24031', 'Boumahra Ahmed', 24),\n" +
"(884, '24032', 'Ain Reggada', 24),\n" +
"(885, '24033', 'Oued Cheham', 24),\n" +
"(886, '24034', 'Djeballah Khemissi', 24),\n" +
"(887, '25001', 'Constantine', 25),\n" +
"(888, '25002', 'Hamma Bouziane', 25),\n" +
"(889, '25003', 'El Haria', 25),\n" +
"(890, '25004', 'Zighoud Youcef', 25),\n" +
"(891, '25005', 'Didouche Mourad', 25),\n" +
"(892, '25006', 'El Khroub', 25),\n" +
"(893, '25007', 'Ain Abid', 25),\n" +
"(894, '25008', 'Beni Hamiden', 25),\n" +
"(895, '25009', 'Ouled Rahmoune', 25),\n" +
"(896, '25010', 'Ain Smara', 25),\n" +
"(897, '25011', 'Mesaoud Boudjeriou', 25),\n" +
"(898, '25012', 'Ibn Ziad', 25),\n" +
"(899, '26001', 'Medea', 26),\n" +
"(900, '26002', 'Ouzera', 26),\n" +
"(901, '26003', 'Ouled Maaref', 26),\n" +
"(902, '26004', 'Ain Boucif', 26),\n" +
"(903, '26005', 'Aissaouia', 26),\n" +
"(904, '26006', 'Ouled Deide', 26),\n" +
"(905, '26007', 'El Omaria', 26),\n" +
"(906, '26008', 'Derrag', 26),\n" +
"(907, '26009', 'El Guelbelkebir', 26),\n" +
"(908, '26010', 'Bouaiche', 26),\n" +
"(909, '26011', 'Mezerena', 26),\n" +
"(910, '26012', 'Ouled Brahim', 26),\n" +
"(911, '26013', 'Damiat', 26),\n" +
"(912, '26014', 'Sidi Ziane', 26),\n" +
"(913, '26015', 'Tamesguida', 26),\n" +
"(914, '26016', 'El Hamdania', 26),\n" +
"(915, '26017', 'Kef Lakhdar', 26),\n" +
"(916, '26018', 'Chelalet El Adhaoura', 26),\n" +
"(917, '26019', 'Bouskene', 26),\n" +
"(918, '26020', 'Rebaia', 26),\n" +
"(919, '26021', 'Bouchrahil', 26),\n" +
"(920, '26022', 'Ouled Hellal', 26),\n" +
"(921, '26023', 'Tafraout', 26),\n" +
"(922, '26024', 'Baata', 26),\n" +
"(923, '26025', 'Boghar', 26),\n" +
"(924, '26026', 'Sidi Naamane', 26),\n" +
"(925, '26027', 'Ouled Bouachra', 26),\n" +
"(926, '26028', 'Sidi Zahar', 26),\n" +
"(927, '26029', 'Oued Harbil', 26),\n" +
"(928, '26030', 'Benchicao', 26),\n" +
"(929, '26031', 'Sidi Damed', 26),\n" +
"(930, '26032', 'Aziz', 26),\n" +
"(931, '26033', 'Souagui', 26),\n" +
"(932, '26034', 'Zoubiria', 26),\n" +
"(933, '26035', 'Ksar El Boukhari', 26),\n" +
"(934, '26036', 'El Azizia', 26),\n" +
"(935, '26037', 'Djouab', 26),\n" +
"(936, '26038', 'Chahbounia', 26),\n" +
"(937, '26039', 'Meghraoua', 26),\n" +
"(938, '26040', 'Cheniguel', 26),\n" +
"(939, '26041', 'Ain Ouksir', 26),\n" +
"(940, '26042', 'Oum El Djalil', 26),\n" +
"(941, '26043', 'Ouamri', 26),\n" +
"(942, '26044', 'Si Mahdjoub', 26),\n" +
"(943, '26045', 'Tlatet Eddoair', 26),\n" +
"(944, '26046', 'Beni Slimane', 26),\n" +
"(945, '26047', 'Berrouaghia', 26),\n" +
"(946, '26048', 'Seghouane', 26),\n" +
"(947, '26049', 'Meftaha', 26),\n" +
"(948, '26050', 'Mihoub', 26),\n" +
"(949, '26051', 'Boughezoul', 26),\n" +
"(950, '26052', 'Tablat', 26),\n" +
"(951, '26053', 'Deux Bassins', 26),\n" +
"(952, '26054', 'Draa Essamar', 26),\n" +
"(953, '26055', 'Sidi Errabia', 26),\n" +
"(954, '26056', 'Bir Ben Laabed', 26),\n" +
"(955, '26057', 'El Ouinet', 26),\n" +
"(956, '26058', 'Ouled Antar', 26),\n" +
"(957, '26059', 'Bouaichoune', 26),\n" +
"(958, '26060', 'Hannacha', 26),\n" +
"(959, '26061', 'Sedraia', 26),\n" +
"(960, '26062', 'Medjebar', 26),\n" +
"(961, '26063', 'Khams Djouamaa', 26),\n" +
"(962, '26064', 'Saneg', 26),\n" +
"(963, '27001', 'Mostaganem', 27),\n" +
"(964, '27002', 'Sayada', 27),\n" +
"(965, '27003', 'Fornaka', 27),\n" +
"(966, '27004', 'Stidia', 27),\n" +
"(967, '27005', 'Ain Nouissy', 27),\n" +
"(968, '27006', 'Hassi Maameche', 27),\n" +
"(969, '27007', 'Ain Tadles', 27),\n" +
"(970, '27008', 'Sour', 27),\n" +
"(971, '27009', 'Oued El Kheir', 27),\n" +
"(972, '27010', 'Sidi Bellater', 27),\n" +
"(973, '27011', 'Kheiredine ', 27),\n" +
"(974, '27012', 'Sidi Ali', 27),\n" +
"(975, '27013', 'Abdelmalek Ramdane', 27),\n" +
"(976, '27014', 'Hadjadj', 27),\n" +
"(977, '27015', 'Nekmaria', 27),\n" +
"(978, '27016', 'Sidi Lakhdar', 27),\n" +
"(979, '27017', 'Achaacha', 27),\n" +
"(980, '27018', 'Khadra', 27),\n" +
"(981, '27019', 'Bouguirat', 27),\n" +
"(982, '27020', 'Sirat', 27),\n" +
"(983, '27021', 'Ain Sidi Cherif', 27),\n" +
"(984, '27022', 'Mesra', 27),\n" +
"(985, '27023', 'Mansourah', 27),\n" +
"(986, '27024', 'Souaflia', 27),\n" +
"(987, '27025', 'Ouled Boughalem', 27),\n" +
"(988, '27026', 'Ouled Maallah', 27),\n" +
"(989, '27027', 'Mezghrane', 27),\n" +
"(990, '27028', 'Ain Boudinar', 27),\n" +
"(991, '27029', 'Tazgait', 27),\n" +
"(992, '27030', 'Safsaf', 27),\n" +
"(993, '27031', 'Touahria', 27),\n" +
"(994, '27032', 'El Hassiane', 27),\n" +
"(995, '28001', 'Msila', 28),\n" +
"(996, '28002', 'Maadid', 28),\n" +
"(997, '28003', 'Hammam Dhalaa', 28),\n" +
"(998, '28004', 'Ouled Derradj', 28),\n" +
"(999, '28005', 'Tarmount', 28),\n" +
"(1000, '28006', 'Mtarfa', 28),\n" +
"(1001, '28007', 'Khoubana', 28),\n" +
"(1002, '28008', 'Mcif', 28),\n" +
"(1003, '28009', 'Chellal', 28),\n" +
"(1004, '28010', 'Ouled Madhi', 28),\n" +
"(1005, '28011', 'Magra', 28),\n" +
"(1006, '28012', 'Berhoum', 28),\n" +
"(1007, '28013', 'Ain Khadra', 28),\n" +
"(1008, '28014', 'Ouled Addi Guebala', 28),\n" +
"(1009, '28015', 'Belaiba', 28),\n" +
"(1010, '28016', 'Sidi Aissa', 28),\n" +
"(1011, '28017', 'Ain El Hadjel', 28),\n" +
"(1012, '28018', 'Sidi Hadjeres', 28),\n" +
"(1013, '28019', 'Ouanougha', 28),\n" +
"(1014, '28020', 'Bou Saada', 28),\n" +
"(1015, '28021', 'Ouled Sidi Brahim', 28),\n" +
"(1016, '28022', 'Sidi Ameur', 28),\n" +
"(1017, '28023', 'Tamsa', 28),\n" +
"(1018, '28024', 'Ben Srour', 28),\n" +
"(1019, '28025', 'Ouled Slimane', 28),\n" +
"(1020, '28026', 'El Houamed', 28),\n" +
"(1021, '28027', 'El Hamel', 28),\n" +
"(1022, '28028', 'Ouled Mansour', 28),\n" +
"(1023, '28029', 'Maarif', 28),\n" +
"(1024, '28030', 'Dehahna', 28),\n" +
"(1025, '28031', 'Bouti Sayah', 28),\n" +
"(1026, '28032', 'Khettouti Sed Djir', 28),\n" +
"(1027, '28033', 'Zarzour', 28),\n" +
"(1028, '28034', 'Oued Chair', 28),\n" +
"(1029, '28035', 'Benzouh', 28),\n" +
"(1030, '28036', 'Bir Foda', 28),\n" +
"(1031, '28037', 'Ain Fares', 28),\n" +
"(1032, '28038', 'Sidi Mhamed', 28),\n" +
"(1033, '28039', 'Ouled Atia', 28),\n" +
"(1034, '28040', 'Souamaa', 28),\n" +
"(1035, '28041', 'Ain El Melh', 28),\n" +
"(1036, '28042', 'Medjedel', 28),\n" +
"(1037, '28043', 'Slim', 28),\n" +
"(1038, '28044', 'Ain Errich', 28),\n" +
"(1039, '28045', 'Beni Ilmane', 28),\n" +
"(1040, '28046', 'Oultene', 28),\n" +
"(1041, '28047', 'Djebel Messaad', 28),\n" +
"(1042, '29001', 'Mascara', 29),\n" +
"(1043, '29002', 'Bou Hanifia', 29),\n" +
"(1044, '29003', 'Tizi', 29),\n" +
"(1045, '29004', 'Hacine', 29),\n" +
"(1046, '29005', 'Maoussa', 29),\n" +
"(1047, '29006', 'Teghennif', 29),\n" +
"(1048, '29007', 'El Hachem', 29),\n" +
"(1049, '29008', 'Sidi Kada', 29),\n" +
"(1050, '29009', 'Zelmata', 29),\n" +
"(1051, '29010', 'Oued El Abtal', 29),\n" +
"(1052, '29011', 'Ain Ferah', 29),\n" +
"(1053, '29012', 'Ghriss', 29),\n" +
"(1054, '29013', 'Froha', 29),\n" +
"(1055, '29014', 'Matemore', 29),\n" +
"(1056, '29015', 'Makdha', 29),\n" +
"(1057, '29016', 'Sidi Boussaid', 29),\n" +
"(1058, '29017', 'El Bordj', 29),\n" +
"(1059, '29018', 'Ain Fekan', 29),\n" +
"(1060, '29019', 'Benian', 29),\n" +
"(1061, '29020', 'Khalouia', 29),\n" +
"(1062, '29021', 'El Menaouer', 29),\n" +
"(1063, '29022', 'Oued Taria', 29),\n" +
"(1064, '29023', 'Aouf', 29),\n" +
"(1065, '29024', 'Ain Fares', 29),\n" +
"(1066, '29025', 'Ain Frass', 29),\n" +
"(1067, '29026', 'Sig', 29),\n" +
"(1068, '29027', 'Oggaz', 29),\n" +
"(1069, '29028', 'Alaimia', 29),\n" +
"(1070, '29029', 'El Gaada', 29),\n" +
"(1071, '29030', 'Zahana', 29),\n" +
"(1072, '29031', 'Mohammadia', 29),\n" +
"(1073, '29032', 'Sidi Abdelmoumene', 29),\n" +
"(1074, '29033', 'Ferraguig', 29),\n" +
"(1075, '29034', 'El Ghomri', 29),\n" +
"(1076, '29035', 'Sedjerara', 29),\n" +
"(1077, '29036', 'Moctadouz', 29),\n" +
"(1078, '29037', 'Bou Henni', 29),\n" +
"(1079, '29038', 'Guettena', 29),\n" +
"(1080, '29039', 'El Mamounia', 29),\n" +
"(1081, '29040', 'El Keurt', 29),\n" +
"(1082, '29041', 'Gharrous', 29),\n" +
"(1083, '29042', 'Gherdjoum', 29),\n" +
"(1084, '29043', 'Chorfa', 29),\n" +
"(1085, '29044', 'Ras Ain Amirouche', 29),\n" +
"(1086, '29045', 'Nesmot', 29),\n" +
"(1087, '29046', 'Sidi Abdeldjebar', 29),\n" +
"(1088, '29047', 'Sehailia', 29),\n" +
"(1089, '30001', 'Ouargla', 30),\n" +
"(1090, '30002', 'Ain Beida', 30),\n" +
"(1091, '30003', 'Ngoussa', 30),\n" +
"(1092, '30004', 'Hassi Messaoud', 30),\n" +
"(1093, '30005', 'Rouissat', 30),\n" +
"(1094, '30006', 'Balidat Ameur', 30),\n" +
"(1095, '30007', 'Tebesbest', 30),\n" +
"(1096, '30008', 'Nezla', 30),\n" +
"(1097, '30009', 'Zaouia El Abidia', 30),\n" +
"(1098, '30010', 'Sidi Slimane', 30),\n" +
"(1099, '30011', 'Sidi Khouiled', 30),\n" +
"(1100, '30012', 'Hassi Ben Abdellah', 30),\n" +
"(1101, '30013', 'Touggourt', 30),\n" +
"(1102, '30014', 'El Hadjira', 30),\n" +
"(1103, '30015', 'Taibet', 30),\n" +
"(1104, '30016', 'Tamacine', 30),\n" +
"(1105, '30017', 'Benaceur', 30),\n" +
"(1106, '30018', 'Mnaguer', 30),\n" +
"(1107, '30019', 'Megarine', 30),\n" +
"(1108, '30020', 'El Allia', 30),\n" +
"(1109, '30021', 'El Borma', 30),\n" +
"(1110, '31001', 'Oran', 31),\n" +
"(1111, '31002', 'Gdyel', 31),\n" +
"(1112, '31003', 'Bir El Djir', 31),\n" +
"(1113, '31004', 'Hassi Bounif', 31),\n" +
"(1114, '31005', 'Es Senia', 31),\n" +
"(1115, '31006', 'Arzew', 31),\n" +
"(1116, '31007', 'Bethioua', 31),\n" +
"(1117, '31008', 'Marsat El Hadjadj', 31),\n" +
"(1118, '31009', 'Ain Turk', 31),\n" +
"(1119, '31010', 'El Ancar', 31),\n" +
"(1120, '31011', 'Oued Tlelat', 31),\n" +
"(1121, '31012', 'Tafraoui', 31),\n" +
"(1122, '31013', 'Sidi Chami', 31),\n" +
"(1123, '31014', 'Boufatis', 31),\n" +
"(1124, '31015', 'Mers El Kebir', 31),\n" +
"(1125, '31016', 'Bousfer', 31),\n" +
"(1126, '31017', 'El Karma', 31),\n" +
"(1127, '31018', 'El Braya', 31),\n" +
"(1128, '31019', 'Hassi Ben Okba', 31),\n" +
"(1129, '31020', 'Ben Freha', 31),\n" +
"(1130, '31021', 'Hassi Mefsoukh', 31),\n" +
"(1131, '31022', 'Sidi Ben Yabka', 31),\n" +
"(1132, '31023', 'Messerghin', 31),\n" +
"(1133, '31024', 'Boutlelis', 31),\n" +
"(1134, '31025', 'Ain Kerma', 31),\n" +
"(1135, '31026', 'Ain Biya', 31),\n" +
"(1136, '32001', 'El Bayadh', 32),\n" +
"(1137, '32002', 'Rogassa', 32),\n" +
"(1138, '32003', 'Stitten', 32),\n" +
"(1139, '32004', 'Brezina', 32),\n" +
"(1140, '32005', 'Ghassoul', 32),\n" +
"(1141, '32006', 'Boualem', 32),\n" +
"(1142, '32007', 'El Abiodh Sidi Cheikh', 32),\n" +
"(1143, '32008', 'Ain El Orak', 32),\n" +
"(1144, '32009', 'Arbaouat', 32),\n" +
"(1145, '32010', 'Bougtoub', 32),\n" +
"(1146, '32011', 'El Kheither', 32),\n" +
"(1147, '32012', 'Kef El Ahmar', 32),\n" +
"(1148, '32013', 'Boussemghoun', 32),\n" +
"(1149, '32014', 'Chellala', 32),\n" +
"(1150, '32015', 'Krakda', 32),\n" +
"(1151, '32016', 'El Bnoud', 32),\n" +
"(1152, '32017', 'Cheguig', 32),\n" +
"(1153, '32018', 'Sidi Ameur', 32),\n" +
"(1154, '32019', 'El Mehara', 32),\n" +
"(1155, '32020', 'Tousmouline', 32),\n" +
"(1156, '32021', 'Sidi Slimane', 32),\n" +
"(1157, '32022', 'Sidi Tifour', 32),\n" +
"(1158, '33001', 'Illizi', 33),\n" +
"(1159, '33002', 'Djanet', 33),\n" +
"(1160, '33003', 'Debdeb', 33),\n" +
"(1161, '33004', 'Bordj Omar Driss', 33),\n" +
"(1162, '33005', 'Bordj El Haouasse', 33),\n" +
"(1163, '33006', 'In Amenas', 33),\n" +
"(1164, '34001', 'Bordj Bou Arreridj', 34),\n" +
"(1165, '34002', 'Ras El Oued', 34),\n" +
"(1166, '34003', 'Bordj Zemoura', 34),\n" +
"(1167, '34004', 'Mansoura', 34),\n" +
"(1168, '34005', 'El Mhir', 34),\n" +
"(1169, '34006', 'Ben Daoud', 34),\n" +
"(1170, '34007', 'El Achir', 34),\n" +
"(1171, '34008', 'Ain Taghrout', 34),\n" +
"(1172, '34009', 'Bordj Ghdir', 34),\n" +
"(1173, '34010', 'Sidi Embarek', 34),\n" +
"(1174, '34011', 'El Hamadia', 34),\n" +
"(1175, '34012', 'Belimour', 34),\n" +
"(1176, '34013', 'Medjana', 34),\n" +
"(1177, '34014', 'Teniet En Nasr', 34),\n" +
"(1178, '34015', 'Djaafra', 34),\n" +
"(1179, '34016', 'El Main', 34),\n" +
"(1180, '34017', 'Ouled Brahem', 34),\n" +
"(1181, '34018', 'Ouled Dahmane', 34),\n" +
"(1182, '34019', 'Hasnaoua', 34),\n" +
"(1183, '34020', 'Khelil', 34),\n" +
"(1184, '34021', 'Taglait', 34),\n" +
"(1185, '34022', 'Ksour', 34),\n" +
"(1186, '34023', 'Ouled Sidi Brahim', 34),\n" +
"(1187, '34024', 'Tafreg', 34),\n" +
"(1188, '34025', 'Colla', 34),\n" +
"(1189, '34026', 'Tixter', 34),\n" +
"(1190, '34027', 'El Ach', 34),\n" +
"(1191, '34028', 'El Anseur', 34),\n" +
"(1192, '34029', 'Tesmart', 34),\n" +
"(1193, '34030', 'Ain Tesra', 34),\n" +
"(1194, '34031', 'Bir Kasdali', 34),\n" +
"(1195, '34032', 'Ghilassa', 34),\n" +
"(1196, '34033', 'Rabta', 34),\n" +
"(1197, '34034', 'Haraza', 34),\n" +
"(1198, '35001', 'Boumerdes', 35),\n" +
"(1199, '35002', 'Boudouaou', 35),\n" +
"(1200, '35004', 'Afir', 35),\n" +
"(1201, '35005', 'Bordj Menaiel', 35),\n" +
"(1202, '35006', 'Baghlia', 35),\n" +
"(1203, '35007', 'Sidi Daoud', 35),\n" +
"(1204, '35008', 'Naciria', 35),\n" +
"(1205, '35009', 'Djinet', 35),\n" +
"(1206, '35010', 'Isser', 35),\n" +
"(1207, '35011', 'Zemmouri', 35),\n" +
"(1208, '35012', 'Si Mustapha', 35),\n" +
"(1209, '35013', 'Tidjelabine', 35),\n" +
"(1210, '35014', 'Chabet El Ameur', 35),\n" +
"(1211, '35015', 'Thenia', 35),\n" +
"(1212, '35018', 'Timezrit', 35),\n" +
"(1213, '35019', 'Corso', 35),\n" +
"(1214, '35020', 'Ouled Moussa', 35),\n" +
"(1215, '35021', 'Larbatache', 35),\n" +
"(1216, '35022', 'Bouzegza Keddara', 35),\n" +
"(1217, '35025', 'Taourga', 35),\n" +
"(1218, '35026', 'Ouled Aissa', 35),\n" +
"(1219, '35027', 'Ben Choud', 35),\n" +
"(1220, '35028', 'Dellys', 35),\n" +
"(1221, '35029', 'Ammal', 35),\n" +
"(1222, '35030', 'Beni Amrane', 35),\n" +
"(1223, '35031', 'Souk El Had', 35),\n" +
"(1224, '35032', 'Boudouaou El Bahri', 35),\n" +
"(1225, '35033', 'Ouled Hedadj', 35),\n" +
"(1226, '35035', 'Laghata', 35),\n" +
"(1227, '35036', 'Hammedi', 35),\n" +
"(1228, '35037', 'Khemis El Khechna', 35),\n" +
"(1229, '35038', 'El Kharrouba', 35),\n" +
"(1230, '36001', 'El Tarf', 36),\n" +
"(1231, '36002', 'Bouhadjar', 36),\n" +
"(1232, '36003', 'Ben Mhidi', 36),\n" +
"(1233, '36004', 'Bougous', 36),\n" +
"(1234, '36005', 'El Kala', 36),\n" +
"(1235, '36006', 'Ain El Assel', 36),\n" +
"(1236, '36007', 'El Aioun', 36),\n" +
"(1237, '36008', 'Bouteldja', 36),\n" +
"(1238, '36009', 'Souarekh', 36),\n" +
"(1239, '36010', 'Berrihane', 36),\n" +
"(1240, '36011', 'Lac Des Oiseaux', 36),\n" +
"(1241, '36012', 'Chefia', 36),\n" +
"(1242, '36013', 'Drean', 36),\n" +
"(1243, '36014', 'Chihani', 36),\n" +
"(1244, '36015', 'Chebaita Mokhtar', 36),\n" +
"(1245, '36016', 'Besbes', 36),\n" +
"(1246, '36017', 'Asfour', 36),\n" +
"(1247, '36018', 'Echatt', 36),\n" +
"(1248, '36019', 'Zerizer', 36),\n" +
"(1249, '36020', 'Zitouna', 36),\n" +
"(1250, '36021', 'Ain Kerma', 36),\n" +
"(1251, '36022', 'Oued Zitoun', 36),\n" +
"(1252, '36023', 'Hammam Beni Salah', 36),\n" +
"(1253, '36024', 'Raml Souk', 36),\n" +
"(1254, '37001', 'Tindouf', 37),\n" +
"(1255, '37002', 'Oum El Assel', 37),\n" +
"(1256, '38001', 'Tissemsilt', 38),\n" +
"(1257, '38002', 'Bordj Bou Naama', 38),\n" +
"(1258, '38003', 'Theniet El Had', 38),\n" +
"(1259, '38004', 'Lazharia', 38),\n" +
"(1260, '38005', 'Beni Chaib', 38),\n" +
"(1261, '38006', 'Lardjem', 38),\n" +
"(1262, '38007', 'Melaab', 38),\n" +
"(1263, '38008', 'Sidi Lantri', 38),\n" +
"(1264, '38009', 'Bordj El Emir Abdelkader', 38),\n" +
"(1265, '38010', 'Layoune', 38),\n" +
"(1266, '38011', 'Khemisti', 38),\n" +
"(1267, '38012', 'Ouled Bessem', 38),\n" +
"(1268, '38013', 'Ammari', 38),\n" +
"(1269, '38014', 'Youssoufia', 38),\n" +
"(1270, '38015', 'Sidi Boutouchent', 38),\n" +
"(1271, '38016', 'Larbaa', 38),\n" +
"(1272, '38017', 'Maasem', 38),\n" +
"(1273, '38018', 'Sidi Abed', 38),\n" +
"(1274, '38019', 'Tamalaht', 38),\n" +
"(1275, '38020', 'Sidi Slimane', 38),\n" +
"(1276, '38021', 'Boucaid', 38),\n" +
"(1277, '38022', 'Beni Lahcene', 38),\n" +
"(1278, '39001', 'El Oued', 39),\n" +
"(1279, '39002', 'Robbah', 39),\n" +
"(1280, '39003', 'Oued El Alenda', 39),\n" +
"(1281, '39004', 'Bayadha', 39),\n" +
"(1282, '39005', 'Nakhla', 39),\n" +
"(1283, '39006', 'Guemar', 39),\n" +
"(1284, '39007', 'Kouinine', 39),\n" +
"(1285, '39008', 'Reguiba', 39),\n" +
"(1286, '39009', 'Hamraia', 39),\n" +
"(1287, '39010', 'Taghzout', 39),\n" +
"(1288, '39011', 'Debila', 39),\n" +
"(1289, '39012', 'Hassani Abdelkrim', 39),\n" +
"(1290, '39013', 'Hassi Khelifa', 39),\n" +
"(1291, '39014', 'Taleb Larbi', 39),\n" +
"(1292, '39015', 'Douar El Ma', 39),\n" +
"(1293, '39016', 'Sidi Aoun', 39),\n" +
"(1294, '39017', 'Trifaoui', 39),\n" +
"(1295, '39018', 'Magrane', 39),\n" +
"(1296, '39019', 'Beni Guecha', 39),\n" +
"(1297, '39020', 'Ourmas', 39),\n" +
"(1298, '39021', 'Still', 39),\n" +
"(1299, '39022', 'Mrara', 39),\n" +
"(1300, '39023', 'Sidi Khellil', 39),\n" +
"(1301, '39024', 'Tendla', 39),\n" +
"(1302, '39025', 'El Ogla', 39),\n" +
"(1303, '39026', 'Mih Ouansa', 39),\n" +
"(1304, '39027', 'El Mghair', 39),\n" +
"(1305, '39028', 'Djamaa', 39),\n" +
"(1306, '39029', 'Oum Touyour', 39),\n" +
"(1307, '39030', 'Sidi Amrane', 39),\n" +
"(1308, '40001', 'Khenchela', 40),\n" +
"(1309, '40002', 'Mtoussa', 40),\n" +
"(1310, '40003', 'Kais', 40),\n" +
"(1311, '40004', 'Baghai', 40),\n" +
"(1312, '40005', 'El Hamma', 40),\n" +
"(1313, '40006', 'Ain Touila', 40),\n" +
"(1314, '40007', 'Taouzianat', 40),\n" +
"(1315, '40008', 'Bouhmama', 40),\n" +
"(1316, '40009', 'El Oueldja', 40),\n" +
"(1317, '40010', 'Remila', 40),\n" +
"(1318, '40011', 'Cherchar', 40),\n" +
"(1319, '40012', 'Djellal', 40),\n" +
"(1320, '40013', 'Babar', 40),\n" +
"(1321, '40014', 'Tamza', 40),\n" +
"(1322, '40015', 'Ensigha', 40),\n" +
"(1323, '40016', 'Ouled Rechache', 40),\n" +
"(1324, '40017', 'El Mahmal', 40),\n" +
"(1325, '40018', 'Msara', 40),\n" +
"(1326, '40019', 'Yabous', 40),\n" +
"(1327, '40020', 'Khirane', 40),\n" +
"(1328, '40021', 'Chelia', 40),\n" +
"(1329, '41001', 'Souk Ahras', 41),\n" +
"(1330, '41002', 'Sedrata', 41),\n" +
"(1331, '41003', 'Hanancha', 41),\n" +
"(1332, '41004', 'Mechroha', 41),\n" +
"(1333, '41005', 'Ouled Driss', 41),\n" +
"(1334, '41006', 'Tiffech', 41),\n" +
"(1335, '41007', 'Zaarouria', 41),\n" +
"(1336, '41008', 'Taoura', 41),\n" +
"(1337, '41009', 'Drea', 41),\n" +
"(1338, '41010', 'Haddada', 41),\n" +
"(1339, '41011', 'Khedara', 41),\n" +
"(1340, '41012', 'Merahna', 41),\n" +
"(1341, '41013', 'Ouled Moumen', 41),\n" +
"(1342, '41014', 'Bir Bouhouche', 41),\n" +
"(1343, '41015', 'Mdaourouche', 41),\n" +
"(1344, '41016', 'Oum El Adhaim', 41),\n" +
"(1345, '41017', 'Ain Zana', 41),\n" +
"(1346, '41018', 'Ain Soltane', 41),\n" +
"(1347, '41019', 'Quillen', 41),\n" +
"(1348, '41020', 'Sidi Fredj', 41),\n" +
"(1349, '41021', 'Safel El Ouiden', 41),\n" +
"(1350, '41022', 'Ragouba', 41),\n" +
"(1351, '41023', 'Khemissa', 41),\n" +
"(1352, '41024', 'Oued Keberit', 41),\n" +
"(1353, '41025', 'Terraguelt', 41),\n" +
"(1354, '41026', 'Zouabi', 41),\n" +
"(1355, '42001', 'Tipaza', 42),\n" +
"(1356, '42002', 'Menaceur', 42),\n" +
"(1357, '42003', 'Larhat', 42),\n" +
"(1358, '42004', 'Douaouda', 42),\n" +
"(1359, '42005', 'Bourkika', 42),\n" +
"(1360, '42006', 'Khemisti', 42),\n" +
"(1361, '42010', 'Aghabal', 42),\n" +
"(1362, '42012', 'Hadjout', 42),\n" +
"(1363, '42013', 'Sidi Amar', 42),\n" +
"(1364, '42014', 'Gouraya', 42),\n" +
"(1365, '42015', 'Nodor', 42),\n" +
"(1366, '42016', 'Chaiba', 42),\n" +
"(1367, '42017', 'Ain Tagourait', 42),\n" +
"(1368, '42022', 'Cherchel', 42),\n" +
"(1369, '42023', 'Damous', 42),\n" +
"(1370, '42024', 'Meurad', 42),\n" +
"(1371, '42025', 'Fouka', 42),\n" +
"(1372, '42026', 'Bou Ismail', 42),\n" +
"(1373, '42027', 'Ahmer El Ain', 42),\n" +
"(1374, '42030', 'Bou Haroun', 42),\n" +
"(1375, '42032', 'Sidi Ghiles', 42),\n" +
"(1376, '42033', 'Messelmoun', 42),\n" +
"(1377, '42034', 'Sidi Rached', 42),\n" +
"(1378, '42035', 'Kolea', 42),\n" +
"(1379, '42036', 'Attatba', 42),\n" +
"(1380, '42040', 'Sidi Semiane', 42),\n" +
"(1381, '42041', 'Beni Milleuk', 42),\n" +
"(1382, '42042', 'Hadjerat Ennous', 42),\n" +
"(1383, '43001', 'Mila', 43),\n" +
"(1384, '43002', 'Ferdjioua', 43),\n" +
"(1385, '43003', 'Chelghoum Laid', 43),\n" +
"(1386, '43004', 'Oued Athmenia', 43),\n" +
"(1387, '43005', 'Ain Mellouk', 43),\n" +
"(1388, '43006', 'Telerghma', 43),\n" +
"(1389, '43007', 'Oued Seguen', 43),\n" +
"(1390, '43008', 'Tadjenanet', 43),\n" +
"(1391, '43009', 'Benyahia Abderrahmane', 43),\n" +
"(1392, '43010', 'Oued Endja', 43),\n" +
"(1393, '43011', 'Ahmed Rachedi', 43),\n" +
"(1394, '43012', 'Ouled Khalouf', 43),\n" +
"(1395, '43013', 'Tiberguent', 43),\n" +
"(1396, '43014', 'Bouhatem', 43),\n" +
"(1397, '43015', 'Rouached', 43),\n" +
"(1398, '43016', 'Tessala Lamatai', 43),\n" +
"(1399, '43017', 'Grarem Gouga', 43),\n" +
"(1400, '43018', 'Sidi Merouane', 43),\n" +
"(1401, '43019', 'Tassadane Haddada', 43),\n" +
"(1402, '43020', 'Derradji Bousselah', 43),\n" +
"(1403, '43021', 'Minar Zarza', 43),\n" +
"(1404, '43022', 'Amira Arras', 43),\n" +
"(1405, '43023', 'Terrai Bainen', 43),\n" +
"(1406, '43024', 'Hamala', 43),\n" +
"(1407, '43025', 'Ain Tine', 43),\n" +
"(1408, '43026', 'El Mechira', 43),\n" +
"(1409, '43027', 'Sidi Khelifa', 43),\n" +
"(1410, '43028', 'Zeghaia', 43),\n" +
"(1411, '43029', 'Elayadi Barbes', 43),\n" +
"(1412, '43030', 'Ain Beida Harriche', 43),\n" +
"(1413, '43031', 'Yahia Beniguecha', 43),\n" +
"(1414, '43032', 'Chigara', 43),\n" +
"(1415, '44001', 'Ain Defla', 44),\n" +
"(1416, '44002', 'Miliana', 44),\n" +
"(1417, '44003', 'Boumedfaa', 44),\n" +
"(1418, '44004', 'Khemis Miliana', 44),\n" +
"(1419, '44005', 'Hammam Righa', 44),\n" +
"(1420, '44006', 'Arib', 44),\n" +
"(1421, '44007', 'Djelida', 44),\n" +
"(1422, '44008', 'El Amra', 44),\n" +
"(1423, '44009', 'Bourached', 44),\n" +
"(1424, '44010', 'El Attaf', 44),\n" +
"(1425, '44011', 'El Abadia', 44),\n" +
"(1426, '44012', 'Djendel', 44),\n" +
"(1427, '44013', 'Oued Chorfa', 44),\n" +
"(1428, '44014', 'Ain Lechiakh', 44),\n" +
"(1429, '44015', 'Oued Djemaa', 44),\n" +
"(1430, '44016', 'Rouina', 44),\n" +
"(1431, '44017', 'Zeddine', 44),\n" +
"(1432, '44018', 'El Hassania', 44),\n" +
"(1433, '44019', 'Bir Ouled Khelifa', 44),\n" +
"(1434, '44020', 'Ain Soltane', 44),\n" +
"(1435, '44021', 'Tarik Ibn Ziad', 44),\n" +
"(1436, '44022', 'Bordj Emir Khaled', 44),\n" +
"(1437, '44023', 'Ain Torki', 44),\n" +
"(1438, '44024', 'Sidi Lakhdar', 44),\n" +
"(1439, '44025', 'Ben Allal', 44),\n" +
"(1440, '44026', 'Ain Benian', 44),\n" +
"(1441, '44027', 'Hoceinia', 44),\n" +
"(1442, '44028', 'Barbouche', 44),\n" +
"(1443, '44029', 'Djemaa Ouled Chikh', 44),\n" +
"(1444, '44030', 'Mekhatria', 44),\n" +
"(1445, '44031', 'Bathia', 44),\n" +
"(1446, '44032', 'Tachta Zegagha', 44),\n" +
"(1447, '44033', 'Ain Bouyahia', 44),\n" +
"(1448, '44034', 'El Maine', 44),\n" +
"(1449, '44035', 'Tiberkanine', 44),\n" +
"(1450, '44036', 'Belaas', 44),\n" +
"(1451, '45001', 'Naama', 45),\n" +
"(1452, '45002', 'Mechria', 45),\n" +
"(1453, '45003', 'Ain Sefra', 45),\n" +
"(1454, '45004', 'Tiout', 45),\n" +
"(1455, '45005', 'Sfissifa', 45),\n" +
"(1456, '45006', 'Moghrar', 45),\n" +
"(1457, '45007', 'Assela', 45),\n" +
"(1458, '45008', 'Djeniane Bourzeg', 45),\n" +
"(1459, '45009', 'Ain Ben Khelil', 45),\n" +
"(1460, '45010', 'Makman Ben Amer', 45),\n" +
"(1461, '45011', 'Kasdir', 45),\n" +
"(1462, '45012', 'El Biod', 45),\n" +
"(1463, '46001', 'Ain Temouchent', 46),\n" +
"(1464, '46002', 'Chaabet El Ham', 46),\n" +
"(1465, '46003', 'Ain Kihal', 46),\n" +
"(1466, '46004', 'Hammam Bouhadjar', 46),\n" +
"(1467, '46005', 'Bou Zedjar', 46),\n" +
"(1468, '46006', 'Oued Berkeche', 46),\n" +
"(1469, '46007', 'Aghlal', 46),\n" +
"(1470, '46008', 'Terga', 46),\n" +
"(1471, '46009', 'Ain El Arbaa', 46),\n" +
"(1472, '46010', 'Tamzoura', 46),\n" +
"(1473, '46011', 'Chentouf', 46),\n" +
"(1474, '46012', 'Sidi Ben Adda', 46),\n" +
"(1475, '46013', 'Aoubellil', 46),\n" +
"(1476, '46014', 'El Malah', 46),\n" +
"(1477, '46015', 'Sidi Boumediene', 46),\n" +
"(1478, '46016', 'Oued Sabah', 46),\n" +
"(1479, '46017', 'Ouled Boudjemaa', 46),\n" +
"(1480, '46018', 'Ain Tolba', 46),\n" +
"(1481, '46019', 'El Amria', 46),\n" +
"(1482, '46020', 'Hassi El Ghella', 46),\n" +
"(1483, '46021', 'Hassasna', 46),\n" +
"(1484, '46022', 'Ouled Kihal', 46),\n" +
"(1485, '46023', 'Beni Saf', 46),\n" +
"(1486, '46024', 'Sidi Safi', 46),\n" +
"(1487, '46025', 'Oulhaca El Gheraba', 46),\n" +
"(1488, '46026', 'Tadmaya', 46),\n" +
"(1489, '46027', 'El Emir Abdelkader', 46),\n" +
"(1490, '46028', 'El Messaid', 46),\n" +
"(1491, '47001', 'Ghardaia', 47),\n" +
"(1492, '47002', 'El Meniaa', 47),\n" +
"(1493, '47003', 'Dhayet Bendhahoua', 47),\n" +
"(1494, '47004', 'Berriane', 47),\n" +
"(1495, '47005', 'Metlili', 47),\n" +
"(1496, '47006', 'El Guerrara', 47),\n" +
"(1497, '47007', 'El Atteuf', 47),\n" +
"(1498, '47008', 'Zelfana', 47),\n" +
"(1499, '47009', 'Sebseb', 47),\n" +
"(1500, '47010', 'Bounoura', 47),\n" +
"(1501, '47011', 'Hassi Fehal', 47),\n" +
"(1502, '47012', 'Hassi Gara', 47),\n" +
"(1503, '47013', 'Mansoura', 47),\n" +
"(1504, '48001', 'Relizane', 48),\n" +
"(1505, '48002', 'Oued Rhiou', 48),\n" +
"(1506, '48003', 'Belaassel Bouzegza', 48),\n" +
"(1507, '48004', 'Sidi Saada', 48),\n" +
"(1508, '48005', 'Ouled Aiche', 48),\n" +
"(1509, '48006', 'Sidi Lazreg', 48),\n" +
"(1510, '48007', 'El Hamadna', 48),\n" +
"(1511, '48008', 'Sidi Mhamed Ben Ali', 48),\n" +
"(1512, '48009', 'Mediouna', 48),\n" +
"(1513, '48010', 'Sidi Khettab', 48),\n" +
"(1514, '48011', 'Ammi Moussa', 48),\n" +
"(1515, '48012', 'Zemmoura', 48),\n" +
"(1516, '48013', 'Beni Dergoun', 48),\n" +
"(1517, '48014', 'Djidiouia', 48),\n" +
"(1518, '48015', 'El Guettar', 48),\n" +
"(1519, '48016', 'Hamri', 48),\n" +
"(1520, '48017', 'El Matmar', 48),\n" +
"(1521, '48018', 'Sidi Mhamed Ben Aouda', 48),\n" +
"(1522, '48019', 'Ain Tarek', 48),\n" +
"(1523, '48020', 'Oued Essalem', 48),\n" +
"(1524, '48021', 'Ouarizane', 48),\n" +
"(1525, '48022', 'Mazouna', 48),\n" +
"(1526, '48023', 'Kalaa', 48),\n" +
"(1527, '48024', 'Ain Rahma', 48),\n" +
"(1528, '48025', 'Yellel', 48),\n" +
"(1529, '48026', 'Oued El Djemaa', 48),\n" +
"(1530, '48027', 'Ramka', 48),\n" +
"(1531, '48028', 'Mendes', 48),\n" +
"(1532, '48029', 'Lahlef', 48),\n" +
"(1533, '48030', 'Beni Zentis', 48),\n" +
"(1534, '48031', 'Souk El Haad', 48),\n" +
"(1535, '48032', 'Dar Ben Abdellah', 48),\n" +
"(1536, '48033', 'El Hassi', 48),\n" +
"(1537, '48034', 'Had Echkalla', 48),\n" +
"(1538, '48035', 'Bendaoud', 48),\n" +
"(1539, '48036', 'El Ouldja', 48),\n" +
"(1540, '48037', 'Merdja Sidi Abed', 48),\n" +
"(1541, '48038', 'Ouled Sidi Mihoub', 48)").executeUpdate();









//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

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