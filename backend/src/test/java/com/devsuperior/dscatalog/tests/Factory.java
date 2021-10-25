package com.devsuperior.dscatalog.tests;

import java.time.Instant;

import com.devsuperior.dscatalog.DTO.ProductDTo;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

public class Factory {

	public static Product createproduct() {
			Product product = new Product(1L, "Phone", "Good Phone", 800.0, "http://img.com/img.png", Instant.parse("2020-10-20T03:00:00Z"));
			product.getCategories().add(creatCatgory());
			return product;
	}
	
	public static ProductDTo createProductDTO() {
		
		Product product = createproduct();
		return new ProductDTo(product, product.getCategories());
	}
	
	public static Category creatCatgory() {
		return new Category(1L, "Eletronics");
	}
}
