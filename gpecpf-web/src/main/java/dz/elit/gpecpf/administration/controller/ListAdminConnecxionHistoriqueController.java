package dz.elit.gpecpf.administration.controller;

import dz.elit.gpecpf.administration.entity.AdminConnecxionHistorique;
import dz.elit.gpecpf.administration.service.AdminConnecxionHistoriqueFacade;
import dz.elit.gpecpf.commun.util.AbstractController;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author leghettas.rabah & chekor samir
 */
@ManagedBean
@ViewScoped
public class ListAdminConnecxionHistoriqueController extends AbstractController implements Serializable {

	@EJB
	private AdminConnecxionHistoriqueFacade adminConnecxionHistoriqueFacade;

	private LazyDataModel<AdminConnecxionHistorique> lazyAdminConnecxionHistorique;

	//------------------- Les variables de recherche personnalisee -------------
	private String utilisateur;
	private String adresseIp;
	private Date dateConnexion;
	//--------------------------------------------------------------------------

	/**
	 * Creates a new instance of AdminConnecxionHistorique Controller
	 */
	public ListAdminConnecxionHistoriqueController() {
	}

	@Override
	protected void initController() {
		search();
	}

	public void search() {
		lazyAdminConnecxionHistorique = new LazyDataModel<AdminConnecxionHistorique>() {
			@Override
			public List<AdminConnecxionHistorique> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
				lazyAdminConnecxionHistorique.setRowCount(adminConnecxionHistoriqueFacade.count(utilisateur, adresseIp, dateConnexion));
				return adminConnecxionHistoriqueFacade.findByParams(first, pageSize, utilisateur, adresseIp, dateConnexion);
			}
		};
	}

	// Getter and setter
	public String getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(String utilisateur) {
		this.utilisateur = utilisateur;
	}

	public String getAdresseIp() {
		return adresseIp;
	}

	public void setAdresseIp(String adresseIp) {
		this.adresseIp = adresseIp;
	}

	public Date getDateConnexion() {
		return dateConnexion;
	}

	public void setDateConnexion(Date dateConnexion) {
		this.dateConnexion = dateConnexion;
	}

	public LazyDataModel<AdminConnecxionHistorique> getLazyAdminConnecxionHistorique() {
		return lazyAdminConnecxionHistorique;
	}

	public void setLazyAdminConnecxionHistorique(LazyDataModel<AdminConnecxionHistorique> lazyAdminConnecxionHistorique) {
		this.lazyAdminConnecxionHistorique = lazyAdminConnecxionHistorique;
	}
}
