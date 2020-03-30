package com.rbello.ExampleSpringBatch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class BatchConfig {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	public DataSource dataSource;
	
	
	@Bean
	public Job processTransactionJob() {
		return jobBuilderFactory.get("processJob")
				.listener(listener())
				.flow(processTransactionFromJSON())
				.end()
				.build();
	}
	
	@Bean
    public Step processTransactionFromJSON(){
		System.out.println("BatchConfig.processTransactionFromJSON()");
        return stepBuilderFactory.get("processTransactionFromJSON")
        		.<Transaction, Transaction> chunk(2)
        	    .reader(transactionReader())
        	    .processor(transactionProcessor())
        	    .writer(transactionWriter(dataSource))
        	    .build();
                               
    }
	@Bean
	public TransactionItemProcessor transactionProcessor() {
	  return new TransactionItemProcessor();
	}
	
	@Bean
	public JobListener listener() {
	  return new JobListener();
	}
	
	@Bean
	public JsonItemReader<Transaction> transactionReader() {
		System.out.println("BatchConfig.transactionReader()");
	   return new JsonItemReaderBuilder<Transaction>()
	                 .jsonObjectReader(new JacksonJsonObjectReader<>(Transaction.class))
	                 .resource(new ClassPathResource("transaction.json"))
	                 .name("transactionJsonItemReader")
	                 .build();
	}
	
	@Bean
	public JdbcBatchItemWriter<Transaction> transactionWriter(DataSource dataSource) {
		System.out.println("BatchConfig.transactionWriter()");
	  return new JdbcBatchItemWriterBuilder<Transaction>()
	    .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
	    .sql("INSERT INTO transaction (id, amount,type,description) VALUES (:id, :amount, :type, :description)")
	    .dataSource(dataSource)
	    .build();
	}
}
