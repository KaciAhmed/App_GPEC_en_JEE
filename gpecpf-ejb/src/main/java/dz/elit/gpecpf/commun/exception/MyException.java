/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.commun.exception;

import javax.ejb.ApplicationException;

/**
 *
 * @author leghettas.rabah
 */
@ApplicationException(rollback=true)
public class MyException extends Exception{   

    public MyException(String message) {
        super(message);
    }

    public MyException(String message, Throwable cause) {
        super(message, cause);
    }   

    public MyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
