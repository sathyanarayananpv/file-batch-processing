package com.hpe.interview.writers;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;

import com.hpe.interview.model.GeoDataModel;

public interface CustomItemWriter extends ItemWriter<GeoDataModel> {

	@BeforeStep
	public void beforeStep(StepExecution stepExecution);

	@AfterStep
	public void afterStep(StepExecution stepExecution);

}
