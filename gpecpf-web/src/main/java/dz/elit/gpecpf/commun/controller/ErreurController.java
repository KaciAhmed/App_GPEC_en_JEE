/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.commun.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;

/**
 *
 * @author chekor.samir
 */
@ManagedBean
@RequestScoped
public class ErreurController {

    /**
     * Creates a new instance of ErreurManagedBean
     */
    public ErreurController() {
    }

    public String getStackTrace() {
        // Get the current JSF context
        FacesContext context = FacesContext.getCurrentInstance();
        Map requestMap = context.getExternalContext().getRequestMap();
        // Fetch the exception
        Throwable ex = (Throwable) requestMap.get("javax.servlet.error.exception");

        // Create a writer for keeping the stacktrace of the exception
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);

        // Fill the stack trace into the write
        fillStackTrace(ex, pw);
        return writer.toString();
    }

    /**
     * Write the stack trace from an exception into a writer.
     *
     * @param ex Exception for which to get the stack trace
     * @param pw PrintWriter to write the stack trace
     */
    private void fillStackTrace(Throwable ex, PrintWriter pw) {
        if (null == ex) {
            return;
        }
        ex.printStackTrace(pw);
        pw.println("Root Cause:");
        // The first time fillStackTrace is called it will always
        //  be a ServletException
        if (ex instanceof ServletException) {
            Throwable cause = ((ServletException) ex).getRootCause();
            if (null != cause) {
                pw.println("Root Cause:");
                fillStackTrace(cause, pw);
            }
        } else {
            // Embedded cause inside the ServletException
            Throwable cause = ex.getCause();

            if (null != cause) {
                pw.println("Cause:");
                fillStackTrace(cause, pw);
            }
        }
    }
}
