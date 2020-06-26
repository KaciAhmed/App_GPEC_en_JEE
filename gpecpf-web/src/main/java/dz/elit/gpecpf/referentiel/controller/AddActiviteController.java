package dz.elit.gpecpf.referentiel.controller;

import dz.elit.gpecpf.administration.service.AdminPrefixCodificationFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.poste.entity.Activite;
import dz.elit.gpecpf.poste.entity.Mission;
import dz.elit.gpecpf.poste.entity.Tache;
import dz.elit.gpecpf.poste.service.ActiviteFacade;
import dz.elit.gpecpf.poste.service.MissionFacade;
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
public class AddActiviteController extends AbstractController implements Serializable {

	@EJB
	private ActiviteFacade activiteFacade;
	@EJB
	private TacheFacade tacheFacade;
	@EJB
	private MissionFacade missionFacade;
	@EJB
	private AdminPrefixCodificationFacade prefixFacade;

	private Activite activite;

	private List<Tache> listTaches;
	private List<Tache> listTachesSelected;

	private List<Mission> listMissions;
	private List<Mission> listMissionsActivite;
	private List<Mission> listMissionsSelected;

	private String code;
	private String libelle;
	private String description;

	private String codePrefix;

	public AddActiviteController() {
	}

	@Override//@PostConstruct
	protected void initController() {
		initAddActivite();
	}

	public void create() {
		try {
			checkCode();
			activiteFacade.create(activite);
			missionFacade.editActivite(activite, listMissionsActivite, new ArrayList());
			MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));
			initAddActivite();
		} catch (MyException ex) {
			ex.printStackTrace();
			MyUtil.addErrorMessage(ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
		}
	}

	private void initAddActivite() {
		try {
			codePrefix = prefixFacade.chercherPrefix().getAct();
		} catch (Exception e) {
			codePrefix = "";
		}
		activite = new Activite();
		activite.setCode(codePrefix);
		listTaches = new ArrayList();
		listTachesSelected = new ArrayList<>();
		listTaches = tacheFacade.findAllOrderByAttribut("code");
		listMissions = new ArrayList();
		listMissionsSelected = new ArrayList<>();
		listMissions = missionFacade.findAllOrderByAttribut("code");
		listMissionsActivite = new ArrayList();
	}

	public void checkCode() throws MyException {
		if (!codePrefix.equals("")) {
			if (!activite.getCode().startsWith(codePrefix) || activite.getCode().equals(codePrefix)) {
				throw new MyException("Le code doit commencer avec le prefix: " + codePrefix + " et suivi d'une chaine.");
			}
		}
	}

	public void addTachesForActivite() {
		if (!listTachesSelected.isEmpty()) {
			activite.addListTaches(listTachesSelected);
			listTaches.removeAll(listTachesSelected);
			listTachesSelected = new ArrayList<>();
		}
	}

	public void removeTacheForActivite(Tache tache) {
		activite.removeTache(tache);
		listTaches.add(tache);
	}

	public void addMissionsForActivite() {
		if (!listMissionsSelected.isEmpty()) {
			listMissionsActivite.addAll(listMissionsSelected);
			listMissions.removeAll(listMissionsSelected);
			listMissionsSelected = new ArrayList<>();
		}
	}

	public void removeMissionForActivite(Mission mission) {
		listMissionsActivite.remove(mission);
		listMissions.add(mission);
	}

	public String getCode() {
		return code;
	}

	public String getLibelle() {
		return libelle;
	}

	public String getDescription() {
		return description;
	}

	public Activite getActivite() {
		return activite;
	}

	public ActiviteFacade getActiviteFacade() {
		return activiteFacade;
	}

	public TacheFacade getTacheFacade() {
		return tacheFacade;
	}

	public List<Tache> getListTaches() {
		return listTaches;
	}

	public List<Tache> getListTachesSelected() {
		return listTachesSelected;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setActivite(Activite activite) {
		this.activite = activite;
	}

	public void setActiviteFacade(ActiviteFacade activiteFacade) {
		this.activiteFacade = activiteFacade;
	}

	public void setTacheFacade(TacheFacade tacheFacade) {
		this.tacheFacade = tacheFacade;
	}

	public void setListTaches(List<Tache> listTaches) {
		this.listTaches = listTaches;
	}

	public void setListTachesSelected(List<Tache> listTachesSelected) {
		this.listTachesSelected = listTachesSelected;
	}

	public MissionFacade getMissionFacade() {
		return missionFacade;
	}

	public void setMissionFacade(MissionFacade missionFacade) {
		this.missionFacade = missionFacade;
	}

	public List<Mission> getListMissions() {
		return listMissions;
	}

	public void setListMissions(List<Mission> listMissions) {
		this.listMissions = listMissions;
	}

	public List<Mission> getListMissionsSelected() {
		return listMissionsSelected;
	}

	public void setListMissionsSelected(List<Mission> listMissionsSelected) {
		this.listMissionsSelected = listMissionsSelected;
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

	public List<Mission> getListMissionsActivite() {
		return listMissionsActivite;
	}

	public void setListMissionsActivite(List<Mission> listMissionsActivite) {
		this.listMissionsActivite = listMissionsActivite;
	}

}
