

package dz.elit.gpecpf.administration.controller;

import dz.elit.gpecpf.administration.entity.AdminModule;
import dz.elit.gpecpf.administration.service.AdminModuleFacade;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author leghettas.rabah
 */
@ManagedBean
@ViewScoped
public class EditModuleController extends AbstractController implements Serializable {
    
    @EJB
    private AdminModuleFacade moduleFacade;
    private AdminModule module;
    /**
     * Creates a new instance of EditModuleController
     */
    public EditModuleController() {
    }
    
    @Override
    protected void initController() {
        module = new AdminModule();
        String id = MyUtil.getRequestParameter("id");
        if (id != null) {
            module = moduleFacade.find(Integer.parseInt(id));

        }
    }
    
    public void edit() {
        try {           
            moduleFacade.edit(module);
            MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//"Module modifie avec succès");
        } catch (Exception ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
        }
    }

    // getter && setter
    public AdminModule getModule() {
        return module;
    }

    public void setModule(AdminModule module) {
        this.module = module;
    }
    
    
}
