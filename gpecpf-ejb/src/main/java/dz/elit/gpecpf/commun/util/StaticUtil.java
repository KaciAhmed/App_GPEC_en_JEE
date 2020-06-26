package dz.elit.gpecpf.commun.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author ayadi
 */
public abstract class StaticUtil {

	public static final String ADMINISTRATION_SCHEMA = "sch_admin";
	public static final String COMPETENCE_SCHEMA = "sch_competence";
	public static final String POSTE_SCHEMA = "sch_poste";
	public static final String EVALUATION_SCHEMA = "sch_evaluation";
	public static final String UNIT_NAME = "gpecpf-ejbPU";

	public static String getDefaultEncryptPassword() {
		return getEncryptPassword("admin");
	}

	/**
	 * *************************************************************************
	 * @param password
	 * @return hashString
	 * ************************************************************************
	 */
	public static String getEncryptPassword(String password) {

		byte[] uniqueKey = password.getBytes();
		byte[] hash = null;

		try {
			hash = MessageDigest.getInstance("SHA-256").digest(uniqueKey);
		} catch (NoSuchAlgorithmException e) {
			throw new Error("No SHA-256 support in this VM.");
		}

		StringBuilder hashString = new StringBuilder();
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(hash[i]);
			if (hex.length() == 1) {
				hashString.append('0');
				hashString.append(hex.charAt(hex.length() - 1));
			} else {
				hashString.append(hex.substring(hex.length() - 2));
			}
		}
		return hashString.toString();
	}
}
