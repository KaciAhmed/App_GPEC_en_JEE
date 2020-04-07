/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.administration.service;
import dz.elit.gpecpf.administration.entity.AdminProfil;
import dz.elit.gpecpf.administration.entity.AdminUtilisateur;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.CustomQueryRedirectors;
import dz.elit.gpecpf.commun.util.StaticUtil;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.eclipse.persistence.jpa.JpaHelper;

/**
 *
 * @author ayadi
 */
@Stateless
public class AdminUtilisateurFacade extends AbstractFacade<AdminUtilisateur> {

    @PersistenceContext(unitName = StaticUtil.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
/* L'annotation @EJB permet de préciser les sessions beans que 
le container EJB va instancier et initialiser automatiquement.*/
    @EJB
    private AdminProfilFacade profilFacade;

    public AdminUtilisateurFacade() {
        super(AdminUtilisateur.class);
    }
// récupérer l'utilisateurs du login passé en parametre
    public AdminUtilisateur findByLogin(String login) {
        Query query = em.createNamedQuery("AdminUtilisateur.findByLogin");
        query.setParameter("login", login);
        List<AdminUtilisateur> list = query.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
// récupérer les utilisateurs qui ont le nom, prénom et login passé en parametre
    public List<AdminUtilisateur> findByNomPrenomLogin(String nom, String prenom, String login) {
        StringBuilder queryStringBuilder = new StringBuilder("SELECT a FROM AdminUtilisateur AS a WHERE 1=1 ");
        if (nom != null && !nom.equals("")) {
            queryStringBuilder.append(" AND  a.nom like :nom ");
        }
        if (prenom != null && !prenom.equals("")) {
            queryStringBuilder.append(" AND  a.prenom like :prenom ");
        }
        if (login != null && !login.equals("")) {
            queryStringBuilder.append(" AND  a.login like :login ");
        }
        queryStringBuilder.append(" ORDER BY a.login ");

        Query q = em.createQuery(queryStringBuilder.toString());

        if (nom != null && !nom.equals("")) {
            q.setParameter("nom","%"+ nom + "%");
        }
        if (prenom != null && !prenom.equals("")) {
            q.setParameter("prenom","%"+ prenom + "%");
        }
        if (login != null && !login.equals("")) {
            q.setParameter("login","%"+ login + "%");
        }

        //Implémentation de visibilité
        JpaHelper.getDatabaseQuery(q).setRedirector(new CustomQueryRedirectors());

        return q.getResultList();
    }
// vérifier si un login éxiste
    private boolean isExisteLogin(String login) {
        AdminUtilisateur utilisateur = findByLogin(login);
        if (utilisateur == null) {
            return false;
        } else {
            return true;
        }
    }

   
    public void create(AdminUtilisateur utilisateur) throws MyException, Exception {
        if (isExisteLogin(utilisateur.getLogin())) {
            throw new MyException("Le login " + utilisateur.getLogin() + " existe déjà ");
        } else {
            super.create(utilisateur);
            for (AdminProfil profil : utilisateur.getListAdminProfil()) {
                profilFacade.edit(profil);
            }
        }
    }

    public void edit(AdminUtilisateur utilisateur, 
            List<AdminProfil> profilsRomoved) throws MyException, Exception {
        {
            /* mise ajour de la liste des profile d'un utilisateur dans la bdd
               c'est utile quand on appéle avec profilsRomoved=null
             */
            for (AdminProfil profil : utilisateur.getListAdminProfil()) {
                profilFacade.edit(profil);
            }
            /* supprimer l'utilisateur de la list des utilisateurs des profile
            passé en parametre et mettre à jour ces profiles dans la bdd*/  
            for (AdminProfil profilRemoved : profilsRomoved) {
                profilRemoved.getListAdminUtilisateurs().remove(utilisateur);
                profilFacade.edit(profilRemoved);
            }
            // mettre à jour l'utilisateur dans la bdd
            super.edit(utilisateur);
        }
    }

    /* supprimer un utilisateur est le supprimer de la liste de ces profille*/
    public void remove(AdminUtilisateur utilisateur) throws Exception {
        for (AdminProfil profil : utilisateur.getListAdminProfil()) {
            profil.getListAdminUtilisateurs().remove(utilisateur);
            profilFacade.edit(profil);
        }
        super.remove(utilisateur);

    }

    public List<AdminUtilisateur> findAllOrderByUnite() {
        Query query = em.createNamedQuery("AdminUtilisateur.findAll");
        JpaHelper.getDatabaseQuery(query).setRedirector(new CustomQueryRedirectors());
        //query.setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH);
        return query.getResultList();
    }
}
