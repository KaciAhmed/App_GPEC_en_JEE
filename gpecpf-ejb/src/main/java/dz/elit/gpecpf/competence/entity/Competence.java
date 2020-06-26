package dz.elit.gpecpf.competence.entity;

import dz.elit.gpecpf.commun.util.StaticUtil;
import dz.elit.gpecpf.other.entity.Notecompetenceemploye;
import dz.elit.gpecpf.poste.entity.Poste;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Kaci Ahmed
 */
@Entity
@Table(name = "competence", schema = StaticUtil.COMPETENCE_SCHEMA)
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Competence.findAll", query = "SELECT c FROM Competence c")
	, @NamedQuery(name = "Competence.findById", query = "SELECT c FROM Competence c WHERE c.id = :id")
	, @NamedQuery(name = "Competence.findByCode", query = "SELECT c FROM Competence c WHERE c.code = :code")
	, @NamedQuery(name = "Competence.findByLibelle", query = "SELECT c FROM Competence c WHERE c.libelle = :libelle")
	, @NamedQuery(name = "Competence.findByComportement", query = "SELECT c FROM Competence c WHERE :comportement MEMBER OF c.listComportement ")
	, @NamedQuery(name = "Competence.findByDescription", query = "SELECT c FROM Competence c WHERE c.description = :description")})

public class Competence implements Comparable, Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;
	@Size(max = 50)
	@Column(name = "code")
	private String code;
	@Size(max = 255)
	@Column(name = "libelle")
	private String libelle;
	@Size(max = 2147483647)
	@Column(name = "description")
	private String description;
	@JoinTable(name = "competence_comportement", joinColumns = {
		@JoinColumn(name = "id_competence", referencedColumnName = "id")}, inverseJoinColumns = {
		@JoinColumn(name = "id_comportement", referencedColumnName = "id")}, schema = StaticUtil.COMPETENCE_SCHEMA)
	@ManyToMany
	private List<Comportement> listComportement = new ArrayList<>();
	@JoinColumn(name = "iddomcom", referencedColumnName = "id")
	@ManyToOne
	private Domainecompetence iddomcom;
	@JoinColumn(name = "idtypcom", referencedColumnName = "id")
	@ManyToOne
	private Typecompetence idtypcom;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "competence")
	private Collection<Notecompetenceemploye> notecompetenceemployeCollection;

	@ManyToMany(mappedBy = "listCompetences")
	private List<Poste> listPostes = new ArrayList<>();

	public Competence() {

	}

	public Competence(Integer id) {
		this.id = id;
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

	public List<Comportement> getListComportement() {
		return listComportement;
	}

	public void setListComportement(List<Comportement> listComportement) {
		this.listComportement = listComportement;
	}

	public Domainecompetence getIddomcom() {
		return iddomcom;
	}

	public void setIddomcom(Domainecompetence iddomcom) {
		this.iddomcom = iddomcom;
	}

	public Typecompetence getIdtypcom() {
		return idtypcom;
	}

	public void setIdtypcom(Typecompetence idtypcom) {
		this.idtypcom = idtypcom;
	}

	@XmlTransient
	public Collection<Notecompetenceemploye> getNotecompetenceemployeCollection() {
		return notecompetenceemployeCollection;
	}

	public void setNotecompetenceemployeCollection(Collection<Notecompetenceemploye> notecompetenceemployeCollection) {
		this.notecompetenceemployeCollection = notecompetenceemployeCollection;
	}

	public List<Poste> getListPostes() {
		return listPostes;
	}

	public void setListPostes(List<Poste> listPostes) {
		this.listPostes = listPostes;
	}

	public void addDomComp(Domainecompetence domaine) {
		this.setIddomcom(domaine);
		domaine.getCompetenceCollection().add(this);
	}

	public void editDomComp(Domainecompetence domaine) {
		Domainecompetence oldDomaine;
		oldDomaine = this.iddomcom;
		this.setIddomcom(domaine);
		domaine.getCompetenceCollection().add(this);
		oldDomaine.getCompetenceCollection().remove(this);

	}

	public void addTypeComp(Typecompetence type) {
		this.setIdtypcom(type);
		type.getCompetenceCollection().add(this);
	}

	public void editTypeComp(Typecompetence type) {
		Typecompetence oldType;
		oldType = this.idtypcom;
		this.setIdtypcom(type);
		type.getCompetenceCollection().add(this);
		oldType.getCompetenceCollection().remove(this);
	}

	public void addComprtement(Comportement comportement) {
		this.getListComportement().add(comportement);
	}

	public void removeComportement(Comportement comportement) {
		this.getListComportement().remove(comportement);
	}

	public void addListComportement(List<Comportement> comportements) {
		for (Comportement comportement : comportements) {
			addComprtement(comportement);
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
		if (!(object instanceof Competence)) {
			return false;
		}
		Competence other = (Competence) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	public int compareTo(Object o) {
		Competence cmt = (Competence) o;
		return this.code.compareToIgnoreCase(cmt.getCode());
	}

	@Override
	public String toString() {
		return "otherEntity.Competence[ id=" + id + " ]";
	}

}
