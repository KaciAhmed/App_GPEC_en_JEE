package dz.elit.gpecpf.poste.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import dz.elit.gpecpf.commun.util.StaticUtil;
import dz.elit.gpecpf.employe.entity.Employe;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
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
@Table(name = "formation", schema = StaticUtil.POSTE_SCHEMA)
@NamedQueries({
	@NamedQuery(name = "Formation.findByCodeWithoutCurrentId", query = "SELECT t FROM Formation t WHERE t.code =:code AND t.id != :id ORDER BY t.code  "),})
public class Formation implements Serializable {

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
	@Size(max = 255)
	@Column(name = "description")
	private String description;
	@Size(max = 255)
	@Column(name = "type")
	private String type;
	@Size(max = 255)
	@Column(name = "exigene")
	private String exigence;
	@ManyToMany(mappedBy = "listFormation")
	private List<Employe> listEmploye = new ArrayList();

	public Formation() {
	}

	public Formation(Integer id) {
		this.id = id;
	}

	public Formation(Integer id, String code, String description, String type, String exigence) {
		this.id = id;
		this.code = code;
		this.description = description;
		this.type = type;
		this.exigence = exigence;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getExigence() {
		return exigence;
	}

	public void setExigence(String exigence) {
		this.exigence = exigence;
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
		if (!(object instanceof Formation)) {
			return false;
		}
		Formation other = (Formation) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "dz.elit.gpecpf.poste.entity.Formation[ id=" + id + " ]";
	}

	public List<Employe> getListEmploye() {
		return listEmploye;
	}

	public void setListEmploye(List<Employe> listEmploye) {
		this.listEmploye = listEmploye;
	}

}
