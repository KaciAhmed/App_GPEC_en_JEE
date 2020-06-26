package dz.elit.gpecpf.commun.util;

/**
 *
 * @author leghettas.rabah
 */
public class FieldValueMatchMode {

	private String name;
	private String value;
	private String matchMode;

	public FieldValueMatchMode(String name, String value, String matchMode) {
		this.name = name;
		this.value = value;
		this.matchMode = matchMode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getMatchMode() {
		return matchMode;
	}

	public void setMatchMode(String matchMode) {
		this.matchMode = matchMode;
	}
}
