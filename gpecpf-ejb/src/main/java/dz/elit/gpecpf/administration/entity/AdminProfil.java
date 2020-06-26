package dz.elit.gpecpf.administration.entity;

import dz.elit.gpecpf.commun.util.StaticUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ayadi
 */
@Entity
@Table(name = "admin_profil", schema = StaticUtil.ADMINISTRATION_SCHEMA)
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "AdminProfil.findById", query = "SELECT a FROM AdminProfil a WHERE a.id = 1")
	,
    @NamedQuery(name = "AdminProfil.findByLibelleWithoutCurrentId", query = "SELECT p FROM AdminProfil p WHERE p.libelle =:libelle AND p.id != :id ORDER BY p.libelle  "),})
public class AdminProfil implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1)
	@Column(name = "libelle", nullable = false, unique = true)
	private String libelle;
	@Basic(optional = false)
	@NotNull
	@Column(name = "description", nullable = false)
	private String description;
	@JoinTable(name = "admin_profil_privilege", joinColumns = {
		@JoinColumn(name = "id_profil", referencedColumnName = "id")}, inverseJoinColumns = {
		@JoinColumn(name = "id_privilege", referencedColumnName = "id")}, schema = StaticUtil.ADMINISTRATION_SCHEMA)
	@ManyToMany
	private List<AdminPrivilege> listAdminPrivilege = new ArrayList<>();
	@ManyToMany(mappedBy = "listAdminProfil")
	private List<AdminUtilisateur> listAdminUtilisateurs = new ArrayList<>();

	public AdminProfil() {
	}

	public AdminProfil(String libelle, String description, List<AdminPrivilege> listPrivileges) {
		this.libelle = libelle;
		this.description = description;
		this.listAdminPrivilege = listPrivileges;
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

	@XmlTransient
	public List<AdminPrivilege> getListAdminPrivilege() {
		return listAdminPrivilege;
	}

	public void setListAdminPrivilege(List<AdminPrivilege> listAdminPrivilege) {
		this.listAdminPrivilege = listAdminPrivilege;
	}

	public List<AdminUtilisateur> getListAdminUtilisateurs() {
		return listAdminUtilisateurs;
	}

	public void setListAdminUtilisateurs(List<AdminUtilisateur> listAdminUtilisateurs) {
		this.listAdminUtilisateurs = listAdminUtilisateurs;
	}

	public void addUtilisateur(AdminUtilisateur utilisateur) {
		this.getListAdminUtilisateurs().add(utilisateur);
		utilisateur.getListAdminProfil().add(this);
	}

	public void removeUtilisateur(AdminUtilisateur utilisateur) {
		this.getListAdminUtilisateurs().remove(utilisateur);
		utilisateur.getListAdminProfil().remove(this);
	}

	public void addListUtilisateurs(List<AdminUtilisateur> utilisateurs) {
		for (AdminUtilisateur utilisateur : utilisateurs) {
			addUtilisateur(utilisateur);
		}
	}

	public void addPrivilege(AdminPrivilege privilege) {
		this.getListAdminPrivilege().add(privilege);
		privilege.getListAdminProfils().add(this);
	}

	public void removePrivilege(AdminPrivilege privilege) {
		this.getListAdminPrivilege().remove(privilege);
		privilege.getListAdminProfils().remove(this);
	}

	public void addListPrivileges(List<AdminPrivilege> privileges) {
		for (AdminPrivilege privilege : privileges) {
			addPrivilege(privilege);
		}
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
		if (!(object instanceof AdminProfil)) {
			return false;
		}
		AdminProfil other = (AdminProfil) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "dz.elit.gpecpf.administration.entity.AdminProfil[ id=" + id + " ]";
	}

}
