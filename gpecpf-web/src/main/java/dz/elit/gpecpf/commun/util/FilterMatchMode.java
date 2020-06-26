package dz.elit.gpecpf.commun.util;

/**
 *
 * @author leghettas.rabah
 */
public enum FilterMatchMode {

	STARTS_WITH("startsWith"),
	ENDS_WITH("endsWith"),
	CONTAINS("contains"),
	EXACT("exact"),
	LESS_THAN("lt"),
	LESS_THAN_EQUALS("lte"),
	GREATER_THAN("gt"),
	GREATER_THAN_EQUALS("gte"),
	EQUALS("equals"),
	IN("in");

	private FilterMatchMode() {
		this.uiParam = null;
	}

	private final String uiParam;

	FilterMatchMode(String uiParam) {
		this.uiParam = uiParam;
	}

	public static FilterMatchMode fromUiParam(String uiParam) {
		for (FilterMatchMode matchMode : values()) {
			if (matchMode.uiParam.equals(uiParam)) {
				return matchMode;
			}
		}
		throw new IllegalArgumentException("No MatchMode found for " + uiParam);
	}

}
