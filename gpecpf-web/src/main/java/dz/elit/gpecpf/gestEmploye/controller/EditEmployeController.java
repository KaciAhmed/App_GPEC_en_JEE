package dz.elit.gpecpf.gestEmploye.controller;

import dz.elit.gpecpf.administration.entity.AdminUtilisateur;
import dz.elit.gpecpf.administration.service.AdminPrefixCodificationFacade;
import dz.elit.gpecpf.administration.service.AdminUtilisateurFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.employe.entity.Employe;
import dz.elit.gpecpf.gestion_employe.service.EmployeFacade;
import dz.elit.gpecpf.other.entity.Commune;
import dz.elit.gpecpf.other.entity.Historiqueemployeposte;
import dz.elit.gpecpf.other.entity.Wilaya;
import dz.elit.gpecpf.other.service.CommuneFacade;
import dz.elit.gpecpf.other.service.WilayaFacade;
import dz.elit.gpecpf.poste.entity.Formation;
import dz.elit.gpecpf.poste.entity.Poste;
import dz.elit.gpecpf.poste.service.FormationFacade;
import dz.elit.gpecpf.poste.service.PosteFacade;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author Kaci Ahmed
 */
@ManagedBean
@ViewScoped
public class EditEmployeController extends AbstractController implements Serializable {

	@EJB
	private EmployeFacade empFacade;
	@EJB
	private FormationFacade formationFacade;

	@EJB
	private WilayaFacade wilayaFacade;
	@EJB
	private CommuneFacade communeFacade;
	@EJB
	private AdminUtilisateurFacade userFacade;
	@EJB
	private PosteFacade posteFacade;
	@EJB
	private AdminPrefixCodificationFacade prefixFacade;

	private List<AdminUtilisateur> listUser;

	private Employe emp;

	private List<Formation> listFormations;
	private List<Formation> listFormationsSelected;

	private List<Wilaya> listWilayas;
	private Wilaya wilayaSelected;
	private Wilaya wil;

	private List<Commune> listCommunes;
	private Commune communeSelected;
	private int idComune;

	private String dtNaiss;
	private String dtRec;
	private String dtDep;
	private Date oldDateDep = null;

	private String oldMatricule;

	SimpleDateFormat formater = null;

	private Historiqueemployeposte histEmpPoste;
	private List<Historiqueemployeposte> listHistEmpOrdone;
	private List<Poste> listPostes;
	private List<Poste> listPostesTaken;
	private List<Poste> listPosteEmp;
	private Poste posteSelected;
	private Poste posteEmp;
	private Poste oldPoste;

	private int ageMinimale;
	private int ageMax;

	private String codePrefix;

	public EditEmployeController() {
	}

	@Override//@PostConstruct
	protected void initController() {
		try {
			codePrefix = prefixFacade.chercherPrefix().getEmploye();
		} catch (Exception e) {
			codePrefix = "";
		}
		initEditEmploye();
		String id = MyUtil.getRequestParameter("id");
		// partie date
		if (id != null) {
			emp = empFacade.find(Integer.parseInt(id));
			formater = new SimpleDateFormat("dd-MM-yyyy");
			dtNaiss = formater.format(emp.getDtNaissance());
			if (emp.getDate_depart() != null) {
				dtDep = formater.format(emp.getDate_depart());
			} else {
				dtRec = formater.format(emp.getDate_recrutement());
			}
			// partie wilaya commune
			recupWilaya();
			idComune = emp.getIdcommune().getId();
			CommuneParWilaya();
			// partie formation   
			listFormationsSelected = emp.getListFormation();
			listFormations = formationFacade.findAllOrderByAttribut("description");
			listFormations.removeAll(listFormationsSelected);

			oldMatricule = emp.getMatricule();
			if (emp.getDate_depart() != null) {
				oldDateDep = emp.getDate_depart();
			}

			// partie poste
			recupPosteEmploye();
			listHistEmpOrdone = new ArrayList<>();
			listPostes.add(posteEmp);
			ordonerListHistEmp();
		}
	}

