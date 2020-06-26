package dz.elit.gpecpf.commun.reporting.engine;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;

/**
 *
 * @author GUEFFAF.MESBAH
 */
public class Reporting {

	/**
	 * Cette méthode permet de télécharger l'état sous format pdf
	 *
	 * @param report
	 * @param source
	 * @param parameters
	 */
	public static void downloadReportPdf(InputStream report, JRDataSource source, Map parameters) throws JRException, FileNotFoundException {
		parameters.put("IgnorePagination", false);

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		try {
			ServletOutputStream servletOutputStream = response.getOutputStream();
			context.responseComplete();

			response.setHeader("Cache-Control", "cache, must-revalidate");
			response.setHeader("Pragma", "public");
			response.setHeader("Content-Disposition", "inline; filename=" + (parameters.get("rapportNom")).toString().replace(" ", "_") + ".pdf");
			response.setContentType("application/pdf");
			//System.out.println("parameters.get(iMgDir) = " + parameters.get("iMgDir"));
			parameters.put("iMgDir", parameters.get("iMgDir"));
			JasperRunManager.runReportToPdfStream(report, servletOutputStream, parameters, source);

			servletOutputStream.flush();
			servletOutputStream.close();
		} catch (IOException | JRException ex) {
			Logger.getLogger(Reporting.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Cette méthode permet de télécharger l'état sous format Excelx
	 *
	 * @param report
	 * @param source
	 * @param parameters
	 */
	public static void downloadReportExcelx(InputStream report, JRDataSource source, Map parameters) throws JRException, FileNotFoundException {
		parameters.put("IgnorePagination", true);

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		try {
			ServletOutputStream servletOutputStream = response.getOutputStream();
			context.responseComplete();
			response.setHeader("Cache-Control", "cache, must-revalidate");
			response.setHeader("Pragma", "public");
			response.setHeader("Content-Disposition", "inline; filename=" + (parameters.get("rapportNom")).toString().replace(" ", "_") + ".xlsx");
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, source);
			ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
			JRXlsxExporter exporter = new JRXlsxExporter();

			exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
			//  exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, xlsReport);
			exporter.setParameter(JExcelApiExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			exporter.setParameter(JExcelApiExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
			exporter.exportReport();
			byte[] reportBytes = xlsReport.toByteArray();

			response.setContentLength(reportBytes.length);
			response.getOutputStream().write(reportBytes, 0, reportBytes.length);

			servletOutputStream.flush();
			servletOutputStream.close();
		} catch (IOException | JRException ex) {
			Logger.getLogger(Reporting.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Cette méthode permet de télécharger l'état sous format Csv
	 *
	 * @param report
	 * @param source
	 * @param parameters
	 */
	public static void downloadReportCsv(InputStream report, JRDataSource source, Map parameters) throws JRException, FileNotFoundException {
		parameters.put("IgnorePagination", true);

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		try {
			ServletOutputStream servletOutputStream = response.getOutputStream();
			context.responseComplete();
			response.setHeader("Cache-Control", "cache, must-revalidate");
			response.setHeader("Pragma", "public");
			response.setHeader("Content-Disposition", "inline; filename=" + (parameters.get("rapportNom")).toString().replace(" ", "_") + ".csv");
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, source);
			ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
			JRCsvExporter exporter = new JRCsvExporter();

			exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
			//  exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
			exporter.setParameter(JExcelApiExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			exporter.setParameter(JExcelApiExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, xlsReport);
			exporter.exportReport();

			byte[] reportBytes = xlsReport.toByteArray();

			response.setContentLength(reportBytes.length);
			response.getOutputStream().write(reportBytes, 0, reportBytes.length);

			servletOutputStream.flush();
			servletOutputStream.close();
		} catch (IOException | JRException ex) {
			Logger.getLogger(Reporting.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Cette méthode permet de télécharger l'état sous format Exel
	 *
	 * @param report
	 * @param source
	 * @param parameters
	 */
	public static void downloadReportExel(InputStream report, JRDataSource source, Map parameters) throws JRException, FileNotFoundException {
		parameters.put("IgnorePagination", true);

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		try {
			ServletOutputStream servletOutputStream = response.getOutputStream();
			context.responseComplete();

			response.setHeader("Cache-Control", "cache, must-revalidate");
			response.setHeader("Pragma", "public");
			response.setHeader("Content-Disposition", "inline; filename=" + (parameters.get("rapportNom")).toString().replace(" ", "_") + ".xls");
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

			JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, source);
			ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
			JRXlsExporter exporter = new JRXlsExporter();

			exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
			//  exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
			exporter.setParameter(JExcelApiExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			exporter.setParameter(JExcelApiExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, xlsReport);
			exporter.exportReport();

			byte[] reportBytes = xlsReport.toByteArray();

			response.setContentLength(reportBytes.length);
			response.getOutputStream().write(reportBytes, 0, reportBytes.length);

			servletOutputStream.flush();
			servletOutputStream.close();
		} catch (IOException | JRException ex) {
			Logger.getLogger(Reporting.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static String printEtat(InputStream url, Map parametre, JRDataSource dataSource) throws IOException, JRException {

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		ServletOutputStream servletOutputStream = response.getOutputStream();
		context.responseComplete();
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "inline; filename=" + (parametre.get("rapportNom")).toString().replace(" ", "_") + ".pdf");
		JasperRunManager.runReportToPdfStream(url, servletOutputStream, parametre, dataSource);
		context.responseComplete();
		servletOutputStream.flush();
		servletOutputStream.close();
		return null;
	}

}
