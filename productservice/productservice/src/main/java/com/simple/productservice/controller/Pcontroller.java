package com.simple.productservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Pcontroller {
	
	@GetMapping("/product")
	public Product getProduct() {
		return new Product(1,"prod1","retry policy",124.12,15.0);
		
	}
}
