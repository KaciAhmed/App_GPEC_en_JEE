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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Dell
 */
@Entity
@Table(name = "uniteorganisationnel",schema = StaticUtil.ADMINISTRATION_SCHEMA)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Uniteorganisationnel.findAll", query = "SELECT u FROM Uniteorganisationnel u")
    , @NamedQuery(name = "Uniteorganisationnel.findById", query = "SELECT u FROM Uniteorganisationnel u WHERE u.id = :id")
    , @NamedQuery(name = "Uniteorganisationnel.findByCode", query = "SELECT u FROM Uniteorganisationnel u WHERE u.code = :code")
    , @NamedQuery(name = "Uniteorganisationnel.findByDenomination", query = "SELECT u FROM Uniteorganisationnel u WHERE u.denomination = :denomination")
    , @NamedQuery(name = "Uniteorganisationnel.findByCapitalsocial", query = "SELECT u FROM Uniteorganisationnel u WHERE u.capitalsocial = :capitalsocial")
    , @NamedQuery(name = "Uniteorganisationnel.findByDatedebactivite", query = "SELECT u FROM Uniteorganisationnel u WHERE u.datedebactivite = :datedebactivite")
    , @NamedQuery(name = "Uniteorganisationnel.findByEmail", query = "SELECT u FROM Uniteorganisationnel u WHERE u.email = :email")
    , @NamedQuery(name = "Uniteorganisationnel.findByFormejuridique", query = "SELECT u FROM Uniteorganisationnel u WHERE u.formejuridique = :formejuridique")
    , @NamedQuery(name = "Uniteorganisationnel.findByTel", query = "SELECT u FROM Uniteorganisationnel u WHERE u.tel = :tel")})
public class Uniteorganisationnel implements Serializable {

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
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "capitalsocial")
    private Double capitalsocial;
    @Column(name = "datedebactivite")
    @Temporal(TemporalType.DATE)
    private Date datedebactivite;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "email")
    private String email;
    @Size(max = 2147483647)
    @Column(name = "formejuridique")
    private String formejuridique;
    @Size(max = 255)
    @Column(name = "tel")
    private String tel;
    @JoinColumn(name = "idcommune", referencedColumnName = "id")
    @ManyToOne
    private Commune idcommune;
    @OneToMany(mappedBy = "iduo")
    private Collection<Poste> posteCollection;

    public Uniteorganisationnel() {
    }

    public Uniteorganisationnel(Integer id, String code, String denomination, Double capitalsocial, Date datedebactivite, String email, String formejuridique, String tel, Commune idcommune, Collection<Poste> posteCollection) {
        this.id = id;
        this.code = code;
        this.denomination = denomination;
        this.capitalsocial = capitalsocial;
        this.datedebactivite = datedebactivite;
        this.email = email;
        this.formejuridique = formejuridique;
        this.tel = tel;
        this.idcommune = idcommune;
        this.posteCollection = posteCollection;
    }
    
    public Uniteorganisationnel(Integer id) {
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

    public Double getCapitalsocial() {
        return capitalsocial;
    }

    public void setCapitalsocial(Double capitalsocial) {
        this.capitalsocial = capitalsocial;
    }

    public Date getDatedebactivite() {
        return datedebactivite;
    }

    public void setDatedebactivite(Date datedebactivite) {
        this.datedebactivite = datedebactivite;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFormejuridique() {
        return formejuridique;
    }

    public void setFormejuridique(String formejuridique) {
        this.formejuridique = formejuridique;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Commune getIdcommune() {
        return idcommune;
    }

    public void setIdcommune(Commune idcommune) {
        this.idcommune = idcommune;
    }

    @XmlTransient
    public Collection<Poste> getPosteCollection() {
        return posteCollection;
    }

    public void setPosteCollection(Collection<Poste> posteCollection) {
        this.posteCollection = posteCollection;
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
        if (!(object instanceof Uniteorganisationnel)) {
            return false;
        }
        Uniteorganisationnel other = (Uniteorganisationnel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "otherEntity.Uniteorganisationnel[ id=" + id + " ]";
    }
    
}
