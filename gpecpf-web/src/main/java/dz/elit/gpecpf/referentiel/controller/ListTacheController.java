package dz.elit.gpecpf.referentiel.controller;

import dz.elit.gpecpf.commun.exception.MyException;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.FieldValueMatchMode;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.poste.entity.Tache;
import dz.elit.gpecpf.poste.service.TacheFacade;
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
public class ListTacheController extends AbstractController implements Serializable {

	@EJB
	private TacheFacade tacheFacade;

	private Tache tache;

	private LazyDataModel<Tache> lazyTache;
	private List<FieldValueMatchMode> fieldValueMatchMode;
	private DataTable dataTable;

	/**
	 * Creates a new instance of ListProfilController
	 */
	public ListTacheController() {
	}

	@Override // @PstConstruct
	protected void initController() {
		lazyTache = new LazyDataModel<Tache>() {
			@Override
			public List<Tache> load(int first, int pageSize, String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {
				fieldValueMatchMode = getFilter(filters, dataTable);
				lazyTache.setRowCount(tacheFacade.count(fieldValueMatchMode));
				return tacheFacade.lazyFilter(first, pageSize, sortField, sortOrder.toString(), fieldValueMatchMode);
			}
		};
	}

	public void initNewTache() {
		tache = new Tache();
	}

	public void remove(Tache tache) {
		try {
			tacheFacade.remove(tache);
			MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));
		} catch (MyException ex) {
			ex.printStackTrace();
			MyUtil.addErrorMessage(ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			MyUtil.addErrorMessage(MyUtil.getBundleCommun("msg_erreur_inconu"));// Erreur inconu
		}
	}

	public DataTable getDataTable() {
		return dataTable;
	}

	public List<FieldValueMatchMode> getFieldValueMatchMode() {
		return fieldValueMatchMode;
	}

	public Tache getTache() {
		return tache;
	}

	public LazyDataModel<Tache> getLazyTache() {
		return lazyTache;
	}

	public TacheFacade getTacheFacade() {
		return tacheFacade;
	}

	public void setDataTable(DataTable dataTable) {
		this.dataTable = dataTable;
	}

	public void setFieldValueMatchMode(List<FieldValueMatchMode> fieldValueMatchMode) {
		this.fieldValueMatchMode = fieldValueMatchMode;
	}

	public void setLazyTache(LazyDataModel<Tache> lazyTache) {
		this.lazyTache = lazyTache;
	}

	public void setTache(Tache tache) {
		this.tache = tache;
	}

	public void setTacheFacade(TacheFacade tacheFacade) {
		this.tacheFacade = tacheFacade;
	}

}
