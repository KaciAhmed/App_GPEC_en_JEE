package dz.elit.gpecpf.referentiel.controller;

import dz.elit.gpecpf.administration.service.AdminPrefixCodificationFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.poste.entity.Mission;
import dz.elit.gpecpf.poste.entity.Activite;
import dz.elit.gpecpf.poste.entity.Poste;
import dz.elit.gpecpf.poste.service.MissionFacade;
import dz.elit.gpecpf.poste.service.ActiviteFacade;
import dz.elit.gpecpf.poste.service.PosteFacade;
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
public class AddMissionController extends AbstractController implements Serializable {

	@EJB
	private MissionFacade missionFacade;
	@EJB
	private ActiviteFacade activiteFacade;
	@EJB
	private PosteFacade posteFacade;
	@EJB
	private AdminPrefixCodificationFacade prefixFacade;

	private Mission mission;

	private List<Activite> listActivites;
	private List<Activite> listActivitesSelected;

	private List<Poste> listPostes;
	private List<Poste> listPostesMission;
	private List<Poste> listPostesSelected;

	private String code;
	private String libelle;
	private String description;

	private String codePrefix;

	public AddMissionController() {
	}

	@Override//@PostConstruct
	protected void initController() {
		initAddMission();
	}

	public void create() {
		try {
			checkCode();
			missionFacade.create(mission);
			posteFacade.editMission(mission, listPostesMission, new ArrayList());
			MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));
			initAddMission();
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
			if (!mission.getCode().startsWith(codePrefix) || mission.getCode().equals(codePrefix)) {
				throw new MyException("Le code doit commencer avec le prefix: " + codePrefix + " et suivi d'une chaine.");
			}
		}
	}

	private void initAddMission() {
		mission = new Mission();
		try {
			codePrefix = prefixFacade.chercherPrefix().getMiss();
		} catch (Exception e) {
			codePrefix = "";
		}
		mission.setCode(codePrefix);
		listActivites = new ArrayList();
		listActivitesSelected = new ArrayList<>();
		listActivites = activiteFacade.findAllOrderByAttribut("code");
		listPostes = new ArrayList();
		listPostes = posteFacade.findAllOrderByAttribut("code");
		listPostesSelected = new ArrayList<>();
		listPostesMission = new ArrayList();
	}

	public void addActivitesForMission() {
		if (!listActivitesSelected.isEmpty()) {
			mission.addListActivites(listActivitesSelected);
			listActivites.removeAll(listActivitesSelected);
			listActivitesSelected = new ArrayList<>();
		}
	}

	public void removeActiviteForMission(Activite activite) {
		mission.removeActivite(activite);
		listActivites.add(activite);
	}

	public void addPostesForMission() {
		if (!listPostesSelected.isEmpty()) {
			listPostesMission.addAll(listPostesSelected);
			listPostes.removeAll(listPostesSelected);
			listPostesSelected = new ArrayList<>();
		}
	}

	public void removePosteForMission(Poste poste) {
		listPostesMission.remove(poste);
		listPostes.add(poste);
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

	public Mission getMission() {
		return mission;
	}

	public MissionFacade getMissionFacade() {
		return missionFacade;
	}

	public ActiviteFacade getActiviteFacade() {
		return activiteFacade;
	}

	public List<Activite> getListActivites() {
		return listActivites;
	}

	public List<Activite> getListActivitesSelected() {
		return listActivitesSelected;
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

	public void setMission(Mission mission) {
		this.mission = mission;
	}

	public void setMissionFacade(MissionFacade missionFacade) {
		this.missionFacade = missionFacade;
	}

	public void setActiviteFacade(ActiviteFacade activiteFacade) {
		this.activiteFacade = activiteFacade;
	}

	public void setListActivites(List<Activite> listActivites) {
		this.listActivites = listActivites;
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

	public PosteFacade getPosteFacade() {
		return posteFacade;
	}

	public void setPosteFacade(PosteFacade posteFacade) {
		this.posteFacade = posteFacade;
	}

	public List<Poste> getListPostes() {
		return listPostes;
	}

	public void setListPostes(List<Poste> listPostes) {
		this.listPostes = listPostes;
	}

	public List<Poste> getListPostesMission() {
		return listPostesMission;
	}

	public void setListPostesMission(List<Poste> listPostesMission) {
		this.listPostesMission = listPostesMission;
	}

	public List<Poste> getListPostesSelected() {
		return listPostesSelected;
	}

	public void setListPostesSelected(List<Poste> listPostesSelected) {
		this.listPostesSelected = listPostesSelected;
	}

}
