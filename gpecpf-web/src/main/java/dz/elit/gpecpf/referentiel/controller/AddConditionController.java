package dz.elit.gpecpf.referentiel.controller;

import dz.elit.gpecpf.administration.entity.Prefixcodification;
import dz.elit.gpecpf.administration.service.AdminPrefixCodificationFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.poste.entity.Condition;
import dz.elit.gpecpf.poste.service.ConditionFacade;
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
public class AddConditionController extends AbstractController implements Serializable {
    @EJB
    private ConditionFacade conditionFacade;
    @EJB
    private AdminPrefixCodificationFacade prefFacade;
    
    private  List<Prefixcodification> listPrefix;
    private Condition condition;
	
    private String code;
    private String description;

    /**
     * Creates a new instance of AddProfilController
     */
    public AddConditionController() {
    }

    @Override//@PostConstruct
    protected void initController() {
        initAddCondition();
        condition = new Condition();
        chercherPrefix();
    }
     public void chercherPrefix()
    {   
        listPrefix =new ArrayList<>();
        listPrefix=prefFacade.findAllOrderByAttribut("id");
        if(!listPrefix.isEmpty())
        condition.setCode(listPrefix.get(0).getCond());
    }

    public void create() {
        try {
            conditionFacade.create(condition);
            MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));
            initAddCondition();
        } catch (MyException ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
        }
    }

    private void initAddCondition() {
        condition = new Condition();
    }

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public Condition getCondition() {
		return condition;
	}

	public ConditionFacade getConditionFacade() {
		return conditionFacade;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public void setConditionFacade(ConditionFacade conditionFacade) {
		this.conditionFacade = conditionFacade;
	}

}
