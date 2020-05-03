package dz.elit.gpecpf.referentiel.controller;

import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.poste.entity.Formation;
import dz.elit.gpecpf.poste.service.FormationFacade;
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
public class AddFormationController extends AbstractController implements Serializable {
    @EJB
    private FormationFacade formationFacade;
    private Formation formation;
	
    private String code;
    private String description;
	private String type;
	private String exigence;

    /**
     * Creates a new instance of AddProfilController
     */
    public AddFormationController() {
    }

    @Override//@PostConstruct
    protected void initController() {
        initAddFormation();
        formation = new Formation();
    }

    public void create() {
        try {
            formationFacade.create(formation);
            MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));
            initAddFormation();
        } catch (MyException ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
        }
    }

    private void initAddFormation() {
        formation = new Formation();
    }

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public Formation getFormation() {
		return formation;
	}

	public FormationFacade getFormationFacade() {
		return formationFacade;
	}

	public String getType() {
		return type;
	}

	public String getExigence() {
		return exigence;
	}
	
	public void setCode(String code) {
		this.code = code;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setFormation(Formation formation) {
		this.formation = formation;
	}

	public void setFormationFacade(FormationFacade formationFacade) {
		this.formationFacade = formationFacade;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setExigence(String exigence) {
		this.exigence = exigence;
	}
	
	

}
