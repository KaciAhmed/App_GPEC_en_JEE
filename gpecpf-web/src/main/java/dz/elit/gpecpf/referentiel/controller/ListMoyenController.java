package dz.elit.gpecpf.referentiel.controller;

import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.FieldValueMatchMode;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.poste.entity.Moyen;
import dz.elit.gpecpf.poste.service.MoyenFacade;
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
 * @author Nadir Ben Mohand
 */
@ManagedBean
@ViewScoped
public class ListMoyenController extends AbstractController implements Serializable {

    @EJB
    private MoyenFacade moyenFacade;

    private Moyen moyen;

    private LazyDataModel<Moyen> lazyMoyen;
    private List<FieldValueMatchMode> fieldValueMatchMode;
    private DataTable dataTable;

    /**
     * Creates a new instance of ListProfilController
     */
    public ListMoyenController() {
    }

    @Override // @PstConstruct
    protected void initController() {
        lazyMoyen = new LazyDataModel<Moyen>() {
            @Override
            public List<Moyen> load(int first, int pageSize, String sortField, SortOrder sortOrder,
                    Map<String, Object> filters) {
                fieldValueMatchMode = getFilter(filters, dataTable);
                lazyMoyen.setRowCount(moyenFacade.count(fieldValueMatchMode));
                return moyenFacade.lazyFilter(first, pageSize, sortField, sortOrder.toString(), fieldValueMatchMode);
            }
        };
    }

    public void initNewMoyen() {
        moyen = new Moyen();
    }

    public void remove(Moyen moyen) {
        try {
            moyenFacade.remove(moyen);
            MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));
        } catch (Exception ex) {
            MyUtil.addErrorMessage("La moyen est relié à des postes.");
        }
    }

    public DataTable getDataTable() {
        return dataTable;
    }

    public List<FieldValueMatchMode> getFieldValueMatchMode() {
        return fieldValueMatchMode;
    }

    public Moyen getMoyen() {
        return moyen;
    }

    public LazyDataModel<Moyen> getLazyMoyen() {
        return lazyMoyen;
    }

    public MoyenFacade getMoyenFacade() {
        return moyenFacade;
    }

    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }

    public void setFieldValueMatchMode(List<FieldValueMatchMode> fieldValueMatchMode) {
        this.fieldValueMatchMode = fieldValueMatchMode;
    }

    public void setLazyMoyen(LazyDataModel<Moyen> lazyMoyen) {
        this.lazyMoyen = lazyMoyen;
    }

    public void setMoyen(Moyen moyen) {
        this.moyen = moyen;
    }

    public void setMoyenFacade(MoyenFacade moyenFacade) {
        this.moyenFacade = moyenFacade;
    }

}
