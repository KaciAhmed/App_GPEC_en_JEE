package dz.elit.gpecpf.competence.controleur;

import dz.elit.gpecpf.administration.service.AdminPrefixCodificationFacade;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.competence.entity.Typecompetence;
import dz.elit.gpecpf.competence.service.TypeCompetenceFacade;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Kaci Ahmed
 */
@ManagedBean
@ViewScoped
public class EditTypeCompetenceController extends AbstractController implements Serializable {

	@EJB
	private TypeCompetenceFacade typeCompFacade;
	@EJB
	private AdminPrefixCodificationFacade prefixFacade;

	private Typecompetence typecomp;

	private String codePrefix;

	@Override//@PostConstruct
	protected void initController() {
		try {
			codePrefix = prefixFacade.chercherPrefix().getTypecomp();
		} catch (Exception e) {
			codePrefix = "";
		}
		String id = MyUtil.getRequestParameter("id");
		if (id != null) {
			typecomp = typeCompFacade.find(Integer.parseInt(id));
		}
	}

	// getter && setter
	public Typecompetence getTypecomp() {
		return typecomp;
	}

	public void setTypecomp(Typecompetence typecomp) {
		this.typecomp = typecomp;
	}

	public TypeCompetenceFacade getTypeCompFacade() {
		return typeCompFacade;
	}

	public void setTypeCompFacade(TypeCompetenceFacade typeCompFacade) {
		this.typeCompFacade = typeCompFacade;
	}

	public AdminPrefixCodificationFacade getPrefixFacade() {
		return prefixFacade;
	}

	public void setPrefixFacade(AdminPrefixCodificationFacade prefixFacade) {
		this.prefixFacade = prefixFacade;
	}

	public String getCodePrefix() {
		return codePrefix;
	}

	public void setCodePrefix(String codePrefix) {
		this.codePrefix = codePrefix;
	}

}
