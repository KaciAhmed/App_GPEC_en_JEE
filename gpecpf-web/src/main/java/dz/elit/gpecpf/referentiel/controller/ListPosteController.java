package dz.elit.gpecpf.referentiel.controller;

import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.FieldValueMatchMode;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.poste.entity.Poste;
import dz.elit.gpecpf.poste.service.PosteFacade;
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
public class ListPosteController extends AbstractController implements Serializable {

    @EJB
    private PosteFacade posteFacade;

    private Poste poste;

    private LazyDataModel<Poste> lazyPoste;
    private List<FieldValueMatchMode> fieldValueMatchMode;
    private DataTable dataTable;

    /**
     * Creates a new instance of ListProfilController
     */
    public ListPosteController() {
    }

    @Override // @PstConstruct
    protected void initController() {
        lazyPoste = new LazyDataModel<Poste>() {
            @Override
            public List<Poste> load(int first, int pageSize, String sortField, SortOrder sortOrder,
                    Map<String, Object> filters) {
                fieldValueMatchMode = getFilter(filters, dataTable);
                lazyPoste.setRowCount(posteFacade.count(fieldValueMatchMode));
                return posteFacade.lazyFilter(first, pageSize, sortField, sortOrder.toString(), fieldValueMatchMode);
            }
        };
    }

    public void initNewPoste() {
        poste = new Poste();
    }

    public void remove(Poste poste) {
        try {
            posteFacade.remove(poste);
            MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));
        } catch (Exception ex) {
			MyUtil.addErrorMessage("Des postes sont reliés à cet poste");
        }
    }

    public DataTable getDataTable() {
        return dataTable;
    }

    public List<FieldValueMatchMode> getFieldValueMatchMode() {
        return fieldValueMatchMode;
    }

    public Poste getPoste() {
        return poste;
    }

    public LazyDataModel<Poste> getLazyPoste() {
        return lazyPoste;
    }

    public PosteFacade getPosteFacade() {
        return posteFacade;
    }

    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }

    public void setFieldValueMatchMode(List<FieldValueMatchMode> fieldValueMatchMode) {
        this.fieldValueMatchMode = fieldValueMatchMode;
    }

    public void setLazyPoste(LazyDataModel<Poste> lazyPoste) {
        this.lazyPoste = lazyPoste;
    }

    public void setPoste(Poste poste) {
        this.poste = poste;
    }

    public void setPosteFacade(PosteFacade posteFacade) {
        this.posteFacade = posteFacade;
    }

}
