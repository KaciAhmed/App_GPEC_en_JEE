package dz.elit.gpecpf.administration.controller;

import dz.elit.gpecpf.administration.entity.AdminUniteOrganisationnelle;
import dz.elit.gpecpf.administration.service.AdminPrefixCodificationFacade;
import dz.elit.gpecpf.administration.service.AdminUniteOrganisationnelleFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.other.entity.Commune;
import dz.elit.gpecpf.other.entity.Wilaya;
import dz.elit.gpecpf.other.service.WilayaFacade;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author chekor.samir
 */
@ManagedBean
@ViewScoped
public class AddUniteOrgController extends AbstractController implements Serializable {

	@EJB
	private AdminUniteOrganisationnelleFacade societeFacade;
	@EJB
	private WilayaFacade wilayaFacade;
	@EJB
	private AdminPrefixCodificationFacade prefixFacade;

	private AdminUniteOrganisationnelle societe;
	private AdminUniteOrganisationnelle societeSelected;

	private List<AdminUniteOrganisationnelle> listUniteOrgs;
	private List<Wilaya> listWilayas;
	private List<Commune> listCommunes;
	private Map<String, String> wilayasMap;
	private Map<String, String> communesMap;
	private Map<String, Map<String, String>> dataMap;

	private Map<String, String> wilayasRegistreCommerceMap;
	private Map<String, String> communesRegistreCommerceMap;
	private Map<String, Map<String, String>> dataRegistreCommerceMap;

	private String codePrefix;

	public AddUniteOrgController() {
	}

	@Override
	protected void initController() {
		try {
			codePrefix = prefixFacade.chercherPrefix().getUnito();
		} catch (Exception e) {
			codePrefix = "";
		}
		societe = new AdminUniteOrganisationnelle();
		societe.setCode(codePrefix);
		societeSelected = new AdminUniteOrganisationnelle();
		listUniteOrgs = societeFacade.findAllOrderByTrie();
		listWilayas = wilayaFacade.findAllOrderByAttribut("nom");
		wilayasMap = new HashMap<>();
		dataMap = new HashMap<>();
		for (Wilaya wilaya : listWilayas) {
			wilayasMap.put(wilaya.getNom(), wilaya.getNom());
			listCommunes = (List<Commune>) wilaya.getCommuneCollection();
			communesMap = new HashMap<>();
			for (Commune commune : listCommunes) {
				communesMap.put(commune.getNom(), commune.getNom());
			}
			dataMap.put(wilaya.getNom(), communesMap);
		}

		wilayasRegistreCommerceMap = new HashMap<>();
		dataRegistreCommerceMap = new HashMap<>();
		for (Wilaya wilaya : listWilayas) {
			wilayasRegistreCommerceMap.put(wilaya.getNom(), wilaya.getNom());
			listCommunes = (List<Commune>) wilaya.getCommuneCollection();
			communesRegistreCommerceMap = new HashMap<>();
			for (Commune commune : listCommunes) {
				communesRegistreCommerceMap.put(commune.getNom(), commune.getNom());
			}
			dataRegistreCommerceMap.put(wilaya.getNom(), communesRegistreCommerceMap);
		}
	}

	public void onWilayaChange() {
		if (societe.getWilayaSecuriteSociale() != null && !societe.getWilayaSecuriteSociale().equals("")) {
			communesMap = dataMap.get(societe.getWilayaSecuriteSociale());
		} else {
			communesMap = new HashMap<>();
		}
	}

	public void onWilayaRegistreCommerceChange() {
		if (societe.getWilayaRegistreCommerce() != null && !societe.getWilayaRegistreCommerce().equals("")) {
			communesRegistreCommerceMap = dataRegistreCommerceMap.get(societe.getWilayaRegistreCommerce());
		} else {
			communesRegistreCommerceMap = new HashMap<>();
		}
	}