	private void initEditEmploye() {
		emp = new Employe();
		//prtie wilaya commune
		listWilayas = new ArrayList();
		listWilayas = wilayaFacade.findAllOrderByAttribut("nom");
		wilayaSelected = new Wilaya();
		idComune = 0;
		listCommunes = new ArrayList<>();
		communeSelected = new Commune();
		listFormations = new ArrayList<>();
		listFormationsSelected = new ArrayList<>();
		// partie user
		listUser = new ArrayList<>();
		// partie poste
		listPostes = new ArrayList<>();
		listPostes = posteFacade.findAllOrderByAttribut("code");
		listPostesTaken = new ArrayList<>();
		listPostesTaken = empFacade.postesFromEmployes();
		listPostes.removeAll(listPostesTaken);
		listPosteEmp = new ArrayList<>();
		ageMinimale = 18;
		ageMax = 70;
	}
	// partie poste

	public String recupDateFormat(Date dt) {
		if (dt != null) {
			formater = new SimpleDateFormat("dd/MM/yyyy");
			return formater.format(dt);
		}
		return "pas encore arriver";
	}

	private void recupPosteEmploye() {

		for (Historiqueemployeposte hist : emp.getListHistoriqueEmployePoste()) {
			if (hist.getDatefin() == null || (emp.getDate_depart() != null && emp.getDate_depart().equals(hist.getDatefin()))) {
				posteEmp = new Poste();
				posteEmp = hist.getPoste();
				posteSelected = new Poste();
				posteSelected = hist.getPoste();
			}
			listPosteEmp.add(hist.getPoste());
		}
	}

	public void ordonerListHistEmp() {
		Historiqueemployeposte hist2 = new Historiqueemployeposte();

		for (Historiqueemployeposte hist : emp.getListHistoriqueEmployePoste()) {
			if (hist.getDatefin() != null && (hist.getHistoriqueemployepostePK().getDatedeb().equals(hist.getDatefin()) || hist.getHistoriqueemployepostePK().getDatedeb().after(hist.getDatefin()))) {
				// rien faire
			} else {
				if (hist.getDatefin() == null || hist.getDatefin().equals(emp.getDate_depart())) {
					hist2 = hist;
				} else {
					listHistEmpOrdone.add(hist);
				}
			}
		}
		listHistEmpOrdone.add(hist2);
	}

	public void addPosteForEmp() {
		if (posteSelected != null && posteSelected.getCode() != null) {
			oldPoste = new Poste();
			oldPoste = posteEmp;
			posteEmp = posteSelected;
		}
	}

	public void creerHistoriqueEmployePoste() {

		Historiqueemployeposte hp2 = recupHistFromPoste(oldPoste);
		hp2.setDatefin(new Date());

		GregorianCalendar cal = new GregorianCalendar();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		Date demain = cal.getTime();
		Historiqueemployeposte hp = recupHistFromPoste(posteSelected);
		if ((hp != null && recupDateFormat(hp.getDatefin()).equals(recupDateFormat(new Date())))) {
			hp.setDatefin(emp.getDate_depart());
		} else {
			hp = new Historiqueemployeposte(emp.getId(), posteSelected.getId(), new Date());
			hp.setEmploye(emp);
			hp.setPoste(posteSelected);
			hp.getHistoriqueemployepostePK().setDatedeb(demain);
			if (emp.getDate_depart() != null) {
				hp.setDatefin(emp.getDate_depart());
			}
			emp.getListHistoriqueEmployePoste().add(hp);
			posteSelected.getListHistoriqueEmployePoste().add(hp);
		}
	}

	public Historiqueemployeposte recupHistFromPoste(Poste post1) {

		List<Historiqueemployeposte> listHist = new ArrayList<>(emp.getListHistoriqueEmployePoste());
		Collections.sort(listHist);
		int dernier = listHist.size();
		dernier--;
		for (int i = dernier; i >= 0; i--) {
			if (listHist.get(i).getPoste().equals(post1)) {
				return listHist.get(i);
			}

		}

		return null;
	}

	public void modifierHistorique() {
		Historiqueemployeposte hp2 = recupHistFromPoste(posteEmp);
		if (emp.getDate_depart() != null) {
			hp2.setDatefin(emp.getDate_depart());
		} else {
			hp2.setDatefin(null);
		}

	}

