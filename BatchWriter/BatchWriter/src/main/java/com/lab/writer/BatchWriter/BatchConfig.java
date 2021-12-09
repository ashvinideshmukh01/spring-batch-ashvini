package com.lab.writer.BatchWriter;

import java.io.IOException;
import java.io.Writer;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.skip.AlwaysSkipItemSkipPolicy;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.web.client.ResourceAccessException;

import com.lab.writer.BatchProcessor.ProductProcessor;
import com.lab.writer.BatchWriter.lister.ProductListner;
import com.lab.writer.BatchWriter.model.Product;

@EnableBatchProcessing
@Configuration
public class BatchConfig {

	@Autowired
	private StepBuilderFactory steps;

	@Autowired
	private JobBuilderFactory jobs;
	
	@Autowired
	private ProdServiceAdaptor prodServiceAdaptor;
	
	public ItemReaderAdapter serviceAdaptor() {
		ItemReaderAdapter readerAdaptor = new ItemReaderAdapter<>();
		readerAdaptor.setTargetObject(prodServiceAdaptor);
		readerAdaptor.setTargetMethod("nextProduct");
		return readerAdaptor;
	}
	
	@StepScope
	@Bean
	public FlatFileItemReader reader(@Value("#{jobParameters[fileInput]}") FileSystemResource input) {
		FlatFileItemReader reader = new FlatFileItemReader<>();
		reader.setResource(input);
		reader.setLinesToSkip(1);
		reader.setLineMapper(new DefaultLineMapper<>() {
			{
				setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {
					{
						setTargetType(Product.class);
					}
				});
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
					setNames(new String [] {"InproductID","productName","prodDesc","price","unit"});
					setDelimiter(",");
					}
				});
			}
		});

		return reader;

	}
	
	@StepScope
	@Bean
	public StaxEventItemWriter xmlWriter(
			@Value("#{jobParameters[fileoutput]}")
			FileSystemResource output) {
		XStreamMarshaller marshller = new XStreamMarshaller();
		StaxEventItemWriter staxEventItemWriter = new StaxEventItemWriter<>();
		staxEventItemWriter.setResource(output);
		staxEventItemWriter.setMarshaller(marshller);
		staxEventItemWriter.setRootTagName("Products");
		return null;
		
	}
	
	@StepScope
	@Bean
	public FlatFileItemWriter writer(
			@Value("#{jobParameters[fileoutput]}")
			FileSystemResource outputFile) {
		FlatFileItemWriter writer = new FlatFileItemWriter<>();
		writer.setResource(outputFile);
		writer.setLineAggregator(new DelimitedLineAggregator<>() {
			{
				setDelimiter(",");
				setFieldExtractor(new BeanWrapperFieldExtractor<>() {
					{
						setNames(new String[] {"InproductID","productName","prodDesc","price","unit"});
					}
				});
			}
		});
		
		writer.setHeaderCallback(new FlatFileHeaderCallback() {
			
			@Override
			public void writeHeader(Writer writer) throws IOException {
				writer.write("InproductID,productName,prodDesc,price,unit");
			}
		});
		//writer.setAppendAllowed(true);
		return writer;
		
	}
	
	@Bean
	public Step step1() {
		return steps.get("step1")
				.chunk(3)
				.reader(reader(null))
				.processor(new ProductProcessor())
				//.reader(serviceAdaptor())
				.writer(writer(null))
				//.faultTolerant()
				//.retry(ResourceAccessException.class)
				//.retryLimit(3)
				//.faultTolerant()
				//.skip(ResourceAccessException.class)
				//.skipLimit(3)
				//.skipPolicy(new AlwaysSkipItemSkipPolicy())
				//.listener(new ProductListner())
				//.writer(xmlWriter(null))
				.build();
		
	}
	
	@Bean
	public Job job1() {
		return jobs.get("job1")
				.start(step1())
				.build();
		
	}
}
