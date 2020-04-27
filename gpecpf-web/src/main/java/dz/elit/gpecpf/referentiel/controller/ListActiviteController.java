package dz.elit.gpecpf.referentiel.controller;

import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.FieldValueMatchMode;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.poste.entity.Activite;
import dz.elit.gpecpf.poste.service.ActiviteFacade;
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
public class ListActiviteController extends AbstractController implements Serializable {

    @EJB
    private ActiviteFacade activiteFacade;

    private Activite activite;

    private LazyDataModel<Activite> lazyActivite;
    private List<FieldValueMatchMode> fieldValueMatchMode;
    private DataTable dataTable;

    /**
     * Creates a new instance of ListProfilController
     */
    public ListActiviteController() {
    }

    @Override // @PstConstruct
    protected void initController() {
        lazyActivite = new LazyDataModel<Activite>() {
            @Override
            public List<Activite> load(int first, int pageSize, String sortField, SortOrder sortOrder,
                    Map<String, Object> filters) {
                fieldValueMatchMode = getFilter(filters, dataTable);
                lazyActivite.setRowCount(activiteFacade.count(fieldValueMatchMode));
                return activiteFacade.lazyFilter(first, pageSize, sortField, sortOrder.toString(), fieldValueMatchMode);
            }
        };
    }

    public void initNewActivite() {
        activite = new Activite();
    }

    public void remove(Activite activite) {
        try {
            activiteFacade.remove(activite);
            MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));
        } catch (Exception ex) {
			MyUtil.addErrorMessage("L'activité est relié à des missions.");
        }
    }

    public DataTable getDataTable() {
        return dataTable;
    }

    public List<FieldValueMatchMode> getFieldValueMatchMode() {
        return fieldValueMatchMode;
    }

    public Activite getActivite() {
        return activite;
    }

    public LazyDataModel<Activite> getLazyActivite() {
        return lazyActivite;
    }

    public ActiviteFacade getActiviteFacade() {
        return activiteFacade;
    }

    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }

    public void setFieldValueMatchMode(List<FieldValueMatchMode> fieldValueMatchMode) {
        this.fieldValueMatchMode = fieldValueMatchMode;
    }

    public void setLazyActivite(LazyDataModel<Activite> lazyActivite) {
        this.lazyActivite = lazyActivite;
    }

    public void setActivite(Activite activite) {
        this.activite = activite;
    }

    public void setActiviteFacade(ActiviteFacade activiteFacade) {
        this.activiteFacade = activiteFacade;
    }

}
