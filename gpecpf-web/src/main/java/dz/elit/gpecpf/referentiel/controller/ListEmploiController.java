package dz.elit.gpecpf.referentiel.controller;

import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.FieldValueMatchMode;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.poste.entity.Emploi;
import dz.elit.gpecpf.poste.service.EmploiFacade;
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
public class ListEmploiController extends AbstractController implements Serializable {

	@EJB
	private EmploiFacade emploiFacade;

	private Emploi emploi;

	private LazyDataModel<Emploi> lazyEmploi;
	private List<FieldValueMatchMode> fieldValueMatchMode;
	private DataTable dataTable;

	/**
	 * Creates a new instance of ListProfilController
	 */
	public ListEmploiController() {
	}

	@Override // @PstConstruct
	protected void initController() {
		lazyEmploi = new LazyDataModel<Emploi>() {
			@Override
			public List<Emploi> load(int first, int pageSize, String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {
				fieldValueMatchMode = getFilter(filters, dataTable);
				lazyEmploi.setRowCount(emploiFacade.count(fieldValueMatchMode));
				return emploiFacade.lazyFilter(first, pageSize, sortField, sortOrder.toString(), fieldValueMatchMode);
			}
		};
	}

	public void initNewEmploi() {
		emploi = new Emploi();
	}

	public void remove(Emploi emploi) {
		try {
			emploiFacade.remove(emploi);
			MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));
		} catch (Exception ex) {
			MyUtil.addErrorMessage("Des postes sont reliés à cet emploi");
		}
	}

	public DataTable getDataTable() {
		return dataTable;
	}

	public List<FieldValueMatchMode> getFieldValueMatchMode() {
		return fieldValueMatchMode;
	}

	public Emploi getEmploi() {
		return emploi;
	}

	public LazyDataModel<Emploi> getLazyEmploi() {
		return lazyEmploi;
	}

	public EmploiFacade getEmploiFacade() {
		return emploiFacade;
	}

	public void setDataTable(DataTable dataTable) {
		this.dataTable = dataTable;
	}

	public void setFieldValueMatchMode(List<FieldValueMatchMode> fieldValueMatchMode) {
		this.fieldValueMatchMode = fieldValueMatchMode;
	}

	public void setLazyEmploi(LazyDataModel<Emploi> lazyEmploi) {
		this.lazyEmploi = lazyEmploi;
	}

	public void setEmploi(Emploi emploi) {
		this.emploi = emploi;
	}

	public void setEmploiFacade(EmploiFacade emploiFacade) {
		this.emploiFacade = emploiFacade;
	}

}
