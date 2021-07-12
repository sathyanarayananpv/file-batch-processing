package com.hpe.interview.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import com.hpe.interview.model.GeoDataModel;
import com.hpe.interview.writers.CommonFlatFileWriter;
import com.hpe.interview.writers.ExcelWriter;
import com.hpe.interview.writers.CustomItemWriter;

/**
 * 
 * @author sathy - File processor batch configuration. Since job is scheduled
 *         via http call, Step,Reader and Writer beans are scoped as "Job" for
 *         perfomance scaling with data integrity.
 */
@Configuration
@EnableBatchProcessing
public class FileBatchProcessorConfig {

	private static Logger logger = LoggerFactory.getLogger(FileBatchProcessorConfig.class);
	private static final int CHUNK_SIZE = 1000;

	// Job Bean
	@Bean
	public Job readCSVFileJob(JobBuilderFactory jobBuilderFactory, Step step) {
		return jobBuilderFactory.get("readCSVFileJob").incrementer(new RunIdIncrementer()).flow(step).end().build();
	}

	// Step Bean
	@Bean
	@JobScope
	public Step step(StepBuilderFactory stepBuilderFactory, CompositeItemWriter<GeoDataModel> compositeWriter,
			FlatFileItemReader<GeoDataModel> reader, CustomItemWriter excelWriter, CustomItemWriter tabSeparatedText,
			CustomItemWriter tabSeparatedCsv) {
		return stepBuilderFactory.get("step").<GeoDataModel, GeoDataModel>chunk(CHUNK_SIZE).reader(reader)
				.writer(compositeWriter).listener(excelWriter).listener(tabSeparatedText).listener(tabSeparatedCsv)
				// .writer(excelWriter)
				.build();
	}

	// Reader Bean
	@Bean
	@JobScope
	public FlatFileItemReader<GeoDataModel> reader(@Value("${input.file}") String inputFileName) {
		logger.info("FlatFileItemReader :: inputfile : " + inputFileName);
		FlatFileItemReader<GeoDataModel> reader = new FlatFileItemReader<GeoDataModel>();
		reader.setResource(new FileSystemResource(inputFileName));
		reader.setLinesToSkip(1);
		reader.setLineMapper(new DefaultLineMapper<GeoDataModel>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(FileProcessorConstants.colNames);
						logger.info("FlatFileItemReader :: column names : "
								+ String.join(",", FileProcessorConstants.colNames));
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<GeoDataModel>() {
					{
						setTargetType(GeoDataModel.class);
					}
				});
			}
		});
		return reader;
	}

	// CompositeWriter Bean for combining multiple writers
	@Bean(destroyMethod = "")
	@JobScope
	public CompositeItemWriter<GeoDataModel> compositeWriter(StaxEventItemWriter<GeoDataModel> xmlItemWriter,
			CustomItemWriter excelWriter, CustomItemWriter tabSeparatedText, CustomItemWriter tabSeparatedCsv) {
		CompositeItemWriter<GeoDataModel> writer = new CompositeItemWriter<>();
		writer.setDelegates(Arrays.asList(xmlItemWriter, excelWriter, tabSeparatedText, tabSeparatedCsv));
		return writer;
	}

	// XMLWriter Bean
	@Bean
	@JobScope
	public StaxEventItemWriter<GeoDataModel> xmlItemWriter(@Value("${output.path}") String outputPath,
			@Value("#{jobParameters['JobID']}") String jobId) throws Exception {
		String fileName = outputPath + jobId + "_xml.xml";
		Map<String, String> aliases = new HashMap<String, String>();
		aliases.put("GeoData", "com.hpe.interview.model.GeoDataModel");
		XStreamMarshaller marshaller = new XStreamMarshaller();
		marshaller.setAliases(aliases);

		StaxEventItemWriter<GeoDataModel> writer = new StaxEventItemWriter<>();
		logger.info("StaxEventItemWriter :: inputfile : " + fileName);
		writer.setResource(new FileSystemResource(fileName));
		writer.setRootTagName("GeoDatas");
		writer.setMarshaller(marshaller);
		writer.setOverwriteOutput(true);
		return writer;
	}

	@Bean
	@JobScope
	public CustomItemWriter excelWriter(@Value("${output.path}") String outputPath,
			@Value("#{jobParameters['JobID']}") String jobId) {
		return new ExcelWriter("_excel.xlsx", outputPath + jobId);
	}

	@Bean
	@JobScope
	public CustomItemWriter tabSeparatedText(@Value("${output.path}") String outputPath,
			@Value("#{jobParameters['JobID']}") String jobId) {
		// Filename and
		return new CommonFlatFileWriter("\t", "_TabSeparatedText.txt", outputPath + jobId);
	}

	@Bean
	@JobScope
	public CustomItemWriter tabSeparatedCsv(@Value("${output.path}") String outputPath,
			@Value("#{jobParameters['JobID']}") String jobId) {
		return new CommonFlatFileWriter("\t", "_TabSeparatedCSV.csv", outputPath + jobId);
	}

	/*
	 * Another straightforward way without stream But perfomance wise its not good.
	 * 
	 * // TextWriter Bean
	 * 
	 * @Bean public FlatFileItemWriter<GeoDataModel>
	 * textWriter(@Value("${output.path}") String outputPath) {
	 * FlatFileItemWriter<GeoDataModel> textWriter = new FlatFileItemWriter<>();
	 * textWriter.setResource(new FileSystemResource(outputPath+"XmlOutput.xml"));
	 * textWriter.setAppendAllowed(false); textWriter.setLineAggregator(new
	 * DelimitedLineAggregator<GeoDataModel>() { { setDelimiter("\t");
	 * setFieldExtractor(new BeanWrapperFieldExtractor<GeoDataModel>() { {
	 * setNames(FileProcessorConstants.colNames); } }); } });
	 * textWriter.setHeaderCallback(new FlatFileHeaderCallback() {
	 * 
	 * @Override public void writeHeader(Writer writer) throws IOException {
	 * writer.write(String.join("\t", FileProcessorConstants.colNames)); } });
	 * return textWriter; }
	 */
}