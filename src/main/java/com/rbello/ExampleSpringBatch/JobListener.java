package com.rbello.ExampleSpringBatch;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class JobListener extends JobExecutionListenerSupport{

	 @Autowired
	 private  JdbcTemplate jdbcTemplate;
	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			System.out.println("JOB COMPLETED SUCCESSFULLY");
			System.out.println("Print transaction from database");
			jdbcTemplate.query("SELECT id, type, amount , description FROM transaction",
			        (rs, row) -> Transaction.builder()
			        .id(rs.getInt("id"))
			        .amount(rs.getDouble("amount"))
			        .type(rs.getString("type"))
			        .description(rs.getString("description"))
			        .build()
			      ).forEach(t -> System.out.println( "Row of <" + t + "> in the database."));
		}else 
		{
			System.out.println("JOB ended whit status: "+jobExecution.getStatus());
		}
	}
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		// TODO Auto-generated method stub
		super.beforeJob(jobExecution);
	}
}
