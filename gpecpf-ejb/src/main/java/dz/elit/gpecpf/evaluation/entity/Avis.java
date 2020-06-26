package dz.elit.gpecpf.evaluation.entity;

import dz.elit.gpecpf.commun.util.StaticUtil;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Nadir Ben Mohand
 */
@Entity
@Table(name = "avis", schema = StaticUtil.EVALUATION_SCHEMA)
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Avis.findAll", query = "SELECT a FROM Avis a"),
	@NamedQuery(name = "Avis.findByEvaluation", query = "SELECT a FROM Avis a WHERE a.evaluation = :evaluation ORDER BY a.id DESC"),
})
public class Avis implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 500)
	@Column(name = "avis", nullable = false)
	private String avis;
	@Column(name = "type")
	private int type;
	@Column(name = "positif")
	private int positif;

	@ManyToOne
	@JoinColumn(name = "id_evaluation", referencedColumnName = "id")
	private Evaluation evaluation;

	public Avis() {
	}

	public Avis(Integer id) {
		this.id = id;
	}

	public Avis(Integer id, String avis, int type, int positif, Evaluation evaluation) {
		this.id = id;
		this.avis = avis;
		this.type = type;
		this.positif = positif;
		this.evaluation = evaluation;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAvis() {
		return avis;
	}

	public void setAvis(String avis) {
		this.avis = avis;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getPositif() {
		return positif;
	}

	public void setPositif(int positif) {
		this.positif = positif;
	}

	public Evaluation getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(Evaluation evaluation) {
		this.evaluation = evaluation;
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
		if (!(object instanceof Avis)) {
			return false;
		}
		Avis other = (Avis) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "dz.elit.gpecpf.evaluation.entity.Avis[ id=" + id + " ]";
	}

}
