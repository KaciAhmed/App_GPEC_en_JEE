package dz.elit.gpecpf.compagne_evaluation.entity;

import dz.elit.gpecpf.commun.util.StaticUtil;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Kaci Ahmed
 */
@Entity
@Table(name = "compagneevaluation", schema = StaticUtil.EVALUATION_SCHEMA)
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Compagneevaluation.findAll", query = "SELECT c FROM Compagneevaluation c")
	,
	@NamedQuery(name = "Compagneevaluation.findById", query = "SELECT c FROM Compagneevaluation c WHERE c.id = :id")
	,
	@NamedQuery(name = "Compagneevaluation.findByCode", query = "SELECT c FROM Compagneevaluation c WHERE c.code = :code")
	,
	@NamedQuery(name = "Compagneevaluation.findByDatedeb", query = "SELECT c FROM Compagneevaluation c WHERE c.datedeb = :datedeb")
	,
	@NamedQuery(name = "Compagneevaluation.findByDatefin", query = "SELECT c FROM Compagneevaluation c WHERE c.datefin = :datefin")
	,
	@NamedQuery(name = "Compagneevaluation.findByActive", query = "SELECT c FROM Compagneevaluation c WHERE c.active = 1"),})
public class Compagneevaluation implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;
	@Size(max = 50)
	@Column(name = "code")
	private String code;
	@Column(name = "datedeb")
	@Temporal(TemporalType.DATE)
	private Date datedeb;
	@Column(name = "datefin")
	@Temporal(TemporalType.DATE)
	private Date datefin;
	@Column(name = "active")
	private Integer active;

	public Compagneevaluation() {
	}

	public Compagneevaluation(Integer id) {
		this.id = id;
	}

	public Compagneevaluation(Integer id, String code, Date datedeb, Date datefin, Integer active) {
		this.id = id;
		this.code = code;
		this.datedeb = datedeb;
		this.datefin = datefin;
		this.active = active;
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

	public Date getDatedeb() {
		return datedeb;
	}

	public void setDatedeb(Date datedeb) {
		this.datedeb = datedeb;
	}

	public Date getDatefin() {
		return datefin;
	}

	public void setDatefin(Date datefin) {
		this.datefin = datefin;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
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
		if (!(object instanceof Compagneevaluation)) {
			return false;
		}
		Compagneevaluation other = (Compagneevaluation) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "otherEntity.Compagneevaluation[ id=" + id + " ]";
	}

}
