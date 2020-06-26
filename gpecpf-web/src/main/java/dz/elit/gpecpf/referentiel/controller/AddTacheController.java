package dz.elit.gpecpf.referentiel.controller;

import dz.elit.gpecpf.administration.service.AdminPrefixCodificationFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.poste.entity.Activite;
import dz.elit.gpecpf.poste.entity.Tache;
import dz.elit.gpecpf.poste.service.ActiviteFacade;
import dz.elit.gpecpf.poste.service.TacheFacade;
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
public class AddTacheController extends AbstractController implements Serializable {

	@EJB
	private TacheFacade tacheFacade;
	@EJB
	private ActiviteFacade activiteFacade;
	@EJB
	private AdminPrefixCodificationFacade prefixFacade;

	private Tache tache;

	private String code;
	private String libelle;
	private String description;

	private List<Activite> listActivites;
	private List<Activite> listActivitesTache;
	private List<Activite> listActivitesSelected;

	private String codePrefix;

	public AddTacheController() {
	}

	@Override//@PostConstruct
	protected void initController() {
		initAddTache();
	}

	public void create() {
		try {
			checkCode();
			tacheFacade.create(tache);
			activiteFacade.editTache(tache, listActivitesTache, new ArrayList());
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

	public void checkCode() throws MyException {
		if (!codePrefix.equals("")) {
			if (!tache.getCode().startsWith(codePrefix) || tache.getCode().equals(codePrefix)) {
				throw new MyException("Le code doit commencer avec le prefix: " + codePrefix + " et suivi d'une chaine.");
			}
		}
	}

	private void initAddTache() {
		tache = new Tache();
		try {
			codePrefix = prefixFacade.chercherPrefix().getTache();
		} catch (Exception e) {
			codePrefix = "";
		}
		tache.setCode(codePrefix);
		listActivites = new ArrayList();
		listActivitesSelected = new ArrayList<>();
		listActivites = activiteFacade.findAllOrderByAttribut("code");
		listActivitesTache = new ArrayList();
	}

	public void addActivitesForTache() {
		if (!listActivitesSelected.isEmpty()) {
			listActivitesTache.addAll(listActivitesSelected);
			listActivites.removeAll(listActivitesSelected);
			listActivitesSelected = new ArrayList<>();
		}
	}

	public void removeActiviteForTache(Activite activite) {
		listActivitesTache.remove(activite);
		listActivites.add(activite);
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

	public void setListActivitesTache(List<Activite> listActivitesTache) {
		this.listActivitesTache = listActivitesTache;
	}

}
