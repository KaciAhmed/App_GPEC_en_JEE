

package dz.elit.gpecpf.administration.controller;

import dz.elit.gpecpf.administration.entity.AdminModule;
import dz.elit.gpecpf.administration.service.AdminModuleFacade;
import dz.elit.gpecpf.commun.controller.MySessionController;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author leghettas.rabah
 */
@ManagedBean
@ViewScoped
public class ListModuleController extends AbstractController implements Serializable {

    @EJB
    private AdminModuleFacade moduleFacade;
    
    @ManagedProperty(value = "#{mySessionController}")
    private MySessionController mySessionController;
    
    private List<AdminModule> listModules;
    private AdminModule module = new AdminModule();

    /**
     * Creates a new instance of ListModuleController
     */
    public ListModuleController() {
    }

    @Override //PostConstruct
    protected void initController() {
        findList();
    }

    private void findList() {
        listModules = moduleFacade.findAllOrderByAttribut("ordre");
    }

    public void moveToTop() {
        try {
            String id = MyUtil.getRequestParameter("id");
            if (id != null) {
                module = moduleFacade.find(Integer.parseInt(id));
            }
            moduleFacade.moveToTop(module);
            findList();
            mySessionController.initInterfaceUser(FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName());
            MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//"Module modifie avec succès");
        } catch (Exception ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
        }
    }

    public void moveToBottom() {
        try {
            String id = MyUtil.getRequestParameter("id");
            if (id != null) {
                module = moduleFacade.find(Integer.parseInt(id));
            }
            moduleFacade.moveToBottom(module);
            findList();
            mySessionController.initInterfaceUser(FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName());
            MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//"Module modifie avec succès");
        } catch (Exception ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
        }
    }

    public List<AdminModule> getListModules() {
        return listModules;
    }

    public AdminModule getModule() {
        return module;
    }

    public void setModule(AdminModule module) {
        this.module = module;
    }

    public MySessionController getMySessionController() {
        return mySessionController;
    }

    public void setMySessionController(MySessionController mySessionController) {
        this.mySessionController = mySessionController;
    }

}
