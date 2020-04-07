/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */ 
package dz.elit.gpecpf.administration.service;

import dz.elit.gpecpf.administration.entity.AdminDroitVisibilite;
import dz.elit.gpecpf.administration.entity.AdminGroupe;
import dz.elit.gpecpf.administration.entity.AdminUtilisateur;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.StaticUtil;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author laidani.youcef
 */ 
@Stateless
public class AdminGroupeFacade extends AbstractFacade<AdminGroupe> {

    @EJB
    private AdminDroitVisibiliteFacade adminDroitVisibiliteFacade;

    @PersistenceContext(unitName = StaticUtil.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AdminGroupeFacade() {
        super(AdminGroupe.class);
    }
     // créer un groupe
    public void creer(AdminGroupe groupe) throws MyException, Exception {

        try {
            if (isLibelleGroupeExiste(groupe)) {
                throw new MyException("Le libelle existe déjà");
            }
                // créer le groupe dans la bdd
            this.create(groupe);
            /* synchroniser l'état des entité utilisateur avec 
                les modification éfféctuer sur la bdd*/ 
            for (int i = 0; i < groupe.getListMembre().size(); i++) {
                em.refresh(em.find(AdminUtilisateur.class, groupe.getListMembre().get(i).getId()));
            }

        } catch (Exception e) {
            System.out.println("Exception = " + e);
        }

    }
  // vu
    private boolean isLibelleGroupeExiste(AdminGroupe groupe) {
        Query q = em.createNamedQuery("AdminGroupe.findByLibelleWithoutCurrentId");
        q.setParameter("libelle", groupe.getLibelle());
        q.setParameter("id", groupe.getId());
        return !q.getResultList().isEmpty();
    }
 // vu
    public void delete(AdminGroupe groupe) throws Exception {
        if (groupe.getAdminDroitVisibiliteList().isEmpty() && groupe.getListMembre().isEmpty()) {
            this.remove(groupe);
        } else {
            throw new MyException("Il y a des utilisateurs apartien a ce groupe, vous ne peuvez pas le supprimer");
        }
    }
 // vu
    public AdminGroupe findById(int idGroupe) {
        try {
            AdminGroupe groupe = (AdminGroupe) em.createNamedQuery("AdminGroupe.findById").setParameter("id", idGroupe).getSingleResult();
            return groupe;
        } catch (Exception e) {
            return null;
        }
    }

    public void modifier(AdminGroupe groupe) throws MyException, Exception {
        if (isLibelleGroupeExiste(groupe)) {
            throw new MyException("Le libelle existe déjà");
        }
        // chercher le groupe
        AdminGroupe grp = this.find(groupe.getId());
        // supprimer tout les droit de visibilité de ce goupe
        for (AdminDroitVisibilite dv : grp.getAdminDroitVisibiliteList()) {
            adminDroitVisibiliteFacade.remove(dv);
        }
        // maj le goupe dans la bdd
        this.edit(groupe);
        
           // maj les utilisateurs de ce goupe dans la bdd
        for (int i = 0; i < groupe.getListMembre().size(); i++) {
            em.refresh(em.find(AdminUtilisateur.class, groupe.getListMembre().get(i).getId()));
        }
    }

}
