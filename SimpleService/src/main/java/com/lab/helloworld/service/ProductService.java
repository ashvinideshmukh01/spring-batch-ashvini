package com.lab.helloworld.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.lab.SimpleService.model.Product;

@Service
public class ProductService {
	
	public ArrayList<Product> getProducts(){
		RestTemplate restTemplate = new RestTemplate();
		String url="http://localhost:8080/products";
		Product[] products = restTemplate.getForObject(url, Product[].class);
		ArrayList<Product> productList = new ArrayList<>();
		for(Product p : products) {
			productList.add(p);
		}
		return productList;
		
	}
}
