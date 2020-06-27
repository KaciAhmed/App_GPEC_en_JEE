package dz.elit.gpecpf.poste.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import dz.elit.gpecpf.commun.util.StaticUtil;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Nadir Ben Mohand
 */
@Entity
@Table(name = "mission", schema = StaticUtil.POSTE_SCHEMA)
@NamedQueries({
	@NamedQuery(name = "Mission.findByCodeWithoutCurrentId", query = "SELECT a FROM Mission a WHERE a.code =:code AND a.id != :id ORDER BY a.code  "),
	@NamedQuery(name = "Mission.findByCode", query = "SELECT a FROM Mission a WHERE a.code =:code"),
	@NamedQuery(name = "Mission.findByLibelle", query = "SELECT a FROM Mission a WHERE a.libelle =:libelle"),
	@NamedQuery(name = "Mission.findByLibelleWithoutCurrentId", query = "SELECT a FROM Mission a WHERE a.libelle =:libelle AND a.id != :id"),
	@NamedQuery(name = "Mission.findByActivite", query = "SELECT a FROM Mission a WHERE :activite MEMBER OF a.listActivites "),})
public class Mission implements Serializable {

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
	@Column(name = "libelle", nullable = false, unique = true, length = 30)
	@NotNull
	private String libelle;
	@Size(max = 255)
	@Column(name = "description")
	private String description;

	@ManyToMany
	@JoinTable(
			name = "mission_activite",
			joinColumns = {
				@JoinColumn(name = "id_mission", referencedColumnName = "id")
			}, inverseJoinColumns = {
				@JoinColumn(name = "id_activite", referencedColumnName = "id")
			}, schema = StaticUtil.POSTE_SCHEMA
	)
	private List<Activite> listActivites = new ArrayList<>();

	public Mission() {
	}

	public Mission(Integer id) {
		this.id = id;
	}

	public Mission(Integer id, String code, String libelle, String description, List<Activite> listActivites) {
		this.id = id;
		this.code = code;
		this.libelle = libelle;
		this.description = description;
		this.listActivites = listActivites;
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

	public List<Activite> getListActivites() {
		return listActivites;
	}

	public void setListActivites(List<Activite> listActivites) {
		this.listActivites = listActivites;
	}

	public void addActivite(Activite activite) {
		this.getListActivites().add(activite);
	}

	public void removeActivite(Activite activite) {
		this.getListActivites().remove(activite);
	}

	public void addListActivites(List<Activite> activites) {
		for (Activite activite : activites) {
			addActivite(activite);
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
		if (!(object instanceof Mission)) {
			return false;
		}
		Mission other = (Mission) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "dz.elit.gpecpf.poste.entity.Mission[ id=" + id + " ]";
	}

}
