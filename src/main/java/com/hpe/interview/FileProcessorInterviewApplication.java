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
public class FileProcessorInterviewApplication {
	private static Logger logger = LoggerFactory.getLogger(FileProcessorInterviewApplication.class);

	/**
	 * @param args - Command Arguments if any
	 */
	public static void main(String[] args) {
		logger.info("Starting File Processing application :: FileProcessorInterviewApplication:main()");
		SpringApplication.run(FileProcessorInterviewApplication.class, args);
	}

}
