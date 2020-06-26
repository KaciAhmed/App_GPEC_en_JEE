package dz.elit.gpecpf.referentiel.controller;

import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.FieldValueMatchMode;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.poste.entity.Mission;
import dz.elit.gpecpf.poste.service.MissionFacade;
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
public class ListMissionController extends AbstractController implements Serializable {

	@EJB
	private MissionFacade missionFacade;

	private Mission mission;

	private LazyDataModel<Mission> lazyMission;
	private List<FieldValueMatchMode> fieldValueMatchMode;
	private DataTable dataTable;

	/**
	 * Creates a new instance of ListProfilController
	 */
	public ListMissionController() {
	}

	@Override // @PstConstruct
	protected void initController() {
		lazyMission = new LazyDataModel<Mission>() {
			@Override
			public List<Mission> load(int first, int pageSize, String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {
				fieldValueMatchMode = getFilter(filters, dataTable);
				lazyMission.setRowCount(missionFacade.count(fieldValueMatchMode));
				return missionFacade.lazyFilter(first, pageSize, sortField, sortOrder.toString(), fieldValueMatchMode);
			}
		};
	}

	public void initNewMission() {
		mission = new Mission();
	}

	public void remove(Mission mission) {
		try {
			missionFacade.remove(mission);
			MyUtil.addInfoMessage(MyUtil.getBundleCommun("msg_operation_effectue_avec_succes"));
		} catch (Exception ex) {
			MyUtil.addErrorMessage("La mission est relié à des postes.");
		}
	}

	public DataTable getDataTable() {
		return dataTable;
	}

	public List<FieldValueMatchMode> getFieldValueMatchMode() {
		return fieldValueMatchMode;
	}

	public Mission getMission() {
		return mission;
	}

	public LazyDataModel<Mission> getLazyMission() {
		return lazyMission;
	}

	public MissionFacade getMissionFacade() {
		return missionFacade;
	}

	public void setDataTable(DataTable dataTable) {
		this.dataTable = dataTable;
	}

	public void setFieldValueMatchMode(List<FieldValueMatchMode> fieldValueMatchMode) {
		this.fieldValueMatchMode = fieldValueMatchMode;
	}

	public void setLazyMission(LazyDataModel<Mission> lazyMission) {
		this.lazyMission = lazyMission;
	}

	public void setMission(Mission mission) {
		this.mission = mission;
	}

	public void setMissionFacade(MissionFacade missionFacade) {
		this.missionFacade = missionFacade;
	}

}
