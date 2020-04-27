package dz.elit.gpecpf.referentiel.controller;

import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.FieldValueMatchMode;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.poste.entity.Condition;
import dz.elit.gpecpf.poste.service.ConditionFacade;
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
public class ListConditionController extends AbstractController implements Serializable {

    @EJB
    private ConditionFacade conditionFacade;

    private Condition condition;

    private LazyDataModel<Condition> lazyCondition;
    private List<FieldValueMatchMode> fieldValueMatchMode;
    private DataTable dataTable;

    /**
     * Creates a new instance of ListProfilController
     */
    public ListConditionController() {
    }

    @Override // @PstConstruct
    protected void initController() {
        lazyCondition = new LazyDataModel<Condition>() {
            @Override
            public List<Condition> load(int first, int pageSize, String sortField, SortOrder sortOrder,
                    Map<String, Object> filters) {
                fieldValueMatchMode = getFilter(filters, dataTable);
                lazyCondition.setRowCount(conditionFacade.count(fieldValueMatchMode));
                return conditionFacade.lazyFilter(first, pageSize, sortField, sortOrder.toString(), fieldValueMatchMode);
            }
        };
    }

    public void initNewCondition() {
        condition = new Condition();
    }

    public void remove(Condition condition) {
        try {
            conditionFacade.remove(condition);
            MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));
        } catch (Exception ex) {
            MyUtil.addErrorMessage("La condition est relié à des postes.");
        }
    }

    public DataTable getDataTable() {
        return dataTable;
    }

    public List<FieldValueMatchMode> getFieldValueMatchMode() {
        return fieldValueMatchMode;
    }

    public Condition getCondition() {
        return condition;
    }

    public LazyDataModel<Condition> getLazyCondition() {
        return lazyCondition;
    }

    public ConditionFacade getConditionFacade() {
        return conditionFacade;
    }

    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }

    public void setFieldValueMatchMode(List<FieldValueMatchMode> fieldValueMatchMode) {
        this.fieldValueMatchMode = fieldValueMatchMode;
    }

    public void setLazyCondition(LazyDataModel<Condition> lazyCondition) {
        this.lazyCondition = lazyCondition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public void setConditionFacade(ConditionFacade conditionFacade) {
        this.conditionFacade = conditionFacade;
    }

}
