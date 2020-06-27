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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Nadir Ben Mohand
 */
@Entity
@Table(name = "type_poste", schema = StaticUtil.POSTE_SCHEMA)
public class TypePoste implements Serializable {

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
	@Column(name = "libelle")
	private String libelle;

	public TypePoste() {
	}

	public TypePoste(Integer id) {
		this.id = id;
	}

	public TypePoste(Integer id, String code, String libelle) {
		this.id = id;
		this.code = code;
		this.libelle = libelle;
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

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof TypePoste)) {
			return false;
		}
		TypePoste other = (TypePoste) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "dz.elit.gpecpf.poste.entity.TypePoste[ id=" + id + " ]";
	}

}
