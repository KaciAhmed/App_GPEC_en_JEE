package dz.elit.gpecpf.commun.util;

import dz.elit.autoconfigure.AutoConfiguration;

/**
 *
 * @author laidani.youcef
 */
public class InitialisationServeurRessource {

	private final static String POSTGRES_USER_NAME = "postgres";
	private final static String POSTGRES_PASSWORD = "postgres";
	private final static String POSTGRES_HOST = "localhost";
	private final static String POSTGRES_PORT = "5432";
	private final static String DB_NAME = "bd_gpecpf";
	private final static String SCH_NAME = "sch_admin";
	private final static String POOL_NAME = "post-gre-sql_bd_gpecpfPool";
	private final static String JNDI_NAME = "gpecpfJNDI";
	private final static String REALM_NAME = "gpecpfRealm";

	private final static String USER_NAME_GLASSFISH = "";
	private final static String PASSWORD_GLASSFISH = "";

	private final static String HOST_GLASSFISH = "localhost";
	private final static String PORT_GLASSFISH = "4848";

	public static void main(String args[]) {
		AutoConfiguration auto = new AutoConfiguration();

		//Cr�er base de donn�e
		auto.CreateDataBase(DB_NAME, POSTGRES_PORT, POSTGRES_USER_NAME, POSTGRES_PASSWORD);
		//Cr�er schema
		auto.CreateSchema(DB_NAME, SCH_NAME, POSTGRES_PORT, POSTGRES_USER_NAME, POSTGRES_PASSWORD);
		auto.CreateSchema(DB_NAME, StaticUtil.COMPETENCE_SCHEMA, POSTGRES_PORT, POSTGRES_USER_NAME, POSTGRES_PASSWORD);
		auto.CreateSchema(DB_NAME, StaticUtil.POSTE_SCHEMA, POSTGRES_PORT, POSTGRES_USER_NAME, POSTGRES_PASSWORD);
		auto.CreateSchema(DB_NAME, StaticUtil.EVALUATION_SCHEMA, POSTGRES_PORT, POSTGRES_USER_NAME, POSTGRES_PASSWORD);
		//Cr?er pool de connection
		auto.CreateJDBCConnectionPool(POSTGRES_USER_NAME, POSTGRES_PASSWORD, POSTGRES_HOST, POSTGRES_PORT, POOL_NAME, DB_NAME, USER_NAME_GLASSFISH, PASSWORD_GLASSFISH, HOST_GLASSFISH, PORT_GLASSFISH);
		//Cr?er JNDI
		auto.CreateJDBCRessource(JNDI_NAME, POOL_NAME, USER_NAME_GLASSFISH, PASSWORD_GLASSFISH, HOST_GLASSFISH, PORT_GLASSFISH);
		//Cr?er Realm
		auto.CreateRealm(REALM_NAME, JNDI_NAME, USER_NAME_GLASSFISH, PASSWORD_GLASSFISH, HOST_GLASSFISH, PORT_GLASSFISH);
	}
}
