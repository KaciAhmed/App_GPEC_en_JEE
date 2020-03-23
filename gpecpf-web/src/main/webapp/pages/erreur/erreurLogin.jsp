<%-- 
    Document   : index
    Created on : octobre 2014 
    Author     : utilisateur
--%>
<%@page import="dz.elit.gpecpf.commun.filter.LoginFilter"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
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
        response.sendRedirect(request.getContextPath() + LoginFilter.WELCOME_PAGE);
    } else {
        String login = request.getParameter("j_username");
        if (login == null) {
            login = "";
        }
        if (session.getAttribute(LoginFilter.ATTRIBUT_NBR_TENTATIVE) == null) {
            session.setAttribute(LoginFilter.ATTRIBUT_NBR_TENTATIVE, 0);
            session.setAttribute(LoginFilter.ATTRIBUT_LOGIN_ERREUR, 0);
            session.setAttribute(LoginFilter.DATEBLOCAGENAME, null);
        }
        if (!login.equals("")) {
            Integer nbr = (Integer) session.getAttribute(LoginFilter.ATTRIBUT_NBR_TENTATIVE);
            if (nbr == LoginFilter.NBRETENTAVIE) {
                //if (date =         = null) {
                session.setAttribute(LoginFilter.ATTRIBUT_NBR_TENTATIVE, 0);
                session.setAttribute(LoginFilter.ATTRIBUT_LOGIN_ERREUR, 0);
                session.setAttribute(LoginFilter.DATEBLOCAGENAME, new Date());
                // }
                response.sendRedirect(request.getContextPath() + LoginFilter.VERROU_PAGE);
            } else {
                session.setAttribute(LoginFilter.ATTRIBUT_NBR_TENTATIVE, nbr + 1);
                session.setAttribute(LoginFilter.ATTRIBUT_LOGIN_ERREUR, 2);
                response.sendRedirect(request.getContextPath() + LoginFilter.LOGIN_PAGE);
            }
        } else {
            session.setAttribute(LoginFilter.ATTRIBUT_LOGIN_ERREUR, 1);
            response.sendRedirect(request.getContextPath() + LoginFilter.LOGIN_PAGE);
        }
    }
%>
