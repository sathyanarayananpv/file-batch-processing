package com.hpe.interview.custom.writers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;

import com.hpe.interview.constants.BatchFileConstants;
import com.hpe.interview.mapper.GeoDataModel;

/**
 * 
 * @author sathy
 *
 *         New Sheet will be created for every 10lakh records
 */
public class ExcelWriterImpl implements CustomItemWriter {
	private static Logger logger = LoggerFactory.getLogger(ExcelWriterImpl.class);
	private String outputFilename;
	private Workbook workbook;
	private CellStyle dataCellStyle;
	private int page = 0;
	private final static int MAX_ROW_ALLOWED = 1000000;
	int counter = 0;

	public ExcelWriterImpl(String fileName, String outputFolderAndPrefix) {
		outputFilename = outputFolderAndPrefix + fileName;
	}

	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Job Step Started("+outputFilename+") for ::" + stepExecution.getJobExecutionId()
		+ ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		// outputFilename += "ExcelOutput.xlsx";
		workbook = new SXSSFWorkbook(100);
		createSheetWorkFlow();

	}

	public Sheet createSheetWorkFlow() {
		Sheet sheet = workbook.createSheet("GeoDatas-Segment" + page);
		sheet.createFreezePane(0, 3, 0, 3);
		sheet.setDefaultColumnWidth(20);
		addHeaders(sheet);
		initDataStyle();
		return sheet;

	}

	@Override
	public void write(List<? extends GeoDataModel> items) throws Exception {
		Sheet sheet = workbook.getSheetAt(page);
		int rowNum = sheet.getLastRowNum();
		if (rowNum >= MAX_ROW_ALLOWED) {
			// Workflow to add new sheet
			page++;
			sheet = createSheetWorkFlow();
			rowNum = sheet.getLastRowNum();
		}
		for (GeoDataModel data : items) {
			rowNum++;
			Row row = sheet.createRow(rowNum);
			createStringCell(row, data.getAnzsic06(), 0);
			createStringCell(row, data.getArea(), 1);
			createStringCell(row, data.getYear(), 2);
			createStringCell(row, String.valueOf(data.getEcCount()), 3);
			createStringCell(row, String.valueOf(data.getGeoCount()), 4);
		}
		counter += items.size();
		logger.debug("Writing " + outputFilename + "::ExcelWriter => Completed(" + counter
				+ ") >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	}

	@AfterStep
	public void afterStep(StepExecution stepExecution) {
		logger.debug("afterStep ExcelWriter::" + outputFilename + " >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		try {
			FileOutputStream fos = new FileOutputStream(outputFilename);
			workbook.write(fos);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Job Step Completed("+outputFilename+") for ::" + stepExecution.getJobExecutionId()
		+ ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	}

	private void addHeaders(Sheet sheet) {

		Workbook wb = sheet.getWorkbook();

		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();

		font.setFontHeightInPoints((short) 10);
		font.setFontName("Arial");
		font.setBold(true);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setFont(font);

		Row row = sheet.createRow(0);
		int col = 0;

		for (String header : BatchFileConstants.colNames) {
			Cell cell = row.createCell(col);
			cell.setCellValue(header);
			cell.setCellStyle(style);
			col++;
		}
	}

	private void initDataStyle() {
		dataCellStyle = workbook.createCellStyle();
		Font font = workbook.createFont();

		font.setFontHeightInPoints((short) 10);
		font.setFontName("Arial");
		// dataCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
		dataCellStyle.setFont(font);
	}

	@SuppressWarnings("deprecation")
	private void createStringCell(Row row, String val, int col) {
		Cell cell = row.createCell(col);
		cell.setCellType(CellType.STRING);
		cell.setCellValue(val);
	}

}