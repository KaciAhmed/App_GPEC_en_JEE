package dz.elit.gpecpf.referentiel.controller;

import dz.elit.gpecpf.poste.service.EmploiFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.poste.entity.Emploi;
import dz.elit.gpecpf.poste.entity.Poste;
import dz.elit.gpecpf.poste.service.PosteFacade;
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
public class EditEmploiController extends AbstractController implements Serializable {

    @EJB
    private EmploiFacade emploiFacade;
	@EJB
	private PosteFacade posteFacade;
	
	private List<Poste> listPostes;
	
    private Emploi emploi;

    private String code;
    private String libelle;
	private String description;

    /**
     * Creates a new instance of AddProfilController
     */
    public EditEmploiController() {
    }

    @Override//@PostConstruct
    protected void initController() {
        initAddEmploi();
        emploi = new Emploi();
        String id = MyUtil.getRequestParameter("id");
        if (id != null) {
            emploi = emploiFacade.find(Integer.parseInt(id));
			listPostes = posteFacade.postesForEmploi(emploi);
        }
    }
	
    public void edit() {
        try {
            emploiFacade.edit(emploi);
            MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));
            initAddEmploi();
        } catch (MyException ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
        }
    }

    private void initAddEmploi() {
        emploi = new Emploi();
		listPostes = new ArrayList<>();
    }

	public Emploi getEmploi() {
		return emploi;
	}

	public void setEmploi(Emploi emploi) {
		this.emploi = emploi;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public EmploiFacade getEmploiFacade() {
		return emploiFacade;
	}

	public void setEmploiFacade(EmploiFacade emploiFacade) {
		this.emploiFacade = emploiFacade;
	}

	public PosteFacade getPosteFacade() {
		return posteFacade;
	}

	public void setPosteFacade(PosteFacade posteFacade) {
		this.posteFacade = posteFacade;
	}

	public List<Poste> getListPostes() {
		return listPostes;
	}

	public void setListPostes(List<Poste> listPostes) {
		this.listPostes = listPostes;
	}
	
	
	
}
