package dz.elit.gpecpf.commun.controller;

import dz.elit.gpecpf.commun.util.AbstractController;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author AYADI hakim && CHEKOR Samir && LEGHATTAS rabah && LAIDANI Youcef
 * ManagedBean avec un scope application pour les informations static
 */
@ManagedBean(name = "myApplicationController", eager = true)
@ApplicationScoped
public class MyApplicationController extends AbstractController implements Serializable {

	private Map<String, String> themes;

	public MyApplicationController() {
	}

	@Override
	protected void initController() {
		initThemes();
	}

	private void initThemes() {
		themes = new TreeMap<>();
		themes.put("Metro", "elit-metro");
		themes.put("Blue", "elit-blue");
	}

	public Map<String, String> getThemes() {
		return themes;
	}

}
