package com.lab.helloworld;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.lab.helloworld.listners.HelloWorldJobExecutionListner;
import com.lab.helloworld.listners.HelloWorldStepExecutionListner;
import com.lab.helloworld.model.Product;
import com.lab.helloworld.processor.InMemoryProcessor;
import com.lab.helloworld.reader.InMemoryReader;
import com.lab.helloworld.writer.ConsoleItemWriter;

@EnableBatchProcessing
@SpringBootApplication()
public class HelloworldApplication {
	
	@Autowired
	JobBuilderFactory jobs;
	@Autowired
	StepBuilderFactory steps;
	@Autowired
	HelloWorldJobExecutionListner helloWorldJobExecutionListner;
	@Autowired
	HelloWorldStepExecutionListner helloWorldStepExecutionListner;
	@Autowired
	InMemoryReader inMemoryReader;
	@Autowired
	InMemoryProcessor inMemoryProcessor;
	@Autowired
	ConsoleItemWriter consoleItemWriter;
	@Autowired
	JdbcCursorItemReader jdbcCursorItemReader;
	@Autowired
	DataSource dataSource;
	@Autowired
	private ProductService productService;
	
	public static void main(String[] args) {
		SpringApplication.run(HelloworldApplication.class, args);
	}

	@Bean
	public Step step1() {
	
		return steps.get("step1")
				.listener(helloWorldStepExecutionListner)
				.tasklet(hellowWorldtasklet())
				.build();
		
	}

	
	@Bean
	public Step step2() {
	
		return steps.get("step2")
				//.<Integer,Integer>chunk(3).reader(inMemoryReader).processor(inMemoryProcessor).writer(consoleItemWriter)
				.chunk(3)
				//.reader(flatFileItemReader(null))
				//.reader(xmlItemReader(null))
				//.reader(jdbcCursorItemReader())
				.reader(jsonItemReader(null))
				.writer(new ConsoleItemWriter())
				.build();
		  
	}

//	@Bean
//	public ConsoleItemWriter writer() {
//		return new ConsoleItemWriter();
//	}
//	
//	@Bean
//	public InMemoryProcessor processor() {
//		return new InMemoryProcessor();
//	}
//
//	@Bean
//	public InMemoryReader reader() {
//		return new InMemoryReader();
//	}

	private Tasklet hellowWorldtasklet() {
		
		return new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.out.println("hello World!!!");				
				return RepeatStatus.FINISHED;
			}
		};
	}
	
	@Bean
	public Job hellowWorldJob() {
		return jobs.get("helloworldjobs")
				.incrementer(new RunIdIncrementer())
				.listener(helloWorldJobExecutionListner)
				.start(step1())
				.next(step2())
				.build();
		
	}
	@StepScope
	@Bean
	public FlatFileItemReader<Product> flatFileItemReader (
			@Value("#{jobParameters['fileInput']}")
			FileSystemResource input) {
		
		FlatFileItemReader<Product> reader= new  FlatFileItemReader<Product>();
		reader.setResource(input);
		reader.setLineMapper(
                new DefaultLineMapper<Product>(){
                    {
                        setLineTokenizer( new DelimitedLineTokenizer() {
                            {
                                setNames( new String[]{"InproductID","productName","prodDesc","price","unit"});
                                setDelimiter(",");
                            }
                        });

                        setFieldSetMapper( new BeanWrapperFieldSetMapper<Product>(){
                            {
                                setTargetType(Product.class);
                            }
                        });
                    }
                }

        );
		reader.setLinesToSkip(1);
		return reader;
	}
	
	@StepScope
	@Bean
	public StaxEventItemReader xmlItemReader(@Value("#{jobParameters['fileInput']}")
	FileSystemResource input)
	{
		StaxEventItemReader reader = new StaxEventItemReader();
		reader.setResource(input);
		reader.setFragmentRootElementName("product");
		reader.setUnmarshaller(new Jaxb2Marshaller(){
            {
                setClassesToBeBound(Product.class);
            }
        });
		return reader;
		
	}
	
	@StepScope
	@Bean
	public JdbcCursorItemReader jdbcCursorItemReader() {
		JdbcCursorItemReader reader = new JdbcCursorItemReader();
		reader.setDataSource(this.dataSource);
		reader.setSql("select * from products");
		reader.setRowMapper(new BeanPropertyRowMapper() {
			{
			setMappedClass(Product.class);
			}
		});
		return reader;
		
	}
	
	@StepScope
	@Bean
	public JsonItemReader jsonItemReader(
			@Value("#{jobParameters['fileInput']}")
			FileSystemResource input) {
		
		JsonItemReader reader = new JsonItemReader(input,new JacksonJsonObjectReader(Product.class) {
		});
		return reader;
		
	}
	
	@Bean
	public ItemReaderAdapter serviceItemReader() {
		ItemReaderAdapter reader  = new ItemReaderAdapter();
		reader.setTargetObject(ProductService);
		return null;
		
	}
}
