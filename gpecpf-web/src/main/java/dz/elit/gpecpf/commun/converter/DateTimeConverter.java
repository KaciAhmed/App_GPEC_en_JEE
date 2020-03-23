/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.commun.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author leghettas.rabah
 */
@FacesConverter(value = "dateTimeConverter")
public class DateTimeConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            return dateFormat.parse(value);
        } catch (ParseException ex) {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion ParseException", "Impossibe de converter le type " + value + " en date"));
            // throw new UnsupportedOperationException("ParseException: Impossibe de converter le type " + value + " en date");
        } catch (Exception ex) {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Exception", "Impossibe de converter le type " + value + " en date"));
            //throw new UnsupportedOperationException("Exception: Impossibe de converter le type " + value + " en date");
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Date) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            return dateFormat.format(value);
        } else {
            throw new UnsupportedOperationException("Impossibe de converter le type " + value.getClass());
        }

    }
}
