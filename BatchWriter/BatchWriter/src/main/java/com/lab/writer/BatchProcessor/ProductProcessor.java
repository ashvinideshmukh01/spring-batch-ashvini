package com.lab.writer.BatchProcessor;

import org.springframework.batch.item.ItemProcessor;

import com.lab.writer.BatchWriter.model.Product;

public class ProductProcessor implements ItemProcessor<Product,Product>{

	
	@Override
	public Product process(Product item) throws Exception {
		
		if(item.getInproductID()==2) {
			return null;
		}else {
			item.setProdDesc(item.getProdDesc().toUpperCase());
		}
		return item;
	}

}
