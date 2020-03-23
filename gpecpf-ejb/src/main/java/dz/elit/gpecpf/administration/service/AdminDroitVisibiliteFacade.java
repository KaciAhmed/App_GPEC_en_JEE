/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.administration.service;

import dz.elit.gpecpf.administration.entity.AdminDroitVisibilite;
import dz.elit.gpecpf.administration.entity.AdminGroupe;
import dz.elit.gpecpf.administration.entity.AdminObjetVisibilite;
import dz.elit.gpecpf.administration.entity.AdminUtilisateur;
import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.StaticUtil;
import java.util.List;
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
public class AdminDroitVisibiliteFacade extends AbstractFacade<AdminDroitVisibilite> {

    @EJB
    private AdminUtilisateurFacade adminUtilisateurFacade;

    @EJB
    private AdminGroupeFacade adminGroupeFacade;

    @PersistenceContext(unitName = StaticUtil.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AdminDroitVisibiliteFacade() {
        super(AdminDroitVisibilite.class);
    }

    public void createListeDroit(AdminUtilisateur utilisateur, AdminObjetVisibilite entity) throws Exception {
        if (utilisateur != null) {

            List<AdminDroitVisibilite> listeDroi = utilisateur.getAdminDroitVisibiliteList();

            removeListeDroit(this.findByUtilisateur(utilisateur, entity));

            for (AdminDroitVisibilite droit : listeDroi) {
                this.create(droit);
            }

            utilisateur.setAdminDroitVisibiliteList(findByUtilisateur(utilisateur));
            adminUtilisateurFacade.edit(utilisateur);

        }
    }

    public void createListeDroitGroupe(AdminGroupe groupe, AdminObjetVisibilite entity) throws Exception {
        if (groupe != null) {

            removeListeDroit(this.findByGroupe(groupe, entity));

            for (AdminDroitVisibilite droit : groupe.getAdminDroitVisibiliteList()) {
                this.create(droit);
            }

            groupe.setAdminDroitVisibiliteList(findByGroupe(groupe));
            adminGroupeFacade.edit(groupe);

        }
    }

    public void removeListeDroit(List<AdminDroitVisibilite> listeDroit) throws Exception {
        for (AdminDroitVisibilite droit : listeDroit) {
            remove(droit);
        }
    }

    private List<AdminDroitVisibilite> findByUtilisateur(AdminUtilisateur utilisateur, AdminObjetVisibilite entity) {
        Query query = em.createNamedQuery("AdminDroitVisibilite.findByUtilisateurEntity");
        query.setParameter("utilisateur", utilisateur.getId()).setParameter("entity", entity.getId());
        List<AdminDroitVisibilite> listeDV = query.getResultList();
        return listeDV;
    }

    public List<AdminDroitVisibilite> findByUtilisateur(AdminUtilisateur utilisateur) {
        Query query = em.createNamedQuery("AdminDroitVisibilite.findByUtilisateur");
        query.setParameter("utilisateur", utilisateur.getId());
        List<AdminDroitVisibilite> listeDV = query.getResultList();
        return listeDV;
    }

    private List<AdminDroitVisibilite> findByGroupe(AdminGroupe groupe, AdminObjetVisibilite entity) {
        Query query = em.createNamedQuery("AdminDroitVisibilite.findByGroupeEntity");
        query.setParameter("groupe", groupe.getId()).setParameter("entity", entity.getId());
        List<AdminDroitVisibilite> listeDV = query.getResultList();
        return listeDV;
    }

    private List<AdminDroitVisibilite> findByGroupe(AdminGroupe groupe) {
        Query query = em.createNamedQuery("AdminDroitVisibilite.findByGroupe");
        query.setParameter("groupe", groupe.getId());
        List<AdminDroitVisibilite> listeDV = query.getResultList();
        return listeDV;
    }
}
