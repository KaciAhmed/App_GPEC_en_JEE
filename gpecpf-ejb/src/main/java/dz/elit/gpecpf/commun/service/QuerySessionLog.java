/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.commun.service;

import dz.elit.gpecpf.administration.entity.AdminHistorique;
import dz.elit.gpecpf.commun.util.StaticUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.logging.AbstractSessionLog;
import org.eclipse.persistence.logging.SessionLog;
import org.eclipse.persistence.logging.SessionLogEntry;

/**
 *
 * @author abdechakour.amine extends
 */
@Stateless
public class QuerySessionLog extends AbstractSessionLog implements SessionLog {

    @PersistenceContext(unitName = StaticUtil.UNIT_NAME)
    private EntityManager em;
    private JSONObject insertJSON;
    AdminHistorique adminHistorique = new AdminHistorique();

    protected EntityManager getEntityManager() {
        EntityManagerFactory entityFactory = Persistence.createEntityManagerFactory(StaticUtil.UNIT_NAME);
        em = (JpaEntityManager) entityFactory.createEntityManager();
        return em;

    }

    @PostPersist
    public void postPersist(Object ob) throws IllegalArgumentException, IllegalAccessException, InstantiationException, Exception {
        creatHistoriqueDonne(ob, "Insert");
    }

    @PostUpdate
    public void postUpdate(Object ob) throws IllegalArgumentException, IllegalAccessException, Exception {
        creatHistoriqueDonne(ob, "Update");
    }

    @PostRemove
    public void postRemove(Object ob) throws IllegalArgumentException, IllegalAccessException, Exception {
        creatHistoriqueDonne(ob, "Delete");
    }

    public String getCurrentUser() {
        try{
                return FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();
            }catch(Exception e){
                return "SYSTEM";
            }
    }

    public String getCurrentHostUser() {
        try{
            return ((javax.servlet.http.HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr();
        }catch(Exception e){
            return "SYSTEM";
        }
    }

    public String getPath() {
        String path;
        FacesContext context = FacesContext.getCurrentInstance();
        if (context == null) {
            path = "SYSTEME";
        } else {
            path = context.getExternalContext().getRequestServletPath();
        }
        return path;
    }

    @Override
    public void log(SessionLogEntry sessionLogEntry) {
        if ("sql".equals(sessionLogEntry.getNameSpace())) {
            String reqSql = sessionLogEntry.getMessage();
            try {
                if ((reqSql.toUpperCase().split("ADMIN_HISTORIQUE").length == 1)
                        && ((reqSql.toUpperCase().split("SELECT").length == 1)
                        || ((reqSql.toUpperCase().split("TRUNCATE").length != 1))
                        || ((reqSql.toUpperCase().split("CREATE").length != 1))
                        || ((reqSql.toUpperCase().split("INSERT").length != 1))
                        || ((reqSql.toUpperCase().split("UPDATE").length != 1))
                        || ((reqSql.toUpperCase().split("DELETE").length != 1)))) {
                    creatHistoriqueSql(reqSql, getCurrentUser(), getCurrentHostUser(), "SQL");

                }
            } catch (JSONException ex) {
                Logger.getLogger(QuerySessionLog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void recup_fild(Object ob, Field[] fil) throws JSONException {
        String name, value;
        boolean inserer;
        List<String> listeAnnotationElimine = new ArrayList<>();
        listeAnnotationElimine.add("XmlTransient");
        for (Field unfil : fil) {
            inserer = true;
            name = unfil.getName();
            if (unfil.getGenericType().toString().split("java.util.List").length > 1) {
                inserer = false;
            }
            unfil.setAccessible(true);
            try {
                if (unfil.get(ob) != null) {
                    value = unfil.get(ob).toString();
                } else {
                    value = null;
                }
            } catch (Exception e) {
                value = "Exception";
            }
            for (java.lang.annotation.Annotation annotation : unfil.getAnnotations()) {
                if ("Id".equalsIgnoreCase(annotation.annotationType().getSimpleName())) {
                    adminHistorique.setIdObjet(value);
                }
                if (listeAnnotationElimine.contains(annotation.annotationType().getSimpleName())) {
                    inserer = false;
                }
            }
            if (unfil.getAnnotations().length > 0) {
                if (inserer) {
                    try {
                        insertJSON.put(name, value);
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        }
    }

    public void creatHistoriqueSql(Object ob, String user, String hostUser, String type) throws JSONException {
        adminHistorique = new AdminHistorique();
        adminHistorique.setDonnee(ob.toString());
        adminHistorique.setDateMouvement(new Date());
        adminHistorique.setUtilisateur(user);
        adminHistorique.setTypeMouvement(type);
        adminHistorique.setAdresseIp(hostUser);
        adminHistorique.setClasse("SQL");
        adminHistorique.setUrl(getPath());

//        try {
//            
//            Gson h = new Gson();
//            String s = h.toJson(adminHistorique);
//            Client.WsAdminHistoriqueFacade.createHistoriqueByString(s);
//            
//        } catch (Exception e) {
        //System.out.println("-----requet----------------non service");
        getEntityManager();
        em.persist(adminHistorique);
        em = null;

    //    }
    }

    public void creatHistoriqueDonne(Object ob, String type) throws JSONException {
       // System.out.println("-------------> =  querySession");
        String user = getCurrentUser();
        String hostUser = getCurrentHostUser();
        insertJSON = new JSONObject();
        adminHistorique = new AdminHistorique();
        recup_fild(ob, ob.getClass().getDeclaredFields());
        recup_fild(ob, ob.getClass().getSuperclass().getDeclaredFields());
        adminHistorique.setIdAdminHistorique(-1);
        adminHistorique.setDonnee(insertJSON.toString());
        adminHistorique.setDateMouvement(new Date());
        adminHistorique.setUtilisateur(user);
        adminHistorique.setTypeMouvement(type);
        adminHistorique.setAdresseIp(hostUser);
        adminHistorique.setClasse(ob.getClass().getName());
        adminHistorique.setUrl(getPath());

//        try {
//            Gson h = new Gson();
//            String s = h.toJson(adminHistorique);
//            Client.WsAdminHistoriqueFacade.createHistoriqueByString(s);
//
//        } catch (Exception e) {
//
//            System.out.println("--------entt-------------non service");
        getEntityManager();
        em.persist(adminHistorique);
        em = null;

    //    }
    }

}
