package dz.elit.gpecpf.other.entity;

import dz.elit.gpecpf.commun.util.StaticUtil;
import dz.elit.gpecpf.employe.entity.Employe;
import java.io.Serializable;
import java.util.Collection;
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
@Table(name = "commune", schema = StaticUtil.ADMINISTRATION_SCHEMA)
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Commune.findAll", query = "SELECT c FROM Commune c")
	, @NamedQuery(name = "Commune.findById", query = "SELECT c FROM Commune c WHERE c.id = :id")
	, @NamedQuery(name = "Commune.findByCode", query = "SELECT c FROM Commune c WHERE c.code = :code")
	, @NamedQuery(name = "Commune.findByNom", query = "SELECT c FROM Commune c WHERE c.nom = :nom")})
public class Commune implements Comparable, Serializable {

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
	@OneToMany(mappedBy = "idcommune")
	private Collection<Employe> employeCollection;
	@JoinColumn(name = "idwilaya", referencedColumnName = "id")
	@ManyToOne
	private Wilaya idwilaya;

	public Commune() {
	}

	public Commune(Integer id) {
		this.id = id;
	}

	public Commune(String code, String nom, Wilaya idwilaya) {
		this.code = code;
		this.nom = nom;
		this.idwilaya = idwilaya;
	}

	public Commune(Integer id, String code, String nom, Wilaya idwilaya) {
		this.id = id;
		this.code = code;
		this.nom = nom;
		this.idwilaya = idwilaya;
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
	public Collection<Employe> getEmployeCollection() {
		return employeCollection;
	}

	public void setEmployeCollection(Collection<Employe> employeCollection) {
		this.employeCollection = employeCollection;
	}

	public Wilaya getIdwilaya() {
		return idwilaya;
	}

	public void setIdwilaya(Wilaya idwilaya) {
		this.idwilaya = idwilaya;
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
		if (!(object instanceof Commune)) {
			return false;
		}
		Commune other = (Commune) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	public int compareTo(Object o) {
		Commune com = (Commune) o;
		return this.getNom().compareToIgnoreCase(com.getNom());
	}

	@Override
	public String toString() {
		return "otherEntity.Commune[ id=" + id + " ]";
	}

}
