/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.commun.controller;

import dz.elit.gpecpf.administration.entity.AdminConnecxionHistorique;
import dz.elit.gpecpf.administration.entity.AdminConnexionEncours;
import dz.elit.gpecpf.administration.entity.AdminModule;
import dz.elit.gpecpf.administration.entity.AdminUtilisateur;
import dz.elit.gpecpf.administration.service.AdminConnecxionHistoriqueFacade;
import dz.elit.gpecpf.administration.service.AdminConnexionEncoursFacade;
import dz.elit.gpecpf.administration.service.AdminModuleFacade;
import dz.elit.gpecpf.administration.service.AdminUtilisateurFacade;
import dz.elit.gpecpf.commun.filter.LoginFilter;
import java.io.Serializable;
import java.security.Principal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.ViewHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Administrateur
 */
@ManagedBean
@SessionScoped
public class MySessionController implements Serializable {

    @EJB
    private AdminUtilisateurFacade utilisateurFacade;
    @EJB
    private AdminConnecxionHistoriqueFacade connecxionHistoriqueFacade;
    @EJB
    private AdminModuleFacade moduleFacade;

    @EJB
    private AdminConnexionEncoursFacade adminConnexionEncoursFacade;

    @ManagedProperty(value = "#{myApplicationController}")
    private MyApplicationController myApplicationController;

    private AdminUtilisateur utilisateurCourant;
    private AdminConnecxionHistorique connecxionHistorique;

    private AdminConnexionEncours connectionEnCours;

    private List<AdminModule> listModules;

    private String theme = "elit-metro";
    private Integer libelleAnnee;

    /**
     * Creates a new instance of MySession
     */
    public MySessionController() {
    }