	// pertie wilaya commune
	public void recupWilaya() {
		wil = new Wilaya();
		wil = emp.getIdcommune().getIdwilaya();

	}

	public void CommuneParWilaya() {
		if (wil != null) {
			wilayaSelected = wilayaFacade.find(wil.getId());
			listCommunes = new ArrayList<>();
			for (Commune com : wilayaSelected.getCommuneCollection()) {
				listCommunes.add(com);
			}
			Collections.sort(listCommunes);
		}

	}

	public void obtenirCommune() {

		if (idComune != 0) {
			communeSelected = communeFacade.find(idComune);
			emp.setIdcommune(communeSelected);
		}

	}
	// partie formation

	public void addFormationForEmploye() {
		if (!listFormationsSelected.isEmpty()) {

			emp.addListFormation(listFormationsSelected);
			listFormations.removeAll(listFormationsSelected);
			listFormationsSelected = new ArrayList<>();
		}
	}

	public void removeFormationForEmploye(Formation frm) {
		emp.removeFormation(frm);
		listFormations.add(frm);

	}
	// edit -----------------------

	private boolean isDateVerifier() {
		if (emp.getDate_recrutement().after(new Date())) {
			MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_date_recrutement"));
			return false;
		} else if (emp.getDate_depart() != null && emp.getDate_depart().before(new Date())) {

			MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_date_depart"));
			return false;
		} else if (emp.getDate_depart() != null) {
			if (emp.getDate_recrutement().after(emp.getDate_depart())) {
				MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_date_recrutement_superieur_date_depart"));
				return false;
			}
		}
		return true;
	}

	private boolean isExisteMatricule(String matricule) {
		Employe emp2 = empFacade.findByMatricule(matricule);
		if (emp2 == null) {
			return false;
		} else {
			return true;
		}
	}

	private boolean isExisteUser() {
		// le cas ou le champ est vide
		if (emp.getUserName().isEmpty()) {
			return true;
		} else {
			listUser = userFacade.findByNomPrenomLoginForEmp(emp.getNom(), emp.getPrenom(), emp.getUserName());
			if (!listUser.isEmpty()) {
				return true;
			}
			return false;
		}
	}

	private boolean userDejaAffecterAvant() {
		// le cas ou le champ est vide
		if (emp.getUserName().isEmpty()) {
			return false;
		} else {
			Employe emp3 = null;
			if (!listUser.isEmpty()) {
				emp3 = empFacade.findByUserName(listUser.get(0).getLogin());
				if (emp3 != null && !emp3.getMatricule().equals(emp.getMatricule())) {
					return true;
				}
			}
			return false;
		}

	}

