package dz.elit.gpecpf.administration.entity;

import dz.elit.gpecpf.commun.util.StaticUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author laidani.youcef
 */
@Entity
@Table(name = "admin_groupe", schema = StaticUtil.ADMINISTRATION_SCHEMA)
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "AdminGroupe.findAll", query = "SELECT a FROM AdminGroupe a")
	,
    @NamedQuery(name = "AdminGroupe.findById", query = "SELECT a FROM AdminGroupe a WHERE a.id = :id")
	,
    @NamedQuery(name = "AdminGroupe.findByLibelle", query = "SELECT a FROM AdminGroupe a WHERE a.libelle = :libelle")
	,
    @NamedQuery(name = "AdminGroupe.findByDescription", query = "SELECT a FROM AdminGroupe a WHERE a.description = :description")
	,
    @NamedQuery(name = "AdminGroupe.findByLibelleWithoutCurrentId", query = "SELECT g FROM AdminGroupe g WHERE g.libelle =:libelle AND g.id != :id ORDER BY g.libelle")})
public class AdminGroupe implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;

	@Column(name = "libelle")
	private String libelle;

	@Column(name = "description")
	private String description;

	@JoinTable(schema = StaticUtil.ADMINISTRATION_SCHEMA, name = "admin_groupe_utilisateur", joinColumns = {
		@JoinColumn(name = "groupe", referencedColumnName = "id")}, inverseJoinColumns = {
		@JoinColumn(name = "utilisateur", referencedColumnName = "id")})
	@ManyToMany(cascade = CascadeType.MERGE)
	private List<AdminUtilisateur> listMembre = new ArrayList();

	@OneToMany(mappedBy = "idGroupe", cascade = CascadeType.ALL)
	private List<AdminDroitVisibilite> adminDroitVisibiliteList = new ArrayList();

	public AdminGroupe() {
	}

	public AdminGroupe(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<AdminUtilisateur> getListMembre() {
		return listMembre;
	}

	public void setListMembre(List<AdminUtilisateur> listMembre) {
		this.listMembre = listMembre;
	}

	@XmlTransient
	public List<AdminDroitVisibilite> getAdminDroitVisibiliteList() {
		return adminDroitVisibiliteList;
	}

	public void setAdminDroitVisibiliteList(List<AdminDroitVisibilite> adminDroitVisibiliteList) {
		this.adminDroitVisibiliteList = adminDroitVisibiliteList;
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
		if (!(object instanceof AdminGroupe)) {
			return false;
		}
		AdminGroupe other = (AdminGroupe) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "re.AdminGroupe[ id=" + id + " ]";
	}

}
