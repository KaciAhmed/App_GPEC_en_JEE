package dz.elit.gpecpf.administration.controller;

import dz.elit.gpecpf.administration.entity.AdminModule;
import dz.elit.gpecpf.administration.entity.AdminPrivilege;
import dz.elit.gpecpf.administration.service.AdminModuleFacade;
import dz.elit.gpecpf.administration.service.AdminPrivilegeFacade;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author leghettas.rabah
 */
@ManagedBean
@ViewScoped
public class ListPrivilegeController extends AbstractController implements Serializable {

	@EJB
	private AdminPrivilegeFacade adminPrivilegeFacade;
	@EJB
	private AdminModuleFacade moduleFacade;

	private List<AdminPrivilege> listPrivileges;
	private List<AdminModule> listModules;
	private AdminModule module;

	private String code;
	private String description;

	/**
	 * Creates a new instance of ListProfilController
	 */
	public ListPrivilegeController() {
	}

	@Override  //@PstConstruct
	protected void initController() {
		listPrivileges = adminPrivilegeFacade.findAllOrderById();
		module = new AdminModule();
		listModules = moduleFacade.findAllOrderByAttribut("code");
	}

	public void rechercher() {
		listPrivileges = adminPrivilegeFacade.findByCodeDescModule(code, description, module);
		if (listPrivileges.isEmpty() || listPrivileges.size() < 1) {
			MyUtil.addInfoMessage(MyUtil.getBundleAdmin("msg_resultat_recherche_null"));
		}
	}

	public List<AdminPrivilege> getListPrivileges() {
		return listPrivileges;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<AdminModule> getListModules() {
		return listModules;
	}

	public void setListModules(List<AdminModule> listModules) {
		this.listModules = listModules;
	}

	public AdminModule getModule() {
		return module;
	}

	public void setModule(AdminModule module) {
		this.module = module;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
