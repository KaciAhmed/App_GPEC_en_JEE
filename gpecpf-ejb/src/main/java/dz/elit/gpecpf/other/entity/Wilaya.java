package dz.elit.gpecpf.other.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Kaci Ahmed
 */
@Entity
@Table(name = "wilaya")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Wilaya.findAll", query = "SELECT w FROM Wilaya w")
	, @NamedQuery(name = "Wilaya.findById", query = "SELECT w FROM Wilaya w WHERE w.id = :id")
	, @NamedQuery(name = "Wilaya.findByCode", query = "SELECT w FROM Wilaya w WHERE w.code = :code")
	, @NamedQuery(name = "Wilaya.findByNom", query = "SELECT w FROM Wilaya w WHERE w.nom = :nom")})
public class Wilaya implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;
	@Size(max = 50)
	@Column(name = "code")
	private String code;
	@Size(max = 255)
	@Column(name = "nom")
	private String nom;
	@OneToMany(mappedBy = "idwilaya", fetch = FetchType.LAZY)
	private Collection<Commune> communeCollection;

	public Wilaya() {
	}

	public Wilaya(Integer id) {
		this.id = id;
	}

	public Wilaya(String code, String nom) {
		this.code = code;
		this.nom = nom;
	}

	public Wilaya(Integer id, String code, String nom) {
		this.id = id;
		this.code = code;
		this.nom = nom;
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

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	@XmlTransient
	public Collection<Commune> getCommuneCollection() {
		return communeCollection;
	}

	public void setCommuneCollection(Collection<Commune> communeCollection) {
		this.communeCollection = communeCollection;
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
		if (!(object instanceof Wilaya)) {
			return false;
		}
		Wilaya other = (Wilaya) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "otherEntity.Wilaya[ id=" + id + " ]";
	}

}
