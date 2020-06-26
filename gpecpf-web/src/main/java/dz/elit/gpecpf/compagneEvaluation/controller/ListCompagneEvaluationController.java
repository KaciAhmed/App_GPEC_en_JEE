package dz.elit.gpecpf.compagneEvaluation.controller;

import dz.elit.gpecpf.administration.entity.Notification;
import dz.elit.gpecpf.administration.service.NotificationFacade;
import dz.elit.gpecpf.commun.controller.Imprimer;
import dz.elit.gpecpf.commun.reporting.engine.Reporting;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.compagne_evaluation.entity.Compagneevaluation;
import dz.elit.gpecpf.employe.entity.Employe;
import dz.elit.gpecpf.gestion_compagne_evaluation.service.CompagneEvaluationFacade;
import dz.elit.gpecpf.gestion_employe.service.EmployeFacade;
import dz.elit.gpecpf.poste.entity.Poste;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Kaci Ahmed
 */
@ManagedBean
@ViewScoped
public class ListCompagneEvaluationController extends AbstractController implements Serializable {

	@EJB
	private CompagneEvaluationFacade compagneFacade;
	@EJB
	private EmployeFacade employeFacade;
	@EJB
	private NotificationFacade notificationFacade;

	@ManagedProperty(value = "#{imprimer}")
	private Imprimer ctrImprimer;

	private List<Compagneevaluation> listCompagne;

	SimpleDateFormat formater = null;
	SimpleDateFormat formater2 = null;

	boolean compagneEnded = false;

	//Les variables de recherche
	private String code;
	private Date dateDeb;
	private Date dateFin;

	private boolean noNew;

	public ListCompagneEvaluationController() {
	}

	@Override //PostConstruct
	protected void initController() {
		try {
			compagneEnded = compagneFacade.checkOutdatedCompagnes();
		} catch (Exception e) {

		}
		if (compagneEnded) {
			List<Employe> employes = employeFacade.findAllOrderByAttribut("id");
			for (Employe employe : employes) {
				Poste poste = employe.recupPosteEmploye();
				if (poste != null) {
					Notification notif = new Notification();
					notif.setDateNotification(new Date());
					notif.setEmploye(employe);
					notif.setVerifiee(0);
					notif.setLibelle("La compagne d'évaluation viens de s'achever");
					notif.setLien("/pages/emp/evaluation/listEval.xhtml");
					try {
						notificationFacade.create(notif);
					} catch (Exception ex) {

					}
				}
			}
		}
		formater = new SimpleDateFormat("yyyy-MM-dd");
		formater2 = new SimpleDateFormat("dd-MM-yyyy");
		findList();
		noNew = compagneFacade.activeCompagne();
	}

	private void findList() {
		listCompagne = new ArrayList<>();
		listCompagne = compagneFacade.findAllOrderByAttribut("code");
		// rechercher();
	}

	public void rechercher() {
		listCompagne = compagneFacade.findByCodeDatedebDatefin(code, dateDeb, dateFin);
		if (listCompagne.isEmpty() || listCompagne.size() < 1) {
			MyUtil.addInfoMessage(MyUtil.getBundleAdmin("msg_resultat_recherche_null"));
		}
	}

	public String formaterDate(Date dt) {
		return formater2.format(dt);
	}

	public void remove(Compagneevaluation compagne) {
		try {
			// à revoir pour le cas de la suppréssion des évaluations
			compagneFacade.remove(compagne);
			noNew = compagneFacade.activeCompagne();
			MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//"Utilisateur supprimé");
			findList();
		} catch (Exception ex) {
			ex.printStackTrace();
			MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
		}
	}

