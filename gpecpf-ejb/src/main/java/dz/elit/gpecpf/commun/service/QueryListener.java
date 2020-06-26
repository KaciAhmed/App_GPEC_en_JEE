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
