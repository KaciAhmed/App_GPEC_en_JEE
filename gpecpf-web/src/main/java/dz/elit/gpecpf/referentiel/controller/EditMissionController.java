package dz.elit.gpecpf.referentiel.controller;

import dz.elit.gpecpf.administration.service.AdminPrefixCodificationFacade;
import dz.elit.gpecpf.poste.service.MissionFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.poste.entity.Mission;
import dz.elit.gpecpf.poste.entity.Activite;
import dz.elit.gpecpf.poste.entity.Poste;
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
public class EditMissionController extends AbstractController implements Serializable {

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
	private List<Poste> listPostesAdd;
	private List<Poste> listPostesDel;

	private String code;
	private String libelle;
	private String description;

	private String codePrefix;

	public EditMissionController() {
	}

	@Override//@PostConstruct
	protected void initController() {
		initAddMission();
		mission = new Mission();
		listActivites = activiteFacade.findAllOrderByAttribut("code");
		String id = MyUtil.getRequestParameter("id");
		if (id != null) {
			mission = missionFacade.find(Integer.parseInt(id));
			listActivites.removeAll(mission.getListActivites());
			listPostesMission = posteFacade.postesForMission(mission);
		}
		listPostes.removeAll(listPostesMission);
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

	public void edit() {
		try {
			checkCode();
			missionFacade.edit(mission);
			MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));
			listPostesAdd.removeAll(posteFacade.postesForMission(mission));
			posteFacade.editMission(mission, listPostesAdd, listPostesDel);
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

	public void addPostesForMission() {
		if (!listPostesSelected.isEmpty()) {
			listPostesMission.addAll(listPostesSelected);
			listPostes.removeAll(listPostesSelected);
			listPostesAdd.addAll(listPostesSelected);
			listPostesDel.removeAll(listPostesSelected);
			listPostesSelected = new ArrayList<>();
		}
	}

	public void removePosteForMission(Poste poste) {
		listPostesMission.remove(poste);
		listPostes.add(poste);
		listPostesAdd.remove(poste);
		listPostesDel.add(poste);
	}

	private void initAddMission() {
		try {
			codePrefix = prefixFacade.chercherPrefix().getMiss();
		} catch (Exception e) {
			codePrefix = "";
		}
		mission = new Mission();
		listActivites = new ArrayList();
		listActivitesSelected = new ArrayList<>();
		listPostes = new ArrayList();
		listPostes = posteFacade.findAllOrderByAttribut("code");
		listPostesSelected = new ArrayList<>();
		listPostesMission = new ArrayList();
		listPostesAdd = new ArrayList();
		listPostesDel = new ArrayList();
	}

	public Mission getMission() {
		return mission;
	}

	public void setMission(Mission mission) {
		this.mission = mission;
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

	public MissionFacade getMissionFacade() {
		return missionFacade;
	}

	public void setMissionFacade(MissionFacade missionFacade) {
		this.missionFacade = missionFacade;
	}

	public ActiviteFacade getActiviteFacade() {
		return activiteFacade;
	}

	public void setActiviteFacade(ActiviteFacade activiteFacade) {
		this.activiteFacade = activiteFacade;
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

	public List<Poste> getListPostesAdd() {
		return listPostesAdd;
	}

	public void setListPostesAdd(List<Poste> listPostesAdd) {
		this.listPostesAdd = listPostesAdd;
	}

	public List<Poste> getListPostesDel() {
		return listPostesDel;
	}

	public void setListPostesDel(List<Poste> listPostesDel) {
		this.listPostesDel = listPostesDel;
	}

}
