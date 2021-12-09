package com.lab.writer.BatchWriter.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.lab.writer.BatchWriter.model.Product;

@Service
public class ProdService {
	
	public Product getProduct() {
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://localhost:8080/product";
		Product p = restTemplate.getForObject(url, Product.class);
		return p;
	}
}
