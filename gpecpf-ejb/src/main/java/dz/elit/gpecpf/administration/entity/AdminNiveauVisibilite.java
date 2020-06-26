package dz.elit.gpecpf.administration.entity;

import dz.elit.gpecpf.commun.util.StaticUtil;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author chekor.samir
 */
@Entity
@Table(name = "admin_niveau_visibilite", schema = StaticUtil.ADMINISTRATION_SCHEMA)
public class AdminNiveauVisibilite implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@Column(nullable = false)
	private String code;

	public AdminNiveauVisibilite() {
	}

	public AdminNiveauVisibilite(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (code != null ? code.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof AdminNiveauVisibilite)) {
			return false;
		}
		AdminNiveauVisibilite other = (AdminNiveauVisibilite) object;
		if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "dz.elit.gpecpf.administration.entity.AdminNiveauVisibilite[ code=" + code + " ]";
	}

}
