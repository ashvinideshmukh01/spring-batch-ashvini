package com.lab.SimpleService.controller;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lab.SimpleService.model.Product;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	@GetMapping("/get")
	public ArrayList<Product> getProducts() {
		ArrayList<Product> products = new ArrayList<>();
		products.add(new Product(2,"toys","from service",124.20,10.0));
		return products;
		
	}
}
