package com.lab.writer.BatchWriter;

import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lab.writer.BatchWriter.model.Product;
import com.lab.writer.BatchWriter.service.ProdService;

@Component
public class ProdServiceAdaptor {
//Logger logger = LoggerFactory.getLogger(Product.class);
	org.slf4j.Logger logger = LoggerFactory.getLogger(Product.class);
	@Autowired
	ProdService service;
	
	public Product nextProduct() throws InterruptedException{
		Product p=null;
		try {
		p= service.getProduct();
		logger.info("connected to service");
		}catch(Exception e){
			logger.info("Exception");
			throw e;
		}
		Thread.sleep(1000);
		return p;
	}
}
