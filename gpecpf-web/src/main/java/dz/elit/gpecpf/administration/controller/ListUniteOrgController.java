package dz.elit.gpecpf.administration.controller;

import dz.elit.gpecpf.administration.entity.AdminUniteOrganisationnelle;
import dz.elit.gpecpf.administration.service.AdminUniteOrganisationnelleFacade;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author chekor.samir
 */
@ManagedBean
@ViewScoped
public class ListUniteOrgController extends AbstractController implements Serializable {

	@EJB
	private AdminUniteOrganisationnelleFacade societeFacade;

	private AdminUniteOrganisationnelle societe;

	private List<AdminUniteOrganisationnelle> listUniteOrgs;

	public ListUniteOrgController() {
	}

	@Override
	protected void initController() {
		societe = new AdminUniteOrganisationnelle();
		listUniteOrgs = societeFacade.findAllOrderByTrie();
	}

	public void remove(AdminUniteOrganisationnelle uniteOrganisationnelle) {
		try {
			if (!uniteOrganisationnelle.getAdminUniteOrganisationnelleList().isEmpty()) {
				MyUtil.addWarnMessage(MyUtil.getBundleCommun("msg_vous_pouvez_pas_supprimer_cette_unite"));
			} else {
				if (uniteOrganisationnelle.getUniteParent() != null) {
					uniteOrganisationnelle.getUniteParent().getAdminUniteOrganisationnelleList().remove(uniteOrganisationnelle);
					societeFacade.edit(uniteOrganisationnelle.getUniteParent());
				}
				societeFacade.remove(uniteOrganisationnelle);
				listUniteOrgs = societeFacade.findAllOrderByTrie();
				MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//"Unité organisationnelle  supprimé");
			}

		} catch (Exception e) {
			e.printStackTrace();
			MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
		}
	}

	// Getter and setter
	public AdminUniteOrganisationnelle getSociete() {
		return societe;
	}

	public void setSociete(AdminUniteOrganisationnelle societe) {
		this.societe = societe;
	}

	public List<AdminUniteOrganisationnelle> getListUniteOrgs() {
		return listUniteOrgs;
	}

	public void setListUniteOrgs(List<AdminUniteOrganisationnelle> listUniteOrgs) {
		this.listUniteOrgs = listUniteOrgs;
	}
}
