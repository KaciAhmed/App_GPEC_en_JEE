package dz.elit.gpecpf.other.entity;

import dz.elit.gpecpf.commun.util.StaticUtil;
import dz.elit.gpecpf.employe.entity.Employe;
import dz.elit.gpecpf.poste.entity.Poste;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
 * @author Kaci Ahmed
 */
@Entity
@Table(name = "historiqueemployeposte", schema = StaticUtil.ADMINISTRATION_SCHEMA)
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Historiqueemployeposte.findAll", query = "SELECT h FROM Historiqueemployeposte h")
	, @NamedQuery(name = "Historiqueemployeposte.findByIdemploye", query = "SELECT h FROM Historiqueemployeposte h WHERE h.historiqueemployepostePK.idemploye = :idemploye")
	, @NamedQuery(name = "Historiqueemployeposte.findByIdposte", query = "SELECT h FROM Historiqueemployeposte h WHERE h.historiqueemployepostePK.idposte = :idposte")
	, @NamedQuery(name = "Historiqueemployeposte.findByDatedeb", query = "SELECT h FROM Historiqueemployeposte h WHERE h.historiqueemployepostePK.datedeb = :datedeb")
	, @NamedQuery(name = "Historiqueemployeposte.findByDatefin", query = "SELECT h FROM Historiqueemployeposte h WHERE h.datefin = :datefin")})
public class Historiqueemployeposte implements Comparable, Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	protected HistoriqueemployepostePK historiqueemployepostePK;
	@Column(name = "datefin")
	@Temporal(TemporalType.DATE)
	private Date datefin;
	@JoinColumn(name = "idemploye", referencedColumnName = "id", insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private Employe employe;
	@JoinColumn(name = "idposte", referencedColumnName = "id", insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private Poste poste;

	public Historiqueemployeposte() {
	}

	public Historiqueemployeposte(HistoriqueemployepostePK historiqueemployepostePK) {
		this.historiqueemployepostePK = historiqueemployepostePK;
	}

	public Historiqueemployeposte(int idemploye, int idposte, Date datedeb) {
		this.historiqueemployepostePK = new HistoriqueemployepostePK(idemploye, idposte, datedeb);
	}

	public HistoriqueemployepostePK getHistoriqueemployepostePK() {
		return historiqueemployepostePK;
	}

	public void setHistoriqueemployepostePK(HistoriqueemployepostePK historiqueemployepostePK) {
		this.historiqueemployepostePK = historiqueemployepostePK;
	}

	public Date getDatefin() {
		return datefin;
	}

	public void setDatefin(Date datefin) {
		this.datefin = datefin;
	}

	public Employe getEmploye() {
		return employe;
	}

	public void setEmploye(Employe employe) {
		this.employe = employe;
	}

	public Poste getPoste() {
		return poste;
	}

	public void setPoste(Poste poste) {
		this.poste = poste;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (historiqueemployepostePK != null ? historiqueemployepostePK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Historiqueemployeposte)) {
			return false;
		}
		Historiqueemployeposte other = (Historiqueemployeposte) object;
		if ((this.historiqueemployepostePK == null && other.historiqueemployepostePK != null) || (this.historiqueemployepostePK != null && !this.historiqueemployepostePK.equals(other.historiqueemployepostePK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "otherEntity.Historiqueemployeposte[ historiqueemployepostePK=" + historiqueemployepostePK + " ]";
	}

	public int compareTo(Object o) {
		Historiqueemployeposte hist = (Historiqueemployeposte) o;
		return this.getHistoriqueemployepostePK().getDatedeb().compareTo(hist.getHistoriqueemployepostePK().getDatedeb());

	}

}
