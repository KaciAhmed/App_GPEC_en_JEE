package dz.elit.gpecpf.referentiel.controller;

import dz.elit.gpecpf.administration.entity.Prefixcodification;
import dz.elit.gpecpf.administration.service.AdminPrefixCodificationFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.poste.entity.Mission;
import dz.elit.gpecpf.poste.entity.Activite;
import dz.elit.gpecpf.poste.service.MissionFacade;
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
public class AddMissionController extends AbstractController implements Serializable {
    @EJB
    private MissionFacade missionFacade;
    @EJB
    private ActiviteFacade activiteFacade;
    @EJB
    private AdminPrefixCodificationFacade prefFacade;
   
    private  List<Prefixcodification> listPrefix;

    private Mission mission;
    
    private List<Activite> listActivites;
    private List<Activite> listActivitesSelected;
	
    private String code;
    private String libelle;
    private String description;

    public AddMissionController() {
    }

    @Override//@PostConstruct
    protected void initController() {
        initAddMission();
    }

    public void create() {
        try {
            missionFacade.create(mission);
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

    private void initAddMission() {
        mission = new Mission();
        chercherPrefix();
        listActivites = new ArrayList();
        listActivitesSelected = new ArrayList<>();
	listActivites = activiteFacade.findAllOrderByAttribut("code");
    }
    public void chercherPrefix()
    {   
        listPrefix =new ArrayList<>();
        listPrefix=prefFacade.findAllOrderByAttribut("id");
        if(!listPrefix.isEmpty())
        mission.setCode(listPrefix.get(0).getMiss());
    }
    public void addActivitesForMission() {
        if(!listActivitesSelected.isEmpty()) {
			mission.addListActivites(listActivitesSelected);
			listActivites.removeAll(listActivitesSelected);
            listActivitesSelected = new ArrayList<>();
        }
    }
	
	public void removeActiviteForMission(Activite activite) {
		mission.removeActivite(activite);
		listActivites.add(activite);
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
	
	

}
