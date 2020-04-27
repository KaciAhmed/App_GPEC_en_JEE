package dz.elit.gpecpf.referentiel.controller;

import dz.elit.gpecpf.poste.service.MoyenFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.poste.entity.Moyen;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Nadir Ben Mohand
 */
@ManagedBean
@ViewScoped
public class EditMoyenController extends AbstractController implements Serializable {

    @EJB
    private MoyenFacade moyenFacade;

    private Moyen moyen;

    private String code;
    private String description;

    /**
     * Creates a new instance of AddProfilController
     */
    public EditMoyenController() {
    }

    @Override//@PostConstruct
    protected void initController() {
        initAddMoyen();
        moyen = new Moyen();
        String id = MyUtil.getRequestParameter("id");
        if (id != null) {
            moyen = moyenFacade.find(Integer.parseInt(id));
        }
    }

    public void edit() {
        try {
            moyenFacade.edit(moyen);
            MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));
            initAddMoyen();
        } catch (MyException ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
        }
    }

    private void initAddMoyen() {
        moyen = new Moyen();
    }

	public void setCode(String code) {
		this.code = code;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setMoyen(Moyen moyen) {
		this.moyen = moyen;
	}

	public void setMoyenFacade(MoyenFacade moyenFacade) {
		this.moyenFacade = moyenFacade;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public Moyen getMoyen() {
		return moyen;
	}

	public MoyenFacade getMoyenFacade() {
		return moyenFacade;
	}
	
	
}
