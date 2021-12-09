package com.simple.productservice.controller;

//import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement(name = "product")
public class Product {
	
	private Integer InproductID;
	private String productName;
	private String prodDesc;
	private Double price;
	private Double unit;
	
	
	
	public Product(Integer inproductID, String productName, String prodDesc, Double price, Double unit) {
		super();
		InproductID = inproductID;
		this.productName = productName;
		this.prodDesc = prodDesc;
		this.price = price;
		this.unit = unit;
	}


	public Integer getInproductID() {
		return InproductID;
	}


	public void setInproductID(Integer inproductID) {
		InproductID = inproductID;
	}


	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}


	public String getProdDesc() {
		return prodDesc;
	}


	public void setProdDesc(String prodDesc) {
		this.prodDesc = prodDesc;
	}


	public Double getPrice() {
		return price;
	}


	public void setPrice(Double price) {
		this.price = price;
	}


	public Double getUnit() {
		return unit;
	}


	public void setUnit(Double unit) {
		this.unit = unit;
	}


	@Override
	public String toString() {
		return "Product [InproductID=" + InproductID + ", productName=" + productName + ", prodDesc=" + prodDesc
				+ ", price=" + price + ", unit=" + unit + "]";
	}
	
	 
}
