package dz.elit.gpecpf.referentiel.controller;

import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.FieldValueMatchMode;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.poste.entity.TypePoste;
import dz.elit.gpecpf.poste.service.TypePosteFacade;
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
public class ListTypePosteController extends AbstractController implements Serializable {

    @EJB
    private TypePosteFacade typePosteFacade;

    private TypePoste typePoste;

    private LazyDataModel<TypePoste> lazyTypePoste;
    private List<FieldValueMatchMode> fieldValueMatchMode;
    private DataTable dataTable;

    /**
     * Creates a new instance of ListProfilController
     */
    public ListTypePosteController() {
    }

    @Override // @PstConstruct
    protected void initController() {
        lazyTypePoste = new LazyDataModel<TypePoste>() {
            @Override
            public List<TypePoste> load(int first, int pageSize, String sortField, SortOrder sortOrder,
                    Map<String, Object> filters) {
                fieldValueMatchMode = getFilter(filters, dataTable);
                lazyTypePoste.setRowCount(typePosteFacade.count(fieldValueMatchMode));
                return typePosteFacade.lazyFilter(first, pageSize, sortField, sortOrder.toString(), fieldValueMatchMode);
            }
        };
    }

    public void initNewTypePoste() {
        typePoste = new TypePoste();
    }

    public void remove(TypePoste typePoste) {
        try {
            typePosteFacade.remove(typePoste);
            MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));
        } catch (Exception ex) {
            MyUtil.addErrorMessage("La typePoste est relié à des activités.");
        }
    }

    public DataTable getDataTable() {
        return dataTable;
    }

    public List<FieldValueMatchMode> getFieldValueMatchMode() {
        return fieldValueMatchMode;
    }

    public TypePoste getTypePoste() {
        return typePoste;
    }

    public LazyDataModel<TypePoste> getLazyTypePoste() {
        return lazyTypePoste;
    }

    public TypePosteFacade getTypePosteFacade() {
        return typePosteFacade;
    }

    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }

    public void setFieldValueMatchMode(List<FieldValueMatchMode> fieldValueMatchMode) {
        this.fieldValueMatchMode = fieldValueMatchMode;
    }

    public void setLazyTypePoste(LazyDataModel<TypePoste> lazyTypePoste) {
        this.lazyTypePoste = lazyTypePoste;
    }

    public void setTypePoste(TypePoste typePoste) {
        this.typePoste = typePoste;
    }

    public void setTypePosteFacade(TypePosteFacade typePosteFacade) {
        this.typePosteFacade = typePosteFacade;
    }

}
