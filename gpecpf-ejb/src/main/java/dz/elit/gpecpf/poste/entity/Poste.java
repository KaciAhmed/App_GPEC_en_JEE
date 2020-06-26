package dz.elit.gpecpf.poste.entity;

import dz.elit.gpecpf.administration.entity.AdminUniteOrganisationnelle;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import dz.elit.gpecpf.commun.util.StaticUtil;
import dz.elit.gpecpf.competence.entity.Competence;
import dz.elit.gpecpf.other.entity.Historiqueemployeposte;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Nadir Ben Mohand
 */
@Entity
@Table(name = "poste", schema = StaticUtil.POSTE_SCHEMA)
@NamedQueries({
	@NamedQuery(name = "Poste.findByCodeWithoutCurrentId", query = "SELECT p FROM Poste p WHERE p.code =:code AND p.id != :id ORDER BY p.code  ")
	,
	@NamedQuery(name = "Poste.findByType", query = "SELECT p FROM Poste p WHERE p.typePoste =:typePoste ")
	,
	@NamedQuery(name = "Poste.findByEmploi", query = "SELECT p FROM Poste p WHERE p.emploi =:emploi ")
	,
	@NamedQuery(name = "Poste.findByResponsable", query = "SELECT p FROM Poste p WHERE p.posteSuperieur =:posteSuperieur ")
	,
	@NamedQuery(name = "Poste.findByCondition", query = "SELECT p FROM Poste p WHERE :condition MEMBER OF p.listConditions ")
	,
	@NamedQuery(name = "Poste.findByMoyen", query = "SELECT p FROM Poste p WHERE :moyen MEMBER OF p.listMoyens ")
	,
	@NamedQuery(name = "Poste.findByFormation", query = "SELECT p FROM Poste p WHERE :formation MEMBER OF p.listFormations ")
	,
	@NamedQuery(name = "Poste.findByMission", query = "SELECT p FROM Poste p WHERE :mission MEMBER OF p.listMissions ")
	,
	@NamedQuery(name = "Poste.findByCompetence", query = "SELECT p FROM Poste p WHERE :competence MEMBER OF p.listCompetences "),})
