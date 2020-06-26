package dz.elit.gpecpf.administration.entity;

import dz.elit.gpecpf.commun.util.StaticUtil;
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
@Table(name = "notification", schema = StaticUtil.ADMINISTRATION_SCHEMA)
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Notification.findByEmploye", query = "SELECT n FROM Notification n WHERE n.employe = :employe ORDER BY n.id DESC"),
	@NamedQuery(name = "Notification.findByEmployeUnseen", query = "SELECT n FROM Notification n WHERE n.employe = :employe and n.verifiee = 0"),
})
public class Notification implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;
	@Column(name = "date_notification")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateNotification;
	@Column(name = "libelle")
	private String libelle;
	@Column(name = "lien")
	private String lien;
	@Column(name = "verifiee")
	private int verifiee;

	@ManyToOne
	@JoinColumn(name = "id_employe", referencedColumnName = "id")
	private Employe employe;

	public Notification() {
	}

	public Notification(Integer id) {
		this.id = id;
	}

	public Notification(Integer id, Date dateNotification, String libelle, String lien, int verifiee) {
		this.id = id;
		this.dateNotification = dateNotification;
		this.libelle = libelle;
		this.lien = lien;
		this.verifiee = verifiee;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDateNotification() {
		return dateNotification;
	}

	public void setDateNotification(Date dateNotification) {
		this.dateNotification = dateNotification;
	}

	public String getLien() {
		return lien;
	}

	public void setLien(String lien) {
		this.lien = lien;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public int getVerifiee() {
		return verifiee;
	}

	public void setVerifiee(int verifiee) {
		this.verifiee = verifiee;
	}

	public Employe getEmploye() {
		return employe;
	}

	public void setEmploye(Employe employe) {
		this.employe = employe;
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
		if (!(object instanceof Notification)) {
			return false;
		}
		Notification other = (Notification) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "dz.elit.gpecpf.administration.entity.Notification[ id=" + id + " ]";
	}

}
