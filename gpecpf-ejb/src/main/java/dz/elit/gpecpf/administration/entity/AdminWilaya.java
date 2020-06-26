package dz.elit.gpecpf.administration.entity;

import dz.elit.gpecpf.commun.util.StaticUtil;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 *
 * @author chekor.samir
 */
@Entity
@Table(name = "admin_wilaya", schema = StaticUtil.ADMINISTRATION_SCHEMA)
public class AdminWilaya implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "code_wilaya", nullable = false)
	private String codeWilaya;
	@Column(name = "nom_wilaya", nullable = false)
	private String nomWilaya;
	@OneToMany(mappedBy = "codeWil")
	@OrderBy(value = "nomCommune")
	private List<AdminCommune> listCummunes;

	public String getCodeWilaya() {
		return codeWilaya;
	}

	public void setCodeWilaya(String codeWilaya) {
		this.codeWilaya = codeWilaya;
	}

	public String getNomWilaya() {
		return nomWilaya;
	}

	public void setNomWilaya(String nomWilaya) {
		this.nomWilaya = nomWilaya;
	}

	public List<AdminCommune> getListCummunes() {
		return listCummunes;
	}

	public void setListCummunes(List<AdminCommune> listCummunes) {
		this.listCummunes = listCummunes;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (codeWilaya != null ? codeWilaya.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the code_wilaya fields are not set
		if (!(object instanceof AdminWilaya)) {
			return false;
		}
		AdminWilaya other = (AdminWilaya) object;
		if ((this.codeWilaya == null && other.codeWilaya != null) || (this.codeWilaya != null && !this.codeWilaya.equals(other.codeWilaya))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "dz.elit.gpecpf.administration.entity.AdminWilaya[ id=" + codeWilaya + " ]";
	}

}