public class Poste implements Comparable, Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;
	@Size(min = 1, max = 20)
	@Column(name = "code", nullable = false, unique = true, length = 20)
	@NotNull
	private String code;
	@Size(min = 1, max = 30)
	@Column(name = "denomination", nullable = false, unique = true, length = 30)
	@NotNull
	private String denomination;
	@Size(max = 65536)
	@Column(name = "def_sommaire")
	private String defSommaire;
	@Column(name = "annee_experience")
	private Integer anneeExperience;
	@Size(max = 255)
	@Column(name = "denom_ant")
	private String denomAnt;
	@Size(max = 255)
	@Column(name = "class_ant")
	private String classAnt;
	@Size(max = 50)
	@Column(name = "code_ant")
	private String codeAnt;
	@Size(max = 255)
	@Column(name = "classement")
	private String classement;
	@Column(name = "date_creation")
	@Temporal(TemporalType.DATE)
	private Date dateCreation;
	@Column(name = "date_maj")
	@Temporal(TemporalType.DATE)
	private Date dateMaj;
	@Column(name = "date_elab_pec")
	@Temporal(TemporalType.DATE)
	private Date dateElabPec;

	@ManyToOne
	@JoinColumn(name = "id_type_poste", referencedColumnName = "id")
	private TypePoste typePoste;

	@ManyToOne
	@JoinColumn(name = "id_emploi", referencedColumnName = "id")
	private Emploi emploi;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_unite_organisationnelle", referencedColumnName = "id")
	private AdminUniteOrganisationnelle adminUniteOrganisationnelle;

	@ManyToMany
	@JoinTable(
			name = "mission_poste",
			joinColumns = @JoinColumn(name = "id_poste", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "id_mission", referencedColumnName = "id"),
			schema = StaticUtil.POSTE_SCHEMA
	)
	private List<Mission> listMissions = new ArrayList<>();
	@ManyToMany
	@JoinTable(
			name = "condition_poste",
			joinColumns = @JoinColumn(name = "id_poste", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "id_condition", referencedColumnName = "id"),
			schema = StaticUtil.POSTE_SCHEMA
	)
	private List<Condition> listConditions = new ArrayList<>();
	@ManyToMany
	@JoinTable(
			name = "moyen_poste",
			joinColumns = @JoinColumn(name = "id_poste", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "id_moyen", referencedColumnName = "id"),
			schema = StaticUtil.POSTE_SCHEMA
	)
	private List<Moyen> listMoyens = new ArrayList<>();
	@JoinColumn(name = "id_superieur", referencedColumnName = "id")
	@ManyToOne
	private Poste posteSuperieur;

	@ManyToMany
	@JoinTable(
			name = "formation_poste",
			joinColumns = @JoinColumn(name = "id_poste", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "id_formation", referencedColumnName = "id"),
			schema = StaticUtil.POSTE_SCHEMA
	)
	private List<Formation> listFormations = new ArrayList<>();

	@ManyToMany
	@JoinTable(
			name = "competence_poste",
			joinColumns = @JoinColumn(name = "id_poste", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "id_competence", referencedColumnName = "id"),
			schema = StaticUtil.COMPETENCE_SCHEMA
	)
	private List<Competence> listCompetences = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "poste")
	private List<Historiqueemployeposte> ListHistoriqueEmployePoste = new ArrayList<>();

	public Poste() {
	}

	public Poste(Integer id) {
		this.id = id;
	}

	public Poste(Integer id, String code, String denomination, String defSommaire, Integer anneeExperience, String denomAnt, String classAnt, String codeAnt, String classement, Date dateCreation, Date dateMaj, Date dateElabPec, TypePoste typePoste, Emploi emploi, AdminUniteOrganisationnelle adminUniteOrganisationnelle, Poste posteSuperieur) {
		this.id = id;
		this.code = code;
		this.denomination = denomination;
		this.defSommaire = defSommaire;
		this.anneeExperience = anneeExperience;
		this.denomAnt = denomAnt;
		this.classAnt = classAnt;
		this.codeAnt = codeAnt;
		this.classement = classement;
		this.dateCreation = dateCreation;
		this.dateMaj = dateMaj;
		this.dateElabPec = dateElabPec;
		this.typePoste = typePoste;
		this.emploi = emploi;
		this.adminUniteOrganisationnelle = adminUniteOrganisationnelle;
		this.posteSuperieur = posteSuperieur;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDenomination() {
		return denomination;
	}

	public void setDenomination(String denomination) {
		this.denomination = denomination;
	}

	public String getDefSommaire() {
		return defSommaire;
	}

	public void setDefSommaire(String defSommaire) {
		this.defSommaire = defSommaire;
	}

	public Integer getAnneeExperience() {
		return anneeExperience;
	}

	public void setAnneeExperience(Integer anneeExperience) {
		this.anneeExperience = anneeExperience;
	}

	public String getDenomAnt() {
		return denomAnt;
	}

	public void setDenomAnt(String denomAnt) {
		this.denomAnt = denomAnt;
	}

	public String getClassAnt() {
		return classAnt;
	}

	public void setClassAnt(String classAnt) {
		this.classAnt = classAnt;
	}

	public String getCodeAnt() {
		return codeAnt;
	}

	public void setCodeAnt(String codeAnt) {
		this.codeAnt = codeAnt;
	}

	public String getClassement() {
		return classement;
	}

	public void setClassement(String classement) {
		this.classement = classement;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public Date getDateMaj() {
		return dateMaj;
	}

	public void setDateMaj(Date dateMaj) {
		this.dateMaj = dateMaj;
	}

	public Date getDateElabPec() {
		return dateElabPec;
	}

	public void setDateElabPec(Date dateElabPec) {
		this.dateElabPec = dateElabPec;
	}

	public TypePoste getTypePoste() {
		return typePoste;
	}

	public void setTypePoste(TypePoste typePoste) {
		this.typePoste = typePoste;
	}

	public Emploi getEmploi() {
		return emploi;
	}

	public void setEmploi(Emploi emploi) {
		this.emploi = emploi;
	}

	public AdminUniteOrganisationnelle getAdminUniteOrganisationnelle() {
		return adminUniteOrganisationnelle;
	}

	public void setAdminUniteOrganisationnelle(AdminUniteOrganisationnelle adminUniteOrganisationnelle) {
		this.adminUniteOrganisationnelle = adminUniteOrganisationnelle;
	}

	public List<Mission> getListMissions() {
		return listMissions;
	}

	public void setListMissions(List<Mission> listMission) {
		this.listMissions = listMission;
	}

	public List<Condition> getListConditions() {
		return listConditions;
	}

	public void setListConditions(List<Condition> listConditions) {
		this.listConditions = listConditions;
	}

	public List<Moyen> getListMoyens() {
		return listMoyens;
	}

	public void setListMoyens(List<Moyen> listMoyens) {
		this.listMoyens = listMoyens;
	}

	public List<Formation> getListFormations() {
		return listFormations;
	}

	public void setListFormations(List<Formation> listFormations) {
		this.listFormations = listFormations;
	}

	public List<Competence> getListCompetences() {
		return listCompetences;
	}

	public void setListCompetences(List<Competence> listCompetences) {
		this.listCompetences = listCompetences;
	}

	public Poste getPosteSuperieur() {
		return posteSuperieur;
	}

	public void setPosteSuperieur(Poste posteSuperieur) {
		this.posteSuperieur = posteSuperieur;
	}

	public List<Historiqueemployeposte> getListHistoriqueEmployePoste() {
		return ListHistoriqueEmployePoste;
	}

	public void setListHistoriqueEmployePoste(List<Historiqueemployeposte> ListHistoriqueEmployePoste) {
		this.ListHistoriqueEmployePoste = ListHistoriqueEmployePoste;
	}

	public void addMission(Mission mission) {
		this.getListMissions().add(mission);
	}

	public void removeMission(Mission mission) {
		this.getListMissions().remove(mission);
	}

	public void addListMissions(List<Mission> missions) {
		for (Mission mission : missions) {
			addMission(mission);
		}
	}

	public void addCondition(Condition condition) {
		this.getListConditions().add(condition);
	}

	public void removeCondition(Condition condition) {
		this.getListConditions().remove(condition);
	}

	public void addListConditions(List<Condition> conditions) {
		for (Condition condition : conditions) {
			addCondition(condition);
		}
	}

	public void addMoyen(Moyen moyen) {
		this.getListMoyens().add(moyen);
	}

	public void removeMoyen(Moyen moyen) {
		this.getListMoyens().remove(moyen);
	}

	public void addListMoyens(List<Moyen> moyens) {
		for (Moyen moyen : moyens) {
			addMoyen(moyen);
		}
	}

	public void addFormation(Formation formation) {
		this.getListFormations().add(formation);
	}

	public void removeFormation(Formation formation) {
		this.getListFormations().remove(formation);
	}

	public void addListFormations(List<Formation> formations) {
		for (Formation formation : formations) {
			addFormation(formation);
		}
	}

	public void addCompetence(Competence competence) {
		this.getListCompetences().add(competence);
	}

	public void removeCompetence(Competence competence) {
		this.getListCompetences().remove(competence);
	}

	public void addListCompetences(List<Competence> competences) {
		for (Competence competence : competences) {
			addCompetence(competence);
		}
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Poste)) {
			return false;
		}
		Poste other = (Poste) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "dz.elit.gpecpf.poste.entity.Poste[ id=" + id + " ]";
	}

	@Override
	public int compareTo(Object o) {
		Poste poste = (Poste) o;
		return this.code.compareToIgnoreCase(poste.getCode());
	}

}
