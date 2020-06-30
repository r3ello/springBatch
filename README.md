# Spring Batch
Example using Spring Batch and Spring Boot to process transactions.
## First step:
read Transaction from JSON using
```
@Bean
	public JsonItemReader<Transaction> transactionReader() {
		return new JsonItemReaderBuilder<Transaction>()
	               .jsonObjectReader(new JacksonJsonObjectReader<>(Transaction.class))
	               .resource(new ClassPathResource("transaction.json"))
	                .name("transactionJsonItemReader")
	                .build();
	}
```
## Next step: 
change Change the sign of the amount, all DEBIT transactions will have a negative sign in the Database 
```
public class TransactionItemProcessor implements ItemProcessor<Transaction, Transaction>{
	@Override
	public Transaction process(Transaction t) throws Exception {
		return Transaction.builder()
		.description(t.getDescription())
		.id(t.getId())
		.type(t.getType())
		.amount(t.getType().equals("CREDIT") ? t.getAmount() : -1*t.getAmount())
		.build();
		
	}

}
```
## Next step 
Write transaction item in Data Base using:
```
@Bean
	public JdbcBatchItemWriter<Transaction> transactionWriter(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<Transaction>()
	    .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
	    .sql("INSERT INTO transaction (id, amount,type,description) VALUES (:id, :amount, :type, :description)")
	    .dataSource(dataSource)
	    .build();
	}
```
# Dependencies
```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-batch</artifactId>
</dependency>
```
# User memory Data Base
```
spring.datasource.url=jdbc:h2:mem:device
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=test
spring.datasource.password=test
```
disable automatic spring batch run
```
spring.batch.job.enabled=false
```
## JSON Data Example
```
[
  {
    "id": 1,
    "amount": 100.2,
    "type": "CREDIT",
    "description": "description1"
  },
  {
    "id": 2,
    "amount": 542.05,
    "type": "CREDIT",
    "description": "description2"
  },
  {
    "id": 3,
    "amount": 985.35,
    "type": "CREDIT",
    "description": "description3"
  },
  {
    "id": 4,
    "amount":741.52,
    "type": "DEBIT",
    "description": "description4"
  },{
    "id": 5,
    "amount":216.57,
    "type": "CREDIT",
    "description": "description5"
  },{
    "id": 6,
    "amount": 100.05,
    "type": "DEBIT",
    "description": "description6"
  },{
    "id":7, 
    "amount": 58.25,
    "type": "DEBIT",
    "description": "description7"
  },{
    "id": 8,
    "amount": 999.9,
    "type": "CREDIT",
    "description": "description8"
  }
]
```
