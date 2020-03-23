/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.commun.util;

import dz.elit.autoconfigure.AutoConfiguration;





/**
 *
 * @author laidani.youcef
 */
public class InitialisationServeurRessource {

    private final static String POSTGRES_USER_NAME = "postgres";
    private final static String POSTGRES_PASSWORD = "0000";
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
    
    

//    private final static String TABLE_NAME = "table_test";
//    private final static String TABLE_ATTRIBUTES = "("
//            + "ID INT NOT NULL, "
//            + "NAME VARCHAR(20) NOT NULL,"
//            + "AGE INT NOT NULL, "
//            + "ADDRESS CHAR(25), "
//            + "SALARY DECIMAL(18, 2), "
//            + "PRIMARY KEY (ID)"
//            + ")";
     public static void main(String args[]) {
         AutoConfiguration auto = new AutoConfiguration();

        //Cr�er base de donn�e
        auto.CreateDataBase(DB_NAME, POSTGRES_PORT, POSTGRES_USER_NAME, POSTGRES_PASSWORD);
        //Cr�er schema
        auto.CreateSchema(DB_NAME, SCH_NAME, POSTGRES_PORT, POSTGRES_USER_NAME, POSTGRES_PASSWORD);
        //auto.CreateTable(DB_NAME, SCH_NAME, TABLE_NAME, TABLE_ATTRIBUTES,POSTGRES_USER_NAME, POSTGRES_PASSWORD);
        //Cr?er pool de connection
        auto.CreateJDBCConnectionPool(POSTGRES_USER_NAME, POSTGRES_PASSWORD, POSTGRES_HOST, POSTGRES_PORT, POOL_NAME, DB_NAME, USER_NAME_GLASSFISH, PASSWORD_GLASSFISH, HOST_GLASSFISH, PORT_GLASSFISH);
        //Cr?er JNDI
        auto.CreateJDBCRessource(JNDI_NAME, POOL_NAME, USER_NAME_GLASSFISH, PASSWORD_GLASSFISH, HOST_GLASSFISH, PORT_GLASSFISH);
        //Cr?er Realm
        auto.CreateRealm(REALM_NAME, JNDI_NAME, USER_NAME_GLASSFISH, PASSWORD_GLASSFISH, HOST_GLASSFISH, PORT_GLASSFISH);
    }
     
     
     //******* Create database ******


        //********* Generate roles of security *****************
//        auto.securityRolesWeb(PATH_WEB_INF, DB_NAME, POSTGRES_HOST, POSTGRES_PORT, POSTGRES_USER_NAME, POSTGRES_PASSWORD);
//        auto.securityConstrainstWeb(PATH_WEB_INF, DB_NAME, POSTGRES_HOST, POSTGRES_PORT, POSTGRES_USER_NAME, POSTGRES_PASSWORD);
//        auto.securityRoleMappings(PATH_WEB_INF, DB_NAME, POSTGRES_HOST, POSTGRES_PORT, POSTGRES_USER_NAME, POSTGRES_PASSWORD);

}
