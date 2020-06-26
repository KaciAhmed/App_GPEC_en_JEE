package dz.elit.gpecpf.commun.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author chekor.samir
 */
public abstract class MyUtil {

	//*************** Déclaration des ressources bundles ************************
	/**
	 * Bundle commun pour tous les modules .
	 */
	public static final String BUNDLE_COMMUN = "/bundle/bundleCommun";
	/**
	 * Bundle pour le module administration.
	 */
	public static final String BUNDLE_ADMIN = "/bundle/bundleAdmin";
	/**
	 * Bundle pour le module referentiel.
	 */
	public static final String BUNDLE_REFERENTIEL = "/bundle/bundleReferentiel";
	/**
	 * Bundle pour le module drh.
	 */
	public static final String BUNDLE_DRH = "/bundle/bundleDrh";
	/**
	 * Bundle pour le module gestionnaire employé.
	 */
	public static final String BUNDLE_GESEMP = "/bundle/bundleGesemp";
	/**
	 * Bundle pour le module employé.
	 */
	public static final String BUNDLE_EMP = "/bundle/bundleEmp";

	public static String getRequestParameter(String key) {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
	}

	public static void addInfoMessage(String msg) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info :", msg));
	}

	public static void addWarnMessage(String msg) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Attention :", msg));
	}

	public static void addErrorMessage(String msg) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur :", msg));
	}

	public static void addFatalMessage(String msg) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal :", msg));
	}

	/**
	 * Gets a string for the given key from this resource bundle name
	 * "BUNDLE_COMMUN" . for All module
	 *
	 * @param value : the key for the desired value
	 * @return the string value for the given key
	 */
	public static String getBundleCommun(String value) {
		try {
			return ResourceBundle.getBundle(BUNDLE_COMMUN).getString(value.trim());
		} catch (MissingResourceException e) {
			e.printStackTrace();
			return "?:" + value + ":?";
		} catch (Exception e) {
			e.printStackTrace();
			return "?:Can't find bundle:?";
		}
	}

	/**
	 * Gets a string for the given key from this resource bundle name
	 * "BUNDLE_ADMIN" . for module administartion
	 *
	 * @param value : the key for the desired value
	 * @return the string value for the given key
	 */
	public static String getBundleAdmin(String value) {
		try {
			return ResourceBundle.getBundle(BUNDLE_ADMIN).getString(value.trim());
		} catch (MissingResourceException e) {
			e.printStackTrace();
			return "?:" + value + ":?";
		} catch (Exception e) {
			e.printStackTrace();
			return "?:Can't find bundle:?";
		}
	}

	/**
	 * Gets a string for the given key from this resource bundle name
	 * "BUNDLE_REFERENTIEL" . for module referentiel
	 *
	 * @param value : the key for the desired value
	 * @return the string value for the given key
	 */
	public static String getBundleReferentiel(String value) {
		try {
			return ResourceBundle.getBundle(BUNDLE_REFERENTIEL).getString(value.trim());
		} catch (MissingResourceException e) {
			e.printStackTrace();
			return "?:" + value + ":?";
		} catch (Exception e) {
			e.printStackTrace();
			return "?:Can't find bundle:?";
		}
	}

	/**
	 * Gets a string for the given key from this resource bundle name
	 * "BUNDLE_DRH" . for module drh
	 *
	 * @param value : the key for the desired value
	 * @return the string value for the given key
	 */
	public static String getBundleDrh(String value) {
		try {
			return ResourceBundle.getBundle(BUNDLE_DRH).getString(value.trim());
		} catch (MissingResourceException e) {
			e.printStackTrace();
			return "?:" + value + ":?";
		} catch (Exception e) {
			e.printStackTrace();
			return "?:Can't find bundle:?";
		}
	}

	/**
	 * Gets a string for the given key from this resource bundle name
	 * "BUNDLE_GESEMP" . for module gestionnaire employé
	 *
	 * @param value : the key for the desired value
	 * @return the string value for the given key
	 */
	public static String getBundleGesemp(String value) {
		try {
			return ResourceBundle.getBundle(BUNDLE_GESEMP).getString(value.trim());
		} catch (MissingResourceException e) {
			e.printStackTrace();
			return "?:" + value + ":?";
		} catch (Exception e) {
			e.printStackTrace();
			return "?:Can't find bundle:?";
		}
	}

	/**
	 * Gets a string for the given key from this resource bundle name
	 * "BUNDLE_EMP" . for module employé
	 *
	 * @param value : the key for the desired value
	 * @return the string value for the given key
	 */
	public static String getBundleEmp(String value) {
		try {
			return ResourceBundle.getBundle(BUNDLE_EMP).getString(value.trim());
		} catch (MissingResourceException e) {
			e.printStackTrace();
			return "?:" + value + ":?";
		} catch (Exception e) {
			e.printStackTrace();
			return "?:Can't find bundle:?";
		}
	}

}
