package dz.elit.gpecpf.referentiel.controller;

import dz.elit.gpecpf.administration.service.AdminPrefixCodificationFacade;
import dz.elit.gpecpf.poste.service.TacheFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.poste.entity.Activite;
import dz.elit.gpecpf.poste.entity.Tache;
import dz.elit.gpecpf.poste.service.ActiviteFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Nadir Ben Mohand
 */
@ManagedBean
@ViewScoped
public class EditTacheController extends AbstractController implements Serializable {

	@EJB
	private TacheFacade tacheFacade;
	@EJB
	private ActiviteFacade activiteFacade;
	@EJB
	private AdminPrefixCodificationFacade prefixFacade;

	private Tache tache;

	private List<Activite> listActivites;
	private List<Activite> listActivitesTache;
	private List<Activite> listActivitesSelected;
	private List<Activite> listActivitesRemoved;
	private List<Activite> listActivitesAdded;

	private String code;
	private String libelle;
	private String description;

	private String codePrefix;

	public EditTacheController() {
	}

	@Override//@PostConstruct
	protected void initController() {
		try {
			codePrefix = prefixFacade.chercherPrefix().getTache();
		} catch (Exception e) {
			codePrefix = "";
		}
		initAddTache();
		String id = MyUtil.getRequestParameter("id");
		if (id != null) {
			tache = tacheFacade.find(Integer.parseInt(id));
			listActivitesTache = activiteFacade.activitesForTache(tache);
			listActivites.removeAll(listActivitesTache);
		}
	}

	public void addActivitesForTache() {
		if (!listActivitesSelected.isEmpty()) {
			listActivitesTache.addAll(listActivitesSelected);
			listActivites.removeAll(listActivitesSelected);
			listActivitesAdded.addAll(listActivitesSelected);
			listActivitesRemoved.removeAll(listActivitesSelected);
			listActivitesSelected = new ArrayList<>();
		}
	}

	public void removeActiviteForTache(Activite activite) {
		listActivitesTache.remove(activite);
		listActivites.add(activite);
		listActivitesRemoved.add(activite);
		listActivitesAdded.remove(activite);
	}

	public void checkCode() throws MyException {
		if (!codePrefix.equals("")) {
			if (!tache.getCode().startsWith(codePrefix) || tache.getCode().equals(codePrefix)) {
				throw new MyException("Le code doit commencer avec le prefix: " + codePrefix + " et suivi d'une chaine.");
			}
		}
	}

	public void edit() {
		try {
			checkCode();
			tacheFacade.edit(tache);
			listActivitesAdded.removeAll(activiteFacade.activitesForTache(tache));
			activiteFacade.editTache(tache, listActivitesAdded, listActivitesRemoved);
			MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));
			initAddTache();
		} catch (MyException ex) {
			ex.printStackTrace();
			MyUtil.addErrorMessage(ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
		}
	}

	private void initAddTache() {
		tache = new Tache();
		listActivites = new ArrayList();
		listActivites = activiteFacade.findAllOrderByAttribut("code");
		listActivitesTache = new ArrayList();
		listActivitesSelected = new ArrayList<>();
		listActivitesRemoved = new ArrayList();
		listActivitesAdded = new ArrayList();
	}

	public TacheFacade getTacheFacade() {
		return tacheFacade;
	}

	public void setTacheFacade(TacheFacade tacheFacade) {
		this.tacheFacade = tacheFacade;
	}

	public ActiviteFacade getActiviteFacade() {
		return activiteFacade;
	}

	public void setActiviteFacade(ActiviteFacade activiteFacade) {
		this.activiteFacade = activiteFacade;
	}

	public Tache getTache() {
		return tache;
	}

	public void setTache(Tache tache) {
		this.tache = tache;
	}

	public List<Activite> getListActivites() {
		return listActivites;
	}

	public void setListActivites(List<Activite> listActivites) {
		this.listActivites = listActivites;
	}

	public List<Activite> getListActivitesSelected() {
		return listActivitesSelected;
	}

	public void setListActivitesSelected(List<Activite> listActivitesSelected) {
		this.listActivitesSelected = listActivitesSelected;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Activite> getListActivitesRemoved() {
		return listActivitesRemoved;
	}

	public void setListActivitesRemoved(List<Activite> listActivitesRemoved) {
		this.listActivitesRemoved = listActivitesRemoved;
	}

	public List<Activite> getListActivitesAdded() {
		return listActivitesAdded;
	}

	public void setListActivitesAdded(List<Activite> listActivitesAdded) {
		this.listActivitesAdded = listActivitesAdded;
	}

	public AdminPrefixCodificationFacade getPrefixFacade() {
		return prefixFacade;
	}

	public void setPrefixFacade(AdminPrefixCodificationFacade prefixFacade) {
		this.prefixFacade = prefixFacade;
	}

	public String getCodePrefix() {
		return codePrefix;
	}

	public void setCodePrefix(String codePrefix) {
		this.codePrefix = codePrefix;
	}

	public List<Activite> getListActivitesTache() {
		return listActivitesTache;
	}

	public void setListActivitesTache(List<Activite> listeActivitesTache) {
		this.listActivitesTache = listeActivitesTache;
	}

}
