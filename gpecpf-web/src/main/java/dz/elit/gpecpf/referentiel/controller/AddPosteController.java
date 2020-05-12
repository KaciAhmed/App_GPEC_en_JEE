package dz.elit.gpecpf.referentiel.controller;

import dz.elit.gpecpf.administration.entity.AdminUniteOrganisationnelle;
import dz.elit.gpecpf.administration.service.AdminUniteOrganisationnelleFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.competence.entity.Competence;
import dz.elit.gpecpf.competence.service.CompetenceFacade;
import dz.elit.gpecpf.poste.entity.Condition;
import dz.elit.gpecpf.poste.entity.Emploi;
import dz.elit.gpecpf.poste.entity.Formation;
import dz.elit.gpecpf.poste.entity.Moyen;
import dz.elit.gpecpf.poste.entity.Mission;
import dz.elit.gpecpf.poste.entity.Poste;
import dz.elit.gpecpf.poste.entity.TypePoste;
import dz.elit.gpecpf.poste.service.ConditionFacade;
import dz.elit.gpecpf.poste.service.EmploiFacade;
import dz.elit.gpecpf.poste.service.FormationFacade;
import dz.elit.gpecpf.poste.service.MissionFacade;
import dz.elit.gpecpf.poste.service.MoyenFacade;
import dz.elit.gpecpf.poste.service.PosteFacade;
import dz.elit.gpecpf.poste.service.TypePosteFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

/**
 *
 * @author Nadir Ben Mohand
 */
@ManagedBean
@ViewScoped
public class AddPosteController extends AbstractController implements Serializable {
    @EJB
	private PosteFacade posteFacade;
	@EJB
    private AdminUniteOrganisationnelleFacade uniteOrganisationnelleFacade;
	@EJB
	private TypePosteFacade typePosteFacade;
	@EJB
	private EmploiFacade emploiFacade;
	@EJB
	private MissionFacade missionFacade;
	@EJB
	private ConditionFacade conditionFacade;
	@EJB
	private MoyenFacade moyenFacade;
	@EJB
	private FormationFacade formationFacade;
	@EJB
	private CompetenceFacade competenceFacade;

	private Poste poste;
	
	private List<AdminUniteOrganisationnelle> listUniteOrganisationnelle;
    private AdminUniteOrganisationnelle uniteOrganisationnelleSelected;
	
	private List<TypePoste> listTypePoste;
	private TypePoste typePosteSelected;
	
	private List<Emploi> listEmploi;
	private Emploi emploiSelected;
	
	private List<Poste> listPoste;
	private Poste posteSelected;
	
	private List<Mission> listMissions;
	private List<Mission> listMissionsSelected;
	
	private List<Condition> listConditions;
	private List<Condition> listConditionsSelected;
	
	private List<Moyen> listMoyens;
	private List<Moyen> listMoyensSelected;
	
	private List<Formation> listFormations;
	private List<Formation> listFormationsSelected;
	
	private List<Competence> listCompetences;
	private List<Competence> listCompetencesSelected;
	
	private SelectItem[] typeOptions = new SelectItem[3];
	private SelectItem[] exigenceOptions = new SelectItem[3];
	
    private String code;
    private String libelle;
    private String description;

    public AddPosteController() {
    }

    @Override//@PostConstruct
    protected void initController() {
		typeOptions[0] = new SelectItem("", "");
		typeOptions[1] = new SelectItem("Académique", "Académique");
		typeOptions[2] = new SelectItem("Professionnel", "Professionnel");
		exigenceOptions[0] = new SelectItem("", "");
		exigenceOptions[1] = new SelectItem("Obligatoire", "Obligatoire");
		exigenceOptions[2] = new SelectItem("Supplémentaire", "Supplémentaire");
        initAddPoste();
    }

