/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.commun.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.el.ValueExpression;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;

/**
 *
 * @author Leghettas rabah
 */
public abstract class AbstractController {

    //PostConstruct
    protected abstract void initController();

    @PostConstruct
    private void init() {
        initController();
    }

    public Map getConstraints(DataTable dt) {
        if (dt != null) {
            Map<String, FilterMatchMode> constraints = new HashMap<>(dt.getColumns().size());

            for (UIColumn column : dt.getColumns()) {
                ValueExpression filterExpression = column.getValueExpression("filterBy");
                if (null != filterExpression) {
                    String filterExpressionString = filterExpression.getExpressionString();
                    //evaluating filtered field id
                    String filteredField = filterExpressionString.substring(filterExpressionString.indexOf('.') + 1, filterExpressionString.indexOf('}'));
                    FilterMatchMode matchMode = FilterMatchMode.fromUiParam(column.getFilterMatchMode());

                    constraints.put(filteredField, matchMode);
                }
            }
            return constraints;
        }
        return null;
    }

    public List<FieldValueMatchMode> getFilter(Map<String, Object> filters, DataTable dt) {
        Map<String, FilterMatchMode> mapMatchMode = getConstraints(dt);
        List<FieldValueMatchMode> fieldValueMatchMode = new ArrayList();
        for (Map.Entry<String, Object> entrySet : filters.entrySet()) {
            String key = entrySet.getKey();
            String value = entrySet.getValue().toString();
            String matchMode = mapMatchMode.get(key).toString();
            if (!value.equals("")) {
                fieldValueMatchMode.add(new FieldValueMatchMode(key, value, matchMode));
            }
        }
        return fieldValueMatchMode;
    }
}
