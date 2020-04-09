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
import otherEntity.Typecompetence;

/**
 *
 * @author ayadi
 */
/* définir que cette ejb qui ressemble a un ejb session qu'il ne peut 
   avoir qu'une seule instance dans un conteneur 
   et Exécution de code au lancement ou à l'arrêt de l'application 
*/
@Singleton

// demander l'initialisation du singleton au lancement de l'aaplication 
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
         em.flush();
//------------------------------------------------------------------------------------------------------------------        
 //***************** Fin Initialisation Module Administration *************************************************************************
		// Initialisation des modules du referentiel
		adminModule = new AdminModule("REFER", "Gestionnaire du Referentiel", "/pages/referentiel/indexReferentiel.xhtml", 2);
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
		em.persist(new AdminPrivilege("REFER_002_006", "Lister les domaine de compétence ", adminModule));
		// Competénce
		em.persist(new AdminPrivilege("REFER_003_001", "Créer une compétence ", adminModule));
		em.persist(new AdminPrivilege("REFER_003_002", "Modifier une compétence ", adminModule));
		em.persist(new AdminPrivilege("REFER_003_003", "Supprimer une compétence ", adminModule));
		em.persist(new AdminPrivilege("REFER_003_004", "Rechercher une compétence ", adminModule));
		em.persist(new AdminPrivilege("REFER_003_005", "Consulter une compétence ", adminModule));
		em.persist(new AdminPrivilege("REFER_003_006", "Lister les compétences ", adminModule));
		// Tache
		em.persist(new AdminPrivilege("REFER_004_001", "Créer une tâche ", adminModule));
		em.persist(new AdminPrivilege("REFER_004_002", "Modifier une tâche ", adminModule));
		em.persist(new AdminPrivilege("REFER_004_003", "Supprimer une tâche ", adminModule));
		em.persist(new AdminPrivilege("REFER_004_004", "Rechercher une tâche ", adminModule));
		em.persist(new AdminPrivilege("REFER_004_005", "Consulter une tâche ", adminModule));
		em.persist(new AdminPrivilege("REFER_004_006", "Lister les tâches ", adminModule));
		// Activité
		em.persist(new AdminPrivilege("REFER_005_001", "Créer une activité ", adminModule));
		em.persist(new AdminPrivilege("REFER_005_002", "Modifier une activité ", adminModule));
		em.persist(new AdminPrivilege("REFER_005_003", "Supprimer une activité ", adminModule));
		em.persist(new AdminPrivilege("REFER_005_004", "Rechercher une activité ", adminModule));
		em.persist(new AdminPrivilege("REFER_005_005", "Consulter une activité ", adminModule));
		em.persist(new AdminPrivilege("REFER_005_006", "Lister les activités ", adminModule));
		// Mission
		em.persist(new AdminPrivilege("REFER_006_001", "Créer une mission ", adminModule));
		em.persist(new AdminPrivilege("REFER_006_002", "Modifier une mission ", adminModule));
		em.persist(new AdminPrivilege("REFER_006_003", "Supprimer une mission ", adminModule));
		em.persist(new AdminPrivilege("REFER_006_004", "Rechercher une mission ", adminModule));
		em.persist(new AdminPrivilege("REFER_006_005", "Consulter une mission ", adminModule));
		em.persist(new AdminPrivilege("REFER_006_006", "Lister les mission ", adminModule));
		// Emploi
		em.persist(new AdminPrivilege("REFER_007_001", "Créer un emploi ", adminModule));
		em.persist(new AdminPrivilege("REFER_007_002", "Modifier un emploi ", adminModule));
		em.persist(new AdminPrivilege("REFER_007_003", "Supprimer un emploi ", adminModule));
		em.persist(new AdminPrivilege("REFER_007_004", "Rechercher un emploi ", adminModule));
		em.persist(new AdminPrivilege("REFER_007_005", "Consulter un emploi ", adminModule));
		em.persist(new AdminPrivilege("REFER_007_006", "Lister les emplois ", adminModule));
		// Poste
		em.persist(new AdminPrivilege("REFER_008_001", "Créer un poste ", adminModule));
		em.persist(new AdminPrivilege("REFER_008_002", "Modifier un poste ", adminModule));
		em.persist(new AdminPrivilege("REFER_008_003", "Supprimer un poste ", adminModule));
		em.persist(new AdminPrivilege("REFER_008_004", "Rechercher un poste ", adminModule));
		em.persist(new AdminPrivilege("REFER_008_005", "Consulter un poste ", adminModule));
		em.persist(new AdminPrivilege("REFER_008_006", "Lister les postes ", adminModule));
		em.flush();
		// Fin Module du referentiel
		// Gestionnaire des employés
		adminModule = new AdminModule("GESEMP", "Gestionnaire Employés", "/pages/gesemp/indexGesemp.xhtml", 3);
		em.persist(adminModule);
                em.flush();
		em.persist(new AdminPrivilege("GESEMP_001_001", "Céer un employé ", adminModule));
                em.persist(new AdminPrivilege("GESEMP_001_002", "Modifier un employé ", adminModule));
                em.persist(new AdminPrivilege("GESEMP_001_003", "Supprimer un employé ", adminModule));
                em.persist(new AdminPrivilege("GESEMP_001_004", "Rechercher un employé ", adminModule));
                em.persist(new AdminPrivilege("GESEMP_001_005", "Consulter un employé ", adminModule));
                em.persist(new AdminPrivilege("GESEMP_001_006", "Lister les employés ", adminModule));
		em.flush();
		// Fin du module gestionnaire des employés
		// DRH
		adminModule = new AdminModule("DRH", "Directeur RH", "/pages/drh/indexDrh.xhtml", 4);
		em.persist(adminModule);
                em.flush();
		// Employé
		em.persist(new AdminPrivilege("DRH_001_004", "Rechercher un employé ", adminModule));
                em.persist(new AdminPrivilege("DRH_001_005", "Consulter un employé ", adminModule));
                em.persist(new AdminPrivilege("DRH_001_006", "Lister les employés ", adminModule));
		// Poste
		em.persist(new AdminPrivilege("DRH_002_004", "Rechercher un poste ", adminModule));
                em.persist(new AdminPrivilege("DRH_002_005", "Consulter un poste ", adminModule));
                em.persist(new AdminPrivilege("DRH_002_006", "Lister les postes ", adminModule));
		// Compagne
		em.persist(new AdminPrivilege("DRH_003_001", "Créer compagne d évaluation", adminModule));
                em.persist(new AdminPrivilege("DRH_003_002", "Modifier compagne d évaluation ", adminModule));
                em.persist(new AdminPrivilege("DRH_003_003", "Supprimer une compagne d évaluation", adminModule));
		em.persist(new AdminPrivilege("DRH_003_004", "Rechercher une compagne d évaluation", adminModule));
                em.persist(new AdminPrivilege("DRH_003_005", "Consulter une compagne d évaluation", adminModule));
                em.persist(new AdminPrivilege("DRH_003_006", "Lister les compagnes d évaluations", adminModule));
		// Reporting
	
                em.persist(new AdminPrivilege("DRH_004_005", "Consulter les reportings ", adminModule));
		em.flush();
		// Fin du module DRH
		// Employé
		adminModule = new AdminModule("EMP", "Employé", "/pages/emp/indexEmp.xhtml", 5);
		em.persist(adminModule);
                 em.flush();
		// Poste
		em.persist(new AdminPrivilege("EMP_001_005", "Consulter sa fiche de poste ", adminModule));
		// Employé
		em.persist(new AdminPrivilege("EMP_002_005", "Consulter sa fiche employé ", adminModule));
		// Evaluation
		em.persist(new AdminPrivilege("EMP_003_001", "Créer une auto évaluation ", adminModule));
                em.persist(new AdminPrivilege("EMP_003_002", "Metre a jour une  évaluation ", adminModule));
                em.persist(new AdminPrivilege("EMP_003_003", "Supprimer une auto évaluation non validé ", adminModule));
            	em.persist(new AdminPrivilege("EMP_003_004", "Rechercher une  évaluation ", adminModule));
                em.persist(new AdminPrivilege("EMP_003_005", "Consulter une évaluation ", adminModule));
                em.persist(new AdminPrivilege("EMP_003_006", "Lister les évaluations ", adminModule));
		em.flush();
        // Initialisation Autres modules
        // Utiliser le meme principe
//-------------------------------------------------------------------------------------------------------------------
 // Initialiser les types de compétence 

                em.persist(new Typecompetence("TYPE_COMP_001", "SAVOIR", "Les connaissances théoriques acquises pendant le parcours scolaire et lors des différentes expériences professionnelle"));
                em.persist(new Typecompetence("TYPE_COMP_002", "SAVOIR-FAIRE", "ensemble de connaissances pratiques, la maîtrise d’un poste, d’un marché ou d’un produit spécifique"));
                em.persist(new Typecompetence("TYPE_COMP_003", "SAVOIR-ÊTRE PROFESSIONNEL", "représente un ensemble de manières d’agir et de capacités relationnelles utiles pour interagir dans un contexte professionnel"));

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
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