	public void download() throws SQLException, IOException {
		String rapportLien = "/dz/elit/harmo/commun/reporting/source/listUtilisateur.jasper";
		InputStream rapport = getClass().getResourceAsStream(rapportLien);
		String SUBREPORT_DIR = getClass().getResource("/dz/elit/harmo/commun/reporting/source/Entete/").getFile();
		String rapportNom = "rapportNom";
		String entreprisFr = "entreprisFr";
		String entreprisAr = "entreprisAr";
		String iSoRapport = "iSoRapport";
		InputStream urlLogo = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/images/images-login/logo.png");
		Map parametres = new HashMap();
		JRBeanCollectionDataSource data = new JRBeanCollectionDataSource(listCompagne);
		parametres.put("rapportNom", rapportNom);
		parametres.put("entreprisFr", entreprisFr);
		parametres.put("entreprisAr", entreprisAr);
		parametres.put("iSoRapport", iSoRapport);
		parametres.put("iMgDir", urlLogo);
		parametres.put("SUBREPORT_DIR", SUBREPORT_DIR);
		ctrImprimer.setData(data);
		ctrImprimer.setParametres(parametres);
		ctrImprimer.setRapport(rapport);
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", false);
		options.put("resizable", false);
		options.put("width", 350);
		options.put("contentHeight", 90);
		options.put("contentWidth", 310);
		RequestContext.getCurrentInstance().openDialog("/pages/commun/download.xhtml", options, null);
	}

	public String telecharger() throws IOException, JRException {

		Map<String, String> param = new HashMap<>();
		param.put("rapportNom", "Test");

		Reporting.printEtat(getClass().getResourceAsStream("/dz/elit/gpecpf/reporting/source/test.jasper"),
				param, new JRBeanCollectionDataSource(listCompagne));
		return "";

	}

	public void creerRapportUnique() throws JRException, FileNotFoundException {

		String rapportLien = "/reporting/source/listUtilisateur.jasper";
		InputStream rapport = getClass().getResourceAsStream(rapportLien);
		String rapportNom = "Test";
		String entreprisFr = "Elit";
		String entreprisAr = "شركة";
		String iSoRapport = "iSoRapport";
		String SUBREPORT_DIR = getClass().getResource("/dz/elit/harmo/commun/reporting/source/Entete/").getFile();
		InputStream urlLogo = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/images/images-login/logo.png");
		Map parametres = new HashMap();
		JRBeanCollectionDataSource data = new JRBeanCollectionDataSource(listCompagne);
		parametres.put("rapportNom", rapportNom);
		parametres.put("entreprisFr", entreprisFr);
		parametres.put("entreprisAr", entreprisAr);
		parametres.put("iSoRapport", iSoRapport);
		parametres.put("iMgDir", urlLogo);
		parametres.put("SUBREPORT_DIR", SUBREPORT_DIR);

		Reporting.downloadReportPdf(rapport, data, parametres);

	}
	// getter && setter 

	public Imprimer getCtrImprimer() {
		return ctrImprimer;
	}

	public void setCtrImprimer(Imprimer ctrImprimer) {
		this.ctrImprimer = ctrImprimer;
	}

	public List<Compagneevaluation> getListCompagne() {
		return listCompagne;
	}

	public void setListCompagne(List<Compagneevaluation> listCompagne) {
		this.listCompagne = listCompagne;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getDateDeb() {
		return dateDeb;
	}

	public void setDateDeb(Date dateDeb) {
		this.dateDeb = dateDeb;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public CompagneEvaluationFacade getCompagneFacade() {
		return compagneFacade;
	}

	public void setCompagneFacade(CompagneEvaluationFacade compagneFacade) {
		this.compagneFacade = compagneFacade;
	}

	public SimpleDateFormat getFormater() {
		return formater;
	}

	public void setFormater(SimpleDateFormat formater) {
		this.formater = formater;
	}

	public SimpleDateFormat getFormater2() {
		return formater2;
	}

	public void setFormater2(SimpleDateFormat formater2) {
		this.formater2 = formater2;
	}

	public boolean getNoNew() {
		return noNew;
	}

	public void setNoNew(boolean noNew) {
		this.noNew = noNew;
	}

}
