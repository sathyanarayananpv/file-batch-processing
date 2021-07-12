package com.hpe.interview.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * RestController for triggering job
 * 
 * @author sathy
 *
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping(path = "/")
public class RestController {

	private static Logger logger = LoggerFactory.getLogger(RestController.class);
	@Autowired
	JobLauncher jobLauncher;
	@Autowired
	Job job;

	@GetMapping(path = "launchJob", produces = "application/json")
	public String getEmployees() throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		final String jobId = String.valueOf(System.currentTimeMillis());
		executorService.execute(new Runnable() {
			public void run() {
				try {
					logger.info("Scheduling JOB for ::" + jobId);
					JobParameters params = new JobParametersBuilder().addString("JobID", jobId).toJobParameters();
					jobLauncher.run(job, params);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		executorService.shutdown();
		return "Job(" + jobId + ") scheduled, please check the logs";
	}
}
