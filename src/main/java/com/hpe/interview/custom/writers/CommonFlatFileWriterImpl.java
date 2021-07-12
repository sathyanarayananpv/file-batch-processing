package com.hpe.interview.custom.writers;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;

import com.hpe.interview.constants.BatchFileConstants;
import com.hpe.interview.mapper.GeoDataModel;

/**
 * CommonFlatFileWriter - Takes input parameter i.e delimiter & filename for
 * generating the files dynamically
 * 
 * @author sathy
 *
 */
public class CommonFlatFileWriterImpl implements CustomItemWriter {
	private static Logger logger = LoggerFactory.getLogger(CommonFlatFileWriterImpl.class);
	FileWriter writer;
	String delimiter = null;
	String fileName = null;
	int counter = 0;

	public CommonFlatFileWriterImpl(String delimiter, String fileName, String outputFolderAndPrefix) {
		super();
		this.delimiter = delimiter;
		this.fileName = outputFolderAndPrefix + fileName;
	}

	/**
	 * File will be created and opened once per job for better perfomance and
	 * scaling
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) {
		try {
			writer = new FileWriter(fileName);
			logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Job Step Started("+fileName+") for ::" + stepExecution.getJobExecutionId()
			+ ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			writer.write(String.join(delimiter, BatchFileConstants.colNames));
		} catch (Exception e) {
			logger.error(e.getMessage());
			closeResource();
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * Write method invoked on step call. Writes the data directly into the opened
	 * filewriter object
	 */
	@Override
	public void write(List<? extends GeoDataModel> items) {
		try {
			writer.write("\n" + (items.stream()
					.map(item -> String.valueOf((new StringBuffer().append(item.getAnzsic06()).append(delimiter)
							.append(item.getArea()).append(delimiter).append(item.getYear()).append(delimiter)
							.append(item.getGeoCount()).append(delimiter).append(item.getEcCount())).toString()))
					.collect(Collectors.joining("\n"))));
			writer.flush();;
		} catch (IOException e) {
			logger.error(e.getMessage());
			closeResource();
			throw new RuntimeException(e.getMessage());
		}
		counter += items.size();
		logger.debug("Writing " + fileName + "::CommonFlatFileWriter => Completed(" + counter
				+ ") >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	}

	/**
	 * File will be closed once the job is done
	 */
	@Override
	public void afterStep(StepExecution stepExecution) {
		logger.debug("afterStep CommonFlatFileWriter::" + fileName + " >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		try {
			writer.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
			closeResource();
			throw new RuntimeException(e.getMessage());
		}
		logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Job Step Completed("+fileName+") for ::" + stepExecution.getJobExecutionId()
		+ ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	}

	public void closeResource() {
		if (null != writer) {
			try {
				writer.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}

	}

}
