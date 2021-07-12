package com.hpe.interview.custom.writers.factory;

import java.util.Map;

import org.springframework.batch.item.ItemWriter;

import com.hpe.interview.mapper.GeoDataModel;
/**
 * 
 * @author sathy
 *
 */
public interface FileWriterFactory {
	public ItemWriter<GeoDataModel> getInstance(String fileType) throws IllegalAccessException;
	public Map<String, Object> getObject();
}
