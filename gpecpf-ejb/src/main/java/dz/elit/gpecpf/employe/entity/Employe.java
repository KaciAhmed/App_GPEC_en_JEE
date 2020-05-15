/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.employe.entity;
import dz.elit.gpecpf.commun.service.QuerySessionLog;
import dz.elit.gpecpf.commun.util.StaticUtil;
import dz.elit.gpecpf.poste.entity.Formation;
import dz.elit.gpecpf.poste.entity.Poste;
import java.io.Serializable;
import java.util.ArrayList;
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
import org.eclipse.persistence.queries.QueryRedirector;
import otherEntity.Commune;
import otherEntity.Evaluation;
import otherEntity.Historiqueemployeposte;
import otherEntity.Notification;

/**
 *
 * @author Kaci Ahmed
 */
@Entity
@Table(name = "employe", schema = StaticUtil.ADMINISTRATION_SCHEMA)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Employe.findAll", query = "SELECT e FROM Employe e")
    , @NamedQuery(name = "Employe.findById", query = "SELECT e FROM Employe e WHERE e.id = :id")
    , @NamedQuery(name = "Employe.findByMatricule", query = "SELECT e FROM Employe e WHERE e.matricule = :matricule")
    , @NamedQuery(name = "Employe.findByNom", query = "SELECT e FROM Employe e WHERE e.nom = :nom")
    , @NamedQuery(name = "Employe.findByPrenom", query = "SELECT e FROM Employe e WHERE e.prenom = :prenom")
    , @NamedQuery(name = "Employe.findByDtNaissance", query = "SELECT e FROM Employe e WHERE e.dtNaissance = :dtNaissance")
    , @NamedQuery(name = "Employe.findByEmail", query = "SELECT e FROM Employe e WHERE e.email = :email")
    , @NamedQuery(name = "Employe.findByAdresse", query = "SELECT e FROM Employe e WHERE e.adresse = :adresse")
    , @NamedQuery(name = "Employe.findByDate_recrutement", query = "SELECT e FROM Employe e WHERE e.date_recrutement = :date_recrutement")
    , @NamedQuery(name = "Employe.findByDate_depart", query = "SELECT e FROM Employe e WHERE e.date_depart = :date_depart")
    , @NamedQuery(name = "Employe.findBySexe", query = "SELECT e FROM Employe e WHERE e.sexe = :sexe")
    , @NamedQuery(name = "Employe.findByClassement", query = "SELECT e FROM Employe e WHERE e.classement = :classement")
    , @NamedQuery(name = "Employe.findByUniteAffectation", query = "SELECT e FROM Employe e WHERE e.uniteAffectation = :uniteAffectation")
    , @NamedQuery(name = "Employe.findByTel", query = "SELECT e FROM Employe e WHERE e.tel = :tel")
    , @NamedQuery(name = "Employe.findByTypeContrat", query = "SELECT e FROM Employe e WHERE e.typeContrat = :typeContrat")
    , @NamedQuery(name = "Employe.findByUserName", query = "SELECT e FROM Employe e WHERE e.userName = :userName")})
public class Employe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 50)
    @Column(name = "matricule")
    private String matricule;
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
    @Column(name = "sexe")
    private String sexe;
    @Size(max = 50)
    @Column(name = "classement")
    private String classement;
    @Size(max = 255)
    @Column(name = "uniteaffectation")
    private String uniteAffectation;
    @Size(max = 255)
    @Column(name = "typecontrat")
    private String typeContrat;
    @Size(max = 20)
    @Column(name = "tel")
    private String tel;
    @Size(max = 255)
    @Column(name = "userName")
    private String userName;
    @JoinTable(
        name = "employeformation", joinColumns = {
            @JoinColumn(name = "idemploye", referencedColumnName = "id")
        }, inverseJoinColumns = {
            @JoinColumn(name = "idform", referencedColumnName = "id")
            }, schema = StaticUtil.ADMINISTRATION_SCHEMA
    )
    @ManyToMany
    private List<Formation> listFormation = new ArrayList();
    @JoinColumn(name = "idcommune", referencedColumnName = "id")
    @ManyToOne
    private Commune idcommune;
    @OneToMany(mappedBy = "idemploye")
    private List <Evaluation> listEvaluation=new ArrayList<>();
    @OneToMany(mappedBy = "idemp")
    private List <Notification> listNotification=new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employe")
    private List<Historiqueemployeposte> listHistoriqueEmployePoste=new ArrayList<>();;

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

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
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

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getClassement() {
        return classement;
    }

    public void setClassement(String classement) {
        this.classement = classement;
    }

    public String getUniteAffectation() {
        return uniteAffectation;
    }

    public void setUniteAffectation(String uniteAffectation) {
        this.uniteAffectation = uniteAffectation;
    }
  
    public String getTypeContrat() {
        return typeContrat;
    }

    public void setTypeContrat(String typecontrat) {
        this.typeContrat = typecontrat;
    }
   
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public List<Formation> getListFormation() {
        return listFormation;
    }

    public void setListFormation(List<Formation> listFormation) {
        this.listFormation = listFormation;
    }
 
    public Commune getIdcommune() {
        return idcommune;
    }

    public void setIdcommune(Commune idcommune) {
        this.idcommune = idcommune;
    }

    public List<Evaluation> getListEvaluation() {
        return listEvaluation;
    }

    public void setListEvaluation(List<Evaluation> listEvaluation) {
        this.listEvaluation = listEvaluation;
    }

    public List<Notification> getListNotification() {
        return listNotification;
    }

    public void setListNotification(List<Notification> listNotification) {
        this.listNotification = listNotification;
    }

    public List<Historiqueemployeposte> getListHistoriqueEmployePoste() {
        return listHistoriqueEmployePoste;
    }

    public void setListHistoriqueEmployePoste(List<Historiqueemployeposte> listHistoriqueEmployePoste) {
        this.listHistoriqueEmployePoste = listHistoriqueEmployePoste;
    }
    
    public void addFormation(Formation frm) {

        this.getListFormation().add(frm);
        frm.getListEmploye().add(this);
    }
    public void addListFormation(List<Formation> lstFrm) {
        for (Formation frm : lstFrm) {
            this.addFormation(frm);
        }
    }
     public void removeFormation(Formation frm) {
         this.getListFormation().remove(frm);            
         frm.getListEmploye().remove(this);
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