    @PostConstruct
    protected void initController() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        libelleAnnee = c.get(Calendar.YEAR);
        initUser();
    }

    public void moduleNavigation(String moduleURL) {
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request
                    = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String url = request.getRequestURL().toString();
            String uri = request.getRequestURI();

            String redirectTO = url.replace(uri, "") + moduleURL;
            externalContext.redirect(redirectTO);
        } catch (Exception e) {
            System.out.println("Exception = " + e);
        }
    }

    public void logTest() {
        // retourner automatiquement sur la page appelante
        FacesContext context = FacesContext.getCurrentInstance();
        System.out.println("context = " + context);
        String viewId = context.getViewRoot().getViewId();
        System.out.println("viewId = " + viewId);
        ViewHandler handler = context.getApplication().getViewHandler();
        System.out.println("handler = " + handler);
        UIViewRoot root = handler.createView(context, viewId);
        System.out.println("root = " + root);
        root.setViewId(viewId);
        //context.setViewRoot(root);
        System.out.println("context2222 = " + context);
    }

    /**
     * Dispatches to the given view id
     *
     * @param context Faces context
     * @param viewId The view id to go to
     */
    private void goToView(FacesContext context, String viewId) {
        ViewHandler viewHandler = context.getApplication().getViewHandler();
        UIViewRoot viewRoot = viewHandler.createView(context, viewId);
        viewRoot.setViewId(viewId);
        context.setViewRoot(viewRoot);
        context.renderResponse();
    }

    /**
     * Dispatches to the given view id
     *
     * @param viewId The view id to go to
     */
    private void goToView(String viewId) {
        FacesContext context = FacesContext.getCurrentInstance();
        ViewHandler viewHandler = context.getApplication().getViewHandler();
        UIViewRoot viewRoot = viewHandler.createView(context, viewId);
        viewRoot.setViewId(viewId);
        context.setViewRoot(viewRoot);
        context.renderResponse();
    }

    private void goToViewRoot() {
        FacesContext context = FacesContext.getCurrentInstance();
        String viewId = context.getViewRoot().getViewId();
        ViewHandler viewHandler = context.getApplication().getViewHandler();
        UIViewRoot viewRoot = viewHandler.createView(context, viewId);
        viewRoot.setViewId(viewId);
        context.setViewRoot(viewRoot);
        context.renderResponse();
    }

    public void initInterfaceUser(String login) {
        listModules = moduleFacade.getListModule(login);
        initConnecxionHistorique();
    }

    public String displayMoreModules() {
        if (listModules == null) {
            return "none";
        }
        return listModules.size() > 6 ? "block" : "none";
    }

    public void initUser() {
        connectionEnCours = new AdminConnexionEncours();
        Principal principal = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
        if (principal != null) {
            utilisateurCourant = utilisateurFacade.findByLogin(principal.getName());
            initInterfaceUser(principal.getName());

            //put the informations of Utilisateur in the session
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.getExternalContext().getSessionMap().put(principal.getName(), utilisateurCourant);
        } else {
            utilisateurCourant = null;
        }
    }

    private String processSSOCookie() {
        String ssoID = "";
        HttpServletRequest request
                = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JSESSIONIDSSO")) {
                    ssoID = cookie.getValue();
                }
            }
        }

        return ssoID;
    }

    private String getAdresseIp() {
        try {
            return ((javax.servlet.http.HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr();
        } catch (Exception e) {
            return "INCONNU";
        }
    }

    private void initConnecxionHistorique() {
        //Historique de connextion
        connecxionHistorique = new AdminConnecxionHistorique();
        connecxionHistorique.setDateConnexion(new Date());
        connecxionHistorique.setAdresseIp(getAdresseIp());
        connecxionHistorique.setUtilisateur((utilisateurCourant != null ? utilisateurCourant.getLogin() : null));
        connecxionHistorique.setIdunique(processSSOCookie());
        try {
            connecxionHistoriqueFacade.create(connecxionHistorique);
            //Connexion en cours
            connectionEnCours.setUtilisateur((utilisateurCourant != null ? (utilisateurCourant.getNom() + " " + utilisateurCourant.getPrenom()) : null));
            connectionEnCours.setDateConnexion(new Date());
            connectionEnCours.setAdresseIp(getAdresseIp());

            //add conextion au application scoped
            //myApplicationController.addConnexion(connecxionHistorique);
            //2eme politique add conextion in the database
            adminConnexionEncoursFacade.create(connectionEnCours);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void updateConnecxionHistorique() {
        connecxionHistorique.setDateDeconnexion(new Date());
        try {
            connecxionHistoriqueFacade.edit(connecxionHistorique);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    public Integer getLoginValue() {
        Integer login_error = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(LoginFilter.ATTRIBUT_LOGIN_ERREUR);
        return login_error == null ? 0 : login_error;
    }

    public String logout() {
        String navigateTo = "";
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext e = context.getExternalContext();
            navigateTo = e.getRequestContextPath();
            e.redirect(e.encodeResourceURL(e.getRequestContextPath() + "/login.xhtml"));
            updateConnecxionHistorique();
        } catch (Exception ea) {
            ea.printStackTrace();
        }
        return navigateTo;
    }

    @PreDestroy
    public void toLogout() {
        updateConnecxionHistorique();
        //ancien politique
        //myApplicationController.removeConnexion(connecxionHistorique);

        try {
            //new politique of connextion en cours
            adminConnexionEncoursFacade.remove(connectionEnCours);
        } catch (Exception e) {
            System.out.println("Exception = " + e);
        }
    }

    public boolean isUserInRole(String role) {
        return FacesContext.getCurrentInstance().getExternalContext().isUserInRole(role);
    }

    public String getLibelleUser() {
        if (utilisateurCourant != null) {
            return utilisateurCourant.getLibelleUser();
        }
        return "Inconnu";
    }

    public AdminUtilisateur getUtilisateurCourant() {
        if (utilisateurCourant == null) {
            initUser();
        }
        return utilisateurCourant;
    }

    public String getTheme() {
        if (utilisateurCourant == null) {
            initUser();
        }
        theme = utilisateurCourant != null ? utilisateurCourant.getTheme() != null ? utilisateurCourant.getTheme() : theme : theme;
        return theme;
    }

    public void setTheme(String theme) {
        if (utilisateurCourant != null) {
            utilisateurCourant.setTheme(theme);
        }
        this.theme = theme;
    }

    public void updateUtilisateur() {
        if (utilisateurCourant != null) {
            try {
                utilisateurFacade.edit(utilisateurCourant);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //  getter && setter
    public Integer getLibelleAnnee() {
        return libelleAnnee;
    }

    public List<AdminModule> getListModules() {
        if (listModules == null || listModules.isEmpty()) {
            initUser();
        }
        return listModules;
    }

    public AdminConnecxionHistorique getConnecxionHistorique() {
        return connecxionHistorique;
    }

    public void setConnecxionHistorique(AdminConnecxionHistorique connecxionHistorique) {
        this.connecxionHistorique = connecxionHistorique;
    }

    public MyApplicationController getMyApplicationController() {
        return myApplicationController;
    }

    public void setMyApplicationController(MyApplicationController myApplicationController) {
        this.myApplicationController = myApplicationController;
    }

}
