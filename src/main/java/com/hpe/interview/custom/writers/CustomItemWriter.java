package com.hpe.interview.custom.writers;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;

import com.hpe.interview.mapper.GeoDataModel;

/**
 * 
 * @author sathy
 *
 */
public interface CustomItemWriter extends ItemWriter<GeoDataModel> {
	/**
	 * beforeStep and afterStep required since the object is injected as
	 * CustomItemWriter reference
	 * 
	 * @param stepExecution
	 */
	@BeforeStep
	public void beforeStep(StepExecution stepExecution);

	@AfterStep
	public void afterStep(StepExecution stepExecution);

}
