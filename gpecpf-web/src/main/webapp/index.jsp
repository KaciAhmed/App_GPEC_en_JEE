
<%@page import="dz.elit.gpecpf.administration.service.AdminUtilisateurFacade"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="javax.naming.Context"%>
<%@page import="dz.elit.gpecpf.administration.entity.AdminUtilisateur"%>
<%@page import="dz.elit.gpecpf.commun.util.StaticUtil"%>
<%@page import="org.primefaces.push.Status.STATUS"%>
<%@page import="javax.faces.application.FacesMessage"%>
<%@page import="javax.faces.context.FacesContext"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.security.Principal"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%
    Principal loginUser = request.getUserPrincipal();

    if (loginUser != null) {
        Context c = new InitialContext();
        
        AdminUtilisateurFacade utilisateurFacade = (AdminUtilisateurFacade) c.lookup("java:global/gpecpf-ear/gpecpf-ejb-1.0/AdminUtilisateurFacade!" + AdminUtilisateurFacade.class.getName());
        AdminUtilisateur utilisateur = utilisateurFacade.findByLogin(loginUser.getName());

        if (request.isUserInRole("INACTIF")) {
            response.sendRedirect(request.getContextPath() + "/pages/erreur/inactif.xhtml");
        } else if (request.isUserInRole("EXPIRE")) {
            response.sendRedirect(request.getContextPath() + "/pages/erreur/expire.xhtml");
        } else if (request.isUserInRole("PAS_ENCORS_ACTIF")) {
            response.sendRedirect(request.getContextPath() + "/pages/erreur/pasEncorsActif.xhtml");
        } else if (utilisateur.getPwd().equals(StaticUtil.getDefaultEncryptPassword())) {
            response.sendRedirect(request.getContextPath() + "/pages/commun/changePwdUtilisateur.xhtml");
        } else {
            response.sendRedirect(request.getContextPath() + "/accueil.xhtml");
        }

    } else {
        response.sendRedirect(request.getContextPath() + "/login.xhtml");
    }
%>
