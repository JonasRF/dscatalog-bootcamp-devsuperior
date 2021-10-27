package com.devsuperior.dscatalog.resources;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.DTO.ProductDTo;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional
public class ProductResourceIT {

	@Autowired
	private ProductService service;
	
	@Autowired
	private ProductRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long countTotalProducts;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
	
	}
	
	@Test
	public void deleteShouldDeleteResourceWhenIdExists() {
		
		service.delete(existingId);
		
		Assertions.assertEquals(countTotalProducts - 1 , repository.count());
	}
	
	@Test
	public void deleteShouldDeleteResourceWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
		service.delete(nonExistingId);
		});
	}
	
	@Test
	public void FindAllShouldReturnPageWhenToSpendParametersOfFilters() {
		
		PageRequest pageRequest = PageRequest.of(0, 12, Sort.by("name"));
		String name = "PC Gamer";
		
		Page<ProductDTo> result = service.findAllPaged(3L, name, pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals("PC Gamer", result.getContent().get(0).getName());
		Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(1).getName());
		Assertions.assertEquals("PC Gamer Boo", result.getContent().get(2).getName());
		Assertions.assertEquals("PC Gamer Card", result.getContent().get(3).getName());
	}
	
	@Test
	public void FindAllShouldReturnPageWhenToSpendParametersOfPAgination() {
		
		PageRequest pageRequest = PageRequest.of(0, 12);
		String name = "PC Gamer";
		
		Page<ProductDTo> result = service.findAllPaged(3L, name, pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(12, result.getSize());
		Assertions.assertEquals(countTotalProducts, repository.count());
	}
	
	@Test
	public void FindAllShouldReturnIsEmptyWhenParametersOfPaginationPast() {
		
		PageRequest pageRequest = PageRequest.of(50, 12, Sort.by("name"));
		String name = "PC Gamer";
		
		Page<ProductDTo> result = service.findAllPaged(3L, name, pageRequest);
		
		Assertions.assertTrue(result.isEmpty());
	}
}
