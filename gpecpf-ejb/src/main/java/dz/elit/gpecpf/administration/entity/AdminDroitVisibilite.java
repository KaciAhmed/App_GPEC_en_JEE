package dz.elit.gpecpf.administration.entity;

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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author laidani.youcef
 */
@Entity
@Table(name = "admin_droit_visibilite", schema = StaticUtil.ADMINISTRATION_SCHEMA)
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "AdminDroitVisibilite.findAll", query = "SELECT a FROM AdminDroitVisibilite a")
	,
    @NamedQuery(name = "AdminDroitVisibilite.findById", query = "SELECT a FROM AdminDroitVisibilite a WHERE a.id = :id")
	,
    @NamedQuery(name = "AdminDroitVisibilite.findByUtilisateur", query = "SELECT a FROM AdminDroitVisibilite a WHERE a.idUtilisateur.id = :utilisateur")
	,
    @NamedQuery(name = "AdminDroitVisibilite.findByUtilisateurEntity", query = "SELECT a FROM AdminDroitVisibilite a WHERE a.idUtilisateur.id = :utilisateur AND a.idObjetVisibilite.id =:entity")
	,
    @NamedQuery(name = "AdminDroitVisibilite.findByGroupe", query = "SELECT a FROM AdminDroitVisibilite a WHERE a.idGroupe.id = :groupe")
	,
    @NamedQuery(name = "AdminDroitVisibilite.findByGroupeEntity", query = "SELECT a FROM AdminDroitVisibilite a WHERE a.idGroupe.id = :groupe AND a.idObjetVisibilite.id =:entity")
})
public class AdminDroitVisibilite implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;
	@JoinColumn(name = "id_objet_visibilite", referencedColumnName = "id")
	@ManyToOne
	private AdminObjetVisibilite idObjetVisibilite;
	@JoinColumn(name = "id_unite_organisationnelle", referencedColumnName = "id")
	@ManyToOne
	private AdminUniteOrganisationnelle idUniteOrganisationnelle;
	@JoinColumn(name = "id_utilisateur", referencedColumnName = "id")
	@ManyToOne
	private AdminUtilisateur idUtilisateur;

	@JoinColumn(name = "id_groupe", referencedColumnName = "id")
	@ManyToOne
	private AdminGroupe idGroupe;

	public AdminDroitVisibilite() {
	}

	public AdminDroitVisibilite(AdminObjetVisibilite idObjetVisibilite, AdminUniteOrganisationnelle idUniteOrganisationnelle, AdminUtilisateur idUtilisateur) {
		this.idObjetVisibilite = idObjetVisibilite;
		this.idUniteOrganisationnelle = idUniteOrganisationnelle;
		this.idUtilisateur = idUtilisateur;
	}

	public AdminDroitVisibilite(AdminObjetVisibilite idObjetVisibilite, AdminUniteOrganisationnelle idUniteOrganisationnelle,
			AdminGroupe idGroupe) {
		this.idObjetVisibilite = idObjetVisibilite;
		this.idUniteOrganisationnelle = idUniteOrganisationnelle;
		this.idGroupe = idGroupe;
	}

	public AdminDroitVisibilite(AdminObjetVisibilite idObjetVisibilite, AdminUniteOrganisationnelle idUniteOrganisationnelle) {
		this.idObjetVisibilite = idObjetVisibilite;
		this.idUniteOrganisationnelle = idUniteOrganisationnelle;
	}

	public AdminDroitVisibilite(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public AdminObjetVisibilite getIdObjetVisibilite() {
		return idObjetVisibilite;
	}

	public void setIdObjetVisibilite(AdminObjetVisibilite idObjetVisibilite) {
		this.idObjetVisibilite = idObjetVisibilite;
	}

	public AdminUniteOrganisationnelle getIdUniteOrganisationnelle() {
		return idUniteOrganisationnelle;
	}

	public void setIdUniteOrganisationnelle(AdminUniteOrganisationnelle idUniteOrganisationnelle) {
		this.idUniteOrganisationnelle = idUniteOrganisationnelle;
	}

	public AdminUtilisateur getIdUtilisateur() {
		return idUtilisateur;
	}

	public void setIdUtilisateur(AdminUtilisateur idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
	}

	public AdminGroupe getIdGroupe() {
		return idGroupe;
	}

	public void setIdGroupe(AdminGroupe idGroupe) {
		this.idGroupe = idGroupe;
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
		if (!(object instanceof AdminDroitVisibilite)) {
			return false;
		}
		AdminDroitVisibilite other = (AdminDroitVisibilite) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "re.AdminDroitVisibilite[ id=" + id + " ]";
	}

}
