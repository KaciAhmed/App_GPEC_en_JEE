package dz.elit.gpecpf.administration.controller;

import dz.elit.gpecpf.administration.entity.AdminPrivilege;
import dz.elit.gpecpf.administration.entity.AdminProfil;
import dz.elit.gpecpf.administration.service.AdminPrivilegeFacade;
import dz.elit.gpecpf.administration.service.AdminProfilFacade;
import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.FieldValueMatchMode;
import dz.elit.gpecpf.commun.util.MyUtil;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Leghettas rabah & Chekor samir
 */
@ManagedBean
@ViewScoped
public class ListProfilController extends AbstractController implements Serializable {

    @EJB
    private AdminProfilFacade adminProfilFacade;
    @EJB
    private AdminPrivilegeFacade adminPrivilegeFacade;

    private AdminProfil adminProfil;

    private List<AdminPrivilege> listPrivileges;
    private LazyDataModel<AdminProfil> lazyAdminProfil;
    private List<FieldValueMatchMode> fieldValueMatchMode;
    private DataTable dataTable;

    /**
     * Creates a new instance of ListProfilController
     */
    public ListProfilController() {
    }

    @Override  //@PstConstruct
    protected void initController() {
        listPrivileges = adminPrivilegeFacade.findAllOrderById();
        lazyAdminProfil = new LazyDataModel<AdminProfil>() {
            @Override
            public List<AdminProfil> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                fieldValueMatchMode = getFilter(filters, dataTable);
                lazyAdminProfil.setRowCount(adminProfilFacade.count(fieldValueMatchMode));
                return adminProfilFacade.lazyFilter(first, pageSize, sortField, sortOrder.toString(), fieldValueMatchMode);
            } 
        };
    }

    public void initNewProfil() {
        adminProfil = new AdminProfil();
    }

    public void remove(AdminProfil adminProfil) {
        try {
            adminProfilFacade.remove(adminProfil);
            MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));//"Profil supprimé avec succès");
        } catch (MyException ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));//Erreur inconu
        }
    }

    public List<AdminPrivilege> getListPrivileges() {
        return listPrivileges;
    }

    public AdminProfil getAdminProfil() {
        return adminProfil;
    }

    public void setAdminProfil(AdminProfil adminProfil) {
        this.adminProfil = adminProfil;
    }

    public LazyDataModel<AdminProfil> getLazyAdminProfil() {
        return lazyAdminProfil;
    }

    public void setLazyAdminProfil(LazyDataModel<AdminProfil> lazyAdminProfil) {
        this.lazyAdminProfil = lazyAdminProfil;
    }

    public DataTable getDataTable() {
        return dataTable;
    }

    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }
}
