package dz.elit.gpecpf.administration.entity;

import java.io.Serializable;
import java.security.Principal;
import java.util.Date;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.persistence.*;

/**
 *
 * @author Leghettas Rabah, Chekor Samir, Laidani Youcef
 */
@MappedSuperclass
public abstract class Audit implements Serializable {

	@Column(name = "creer_le")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creerLe;

	@Column(name = "modifier_le")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifierLe;

	@JoinColumn(name = "creer_par", referencedColumnName = "id")
	@ManyToOne
	private AdminUtilisateur creerPar;

	@JoinColumn(name = "modifier_par", referencedColumnName = "id")
	@ManyToOne
	private AdminUtilisateur modifierPar;

	@JoinColumn(name = "id_unite_organisationnelle_createur", referencedColumnName = "id")
	@ManyToOne
	private AdminUniteOrganisationnelle adminUniteOrganisationnelleCreateur;

	public Audit() {
	}

	public Date getCreerLe() {
		return creerLe;
	}

	public void setCreerLe(Date creerLe) {
		this.creerLe = creerLe;
	}

	public Date getModifierLe() {
		return modifierLe;
	}

	public void setModifierLe(Date modifierLe) {
		this.modifierLe = modifierLe;
	}

	public AdminUtilisateur getCreerPar() {
		return creerPar;
	}

	public void setCreerPar(AdminUtilisateur creerPar) {
		this.creerPar = creerPar;
	}

	public AdminUtilisateur getModifierPar() {
		return modifierPar;
	}

	public void setModifierPar(AdminUtilisateur modifierPar) {
		this.modifierPar = modifierPar;
	}

	public AdminUniteOrganisationnelle getAdminUniteOrganisationnelleCreateur() {
		return adminUniteOrganisationnelleCreateur;
	}

	public void setAdminUniteOrganisationnelleCreateur(AdminUniteOrganisationnelle adminUniteOrganisationnelleCreateur) {
		this.adminUniteOrganisationnelleCreateur = adminUniteOrganisationnelleCreateur;
	}

	public AdminUtilisateur findUtilisateur() throws NamingException {
		try {
			Principal principal = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
			FacesContext facesContext = FacesContext.getCurrentInstance();
			AdminUtilisateur utilisateur = (AdminUtilisateur) facesContext.getExternalContext().getSessionMap().get(principal.getName());
			if (utilisateur != null) {
				return utilisateur;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.getStackTrace();
			return null;
		}
	}

	@PrePersist
	public void prepersist() throws NamingException {
		AdminUtilisateur adminUtilisateur = findUtilisateur();
		if (adminUtilisateur != null) {
			setCreerPar(adminUtilisateur);
			setCreerLe(new Date());
			// I commented this after I modified something idk lol
			//setAdminUniteOrganisationnelleCreateur(adminUtilisateur.getAdminUniteOrganisationnelle());
		}
	}

	//@PreUpdate
	public void preupdate() throws NamingException {
		AdminUtilisateur adminUtilisateur = findUtilisateur();
		if (adminUtilisateur != null) {
			setModifierPar(adminUtilisateur);
			setModifierLe(new Date());
		}
	}
}
