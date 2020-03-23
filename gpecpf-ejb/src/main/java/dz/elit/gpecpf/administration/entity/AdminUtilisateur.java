/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.administration.entity;

import dz.elit.gpecpf.commun.service.QuerySessionLog;
import dz.elit.gpecpf.commun.util.StaticUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.queries.QueryRedirector;
//import org.hibernate.validator.constraints.Email;

/**
 *
 * @author ayadi
 */
@Entity
@EntityListeners(QuerySessionLog.class)
@Table(name = "admin_utilisateur", schema = StaticUtil.ADMINISTRATION_SCHEMA)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AdminUtilisateur.findByLogin", query = "SELECT u FROM AdminUtilisateur u WHERE u.login=:login"),
    @NamedQuery(name = "AdminUtilisateur.findAll", query = "SELECT u FROM AdminUtilisateur u"),
    @NamedQuery(name = "AdminUtilisateur.findByProfil", query = "SELECT u FROM AdminUtilisateur u WHERE :profil MEMBER OF u.listAdminProfil "),})
public class AdminUtilisateur extends Audit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nom", nullable = false, length = 50)
    private String nom;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "prenom", nullable = false, length = 50)
    private String prenom;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "login", nullable = false, unique = true, length = 100)
    private String login;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "pwd", nullable = false)
    private String pwd;
    //@Email(regexp = "^|(^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$)", message = "Adresse mail incorrecte")
    //@Email(regexp="^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)+$",message="Adresse mail incorecte")
    @Column(name = "email")
    private String email;
    @Pattern(regexp = "([0-9]*)", message = "Format téléphone incorrecte ")
    @Column(name = "tel_bureau")
    private String telephoneBureau;
    @Pattern(regexp = "^|([0-9]{10}$)", message = "Format téléphone incorrecte ")
    @Column(name = "tel_mobile")
    private String telephoneMobile;
    @Pattern(regexp = "([0-9]*)", message = "Format fax incorrecte ")
    @Column(name = "fax")
    private String fax;
    @Column(name = "theme", length = 50)
    private String theme = "elit-metro";
    @Column(name = "adresse1")
    private String adresse1;
    @Column(name = "adresse2")
    private String adresse2;
    @Basic(optional = false)
    @NotNull
    @Column(name = "active", nullable = false)
    private boolean active;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_activation", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dateActivation;
    @Column(name = "date_expiration")
    @Temporal(TemporalType.DATE)
    private Date dateExpiration;
    @JoinTable(name = "admin_utilisateur_profil", joinColumns = {
        @JoinColumn(name = "id_utilisateur", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "id_profil", referencedColumnName = "id")}, schema = StaticUtil.ADMINISTRATION_SCHEMA)
    @ManyToMany
    private List<AdminProfil> listAdminProfil = new ArrayList();
    @JoinColumn(name = "id_unite_organisationnelle", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AdminUniteOrganisationnelle adminUniteOrganisationnelle;

    @OneToMany(mappedBy = "idUtilisateur")
    private List<AdminDroitVisibilite> adminDroitVisibiliteList;

    @ManyToMany(cascade = CascadeType.MERGE, mappedBy = "listMembre")
    private List<AdminGroupe> listAdminGroupe = new ArrayList<>();

//    //pour la versionning
//    @Version
//    private Long version;
    public AdminUtilisateur() {
    }

    public AdminUtilisateur(String nom, String prenom, String login, String pwd, Date dateActivation, List<AdminProfil> listAdminProfils) {
        this.nom = nom;
        this.prenom = prenom;
        this.login = login;
        this.pwd = pwd;
        this.dateActivation = dateActivation;
        this.listAdminProfil = listAdminProfils;        
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {

        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {

        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getLogin() {

        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPwd() {

        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public boolean getActive() {

        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getDateActivation() {

        return dateActivation;
    }

    public void setDateActivation(Date dateActivation) {
        this.dateActivation = dateActivation;
    }

    public Date getDateExpiration() {

        return dateExpiration;
    }

    public void setDateExpiration(Date dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public String getTheme() {

        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void addProfil(AdminProfil profil) {
        this.getListAdminProfil().add(profil);
        profil.getListAdminUtilisateurs().add(this);
    }

    public void addListProfils(List<AdminProfil> profils) {
        for (AdminProfil profil : profils) {
            addProfil(profil);
        }
    }

    public void removeProfil(AdminProfil profil) {
        this.getListAdminProfil().remove(profil);
        profil.getListAdminUtilisateurs().remove(this);
    }

    public List<AdminProfil> getListAdminProfil() {
        return listAdminProfil;
    }

    public void setListAdminProfil(List<AdminProfil> listAdminProfil) {
        this.listAdminProfil = listAdminProfil;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephoneBureau() {

        return telephoneBureau;
    }

    public void setTelephoneBureau(String telephoneBureau) {
        this.telephoneBureau = telephoneBureau;
    }

    public String getTelephoneMobile() {

        return telephoneMobile;
    }

    public void setTelephoneMobile(String telephoneMobile) {
        this.telephoneMobile = telephoneMobile;
    }

    public String getAdresse1() {

        return adresse1;
    }

    public void setAdresse1(String adresse1) {
        this.adresse1 = adresse1;
    }

    public String getAdresse2() {

        return adresse2;
    }

    public void setAdresse2(String adresse2) {
        this.adresse2 = adresse2;
    }

    public String getFax() {

        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public AdminUniteOrganisationnelle getAdminUniteOrganisationnelle() {
        return adminUniteOrganisationnelle;
    }

    public void setAdminUniteOrganisationnelle(AdminUniteOrganisationnelle adminUniteOrganisationnelle) {
        this.adminUniteOrganisationnelle = adminUniteOrganisationnelle;
    }

    public List<AdminDroitVisibilite> getAdminDroitVisibiliteList() {
        return adminDroitVisibiliteList;
    }

    public void setAdminDroitVisibiliteList(List<AdminDroitVisibilite> adminDroitVisibiliteList) {
        this.adminDroitVisibiliteList = adminDroitVisibiliteList;
    }

    public List<AdminGroupe> getListAdminGroupe() {
        return listAdminGroupe;
    }

    public void setListAdminGroupe(List<AdminGroupe> listAdminGroupe) {
        this.listAdminGroupe = listAdminGroupe;
    }

//    public Long getVersion() {
//        return version;
//    }
//
//    public void setVersion(Long version) {
//        this.version = version;
//    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AdminUtilisateur)) {
            return false;
        }
        AdminUtilisateur other = (AdminUtilisateur) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dz.elit.gpecpf.administration.entity.AdminUtilisateur[ id=" + id + " ]";
    }

    public String getLibelleUser() {

        return nom + " " + prenom;
    }
}