	public void create() {
		try {
			checkCode();
			societe.setTrie((societeSelected != null && societeSelected.getId() != null) ? societeSelected.getTrie() + societe.getCode() : societe.getCode());
			if (societeSelected != null && societeSelected.getId() != null) {
				societe.setUniteParent(societeSelected);
			}
			societe.setNiveau((societe.getUniteParent() == null || societe.getUniteParent().getId() == null) ? 1 : (societe.getUniteParent().getNiveau() + 1));
			societeFacade.create(societe);
			if (societeSelected != null && societeSelected.getId() != null) {
				societeSelected.getAdminUniteOrganisationnelleList().add(societe);
				societeFacade.edit(societeSelected);
			}
			MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//"Unité organisationnelle crée avec succè");
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
			if (!societe.getCode().startsWith(codePrefix) || societe.getCode().equals(codePrefix)) {
				throw new MyException("Le code doit commencer avec le prefix: " + codePrefix + " et suivi d'une chaine.");
			}
		}
	}

	// Getter and setter
	public AdminUniteOrganisationnelle getSociete() {
		return societe;
	}

	public void setSociete(AdminUniteOrganisationnelle societe) {
		this.societe = societe;
	}

	public List<AdminUniteOrganisationnelle> getListUniteOrgs() {
		return listUniteOrgs;
	}

	public void setListUniteOrgs(List<AdminUniteOrganisationnelle> listUniteOrgs) {
		this.listUniteOrgs = listUniteOrgs;
	}

	public AdminUniteOrganisationnelle getSocieteSelected() {
		return societeSelected;
	}

	public void setSocieteSelected(AdminUniteOrganisationnelle societeSelected) {
		this.societeSelected = societeSelected;
	}

	public Map<String, String> getWilayasMap() {
		return wilayasMap;
	}

	public void setWilayasMap(Map<String, String> wilayasMap) {
		this.wilayasMap = wilayasMap;
	}

	public Map<String, String> getCommunesMap() {
		return communesMap;
	}

	public void setCommunesMap(Map<String, String> communesMap) {
		this.communesMap = communesMap;
	}

	public Map<String, String> getWilayasRegistreCommerceMap() {
		return wilayasRegistreCommerceMap;
	}

	public void setWilayasRegistreCommerceMap(Map<String, String> wilayasRegistreCommerceMap) {
		this.wilayasRegistreCommerceMap = wilayasRegistreCommerceMap;
	}

	public Map<String, String> getCommunesRegistreCommerceMap() {
		return communesRegistreCommerceMap;
	}

	public void setCommunesRegistreCommerceMap(Map<String, String> communesRegistreCommerceMap) {
		this.communesRegistreCommerceMap = communesRegistreCommerceMap;
	}

	public AdminUniteOrganisationnelleFacade getSocieteFacade() {
		return societeFacade;
	}

	public void setSocieteFacade(AdminUniteOrganisationnelleFacade societeFacade) {
		this.societeFacade = societeFacade;
	}

	public WilayaFacade getWilayaFacade() {
		return wilayaFacade;
	}

	public void setWilayaFacade(WilayaFacade wilayaFacade) {
		this.wilayaFacade = wilayaFacade;
	}

	public AdminPrefixCodificationFacade getPrefixFacade() {
		return prefixFacade;
	}

	public void setPrefixFacade(AdminPrefixCodificationFacade prefixFacade) {
		this.prefixFacade = prefixFacade;
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

	public Map<String, Map<String, String>> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, Map<String, String>> dataMap) {
		this.dataMap = dataMap;
	}

	public Map<String, Map<String, String>> getDataRegistreCommerceMap() {
		return dataRegistreCommerceMap;
	}

	public void setDataRegistreCommerceMap(Map<String, Map<String, String>> dataRegistreCommerceMap) {
		this.dataRegistreCommerceMap = dataRegistreCommerceMap;
	}

	public String getCodePrefix() {
		return codePrefix;
	}

	public void setCodePrefix(String codePrefix) {
		this.codePrefix = codePrefix;
	}

}
