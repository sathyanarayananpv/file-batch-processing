package com.hpe.interview.constants;

/**
 * 
 * @author sathy
 *
 */
public class BatchFileConstants {
	public static final String[] colNames = { "anzsic06", "Area", "year", "geo_count", "ec_count" };
	public static final int CHUNK_SIZE = 1000;
	public static final String xmlFileNameSuffix = "_excel.xlsx";
	public static final String txtFileNameSuffix = "_text.txt";
	public static final String csvFileNameSuffix = "_csv.csv";
	public static final String tabDelimiter = "\t";

	public static enum FileTypes {
		csvTabFile, textTablFile, excelFile, xmlFile;
	}
}
