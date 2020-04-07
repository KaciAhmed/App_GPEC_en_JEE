/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    
    private final String uiParam;
    
    private FilterMatchMode() {
        this.uiParam = null;
    }

    FilterMatchMode(String uiParam) {
        this.uiParam = uiParam;
    }
// cherhcer l'objet corréspendent à lma chaine du filtre demander
    public static FilterMatchMode fromUiParam(String uiParam) {
        for (FilterMatchMode matchMode : values()) {
            if (matchMode.uiParam.equals(uiParam)) {
                return matchMode;
            }
        }
        throw new IllegalArgumentException("No MatchMode found for " + uiParam);
    }

}
