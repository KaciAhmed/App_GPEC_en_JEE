package dz.elit.gpecpf.poste.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import dz.elit.gpecpf.commun.util.StaticUtil;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Nadir Ben Mohand
 */
@Entity
@Table(name = "tache", schema = StaticUtil.POSTE_SCHEMA)
@NamedQueries({
	@NamedQuery(name = "Tache.findByCodeWithoutCurrentId", query = "SELECT t FROM Tache t WHERE t.code =:code AND t.id != :id ORDER BY t.code  "),
	@NamedQuery(name = "Tache.findByCode", query = "SELECT t FROM Tache t WHERE t.code =:code"),
})
public class Tache implements Serializable {

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

	public Tache() {
	}

	public Tache(Integer id) {
		this.id = id;
	}

	public Tache(Integer id, String code, String description) {
		this.id = id;
		this.code = code;
		this.description = description;
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

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Tache)) {
			return false;
		}
		Tache other = (Tache) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "dz.elit.gpecpf.poste.entity.Tache[ id=" + id + " ]";
	}

}
