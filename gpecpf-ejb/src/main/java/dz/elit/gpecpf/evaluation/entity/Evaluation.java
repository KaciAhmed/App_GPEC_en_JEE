package dz.elit.gpecpf.evaluation.entity;

import dz.elit.gpecpf.commun.util.StaticUtil;
import dz.elit.gpecpf.compagne_evaluation.entity.Compagneevaluation;
import dz.elit.gpecpf.employe.entity.Employe;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Nadir Ben Mohand
 */
@Entity
@Table(name = "evaluation", schema = StaticUtil.EVALUATION_SCHEMA)
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Evaluation.findAll", query = "SELECT e FROM Evaluation e"),
	@NamedQuery(name = "Evaluation.findByCompagne", query = "SELECT e FROM Evaluation e WHERE e.compagne = :compagne ORDER BY e.id DESC"),
	@NamedQuery(name = "Evaluation.findByEmploye", query = "SELECT e FROM Evaluation e WHERE e.employe = :employe ORDER BY e.id DESC"),
	@NamedQuery(name = "Evaluation.findByCompagneEmploye", query = "SELECT e FROM Evaluation e WHERE e.employe = :employe and e.compagne = :compagne ORDER BY e.id DESC"),
	@NamedQuery(name = "Evaluation.findBySupN1", query = "SELECT e FROM Evaluation e WHERE e.supN1 = :supN1 ORDER BY e.id DESC"),
	@NamedQuery(name = "Evaluation.findByCompagneSupN1", query = "SELECT e FROM Evaluation e WHERE e.supN1 = :supN1 and e.compagne = :compagne ORDER BY e.id DESC"),
	@NamedQuery(name = "Evaluation.findBySupN2", query = "SELECT e FROM Evaluation e WHERE e.supN2 = :supN2 ORDER BY e.id DESC"),
	@NamedQuery(name = "Evaluation.findByCompagneSupN2", query = "SELECT e FROM Evaluation e WHERE e.supN2 = :supN2 and e.compagne = :compagne ORDER BY e.id DESC"),
	@NamedQuery(name = "Evaluation.findBySupN3", query = "SELECT e FROM Evaluation e WHERE e.supN3 = :supN3 ORDER BY e.id DESC"),
	@NamedQuery(name = "Evaluation.findByCompagneSupN3", query = "SELECT e FROM Evaluation e WHERE e.supN3 = :supN3 and e.compagne = :compagne ORDER BY e.id DESC"),
	@NamedQuery(name = "Evaluation.findByEvaluatedFinish", query = "SELECT e FROM Evaluation e WHERE e.compagne = :compagne and e.etat = -6"),
})
public class Evaluation implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;
	@Column(name = "date_evaluation")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateEvaluation;
	@Column(name = "archive")
	private int archive;
	@Column(name = "etat")
	private int etat;

	@ManyToOne
	@JoinColumn(name = "id_compagne", referencedColumnName = "id")
	private Compagneevaluation compagne;

	@ManyToOne
	@JoinColumn(name = "id_employe", referencedColumnName = "id")
	private Employe employe;
	
	@ManyToOne
	@JoinColumn(name = "id_sup_n1", referencedColumnName = "id")
	private Employe supN1;
	
	@ManyToOne
	@JoinColumn(name = "id_sup_n2", referencedColumnName = "id")
	private Employe supN2;
	
	@ManyToOne
	@JoinColumn(name = "id_sup_n3", referencedColumnName = "id")
	private Employe supN3;

	public Evaluation() {
	}

	public Evaluation(Integer id) {
		this.id = id;
	}

	public Evaluation(Integer id, Date dateEvaluation, int archive, int etat) {
		this.id = id;
		this.dateEvaluation = dateEvaluation;
		this.archive = archive;
		this.etat = etat;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDateEvaluation() {
		return dateEvaluation;
	}

	public void setDateEvaluation(Date dateEvaluation) {
		this.dateEvaluation = dateEvaluation;
	}

	public int getArchive() {
		return archive;
	}

	public void setArchive(int archive) {
		this.archive = archive;
	}

	public int getEtat() {
		return etat;
	}

	public void setEtat(int etat) {
		this.etat = etat;
	}

	public Compagneevaluation getCompagne() {
		return compagne;
	}

	public void setCompagne(Compagneevaluation compagne) {
		this.compagne = compagne;
	}

	public Employe getEmploye() {
		return employe;
	}

	public void setEmploye(Employe employe) {
		this.employe = employe;
	}

	public Employe getSupN1() {
		return supN1;
	}

	public void setSupN1(Employe supN1) {
		this.supN1 = supN1;
	}

	public Employe getSupN2() {
		return supN2;
	}

	public void setSupN2(Employe supN2) {
		this.supN2 = supN2;
	}

	public Employe getSupN3() {
		return supN3;
	}

	public void setSupN3(Employe supN3) {
		this.supN3 = supN3;
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
		if (!(object instanceof Evaluation)) {
			return false;
		}
		Evaluation other = (Evaluation) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "dz.elit.gpecpf.evaluation.entity.Evaluation[ id=" + id + " ]";
	}

}
