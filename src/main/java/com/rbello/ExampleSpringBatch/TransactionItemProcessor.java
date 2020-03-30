package com.rbello.ExampleSpringBatch;

import org.springframework.batch.item.ItemProcessor;

public class TransactionItemProcessor implements ItemProcessor<Transaction, Transaction>{

	/**
	 *  Change the sign of the amount, 
	 *  all DEBIT transactions will have a 
	 *  negative sign in the Database 
	 */
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
