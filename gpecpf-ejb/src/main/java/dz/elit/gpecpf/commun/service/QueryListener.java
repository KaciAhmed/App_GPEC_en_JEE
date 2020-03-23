/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.commun.service;

import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.logging.SessionLog;
import org.eclipse.persistence.sessions.Session;

/**
 *
 * @author abdechakour.amine
 */
public class QueryListener implements SessionCustomizer {

    @Override
    public void customize(Session aSession) throws Exception {

        SessionLog aCustomLogger = new QuerySessionLog();
        aCustomLogger.setLevel(1);
        aSession.setSessionLog(aCustomLogger);
    }

}
