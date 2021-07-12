package com.hpe.interview.custom.writers.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hpe.interview.constants.BatchFileConstants;
import com.hpe.interview.custom.writers.CustomItemWriter;
import com.hpe.interview.mapper.GeoDataModel;
/**
 * 
 * @author sathy
 *
 */
@Component
@JobScope
public class FileWriterFactoryImpl implements FileWriterFactory
{

	@Autowired
	private CustomItemWriter excelWriter;
	@Autowired
	private CustomItemWriter tabSeparatedText;
	@Autowired
	private CustomItemWriter tabSeparatedCsv;
	@Autowired
	private StaxEventItemWriter<GeoDataModel> xmlItemWriter;

	private static final Map<String, Object> handler = new HashMap<String, Object>();
	
	@Override
	@PostConstruct
	public Map<String, Object> getObject() {
		handler.put(BatchFileConstants.FileTypes.excelFile.toString(), excelWriter);
		handler.put(BatchFileConstants.FileTypes.textTablFile.toString(), tabSeparatedText);
		handler.put(BatchFileConstants.FileTypes.csvTabFile.toString(), tabSeparatedCsv);
		handler.put(BatchFileConstants.FileTypes.xmlFile.toString(), xmlItemWriter);
		return handler;
	}

	@Override
	@SuppressWarnings("unchecked")
	public ItemWriter<GeoDataModel> getInstance(String fileType) throws IllegalAccessException {
		return (ItemWriter<GeoDataModel>) Optional.ofNullable(handler.get(fileType))
				.orElseThrow(() -> new IllegalAccessException("Invalid File Type"));
	}

}
