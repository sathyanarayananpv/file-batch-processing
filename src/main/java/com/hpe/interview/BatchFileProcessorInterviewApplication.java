package com.hpe.interview;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

/**
 * 
 * @author sathy
 *
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
public class BatchFileProcessorInterviewApplication {
	private static Logger logger = LoggerFactory.getLogger(BatchFileProcessorInterviewApplication.class);

	/**
	 * @param args - Command Arguments if any
	 */
	public static void main(String[] args) {
		logger.info("Starting File Processing application...");
		SpringApplication.run(BatchFileProcessorInterviewApplication.class, args);
	}

}
