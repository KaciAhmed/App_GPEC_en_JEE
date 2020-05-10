package dz.elit.gpecpf.referentiel.controller;

import dz.elit.gpecpf.administration.entity.Prefixcodification;
import dz.elit.gpecpf.administration.service.AdminPrefixCodificationFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.poste.entity.Activite;
import dz.elit.gpecpf.poste.entity.Tache;
import dz.elit.gpecpf.poste.service.ActiviteFacade;
import dz.elit.gpecpf.poste.service.TacheFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Nadir Ben Mohand
 */
@ManagedBean
@ViewScoped
public class AddActiviteController extends AbstractController implements Serializable {
    @EJB
    private ActiviteFacade activiteFacade;
    @EJB
    private TacheFacade tacheFacade;
    @EJB
    private AdminPrefixCodificationFacade prefFacade;
    
    private  List<Prefixcodification> listPrefix;

    private Activite activite;
    
    private List<Tache> listTaches;
    private List<Tache> listTachesSelected;
	
    private String code;
    private String libelle;
    private String description;

    public AddActiviteController() {
    }

    @Override//@PostConstruct
    protected void initController() {
        initAddActivite();
    }

    public void create() {
        try {
            activiteFacade.create(activite);
            MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));
            initAddActivite();
        } catch (MyException ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
        }
    }

    private void initAddActivite() {
        activite = new Activite();
        chercherPrefix();
        listTaches = new ArrayList();
        listTachesSelected = new ArrayList<>();
		listTaches = tacheFacade.findAllOrderByAttribut("code");
    }
    public void chercherPrefix()
    {
        listPrefix =new ArrayList<>();
        listPrefix=prefFacade.findAllOrderByAttribut("id");
        if(!listPrefix.isEmpty())
        activite.setCode(listPrefix.get(0).getAct());
    }

    public void addTachesForActivite() {
        if(!listTachesSelected.isEmpty()) {
			activite.addListTaches(listTachesSelected);
			listTaches.removeAll(listTachesSelected);
            listTachesSelected = new ArrayList<>();
        }
    }
	
	public void removeTacheForActivite(Tache tache) {
		activite.removeTache(tache);
		listTaches.add(tache);
	}

	public String getCode() {
		return code;
	}

	public String getLibelle() {
		return libelle;
	}
	
	public String getDescription() {
		return description;
	}

	public Activite getActivite() {
		return activite;
	}

	public ActiviteFacade getActiviteFacade() {
		return activiteFacade;
	}

	public TacheFacade getTacheFacade() {
		return tacheFacade;
	}

	public List<Tache> getListTaches() {
		return listTaches;
	}

	public List<Tache> getListTachesSelected() {
		return listTachesSelected;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setActivite(Activite activite) {
		this.activite = activite;
	}

	public void setActiviteFacade(ActiviteFacade activiteFacade) {
		this.activiteFacade = activiteFacade;
	}

	public void setTacheFacade(TacheFacade tacheFacade) {
		this.tacheFacade = tacheFacade;
	}

	public void setListTaches(List<Tache> listTaches) {
		this.listTaches = listTaches;
	}

	public void setListTachesSelected(List<Tache> listTachesSelected) {
		this.listTachesSelected = listTachesSelected;
	}
	
	

}
