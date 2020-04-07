/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package otherEntity;

import dz.elit.gpecpf.commun.util.StaticUtil;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Dell
 */
@Entity
@Table(name = "employe",schema = StaticUtil.ADMINISTRATION_SCHEMA)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Employe.findAll", query = "SELECT e FROM Employe e")
    , @NamedQuery(name = "Employe.findById", query = "SELECT e FROM Employe e WHERE e.id = :id")
    , @NamedQuery(name = "Employe.findByNom", query = "SELECT e FROM Employe e WHERE e.nom = :nom")
    , @NamedQuery(name = "Employe.findByPrenom", query = "SELECT e FROM Employe e WHERE e.prenom = :prenom")
    , @NamedQuery(name = "Employe.findByEmail", query = "SELECT e FROM Employe e WHERE e.email = :email")
    , @NamedQuery(name = "Employe.findByUsername", query = "SELECT e FROM Employe e WHERE e.username = :username")
    , @NamedQuery(name = "Employe.findByPassword", query = "SELECT e FROM Employe e WHERE e.password = :password")
    , @NamedQuery(name = "Employe.findByAdresse", query = "SELECT e FROM Employe e WHERE e.adresse = :adresse")
    , @NamedQuery(name = "Employe.findByCreerle", query = "SELECT e FROM Employe e WHERE e.creerle = :creerle")
    , @NamedQuery(name = "Employe.findByModiferle", query = "SELECT e FROM Employe e WHERE e.modiferle = :modiferle")
    , @NamedQuery(name = "Employe.findByTel", query = "SELECT e FROM Employe e WHERE e.tel = :tel")})
public class Employe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 50)
    @Column(name = "nom")
    private String nom;
    @Size(max = 50)
    @Column(name = "prenom")
    private String prenom;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "email")
    private String email;
    @Size(max = 255)
    @Column(name = "username")
    private String username;
    @Size(max = 255)
    @Column(name = "password")
    private String password;
    @Size(max = 2147483647)
    @Column(name = "adresse")
    private String adresse;
    @Column(name = "creerle")
    @Temporal(TemporalType.DATE)
    private Date creerle;
    @Column(name = "modiferle")
    @Temporal(TemporalType.DATE)
    private Date modiferle;
    @Size(max = 20)
    @Column(name = "tel")
    private String tel;
    @OneToMany(mappedBy = "creerpar")
    private Collection<Employe> employeCollection;
    @JoinColumn(name = "creerpar", referencedColumnName = "id")
    @ManyToOne
    private Employe creerpar;
    @OneToMany(mappedBy = "modifierpar")
    private Collection<Employe> employeCollection1;
    @JoinColumn(name = "modifierpar", referencedColumnName = "id")
    @ManyToOne
    private Employe modifierpar;
    @OneToMany(mappedBy = "idemploye")
    private Collection<Evaluation> evaluationCollection;
    @OneToMany(mappedBy = "idemp")
    private Collection<Notification> notificationCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employe")
    private Collection<Historiqueemployeposte> historiqueemployeposteCollection;

    public Employe() {
    }

    public Employe(Integer id, String nom, String prenom, String email, String username, String password, String adresse, Date creerle, Date modiferle, String tel, Collection<Employe> employeCollection, Employe creerpar, Collection<Employe> employeCollection1, Employe modifierpar, Collection<Evaluation> evaluationCollection, Collection<Notification> notificationCollection, Collection<Historiqueemployeposte> historiqueemployeposteCollection) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.username = username;
        this.password = password;
        this.adresse = adresse;
        this.creerle = creerle;
        this.modiferle = modiferle;
        this.tel = tel;
        this.employeCollection = employeCollection;
        this.creerpar = creerpar;
        this.employeCollection1 = employeCollection1;
        this.modifierpar = modifierpar;
        this.evaluationCollection = evaluationCollection;
        this.notificationCollection = notificationCollection;
        this.historiqueemployeposteCollection = historiqueemployeposteCollection;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Date getCreerle() {
        return creerle;
    }

    public void setCreerle(Date creerle) {
        this.creerle = creerle;
    }

    public Date getModiferle() {
        return modiferle;
    }

    public void setModiferle(Date modiferle) {
        this.modiferle = modiferle;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @XmlTransient
    public Collection<Employe> getEmployeCollection() {
        return employeCollection;
    }

    public void setEmployeCollection(Collection<Employe> employeCollection) {
        this.employeCollection = employeCollection;
    }

    public Employe getCreerpar() {
        return creerpar;
    }

    public void setCreerpar(Employe creerpar) {
        this.creerpar = creerpar;
    }

    @XmlTransient
    public Collection<Employe> getEmployeCollection1() {
        return employeCollection1;
    }

    public void setEmployeCollection1(Collection<Employe> employeCollection1) {
        this.employeCollection1 = employeCollection1;
    }

    public Employe getModifierpar() {
        return modifierpar;
    }

    public void setModifierpar(Employe modifierpar) {
        this.modifierpar = modifierpar;
    }

    @XmlTransient
    public Collection<Evaluation> getEvaluationCollection() {
        return evaluationCollection;
    }

    public void setEvaluationCollection(Collection<Evaluation> evaluationCollection) {
        this.evaluationCollection = evaluationCollection;
    }

    @XmlTransient
    public Collection<Notification> getNotificationCollection() {
        return notificationCollection;
    }

    public void setNotificationCollection(Collection<Notification> notificationCollection) {
        this.notificationCollection = notificationCollection;
    }

    @XmlTransient
    public Collection<Historiqueemployeposte> getHistoriqueemployeposteCollection() {
        return historiqueemployeposteCollection;
    }

    public void setHistoriqueemployeposteCollection(Collection<Historiqueemployeposte> historiqueemployeposteCollection) {
        this.historiqueemployeposteCollection = historiqueemployeposteCollection;
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
        if (!(object instanceof Employe)) {
            return false;
        }
        Employe other = (Employe) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "otherEntity.Employe[ id=" + id + " ]";
    }
    
}
