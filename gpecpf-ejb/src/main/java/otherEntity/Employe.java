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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Dell
 */
@Entity
@Table(name = "employe", schema = StaticUtil.ADMINISTRATION_SCHEMA)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Employe.findAll", query = "SELECT e FROM Employe e")
    , @NamedQuery(name = "Employe.findById", query = "SELECT e FROM Employe e WHERE e.id = :id")
    , @NamedQuery(name = "Employe.findBycode", query = "SELECT e FROM Employe e WHERE e.code = :code")
    , @NamedQuery(name = "Employe.findByNom", query = "SELECT e FROM Employe e WHERE e.nom = :nom")
    , @NamedQuery(name = "Employe.findByPrenom", query = "SELECT e FROM Employe e WHERE e.prenom = :prenom")
    , @NamedQuery(name = "Employe.findByDtNaissance", query = "SELECT e FROM Employe e WHERE e.dtNaissance = :dtNaissance")
    , @NamedQuery(name = "Employe.findByEmail", query = "SELECT e FROM Employe e WHERE e.email = :email")
    , @NamedQuery(name = "Employe.findByAdresse", query = "SELECT e FROM Employe e WHERE e.adresse = :adresse")
    , @NamedQuery(name = "Employe.findByDate_recrutement", query = "SELECT e FROM Employe e WHERE e.date_recrutement = :date_recrutement")
    , @NamedQuery(name = "Employe.findByDate_depart", query = "SELECT e FROM Employe e WHERE e.date_depart = :date_depart")
    , @NamedQuery(name = "Employe.findByGrade", query = "SELECT e FROM Employe e WHERE e.grade = :grade")
    , @NamedQuery(name = "Employe.findByClassement", query = "SELECT e FROM Employe e WHERE e.classement = :classement")
    , @NamedQuery(name = "Employe.findByCodeservice", query = "SELECT e FROM Employe e WHERE e.codeservice = :codeservice")
    , @NamedQuery(name = "Employe.findByTel", query = "SELECT e FROM Employe e WHERE e.tel = :tel")
    , @NamedQuery(name = "Employe.findBySpecialit\u00e9", query = "SELECT e FROM Employe e WHERE e.specialit\u00e9 = :specialit\u00e9")
    , @NamedQuery(name = "Employe.findByAutre", query = "SELECT e FROM Employe e WHERE e.autre = :autre")})
public class Employe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 50)
    @Column(name = "code")
    private String code;
    @Size(max = 50)
    @Column(name = "nom")
    private String nom;
    @Size(max = 50)
    @Column(name = "prenom")
    private String prenom;
    @Column(name = "dt_naissance")
    @Temporal(TemporalType.DATE)
    private Date dtNaissance;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "email")
    private String email;
    @Size(max = 2147483647)
    @Column(name = "adresse")
    private String adresse;
    @Column(name = "date_recrutement")
    @Temporal(TemporalType.DATE)
    private Date date_recrutement;
    @Column(name = "date_depart")
    @Temporal(TemporalType.DATE)
    private Date date_depart;
    @Size(max = 50)
    @Column(name = "grade")
    private String grade;
    @Size(max = 50)
    @Column(name = "classement")
    private String classement;
    @Size(max = 50)
    @Column(name = "codeservice")
    private String codeservice;
    @Size(max = 20)
    @Column(name = "tel")
    private String tel;
    @Size(max = 50)
    @Column(name = "specialit\u00e9")
    private String specialité;
    @Size(max = 50)
    @Column(name = "autre")
    private String autre;
    @JoinTable(name = "employeformation", joinColumns = {
        @JoinColumn(name = "idemploye", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "idform", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Formation> formationCollection;
    @JoinColumn(name = "idcommune", referencedColumnName = "id")
    @ManyToOne
    private Commune idcommune;
    @OneToMany(mappedBy = "idemploye")
    private Collection<Evaluation> evaluationCollection;
    @OneToMany(mappedBy = "idemp")
    private Collection<Notification> notificationCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employe")
    private Collection<Historiqueemployeposte> historiqueemployeposteCollection;

    public Employe() {
    }

    public Employe(Integer id) {
        this.id = id;
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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Date getDtNaissance() {
        return dtNaissance;
    }

    public void setDtNaissance(Date dtNaissance) {
        this.dtNaissance = dtNaissance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getClassement() {
        return classement;
    }

    public void setClassement(String classement) {
        this.classement = classement;
    }

    public String getCodeservice() {
        return codeservice;
    }

    public void setCodeservice(String codeservice) {
        this.codeservice = codeservice;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getSpecialité() {
        return specialité;
    }

    public void setSpecialité(String specialité) {
        this.specialité = specialité;
    }

    public String getAutre() {
        return autre;
    }

    public void setAutre(String autre) {
        this.autre = autre;
    }

    public Date getDate_recrutement() {
        return date_recrutement;
    }

    public void setDate_recrutement(Date date_recrutement) {
        this.date_recrutement = date_recrutement;
    }

    public Date getDate_depart() {
        return date_depart;
    }

    public void setDate_depart(Date date_depart) {
        this.date_depart = date_depart;
    }
    

    @XmlTransient
    public Collection<Formation> getFormationCollection() {
        return formationCollection;
    }

    public void setFormationCollection(Collection<Formation> formationCollection) {
        this.formationCollection = formationCollection;
    }

    public Commune getIdcommune() {
        return idcommune;
    }

    public void setIdcommune(Commune idcommune) {
        this.idcommune = idcommune;
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

    public void addFormation(Formation frm) {
        this.getFormationCollection().add(frm);
        frm.getEmployeCollection().add(this);
    }
    public void addListFormation(List<Formation> lstFrm) {
        for (Formation frm : lstFrm) {
            addFormation(frm);
        }
    }
     public void removeFormation(Formation frm) {
         this.getFormationCollection().remove(frm);
         frm.getEmployeCollection().remove(this);
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
