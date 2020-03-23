
package dz.elit.gpecpf.administration.controller;

import dz.elit.gpecpf.administration.entity.AdminModule;
import dz.elit.gpecpf.administration.entity.AdminPrivilege;
import dz.elit.gpecpf.administration.service.AdminModuleFacade;
import dz.elit.gpecpf.administration.service.AdminPrivilegeFacade;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author leghettas.rabah
 */
@ManagedBean
@ViewScoped
public class EditPrivilegeController extends AbstractController implements Serializable {

    @EJB
    private AdminPrivilegeFacade privilegeFacade;
    @EJB
    private AdminModuleFacade moduleFacade;

    private AdminPrivilege privilege;
    private AdminModule module;

    private List<AdminModule> listModules;


    /**
     * Creates a new instance of AddProfilController
     */
    public EditPrivilegeController() {
    }

    @Override//@PostConstruct
    protected void initController() {
        module = new AdminModule();
        listModules = moduleFacade.findAllOrderByAttribut("code");

        String id = MyUtil.getRequestParameter("id");
        if (id != null) {
            privilege=privilegeFacade.find(Integer.parseInt(id));
        }
        
        
    }
    public void edit() {
        try {
            privilegeFacade.edit(privilege);
            MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//"Privilège modifié avec succès");
        } catch (Exception ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
        }
    }

    // Getter and setter

    public AdminPrivilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(AdminPrivilege privilege) {
        this.privilege = privilege;
    }

    public List<AdminModule> getListModules() {
        return listModules;
    }

    public void setListModules(List<AdminModule> listModules) {
        this.listModules = listModules;
    }

    public AdminModule getModule() {
        return module;
    }

    public void setModule(AdminModule module) {
        this.module = module;
    }

}
