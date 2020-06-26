package dz.elit.gpecpf.compagneEvaluation.controller;

import dz.elit.gpecpf.poste.service.PosteFacade;
import dz.elit.gpecpf.commun.util.AbstractController;
import dz.elit.gpecpf.commun.util.MyUtil;
import dz.elit.gpecpf.compagne_evaluation.entity.Compagneevaluation;
import dz.elit.gpecpf.competence.entity.Competence;
import dz.elit.gpecpf.competence.service.CompetenceFacade;
import dz.elit.gpecpf.evaluation.entity.Candidature;
import dz.elit.gpecpf.evaluation.entity.CandidatureCompetence;
import dz.elit.gpecpf.evaluation.entity.CandidaturePoint;
import dz.elit.gpecpf.evaluation.entity.Evaluation;
import dz.elit.gpecpf.evaluation.entity.Note;
import dz.elit.gpecpf.evaluation.service.EvaluationFacade;
import dz.elit.gpecpf.evaluation.service.NoteFacade;
import dz.elit.gpecpf.gestion_compagne_evaluation.service.CompagneEvaluationFacade;
import dz.elit.gpecpf.poste.entity.Poste;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.chart.DonutChartModel;

/**
 *
 * @author Nadir Ben Mohand
 */
@ManagedBean
@ViewScoped
public class CandidatsController extends AbstractController implements Serializable {

	@EJB
	private PosteFacade posteFacade;
	@EJB
	private CompetenceFacade competenceFacade;
	@EJB
	private CompagneEvaluationFacade compagneFacade;
	@EJB
	private EvaluationFacade evaluationFacade;
	@EJB
	private NoteFacade noteFacade;

	private Poste poste;
	private Compagneevaluation compagne;
	private List<Evaluation> evaluations;
	private List<DonutChartModel> models;
	private List<DonutChartModel> models2;

	private int hasErrors = 0;

	public CandidatsController() {
	}

	@Override
	protected void initController() {
		initAddPoste();
	}

	private void initAddPoste() {
		boolean active = compagneFacade.activeCompagne();
		hasErrors = (active) ? 1 : 0;
		if (active) {
			compagne = compagneFacade.currentCompagne();
		} else {
			List<Compagneevaluation> listCompagnes = compagneFacade.findAllOrderById();
			if (listCompagnes.isEmpty()) {
				hasErrors = 2;
			} else {
				compagne = listCompagnes.get(listCompagnes.size() - 1);
			}
		}
		evaluations = evaluationFacade.evaluationEndForCompagne(compagne);
		poste = new Poste();
		String id = MyUtil.getRequestParameter("id");
		if (id != null) {
			poste = posteFacade.find(Integer.parseInt(id));
		}
		createDonutModels();
	}

	private void createDonutModels() {
		List<Candidature> listCandidats;
		listCandidats = new ArrayList<>();
		for (Evaluation evaluation : evaluations) {
			List<Note> mine = noteFacade.notesForEvaluation(evaluation);
			listCandidats.add(new Candidature(evaluation, mine, poste.getListCompetences()));
		}
		int x = (listCandidats.size() > 4) ? 4 : listCandidats.size();
		models = new ArrayList<>();
		Collections.sort(listCandidats, new CandidaturePoint().reversed());
		for (int i = 1; i <= x; i++) {
			models.add(initModel(i, listCandidats.get(i - 1)));
		}
		models2 = new ArrayList<>();
		Collections.sort(listCandidats, new CandidatureCompetence().thenComparing(new CandidaturePoint()).reversed());
		for (int i = 1; i <= x; i++) {
			models2.add(initModel(i, listCandidats.get(i - 1)));
		}
	}

	private DonutChartModel initModel(int i, Candidature candidature) {
		DonutChartModel model = new DonutChartModel();
		Map<String, Number> circle = new LinkedHashMap<String, Number>();
		for (Competence comp : poste.getListCompetences()) {
			Note note = noteFacade.noteForCompetenceEvaluation(comp, candidature.getEvaluation());
			int point = (note == null) ? 0 : note.getNoteSuperieure();
			circle.put(comp.getLibelle(), point);
		}
		circle.put("*ECART*", poste.getListCompetences().size() * 5 - candidature.getNoteTotal());
		model.addCircle(circle);
		model.setTitle("Candidat " + i + ": " + candidature.getEvaluation().getEmploye().getNom() + " " + candidature.getEvaluation().getEmploye().getPrenom());
		model.setLegendPosition("e");
		model.setSliceMargin(5);
		int p = (int) poste.getListCompetences().size() / 10;
		model.setLegendCols(p);
		model.setShowDataLabels(true);
		model.setDataFormat("value");
		model.setShadow(true);
		return model;
	}

	public PosteFacade getPosteFacade() {
		return posteFacade;
	}

	public void setPosteFacade(PosteFacade posteFacade) {
		this.posteFacade = posteFacade;
	}

	public CompetenceFacade getCompetenceFacade() {
		return competenceFacade;
	}

	public void setCompetenceFacade(CompetenceFacade competenceFacade) {
		this.competenceFacade = competenceFacade;
	}

	public CompagneEvaluationFacade getCompagneFacade() {
		return compagneFacade;
	}

	public void setCompagneFacade(CompagneEvaluationFacade compagneFacade) {
		this.compagneFacade = compagneFacade;
	}

	public EvaluationFacade getEvaluationFacade() {
		return evaluationFacade;
	}

	public void setEvaluationFacade(EvaluationFacade evaluationFacade) {
		this.evaluationFacade = evaluationFacade;
	}

	public NoteFacade getNoteFacade() {
		return noteFacade;
	}

	public void setNoteFacade(NoteFacade noteFacade) {
		this.noteFacade = noteFacade;
	}

	public Poste getPoste() {
		return poste;
	}

	public void setPoste(Poste poste) {
		this.poste = poste;
	}

	public Compagneevaluation getCompagne() {
		return compagne;
	}

	public void setCompagne(Compagneevaluation compagne) {
		this.compagne = compagne;
	}

	public List<Evaluation> getEvaluations() {
		return evaluations;
	}

	public void setEvaluations(List<Evaluation> evaluations) {
		this.evaluations = evaluations;
	}

	public List<DonutChartModel> getModels() {
		return models;
	}

	public void setModels(List<DonutChartModel> models) {
		this.models = models;
	}

	public List<DonutChartModel> getModels2() {
		return models2;
	}

	public void setModels2(List<DonutChartModel> models2) {
		this.models2 = models2;
	}

	public int getHasErrors() {
		return hasErrors;
	}

	public void setHasErrors(int hasErrors) {
		this.hasErrors = hasErrors;
	}

}