    public void create() {
        try {
			if (uniteOrganisationnelleSelected != null && uniteOrganisationnelleSelected.getId() != null) {
				poste.setAdminUniteOrganisationnelle(uniteOrganisationnelleSelected);
			}
			if (typePosteSelected != null && typePosteSelected.getId() != null) {
				poste.setTypePoste(typePosteSelected);
			}
			if (emploiSelected != null && emploiSelected.getId() != null) {
				poste.setEmploi(emploiSelected);
			}
			if (posteSelected != null  && posteSelected.getId() != null) {
				poste.setPosteSuperieur(posteSelected);
			}
			poste.setDateCreation(new Date());
            posteFacade.create(poste);
            MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));
            initAddPoste();
        } catch (MyException ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
        }
    }

    private void initAddPoste() {
		poste = new Poste();
		listUniteOrganisationnelle = uniteOrganisationnelleFacade.findAllOrderByAttribut("id");
		uniteOrganisationnelleSelected = new AdminUniteOrganisationnelle();
		listTypePoste = typePosteFacade.findAllOrderByAttribut("id");
		typePosteSelected = new TypePoste();
        listEmploi = emploiFacade.findAllOrderByAttribut("id");
		emploiSelected = new Emploi();
		listPoste = posteFacade.findAllOrderByAttribut("code");
		posteSelected = new Poste();
		listMissions = new ArrayList();
        listMissionsSelected = new ArrayList<>();
		listMissions = missionFacade.findAllOrderByAttribut("code");
		listConditions = new ArrayList();
        listConditionsSelected = new ArrayList<>();
		listConditions = conditionFacade.findAllOrderByAttribut("code");
		listMoyens = new ArrayList();
        listMoyensSelected = new ArrayList<>();
		listMoyens = moyenFacade.findAllOrderByAttribut("code");
		listFormations = new ArrayList();
        listFormationsSelected = new ArrayList<>();
		listFormations = formationFacade.findAllOrderByAttribut("code");
		listCompetences = new ArrayList();
        listCompetencesSelected = new ArrayList<>();
		listCompetences = competenceFacade.findAllOrderByAttribut("code");
    }
	
	public void addMissionsForPoste() {
		if(!listMissionsSelected.isEmpty()) {
			poste.addListMissions(listMissionsSelected);
			listMissions.removeAll(listMissionsSelected);
			listMissionsSelected = new ArrayList<>();
		}
	}
	
	public void removeMissionForPoste(Mission mission) {
		poste.removeMission(mission);
		listMissions.add(mission);
	}
	
	public void addConditionsForPoste() {
		if(!listConditionsSelected.isEmpty()) {
			poste.addListConditions(listConditionsSelected);
			listConditions.removeAll(listConditionsSelected);
			listConditionsSelected = new ArrayList<>();
		}
	}
	
	public void removeConditionForPoste(Condition condition) {
		poste.removeCondition(condition);
		listConditions.add(condition);
	}
	
	public void addMoyensForPoste() {
		if(!listMoyensSelected.isEmpty()) {
			poste.addListMoyens(listMoyensSelected);
			listMoyens.removeAll(listMoyensSelected);
			listMoyensSelected = new ArrayList<>();
		}
	}
	
	public void removeMoyenForPoste(Moyen moyen) {
		poste.removeMoyen(moyen);
		listMoyens.add(moyen);
	}
	
	public void addFormationsForPoste() {
		if(!listFormationsSelected.isEmpty()) {
			poste.addListFormations(listFormationsSelected);
			listFormations.removeAll(listFormationsSelected);
			listFormationsSelected = new ArrayList<>();
		}
	}
	
	public void removeFormationForPoste(Formation formation) {
		poste.removeFormation(formation);
		listFormations.add(formation);
	}
	
	public void addCompetencesForPoste() {
		if(!listCompetencesSelected.isEmpty()) {
			poste.addListCompetences(listCompetencesSelected);
			listCompetences.removeAll(listCompetencesSelected);
			listCompetencesSelected = new ArrayList<>();
		}
	}
	
	public void removeCompetenceForPoste(Competence competence) {
		poste.removeCompetence(competence);
		listCompetences.add(competence);
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

	public Poste getPoste() {
		return poste;
	}

	public PosteFacade getPosteFacade() {
		return posteFacade;
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
	
	public void setPoste(Poste poste) {
		this.poste = poste;
	}

	public void setPosteFacade(PosteFacade posteFacade) {
		this.posteFacade = posteFacade;
	}

	public AdminUniteOrganisationnelleFacade getUniteOrganisationnelleFacade() {
		return uniteOrganisationnelleFacade;
	}

	public void setUniteOrganisationnelleFacade(AdminUniteOrganisationnelleFacade uniteOrganisationnelleFacade) {
		this.uniteOrganisationnelleFacade = uniteOrganisationnelleFacade;
	}

	public List<AdminUniteOrganisationnelle> getListUniteOrganisationnelle() {
		return listUniteOrganisationnelle;
	}

	public void setListUniteOrganisationnelle(List<AdminUniteOrganisationnelle> listUniteOrganisationnelle) {
		this.listUniteOrganisationnelle = listUniteOrganisationnelle;
	}

	public AdminUniteOrganisationnelle getUniteOrganisationnelleSelected() {
		return uniteOrganisationnelleSelected;
	}

	public void setUniteOrganisationnelleSelected(AdminUniteOrganisationnelle uniteOrganisationnelleSelected) {
		this.uniteOrganisationnelleSelected = uniteOrganisationnelleSelected;
	}

	public TypePosteFacade getTypePosteFacade() {
		return typePosteFacade;
	}

	public void setTypePosteFacade(TypePosteFacade typePosteFacade) {
		this.typePosteFacade = typePosteFacade;
	}

	public EmploiFacade getEmploiFacade() {
		return emploiFacade;
	}

	public void setEmploiFacade(EmploiFacade emploiFacade) {
		this.emploiFacade = emploiFacade;
	}

	public List<TypePoste> getListTypePoste() {
		return listTypePoste;
	}

	public void setListTypePoste(List<TypePoste> listTypePoste) {
		this.listTypePoste = listTypePoste;
	}

	public TypePoste getTypePosteSelected() {
		return typePosteSelected;
	}

	public void setTypePosteSelected(TypePoste typePosteSelected) {
		this.typePosteSelected = typePosteSelected;
	}

	public List<Emploi> getListEmploi() {
		return listEmploi;
	}

	public void setListEmploi(List<Emploi> listEmploi) {
		this.listEmploi = listEmploi;
	}

	public Emploi getEmploiSelected() {
		return emploiSelected;
	}

	public void setEmploiSelected(Emploi emploiSelected) {
		this.emploiSelected = emploiSelected;
	}

	public List<Poste> getListPoste() {
		return listPoste;
	}

	public void setListPoste(List<Poste> listPoste) {
		this.listPoste = listPoste;
	}

	public Poste getPosteSelected() {
		return posteSelected;
	}

	public void setPosteSelected(Poste posteSelected) {
		this.posteSelected = posteSelected;
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

	public ConditionFacade getConditionFacade() {
		return conditionFacade;
	}

	public void setConditionFacade(ConditionFacade conditionFacade) {
		this.conditionFacade = conditionFacade;
	}

	public MoyenFacade getMoyenFacade() {
		return moyenFacade;
	}

	public void setMoyenFacade(MoyenFacade moyenFacade) {
		this.moyenFacade = moyenFacade;
	}

	public FormationFacade getFormationFacade() {
		return formationFacade;
	}

	public void setFormationFacade(FormationFacade formationFacade) {
		this.formationFacade = formationFacade;
	}

	public List<Condition> getListConditions() {
		return listConditions;
	}

	public void setListConditions(List<Condition> listConditions) {
		this.listConditions = listConditions;
	}

	public List<Condition> getListConditionsSelected() {
		return listConditionsSelected;
	}

	public void setListConditionsSelected(List<Condition> listConditionsSelected) {
		this.listConditionsSelected = listConditionsSelected;
	}

	public List<Moyen> getListMoyens() {
		return listMoyens;
	}

	public void setListMoyens(List<Moyen> listMoyens) {
		this.listMoyens = listMoyens;
	}

	public List<Moyen> getListMoyensSelected() {
		return listMoyensSelected;
	}

	public void setListMoyensSelected(List<Moyen> listMoyensSelected) {
		this.listMoyensSelected = listMoyensSelected;
	}

	public List<Formation> getListFormations() {
		return listFormations;
	}

	public void setListFormations(List<Formation> listFormations) {
		this.listFormations = listFormations;
	}

	public List<Formation> getListFormationsSelected() {
		return listFormationsSelected;
	}

	public void setListFormationsSelected(List<Formation> listFormationsSelected) {
		this.listFormationsSelected = listFormationsSelected;
	}

	public SelectItem[] getTypeOptions() {
		return typeOptions;
	}

	public void setTypeOptions(SelectItem[] typeOptions) {
		this.typeOptions = typeOptions;
	}

	public SelectItem[] getExigenceOptions() {
		return exigenceOptions;
	}

	public void setExigenceOptions(SelectItem[] exigenceOptions) {
		this.exigenceOptions = exigenceOptions;
	}

	public CompetenceFacade getCompetenceFacade() {
		return competenceFacade;
	}

	public void setCompetenceFacade(CompetenceFacade competenceFacade) {
		this.competenceFacade = competenceFacade;
	}

	public List<Competence> getListCompetences() {
		return listCompetences;
	}

	public void setListCompetences(List<Competence> listCompetences) {
		this.listCompetences = listCompetences;
	}

	public List<Competence> getListCompetencesSelected() {
		return listCompetencesSelected;
	}

	public void setListCompetencesSelected(List<Competence> listCompetencesSelected) {
		this.listCompetencesSelected = listCompetencesSelected;
	}
	
}
