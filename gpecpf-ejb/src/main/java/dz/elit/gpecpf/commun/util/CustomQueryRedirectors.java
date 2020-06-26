package dz.elit.gpecpf.commun.util;

import dz.elit.gpecpf.administration.entity.AdminDroitVisibilite;
import dz.elit.gpecpf.administration.entity.AdminGroupe;
import dz.elit.gpecpf.administration.entity.AdminUtilisateur;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.internal.sessions.AbstractRecord;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.queries.DatabaseQuery;
import org.eclipse.persistence.queries.QueryRedirector;
import org.eclipse.persistence.queries.ReadAllQuery;
import org.eclipse.persistence.sessions.Record;
import org.eclipse.persistence.sessions.Session;

/**
 *
 * @author laidani.youcef, leghettas.rabah
 */
public class CustomQueryRedirectors implements QueryRedirector {

	@Override
	public Object invokeQuery(DatabaseQuery query, Record arguments, Session session) {

		ReadAllQuery readAllQuery = (ReadAllQuery) query;

		//Récuperer la première expression
		Expression firstExpression = readAllQuery.getSelectionCriteria();

		//récupérer l'utilisateur depuit la Session
		Principal principal = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		AdminUtilisateur utilisateur = (AdminUtilisateur) facesContext.getExternalContext().getSessionMap().get(principal.getName());

		ExpressionBuilder builder = readAllQuery.getExpressionBuilder();

		//Récuperer les information Creer_Par cette utilisateur
		Expression expression_creer_par = builder.getField("creer_par").equal(utilisateur.getId());

		//Récuperer les donnée qui sont visible par Unité organisationnelle de l'utilisateur et des groupe des appartien cette utilisateur 
		List<AdminDroitVisibilite> listeDroitUtilisateurAndGroupe = new ArrayList<>();
		listeDroitUtilisateurAndGroupe.addAll(utilisateur.getAdminDroitVisibiliteList());
		for (AdminGroupe groupe : utilisateur.getListAdminGroupe()) {
			listeDroitUtilisateurAndGroupe.addAll(groupe.getAdminDroitVisibiliteList());
		}
		Map<String, List<Integer>> collections = mesDroisVisibilite(listeDroitUtilisateurAndGroupe);

		Expression expression_droit_unite = null;
		if (collections.get(query.getReferenceClass().getSimpleName()) != null) {
			expression_droit_unite = builder.getField("id_unite_organisationnelle_createur").in(
					collections.get(query.getReferenceClass().getSimpleName()));
		} else {
			expression_droit_unite = builder.getField("id_unite_organisationnelle_createur").equal(0);
		}

		if (firstExpression != null) {
			readAllQuery.setSelectionCriteria(firstExpression.and(expression_creer_par.or(expression_droit_unite)));
		} else {
			readAllQuery.setSelectionCriteria(expression_creer_par.or(expression_droit_unite));
		}

		readAllQuery.setDoNotRedirect(true);

		return query.execute((AbstractSession) session, (AbstractRecord) arguments);

	}

	private Map<String, List<Integer>> mesDroisVisibilite(List<AdminDroitVisibilite> adminDroitVisibiliteList) {
		Map<String, List<Integer>> droisVisibilite = new HashMap<>();
		for (AdminDroitVisibilite drois : adminDroitVisibiliteList) {
			List<Integer> dr = new ArrayList<>();
			if (droisVisibilite.containsKey(drois.getIdObjetVisibilite().getEntity())) {
				droisVisibilite.get(drois.getIdObjetVisibilite().getEntity()).add(drois.getIdUniteOrganisationnelle().getId());
			} else {
				dr.add(drois.getIdUniteOrganisationnelle().getId());
				droisVisibilite.put(drois.getIdObjetVisibilite().getEntity(), dr);
			}
		}

		return droisVisibilite;
	}

}
