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
@Table(name = "poste", schema = StaticUtil.ADMINISTRATION_SCHEMA)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Poste.findAll", query = "SELECT p FROM Poste p")
    , @NamedQuery(name = "Poste.findById", query = "SELECT p FROM Poste p WHERE p.id = :id")
    , @NamedQuery(name = "Poste.findByCode", query = "SELECT p FROM Poste p WHERE p.code = :code")
    , @NamedQuery(name = "Poste.findByDenomination", query = "SELECT p FROM Poste p WHERE p.denomination = :denomination")
    , @NamedQuery(name = "Poste.findByDefsomaire", query = "SELECT p FROM Poste p WHERE p.defsomaire = :defsomaire")
    , @NamedQuery(name = "Poste.findByAnneeexperience", query = "SELECT p FROM Poste p WHERE p.anneeexperience = :anneeexperience")
    , @NamedQuery(name = "Poste.findByDenomant", query = "SELECT p FROM Poste p WHERE p.denomant = :denomant")
    , @NamedQuery(name = "Poste.findByClassant", query = "SELECT p FROM Poste p WHERE p.classant = :classant")
    , @NamedQuery(name = "Poste.findByCodeant", query = "SELECT p FROM Poste p WHERE p.codeant = :codeant")
    , @NamedQuery(name = "Poste.findByQualrequise", query = "SELECT p FROM Poste p WHERE p.qualrequise = :qualrequise")
    , @NamedQuery(name = "Poste.findByClassement", query = "SELECT p FROM Poste p WHERE p.classement = :classement")
    , @NamedQuery(name = "Poste.findByDatecreation", query = "SELECT p FROM Poste p WHERE p.datecreation = :datecreation")
    , @NamedQuery(name = "Poste.findByDatemaj", query = "SELECT p FROM Poste p WHERE p.datemaj = :datemaj")
    , @NamedQuery(name = "Poste.findByDateelabpec", query = "SELECT p FROM Poste p WHERE p.dateelabpec = :dateelabpec")})
