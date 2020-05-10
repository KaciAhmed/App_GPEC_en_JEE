/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.administration.entity;

import dz.elit.gpecpf.commun.util.StaticUtil;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Kaci Ahmed
 */
@Entity
@Table(name = "prefixcodification", schema = StaticUtil.ADMINISTRATION_SCHEMA)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Prefixcodification.findAll", query = "SELECT p FROM Prefixcodification p")
    , @NamedQuery(name = "Prefixcodification.findById", query = "SELECT p FROM Prefixcodification p WHERE p.id = :id")
    , @NamedQuery(name = "Prefixcodification.findByDomcomp", query = "SELECT p FROM Prefixcodification p WHERE p.domcomp = :domcomp")
    , @NamedQuery(name = "Prefixcodification.findByTypecomp", query = "SELECT p FROM Prefixcodification p WHERE p.typecomp = :typecomp")
    , @NamedQuery(name = "Prefixcodification.findByComp", query = "SELECT p FROM Prefixcodification p WHERE p.comp = :comp")
    , @NamedQuery(name = "Prefixcodification.findByComport", query = "SELECT p FROM Prefixcodification p WHERE p.comport = :comport")
    , @NamedQuery(name = "Prefixcodification.findByEmploye", query = "SELECT p FROM Prefixcodification p WHERE p.employe = :employe")
    , @NamedQuery(name = "Prefixcodification.findByNotif", query = "SELECT p FROM Prefixcodification p WHERE p.notif = :notif")
    , @NamedQuery(name = "Prefixcodification.findByUnito", query = "SELECT p FROM Prefixcodification p WHERE p.unito = :unito")
    , @NamedQuery(name = "Prefixcodification.findByEmploi", query = "SELECT p FROM Prefixcodification p WHERE p.emploi = :emploi")
    , @NamedQuery(name = "Prefixcodification.findByTypeposte", query = "SELECT p FROM Prefixcodification p WHERE p.typeposte = :typeposte")
    , @NamedQuery(name = "Prefixcodification.findByPoste", query = "SELECT p FROM Prefixcodification p WHERE p.poste = :poste")
    , @NamedQuery(name = "Prefixcodification.findByForm", query = "SELECT p FROM Prefixcodification p WHERE p.form = :form")
    , @NamedQuery(name = "Prefixcodification.findByCond", query = "SELECT p FROM Prefixcodification p WHERE p.cond = :cond")
    , @NamedQuery(name = "Prefixcodification.findByMoy", query = "SELECT p FROM Prefixcodification p WHERE p.moy = :moy")
    , @NamedQuery(name = "Prefixcodification.findByMiss", query = "SELECT p FROM Prefixcodification p WHERE p.miss = :miss")
    , @NamedQuery(name = "Prefixcodification.findByAct", query = "SELECT p FROM Prefixcodification p WHERE p.act = :act")
    , @NamedQuery(name = "Prefixcodification.findByTache", query = "SELECT p FROM Prefixcodification p WHERE p.tache = :tache")
    , @NamedQuery(name = "Prefixcodification.findByCompeva", query = "SELECT p FROM Prefixcodification p WHERE p.compeva = :compeva")
    , @NamedQuery(name = "Prefixcodification.findByEva", query = "SELECT p FROM Prefixcodification p WHERE p.eva = :eva")
    , @NamedQuery(name = "Prefixcodification.findByAvis", query = "SELECT p FROM Prefixcodification p WHERE p.avis = :avis")})
public class Prefixcodification implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 50)
    @Column(name = "domcomp")
    private String domcomp;
    @Size(max = 50)
    @Column(name = "typecomp")
    private String typecomp;
    @Size(max = 50)
    @Column(name = "comp")
    private String comp;
    @Size(max = 50)
    @Column(name = "comport")
    private String comport;
    @Size(max = 50)
    @Column(name = "employe")
    private String employe;
    @Size(max = 50)
    @Column(name = "notif")
    private String notif;
    @Size(max = 50)
    @Column(name = "unito")
    private String unito;
    @Size(max = 50)
    @Column(name = "emploi")
    private String emploi;
    @Size(max = 50)
    @Column(name = "typeposte")
    private String typeposte;
    @Size(max = 50)
    @Column(name = "poste")
    private String poste;
    @Size(max = 50)
    @Column(name = "form")
    private String form;
    @Size(max = 50)
    @Column(name = "cond")
    private String cond;
    @Size(max = 50)
    @Column(name = "moy")
    private String moy;
    @Size(max = 50)
    @Column(name = "miss")
    private String miss;
    @Size(max = 50)
    @Column(name = "act")
    private String act;
    @Size(max = 50)
    @Column(name = "tache")
    private String tache;
    @Size(max = 50)
    @Column(name = "compeva")
    private String compeva;
    @Size(max = 50)
    @Column(name = "eva")
    private String eva;
    @Size(max = 50)
    @Column(name = "avis")
    private String avis;

    public Prefixcodification() {
    }

    public Prefixcodification(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDomcomp() {
        return domcomp;
    }

    public void setDomcomp(String domcomp) {
        this.domcomp = domcomp;
    }

    public String getTypecomp() {
        return typecomp;
    }

    public void setTypecomp(String typecomp) {
        this.typecomp = typecomp;
    }

    public String getComp() {
        return comp;
    }

    public void setComp(String comp) {
        this.comp = comp;
    }

    public String getComport() {
        return comport;
    }

    public void setComport(String comport) {
        this.comport = comport;
    }

    public String getEmploye() {
        return employe;
    }

    public void setEmploye(String employe) {
        this.employe = employe;
    }

    public String getNotif() {
        return notif;
    }

    public void setNotif(String notif) {
        this.notif = notif;
    }

    public String getUnito() {
        return unito;
    }

    public void setUnito(String unito) {
        this.unito = unito;
    }

    public String getEmploi() {
        return emploi;
    }

    public void setEmploi(String emploi) {
        this.emploi = emploi;
    }

    public String getTypeposte() {
        return typeposte;
    }

    public void setTypeposte(String typeposte) {
        this.typeposte = typeposte;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getCond() {
        return cond;
    }

    public void setCond(String cond) {
        this.cond = cond;
    }

    public String getMoy() {
        return moy;
    }

    public void setMoy(String moy) {
        this.moy = moy;
    }

    public String getMiss() {
        return miss;
    }

    public void setMiss(String miss) {
        this.miss = miss;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getTache() {
        return tache;
    }

    public void setTache(String tache) {
        this.tache = tache;
    }

    public String getCompeva() {
        return compeva;
    }

    public void setCompeva(String compeva) {
        this.compeva = compeva;
    }

    public String getEva() {
        return eva;
    }

    public void setEva(String eva) {
        this.eva = eva;
    }

    public String getAvis() {
        return avis;
    }

    public void setAvis(String avis) {
        this.avis = avis;
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
        if (!(object instanceof Prefixcodification)) {
            return false;
        }
        Prefixcodification other = (Prefixcodification) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dz.elit.gpecpf.administration.entity.Prefixcodification[ id=" + id + " ]";
    }
    
}