	private Boolean vrfDateNaissance() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(emp.getDtNaissance());
		int annee_naiss = calendar.get(Calendar.YEAR);
		int mois_naiss = calendar.get(Calendar.MONTH);
		int jour_naiss = calendar.get(Calendar.DATE);
		calendar = Calendar.getInstance();
		calendar.setTime(emp.getDate_recrutement());
		int anneRec = calendar.get(Calendar.YEAR);
		int moisRec = calendar.get(Calendar.MONTH);
		int jourRec = calendar.get(Calendar.DATE);
		if (annee_naiss + ageMinimale < anneRec) {
			return true;
		} else if (annee_naiss + ageMinimale == anneRec) {
			if (mois_naiss < moisRec) {
				return true;
			} else if (mois_naiss == moisRec && jour_naiss <= jourRec) {
				return true;
			}
		}
		return false;
	}

	private Boolean vrfAgeMax() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(emp.getDtNaissance());
		int annee_naiss = calendar.get(Calendar.YEAR);
		int mois_naiss = calendar.get(Calendar.MONTH);
		int jour_naiss = calendar.get(Calendar.DATE);
		calendar = Calendar.getInstance();
		calendar.setTime(emp.getDate_recrutement());
		int anneRec = calendar.get(Calendar.YEAR);
		int moisRec = calendar.get(Calendar.MONTH);
		int jourRec = calendar.get(Calendar.DATE);
		if (annee_naiss + ageMax > anneRec) {
			return true;
		} else if (annee_naiss + ageMax == anneRec) {
			if (mois_naiss > moisRec) {
				return true;
			} else if (mois_naiss == moisRec && jour_naiss > jourRec) {
				return true;
			}
		}
		return false;
	}

	private Boolean vrfDtNaiss_DtRec() {
		if (emp.getDate_recrutement().before(emp.getDtNaissance())) {
			return false;
		}
		return true;
	}

	public void edit() {
		try {
			checkCode();
			if (isDateVerifier()) {
				emp.setMatricule(emp.getMatricule().toUpperCase());
				if (isExisteMatricule(emp.getMatricule()) && !emp.getMatricule().equals(oldMatricule)) {
					MyUtil.addErrorMessage(MyUtil.getBundleCommun(" msg_erreur_existe_matricule"));//Erreur inconu   
				} else if (emp.getListFormation().isEmpty()) {
					MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_list_formation_vide"));//Erreur inconu   
				} else if (!isExisteUser()) {
					MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_utilisateur_inexistant"));//Erreur inconu   
				} else if (userDejaAffecterAvant()) {
					MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_utilisateur_deja_affecte"));//Erreur inconu   
				} else if (!vrfAgeMax()) {
					MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_employe_vieux"));
				} else if (!vrfDtNaiss_DtRec()) {
					MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_dt_naissance_sup_dt_recrutement"));
				} else if ((oldPoste != null && !oldPoste.equals(posteEmp))) {
					creerHistoriqueEmployePoste();
				} else if ((oldDateDep == null && emp.getDate_depart() != null) || (oldDateDep != null && emp.getDate_depart() != null && !oldDateDep.equals(emp.getDate_depart()) || (oldDateDep != null && emp.getDate_depart() == null))) {
					modifierHistorique();
				}
			}
			empFacade.edit(emp);
			oldMatricule = emp.getMatricule();
			MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//Employé édité avec succès

		} catch (MyException ex) {
			ex.printStackTrace();
			MyUtil.addErrorMessage(ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
		}
	}

	public void checkCode() throws MyException {
		if (!codePrefix.equals("")) {
			if (!emp.getMatricule().startsWith(codePrefix) || emp.getMatricule().equals(codePrefix)) {
				throw new MyException("Le matricule doit commencer avec le prefix: " + codePrefix + " et suivi d'une chaine.");
			}
		}
	}

	// getter && setter 
	public Employe getEmp() {
		return emp;
	}

	public void setEmp(Employe emp) {
		this.emp = emp;
	}

	public String getDtNaiss() {
		return dtNaiss;
	}

	public void setDtNaiss(String dtNaiss) {
		this.dtNaiss = dtNaiss;
	}

	public String getDtRec() {
		return dtRec;
	}

	public void setDtRec(String dtRec) {
		this.dtRec = dtRec;
	}

	public String getDtDep() {
		return dtDep;
	}

	public void setDtDep(String dtDep) {
		this.dtDep = dtDep;
	}

	public List<Wilaya> getListWilayas() {
		return listWilayas;
	}

	public void setListWilayas(List<Wilaya> listWilayas) {
		this.listWilayas = listWilayas;
	}

	public List<Commune> getListCommunes() {
		return listCommunes;
	}

	public void setListCommunes(List<Commune> listCommunes) {
		this.listCommunes = listCommunes;
	}

	public int getIdComune() {
		return idComune;
	}

	public void setIdComune(int idComune) {
		this.idComune = idComune;
	}
// partie formation

	public List<Formation> getListFormations() {
		return listFormations;
	}

	public void setListFormations(List<Formation> listFormations) {
		this.listFormations = listFormations;
	}

	public List<Formation> getListFormationsSelected() {
		return listFormationsSelected;
	}

	public void setListFormationsSelected(List<Formation> listFormationsSelected) {
		this.listFormationsSelected = listFormationsSelected;
	}

	public Wilaya getWil() {
		return wil;
	}

	public void setWil(Wilaya wil) {
		this.wil = wil;
	}

	public List<Poste> getListPostes() {
		return listPostes;
	}

	public void setListPostes(List<Poste> listPostes) {
		this.listPostes = listPostes;
	}

	public List<Poste> getListPosteEmp() {
		return listPosteEmp;
	}

	public void setListPosteEmp(List<Poste> listPosteEmp) {
		this.listPosteEmp = listPosteEmp;
	}

	public Poste getPosteSelected() {
		return posteSelected;
	}

	public void setPosteSelected(Poste posteSelected) {
		this.posteSelected = posteSelected;
	}

	public Poste getPosteEmp() {
		return posteEmp;
	}

	public void setPosteEmp(Poste posteEmp) {
		this.posteEmp = posteEmp;
	}

	public EmployeFacade getEmpFacade() {
		return empFacade;
	}

	public void setEmpFacade(EmployeFacade empFacade) {
		this.empFacade = empFacade;
	}

	public FormationFacade getFormationFacade() {
		return formationFacade;
	}

	public void setFormationFacade(FormationFacade formationFacade) {
		this.formationFacade = formationFacade;
	}

	public WilayaFacade getWilayaFacade() {
		return wilayaFacade;
	}

	public void setWilayaFacade(WilayaFacade wilayaFacade) {
		this.wilayaFacade = wilayaFacade;
	}

	public CommuneFacade getCommuneFacade() {
		return communeFacade;
	}

	public void setCommuneFacade(CommuneFacade communeFacade) {
		this.communeFacade = communeFacade;
	}

	public AdminUtilisateurFacade getUserFacade() {
		return userFacade;
	}

	public void setUserFacade(AdminUtilisateurFacade userFacade) {
		this.userFacade = userFacade;
	}

	public PosteFacade getPosteFacade() {
		return posteFacade;
	}

	public void setPosteFacade(PosteFacade posteFacade) {
		this.posteFacade = posteFacade;
	}

	public AdminPrefixCodificationFacade getPrefixFacade() {
		return prefixFacade;
	}

	public void setPrefixFacade(AdminPrefixCodificationFacade prefixFacade) {
		this.prefixFacade = prefixFacade;
	}

	public List<AdminUtilisateur> getListUser() {
		return listUser;
	}

	public void setListUser(List<AdminUtilisateur> listUser) {
		this.listUser = listUser;
	}

	public Wilaya getWilayaSelected() {
		return wilayaSelected;
	}

	public void setWilayaSelected(Wilaya wilayaSelected) {
		this.wilayaSelected = wilayaSelected;
	}

	public Commune getCommuneSelected() {
		return communeSelected;
	}

	public void setCommuneSelected(Commune communeSelected) {
		this.communeSelected = communeSelected;
	}

	public String getOldMatricule() {
		return oldMatricule;
	}

	public void setOldMatricule(String oldMatricule) {
		this.oldMatricule = oldMatricule;
	}

	public SimpleDateFormat getFormater() {
		return formater;
	}

	public void setFormater(SimpleDateFormat formater) {
		this.formater = formater;
	}

	public Historiqueemployeposte getHistEmpPoste() {
		return histEmpPoste;
	}

	public void setHistEmpPoste(Historiqueemployeposte histEmpPoste) {
		this.histEmpPoste = histEmpPoste;
	}

	public Poste getOldPoste() {
		return oldPoste;
	}

	public void setOldPoste(Poste oldPoste) {
		this.oldPoste = oldPoste;
	}

	public int getAgeMinimale() {
		return ageMinimale;
	}

	public void setAgeMinimale(int ageMinimale) {
		this.ageMinimale = ageMinimale;
	}

	public String getCodePrefix() {
		return codePrefix;
	}

	public void setCodePrefix(String codePrefix) {
		this.codePrefix = codePrefix;
	}

	public Date getOldDateDep() {
		return oldDateDep;
	}

	public void setOldDateDep(Date oldDateDep) {
		this.oldDateDep = oldDateDep;
	}

	public List<Historiqueemployeposte> getListHistEmpOrdone() {
		return listHistEmpOrdone;
	}

	public void setListHistEmpOrdone(List<Historiqueemployeposte> listHistEmpOrdone) {
		this.listHistEmpOrdone = listHistEmpOrdone;
	}

	public List<Poste> getListPostesTaken() {
		return listPostesTaken;
	}

	public void setListPostesTaken(List<Poste> listPostesTaken) {
		this.listPostesTaken = listPostesTaken;
	}

}
