package dz.elit.gpecpf.administration.entity;

import dz.elit.gpecpf.commun.util.StaticUtil;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author abdechakour.amine
 */
@Entity
@Table(name = "admin_historique", schema = StaticUtil.ADMINISTRATION_SCHEMA)
public class AdminHistorique implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_admin_historique")
	private Integer idAdminHistorique;
	@Column(name = "donnee", columnDefinition = "TEXT")
	private String donnee;
	@Column(name = "utilisateur")
	private String utilisateur;
	@Column(name = "date_mouvement")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateMouvement;
	@Column(name = "type_mouvement")
	private String typeMouvement;
	@Column(name = "adresse_ip")
	private String adresseIp;
	@Column(name = "url")
	private String url;
	@Column(name = "classe")
	private String classe;
	@Column(name = "id_objet")
	private String idObjet;

	public AdminHistorique() {
	}

	public AdminHistorique(Integer idAdminHistorique) {
		this.idAdminHistorique = idAdminHistorique;
	}

	public Integer getIdAdminHistorique() {
		return idAdminHistorique;
	}

	public void setIdAdminHistorique(Integer idAdminHistorique) {
		this.idAdminHistorique = idAdminHistorique;
	}

	public String getDonnee() {
		return donnee;
	}

	public void setDonnee(String donnee) {
		this.donnee = donnee;
	}

	public String getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(String utilisateur) {
		this.utilisateur = utilisateur;
	}

	public Date getDateMouvement() {
		return dateMouvement;
	}

	public void setDateMouvement(Date dateMouvement) {
		this.dateMouvement = dateMouvement;
	}

	public String getTypeMouvement() {
		return typeMouvement;
	}

	public void setTypeMouvement(String typeMouvement) {
		this.typeMouvement = typeMouvement;
	}

	public String getAdresseIp() {
		return adresseIp;
	}

	public void setAdresseIp(String adresseIp) {
		this.adresseIp = adresseIp;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public String getIdObjet() {
		return idObjet;
	}

	public void setIdObjet(String idObjet) {
		this.idObjet = idObjet;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idAdminHistorique != null ? idAdminHistorique.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof AdminHistorique)) {
			return false;
		}
		AdminHistorique other = (AdminHistorique) object;
		if ((this.idAdminHistorique == null && other.idAdminHistorique != null) || (this.idAdminHistorique != null && !this.idAdminHistorique.equals(other.idAdminHistorique))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "dz.elit.gpecpf.administration.entity.AdminHistorique[ idAdminHistorique=" + idAdminHistorique + " ]";
	}

}
