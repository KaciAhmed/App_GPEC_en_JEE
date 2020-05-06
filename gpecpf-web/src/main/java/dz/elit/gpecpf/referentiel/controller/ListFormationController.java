package dz.elit.gpecpf.referentiel.controller;

import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.FieldValueMatchMode;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.poste.entity.Formation;
import dz.elit.gpecpf.poste.service.FormationFacade;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Nadir Ben Mohand
 */
@ManagedBean
@ViewScoped
public class ListFormationController extends AbstractController implements Serializable {

    @EJB
    private FormationFacade formationFacade;

    private Formation formation;

    private LazyDataModel<Formation> lazyFormation;
    private List<FieldValueMatchMode> fieldValueMatchMode;
    private DataTable dataTable;
	
	private SelectItem[] typeOptions = new SelectItem[3];
	private SelectItem[] exigenceOptions = new SelectItem[3];

    /**
     * Creates a new instance of ListProfilController
     */
    public ListFormationController() {
    }

    @Override // @PstConstruct
    protected void initController() {
		typeOptions[0] = new SelectItem("", "");
		typeOptions[1] = new SelectItem("Académique", "Académique");
		typeOptions[2] = new SelectItem("Professionnel", "Professionnel");
		exigenceOptions[0] = new SelectItem("", "");
		exigenceOptions[1] = new SelectItem("Obligatoire", "Obligatoire");
		exigenceOptions[2] = new SelectItem("Supplémentaire", "Supplémentaire");
        lazyFormation = new LazyDataModel<Formation>() {
            @Override
            public List<Formation> load(int first, int pageSize, String sortField, SortOrder sortOrder,
                    Map<String, Object> filters) {
                fieldValueMatchMode = getFilter(filters, dataTable);
                lazyFormation.setRowCount(formationFacade.count(fieldValueMatchMode));
                return formationFacade.lazyFilter(first, pageSize, sortField, sortOrder.toString(), fieldValueMatchMode);
            }
        };
    }

    public void initNewFormation() {
        formation = new Formation();
    }

    public void remove(Formation formation) {
        try {
            formationFacade.remove(formation);
            MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));
        } catch (Exception ex) {
            MyUtil.addErrorMessage("Des utilisateurs ou des postes sont relié à cet Formation.");
        }
    }

    public DataTable getDataTable() {
        return dataTable;
    }

    public List<FieldValueMatchMode> getFieldValueMatchMode() {
        return fieldValueMatchMode;
    }

    public Formation getFormation() {
        return formation;
    }

    public LazyDataModel<Formation> getLazyFormation() {
        return lazyFormation;
    }

    public FormationFacade getFormationFacade() {
        return formationFacade;
    }

	public SelectItem[] getExigenceOptions() {
		return exigenceOptions;
	}

	public SelectItem[] getTypeOptions() {
		return typeOptions;
	}
	
    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }

    public void setFieldValueMatchMode(List<FieldValueMatchMode> fieldValueMatchMode) {
        this.fieldValueMatchMode = fieldValueMatchMode;
    }

    public void setLazyFormation(LazyDataModel<Formation> lazyFormation) {
        this.lazyFormation = lazyFormation;
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
    }

    public void setFormationFacade(FormationFacade formationFacade) {
        this.formationFacade = formationFacade;
    }

	public void setExigenceOptions(SelectItem[] exigenceOptions) {
		this.exigenceOptions = exigenceOptions;
	}

	public void setTypeOptions(SelectItem[] typeOptions) {
		this.typeOptions = typeOptions;
	}
	
	

}