public class Poste implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Size(max = 50)
    @Column(name = "code")
    private String code;
    @Size(max = 255)
    @Column(name = "denomination")
    private String denomination;
    @Size(max = 2147483647)
    @Column(name = "defsomaire")
    private String defsomaire;
    @Column(name = "anneeexperience")
    private Integer anneeexperience;
    @Size(max = 255)
    @Column(name = "denomant")
    private String denomant;
    @Size(max = 255)
    @Column(name = "classant")
    private String classant;
    @Size(max = 50)
    @Column(name = "codeant")
    private String codeant;
    @Size(max = 2147483647)
    @Column(name = "qualrequise")
    private String qualrequise;
    @Size(max = 255)
    @Column(name = "classement")
    private String classement;
    @Column(name = "datecreation")
    @Temporal(TemporalType.DATE)
    private Date datecreation;
    @Column(name = "datemaj")
    @Temporal(TemporalType.DATE)
    private Date datemaj;
    @Column(name = "dateelabpec")
    @Temporal(TemporalType.DATE)
    private Date dateelabpec;
    @ManyToMany(mappedBy = "posteCollection")
    private Collection<Mission> missionCollection;
    @ManyToMany(mappedBy = "posteCollection")
    private Collection<Moyen> moyenCollection;
    @ManyToMany(mappedBy = "posteCollection")
    private Collection<Condition> conditionCollection;
    @ManyToMany(mappedBy = "posteCollection")
    private Collection<Formation> formationCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "poste")
    private Collection<Historiqueemployeposte> historiqueemployeposteCollection;
    @JoinColumn(name = "idemploi", referencedColumnName = "id")
    @ManyToOne
    private Emploi idemploi;
    @OneToMany(mappedBy = "idpostmere")
    private Collection<Poste> posteCollection;
    @JoinColumn(name = "idpostmere", referencedColumnName = "id")
    @ManyToOne
    private Poste idpostmere;
    @JoinColumn(name = "idtypeposte", referencedColumnName = "id")
    @ManyToOne
    private Typeposte idtypeposte;
    @JoinColumn(name = "iduo", referencedColumnName = "id")
    @ManyToOne
    private Uniteorganisationnel iduo;

    public Poste() {
    }

    public Poste(Integer id, String code, String denomination, String defsomaire, Integer anneeexperience, String denomant, String classant, String codeant, String qualrequise, String classement, Date datecreation, Date datemaj, Date dateelabpec, Collection<Mission> missionCollection, Collection<Moyen> moyenCollection, Collection<Condition> conditionCollection, Collection<Formation> formationCollection, Collection<Historiqueemployeposte> historiqueemployeposteCollection, Emploi idemploi, Collection<Poste> posteCollection, Poste idpostmere, Typeposte idtypeposte, Uniteorganisationnel iduo) {
        this.id = id;
        this.code = code;
        this.denomination = denomination;
        this.defsomaire = defsomaire;
        this.anneeexperience = anneeexperience;
        this.denomant = denomant;
        this.classant = classant;
        this.codeant = codeant;
        this.qualrequise = qualrequise;
        this.classement = classement;
        this.datecreation = datecreation;
        this.datemaj = datemaj;
        this.dateelabpec = dateelabpec;
        this.missionCollection = missionCollection;
        this.moyenCollection = moyenCollection;
        this.conditionCollection = conditionCollection;
        this.formationCollection = formationCollection;
        this.historiqueemployeposteCollection = historiqueemployeposteCollection;
        this.idemploi = idemploi;
        this.posteCollection = posteCollection;
        this.idpostmere = idpostmere;
        this.idtypeposte = idtypeposte;
        this.iduo = iduo;
    }
    
    public Poste(Integer id) {
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

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public String getDefsomaire() {
        return defsomaire;
    }

    public void setDefsomaire(String defsomaire) {
        this.defsomaire = defsomaire;
    }

    public Integer getAnneeexperience() {
        return anneeexperience;
    }

    public void setAnneeexperience(Integer anneeexperience) {
        this.anneeexperience = anneeexperience;
    }

    public String getDenomant() {
        return denomant;
    }

    public void setDenomant(String denomant) {
        this.denomant = denomant;
    }

    public String getClassant() {
        return classant;
    }

    public void setClassant(String classant) {
        this.classant = classant;
    }

    public String getCodeant() {
        return codeant;
    }

    public void setCodeant(String codeant) {
        this.codeant = codeant;
    }

    public String getQualrequise() {
        return qualrequise;
    }

    public void setQualrequise(String qualrequise) {
        this.qualrequise = qualrequise;
    }

    public String getClassement() {
        return classement;
    }

    public void setClassement(String classement) {
        this.classement = classement;
    }

    public Date getDatecreation() {
        return datecreation;
    }

    public void setDatecreation(Date datecreation) {
        this.datecreation = datecreation;
    }

    public Date getDatemaj() {
        return datemaj;
    }

    public void setDatemaj(Date datemaj) {
        this.datemaj = datemaj;
    }

    public Date getDateelabpec() {
        return dateelabpec;
    }

    public void setDateelabpec(Date dateelabpec) {
        this.dateelabpec = dateelabpec;
    }

    @XmlTransient
    public Collection<Mission> getMissionCollection() {
        return missionCollection;
    }

    public void setMissionCollection(Collection<Mission> missionCollection) {
        this.missionCollection = missionCollection;
    }

    @XmlTransient
    public Collection<Moyen> getMoyenCollection() {
        return moyenCollection;
    }

    public void setMoyenCollection(Collection<Moyen> moyenCollection) {
        this.moyenCollection = moyenCollection;
    }

    @XmlTransient
    public Collection<Condition> getConditionCollection() {
        return conditionCollection;
    }

    public void setConditionCollection(Collection<Condition> conditionCollection) {
        this.conditionCollection = conditionCollection;
    }

    @XmlTransient
    public Collection<Formation> getFormationCollection() {
        return formationCollection;
    }

    public void setFormationCollection(Collection<Formation> formationCollection) {
        this.formationCollection = formationCollection;
    }

    @XmlTransient
    public Collection<Historiqueemployeposte> getHistoriqueemployeposteCollection() {
        return historiqueemployeposteCollection;
    }

    public void setHistoriqueemployeposteCollection(Collection<Historiqueemployeposte> historiqueemployeposteCollection) {
        this.historiqueemployeposteCollection = historiqueemployeposteCollection;
    }

    public Emploi getIdemploi() {
        return idemploi;
    }

    public void setIdemploi(Emploi idemploi) {
        this.idemploi = idemploi;
    }

    @XmlTransient
    public Collection<Poste> getPosteCollection() {
        return posteCollection;
    }

    public void setPosteCollection(Collection<Poste> posteCollection) {
        this.posteCollection = posteCollection;
    }

    public Poste getIdpostmere() {
        return idpostmere;
    }

    public void setIdpostmere(Poste idpostmere) {
        this.idpostmere = idpostmere;
    }

    public Typeposte getIdtypeposte() {
        return idtypeposte;
    }

    public void setIdtypeposte(Typeposte idtypeposte) {
        this.idtypeposte = idtypeposte;
    }

    public Uniteorganisationnel getIduo() {
        return iduo;
    }

    public void setIduo(Uniteorganisationnel iduo) {
        this.iduo = iduo;
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
        if (!(object instanceof Poste)) {
            return false;
        }
        Poste other = (Poste) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "otherEntity.Poste[ id=" + id + " ]";
    }
    
}
