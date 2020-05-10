package dz.elit.gpecpf.referentiel.controller;

import dz.elit.gpecpf.administration.entity.Prefixcodification;
import dz.elit.gpecpf.administration.service.AdminPrefixCodificationFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.poste.entity.Tache;
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
public class AddTacheController extends AbstractController implements Serializable {
    @EJB
    private TacheFacade tacheFacade;
    @EJB
    private AdminPrefixCodificationFacade prefFacade;
   
    private  List<Prefixcodification> listPrefix;
    private Tache tache;
	
    private String code;
    private String description;

    /**
     * Creates a new instance of AddProfilController
     */
    public AddTacheController() {
    }

    @Override//@PostConstruct
    protected void initController() {
        initAddTache();
        tache = new Tache();
        chercherPrefix();
    }
    public void chercherPrefix()
    {   
        listPrefix =new ArrayList<>();
        listPrefix=prefFacade.findAllOrderByAttribut("id");
        if(!listPrefix.isEmpty())
        tache.setCode(listPrefix.get(0).getTache());
    }

    public void create() {
        try {
            tacheFacade.create(tache);
            MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));
            initAddTache();
        } catch (MyException ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
        }
    }

    private void initAddTache() {
        tache = new Tache();
    }

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public Tache getTache() {
		return tache;
	}

	public TacheFacade getTacheFacade() {
		return tacheFacade;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTache(Tache tache) {
		this.tache = tache;
	}

	public void setTacheFacade(TacheFacade tacheFacade) {
		this.tacheFacade = tacheFacade;
	}

}
