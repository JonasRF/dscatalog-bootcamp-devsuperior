package com.devsuperior.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	ProductRepository repository;
	
	long ExistingId = 1L;
	long nonExistingId = 1000L;
	long countTotalproducts = 25L;
	
	@BeforeEach
	void setUp() throws Exception {
		
		 ExistingId = 1L;
		 nonExistingId = 1000L;
		 countTotalproducts = 25L;
	}
	
	@Test
	public void findByIdShouldReturnNotEmptyWhenIdExist() {
		
		 Optional<Product> result = repository.findById(ExistingId);
		
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test
	public void findByIdShouldReturnEmptyWhenIdDoesNotExists() {
		
		 Optional<Product> result = repository.findById(nonExistingId);
		
		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void saveShouldPersistsWithAutoIncrementWhenIdNull() {
		
		Product product = Factory.createproduct();
		product.setId(null);
		
		product = repository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalproducts + 1, product.getId());
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		
		repository.deleteById(ExistingId);
		
		Optional<Product> result =  repository.findById(ExistingId);
		
		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIDDoesNotExist() {
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(nonExistingId);
		});
	}
}
