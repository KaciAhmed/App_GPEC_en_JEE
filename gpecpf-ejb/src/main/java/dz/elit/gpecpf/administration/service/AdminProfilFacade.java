package dz.elit.gpecpf.administration.service;

import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.administration.entity.AdminProfil;
import dz.elit.gpecpf.administration.entity.AdminUtilisateur;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.StaticUtil;
import java.util.ArrayList;
import java.util.Arrays;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author ayadi
 */
@Stateless
public class AdminProfilFacade extends AbstractFacade<AdminProfil> {

    @PersistenceContext(unitName = StaticUtil.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
/*L'annotation @EJB permet de préciser les sessions beans que 
le container EJB va instancier et initialiser automatiquement.
*/
    @EJB
    private AdminUtilisateurFacade utilisateurFacade;

    public AdminProfilFacade() {
        super(AdminProfil.class);
    }

   /* créer un profille est mettre à jour 
     les utilisateurs qui posséde se profile
    */
    public void create(AdminProfil profil) throws MyException, Exception {
        if (isLibelleProfilExiste(profil)) {
            throw new MyException("Le libelle existe déjà");
        }
        super.create(profil);
        for (AdminUtilisateur utilisateur : profil.getListAdminUtilisateurs()) {
            utilisateurFacade.edit(utilisateur, new ArrayList<AdminProfil>());
        }

    }
   /* vérifier si le libellé d'un profile existe*/
    private boolean isLibelleProfilExiste(AdminProfil profil) {
        Query q = em.createNamedQuery("AdminProfil.findByLibelleWithoutCurrentId");
        q.setParameter("libelle", profil.getLibelle());
        q.setParameter("id", profil.getId());
        return !q.getResultList().isEmpty();
    }
     // ajouter le profile au utilisateur de ce profile 
    /* supprimer le profile de la liste des profiles des utilisateur dans la 
        liste utilisateursRemoved
    */
    // metre a jour la table du profil
    public void edit(AdminProfil profil, 
                            List<AdminUtilisateur> utilisateursRemoved)
                                      throws MyException, Exception 
    {
       /* if (isLibelleProfilExiste(profil)) {
            throw new MyException("Le libelle existe déjà");
        }*/
        /* ajouter le profile à tout les utilisateur de sa liste d'utilisateurs
           si le profile est modifier il va remplacer l'ancien dans la liste
            des utilisateur
           
        */
        for (AdminUtilisateur utilisateur : profil.getListAdminUtilisateurs()) {
            utilisateur.getListAdminProfil().add(profil);
           //  mettre a jour l'utilisateur dans la bdd
            utilisateurFacade.edit(utilisateur, new ArrayList<AdminProfil>());// edit(utilisateur);
        }
        // supprimer le profile des utilsateur de utilisateursRemoved
        for (AdminUtilisateur utilisateur : utilisateursRemoved) {
            utilisateur.getListAdminProfil().remove(profil);
            //  mettre a jour l'utilisateur dans la bdd
            utilisateurFacade.edit(utilisateur, new ArrayList<AdminProfil>());// edit(utilisateur);
        }
        //  mettre a jour le profile dans la bdd
        super.edit(profil);
    }
// supprimer un profile est mettre à un jour les utilisateurs qui le posséde
    public void remove(AdminProfil profil) throws MyException, Exception {
        if (isProfilAffectForUser(profil)) {
            throw new MyException("Il y a des utilisateurs ayant ce profil, vous ne peuvez pas le supprimer ");
        }
        for (AdminUtilisateur utilisateur : profil.getListAdminUtilisateurs()) {
            utilisateurFacade.edit(utilisateur, Arrays.asList(profil));// edit(utilisateur);
        }
        super.remove(profil);
    }

//    @Override
//    public void remove(AdminProfil profil) throws MyException, Exception {
//        if (isProfilAffectForUser(profil)) {
//            throw new MyException("Il y a des utilisateurs ayant ce profil, ne peut pas le supprimer ");
//        }
//        super.remove(profil);
//    }
    
   // vérifier si un profile est affécté
    private boolean isProfilAffectForUser(AdminProfil profil) {
        Query q = em.createNamedQuery("AdminUtilisateur.findByProfil");
        q.setParameter("profil", profil);
        List<AdminUtilisateur> list = q.getResultList();
        return !list.isEmpty() && list.size() >= 1;

    }
    // rechercher un profile via son id
    public AdminProfil findById(String id) {
        Query query = em.createNamedQuery("AdminProfil.findById");
        //query.setParameter("id", id);
        List<AdminProfil> list = query.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }


}
