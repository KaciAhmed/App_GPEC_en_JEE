package dz.elit.gpecpf.administration.entity;

import dz.elit.gpecpf.commun.service.QuerySessionLog;
import dz.elit.gpecpf.commun.util.StaticUtil;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author chekor.samir
 */
@Entity
@EntityListeners(QuerySessionLog.class)
@Table(name = "admin_unite_organisationnelle", schema = StaticUtil.ADMINISTRATION_SCHEMA)
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "AdminUniteOrganisationnelle.findById", query = "SELECT a FROM AdminUniteOrganisationnelle a WHERE a.id = :id")
	,
    @NamedQuery(name = "AdminUniteOrganisationnelle.findByCode", query = "SELECT a FROM AdminUniteOrganisationnelle a WHERE a.code = :code")
	,
    @NamedQuery(name = "AdminUniteOrganisationnelle.findByNiveau", query = "SELECT a FROM AdminUniteOrganisationnelle a WHERE a.niveau = :niveau")})
public class AdminUniteOrganisationnelle extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	private Integer id;
	@Column(name = "adresse_raison_sociale", length = 255)
	private String adresseRaisonSociale;
	@Size(max = 255)
	@Column(name = "adresse_registre_commerce", length = 255)
	private String adresseRegistreCommerce;
	@Size(max = 255)
	@Column(name = "adresse_securite_sociale", length = 255)
	private String adresseSecuriteSociale;
	@Column(name = "capital_social")
	private BigInteger capitalSocial;
	@Size(max = 255)
	@Column(name = "code", unique = true)

	private String code;
	@Size(max = 255)
	@Column(name = "code_activite_fiscal", length = 255)
	private String codeActiviteFiscal;
	@Size(max = 255)
	@Column(name = "code_postal_raison_sociale", length = 255)
	private String codePostalRaisonSociale;
	@Size(max = 255)
	@Column(name = "code_postal_registre_commerce", length = 255)
	private String codePostalRegistreCommerce;
	@Size(max = 255)
	@Column(name = "code_postal_securite_sociale", length = 255)
	private String codePostalSecuriteSociale;
	@Size(max = 255)
	@Column(name = "commune_raison_sociale", length = 255)
	private String communeRaisonSociale;
	@Size(max = 255)
	@Column(name = "commune_registre_commerce", length = 255)
	private String communeRegistreCommerce;
	@Size(max = 255)
	@Column(name = "commune_securite_sociale", length = 255)
	private String communeSecuriteSociale;
	@Column(name = "date_debut_activite")
	@Temporal(TemporalType.DATE)
	private Date dateDebutActivite;
	@Column(name = "date_delivrance")
	@Temporal(TemporalType.DATE)
	private Date dateDelivrance;
	@Size(max = 255)
	@Column(name = "denomination_ar", length = 255)
	private String denominationAr;
	@Size(max = 255)
	@Column(name = "denomination_fr", length = 255)
	private String denominationFr;
	@Pattern(regexp = "^|([a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?)", message = "Invalid email")
	private String email;
	@Column(name = "fax_a")
	private String faxA;
	@Column(name = "fax_b")
	private String faxB;
	@Column(name = "forme_juridique")
	private String formeJuridique;
	private String nis;
	@Column(name = "num_article_imposition")
	private String numArticleImposition;
	@Column(name = "num_fiscal")
	private String numFiscal;
	@Column(name = "num_registre_commerce")
	private String numRegistreCommerce;
	@Column(name = "regime_fiscal")
	private String regimeFiscal;
	private String rib;
	@Column(name = "site_web")
	private String siteWeb;
	@Column(name = "tele_a")
	private String teleA;
	@Column(name = "tele_b")
	private String teleB;
	private String tin;
	@Column(name = "wilaya_raison_sociale")
	private String wilayaRaisonSociale;
	@Column(name = "wilaya_registre_commerce")
	private String wilayaRegistreCommerce;
	@Column(name = "wilaya_securite_sociale")
	private String wilayaSecuriteSociale;
	@Column(name = "niveau")
	private Integer niveau;
	@Column(name = "trie")
	private String trie;
	@OneToMany(mappedBy = "uniteParent")
	private List<AdminUniteOrganisationnelle> adminUniteOrganisationnelleList;
	@JoinColumn(name = "unite_parent", referencedColumnName = "id")
	@ManyToOne
	private AdminUniteOrganisationnelle uniteParent;

	@Transient
	private String affichage;

	public AdminUniteOrganisationnelle() {
	}

	public AdminUniteOrganisationnelle(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAdresseRaisonSociale() {
		return adresseRaisonSociale;
	}

	public void setAdresseRaisonSociale(String adresseRaisonSociale) {
		this.adresseRaisonSociale = adresseRaisonSociale;
	}

	public String getAdresseRegistreCommerce() {
		return adresseRegistreCommerce;
	}

	public void setAdresseRegistreCommerce(String adresseRegistreCommerce) {
		this.adresseRegistreCommerce = adresseRegistreCommerce;
	}

	public String getAdresseSecuriteSociale() {
		return adresseSecuriteSociale;
	}

	public void setAdresseSecuriteSociale(String adresseSecuriteSociale) {
		this.adresseSecuriteSociale = adresseSecuriteSociale;
	}

	public BigInteger getCapitalSocial() {
		return capitalSocial;
	}

	public void setCapitalSocial(BigInteger capitalSocial) {
		this.capitalSocial = capitalSocial;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCodeActiviteFiscal() {
		return codeActiviteFiscal;
	}

	public void setCodeActiviteFiscal(String codeActiviteFiscal) {
		this.codeActiviteFiscal = codeActiviteFiscal;
	}

	public String getCodePostalRaisonSociale() {
		return codePostalRaisonSociale;
	}

	public void setCodePostalRaisonSociale(String codePostalRaisonSociale) {
		this.codePostalRaisonSociale = codePostalRaisonSociale;
	}

	public String getCodePostalRegistreCommerce() {
		return codePostalRegistreCommerce;
	}

	public void setCodePostalRegistreCommerce(String codePostalRegistreCommerce) {
		this.codePostalRegistreCommerce = codePostalRegistreCommerce;
	}

	public String getCodePostalSecuriteSociale() {
		return codePostalSecuriteSociale;
	}

	public void setCodePostalSecuriteSociale(String codePostalSecuriteSociale) {
		this.codePostalSecuriteSociale = codePostalSecuriteSociale;
	}

	public String getCommuneRaisonSociale() {
		return communeRaisonSociale;
	}

	public void setCommuneRaisonSociale(String communeRaisonSociale) {
		this.communeRaisonSociale = communeRaisonSociale;
	}

	public String getCommuneRegistreCommerce() {
		return communeRegistreCommerce;
	}

	public void setCommuneRegistreCommerce(String communeRegistreCommerce) {
		this.communeRegistreCommerce = communeRegistreCommerce;
	}

	public String getCommuneSecuriteSociale() {
		return communeSecuriteSociale;
	}

	public void setCommuneSecuriteSociale(String communeSecuriteSociale) {
		this.communeSecuriteSociale = communeSecuriteSociale;
	}

	public Date getDateDebutActivite() {
		return dateDebutActivite;
	}

	public void setDateDebutActivite(Date dateDebutActivite) {
		this.dateDebutActivite = dateDebutActivite;
	}

	public Date getDateDelivrance() {
		return dateDelivrance;
	}

	public void setDateDelivrance(Date dateDelivrance) {
		this.dateDelivrance = dateDelivrance;
	}

	public String getDenominationAr() {
		return denominationAr;
	}

	public void setDenominationAr(String denominationAr) {
		this.denominationAr = denominationAr;
	}

	public String getDenominationFr() {
		return denominationFr;
	}

	public void setDenominationFr(String denominationFr) {
		this.denominationFr = denominationFr;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFaxA() {
		return faxA;
	}

	public void setFaxA(String faxA) {
		this.faxA = faxA;
	}

	public String getFaxB() {
		return faxB;
	}

	public void setFaxB(String faxB) {
		this.faxB = faxB;
	}

	public String getFormeJuridique() {
		return formeJuridique;
	}

	public void setFormeJuridique(String formeJuridique) {
		this.formeJuridique = formeJuridique;
	}

	public String getNis() {
		return nis;
	}

	public void setNis(String nis) {
		this.nis = nis;
	}

	public String getNumArticleImposition() {
		return numArticleImposition;
	}

	public void setNumArticleImposition(String numArticleImposition) {
		this.numArticleImposition = numArticleImposition;
	}

	public String getNumFiscal() {
		return numFiscal;
	}

	public void setNumFiscal(String numFiscal) {
		this.numFiscal = numFiscal;
	}

	public String getNumRegistreCommerce() {
		return numRegistreCommerce;
	}

	public void setNumRegistreCommerce(String numRegistreCommerce) {
		this.numRegistreCommerce = numRegistreCommerce;
	}

	public String getRegimeFiscal() {
		return regimeFiscal;
	}

	public void setRegimeFiscal(String regimeFiscal) {
		this.regimeFiscal = regimeFiscal;
	}

	public String getRib() {
		return rib;
	}

	public void setRib(String rib) {
		this.rib = rib;
	}

	public String getSiteWeb() {
		return siteWeb;
	}

	public void setSiteWeb(String siteWeb) {
		this.siteWeb = siteWeb;
	}

	public String getTeleA() {
		return teleA;
	}

	public void setTeleA(String teleA) {
		this.teleA = teleA;
	}

	public String getTeleB() {
		return teleB;
	}

	public void setTeleB(String teleB) {
		this.teleB = teleB;
	}

	public String getTin() {
		return tin;
	}

	public void setTin(String tin) {
		this.tin = tin;
	}

	public String getWilayaRaisonSociale() {
		return wilayaRaisonSociale;
	}

	public void setWilayaRaisonSociale(String wilayaRaisonSociale) {
		this.wilayaRaisonSociale = wilayaRaisonSociale;
	}

	public String getWilayaRegistreCommerce() {
		return wilayaRegistreCommerce;
	}

	public void setWilayaRegistreCommerce(String wilayaRegistreCommerce) {
		this.wilayaRegistreCommerce = wilayaRegistreCommerce;
	}

	public String getWilayaSecuriteSociale() {
		return wilayaSecuriteSociale;
	}

	public void setWilayaSecuriteSociale(String wilayaSecuriteSociale) {
		this.wilayaSecuriteSociale = wilayaSecuriteSociale;
	}

	public Integer getNiveau() {
		return niveau;
	}

	public void setNiveau(Integer niveau) {
		this.niveau = niveau;
	}

	public String getTrie() {
		return trie;
	}

	public void setTrie(String trie) {
		this.trie = trie;
	}

	@XmlTransient
	public List<AdminUniteOrganisationnelle> getAdminUniteOrganisationnelleList() {
		return adminUniteOrganisationnelleList;
	}

	public void setAdminUniteOrganisationnelleList(List<AdminUniteOrganisationnelle> adminUniteOrganisationnelleList) {
		this.adminUniteOrganisationnelleList = adminUniteOrganisationnelleList;
	}

	public AdminUniteOrganisationnelle getUniteParent() {
		return uniteParent;
	}

	public void setUniteParent(AdminUniteOrganisationnelle uniteParent) {
		this.uniteParent = uniteParent;
	}

	public String getAffichage() {
		return getVideDe(niveau) + code + "";
	}

	public void setAffichage(String affichage) {
		this.affichage = affichage;
	}

	private String getVideDe(int i) {
		String res = "";
		for (int j = 1; j < i; j++) {
			res = res + "---";
		}
		return res;
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
		if (!(object instanceof AdminUniteOrganisationnelle)) {
			return false;
		}
		AdminUniteOrganisationnelle other = (AdminUniteOrganisationnelle) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "AdminUniteOrganisationnelle[ id=" + id + ",affichage:" + affichage;
	}

}
