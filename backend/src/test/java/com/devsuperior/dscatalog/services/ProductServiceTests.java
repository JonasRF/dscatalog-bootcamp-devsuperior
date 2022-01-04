package com.devsuperior.dscatalog.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.DTO.ProductDTo;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private PageImpl<Product> page;
	private Category category;
	private Product product;
	private ProductDTo productDTo;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 2L;
		nonExistingId = 1000L;
		dependentId = 4L;
		productDTo = Factory.createProductDTO();
		category = Factory.creatCatgory();
		product = Factory.createproduct();
		page = new PageImpl<>(List.of(product));
	
		
		when(repository.find(ArgumentMatchers.any(), ArgumentMatchers.any(),(Pageable)ArgumentMatchers.any())).thenReturn(page);
		
		when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		
		when(repository.findById(existingId)).thenReturn(Optional.of(product));
		
		when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		when(repository.getOne(existingId)).thenReturn(product);
		
		when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		when(categoryRepository.getOne(existingId)).thenReturn(category);
		
		when(categoryRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		doNothing().when(repository).deleteById(existingId);
		
		doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
		
		doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
		
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
	    service.update(nonExistingId, productDTo);
		});	
	}
	
	@Test
	public void updateShouldReturnProductDToWhenIdExist() {
		
		ProductDTo result = service.update(existingId, productDTo);
		
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
	    service.findById(nonExistingId);
		});	
	}
	
	@Test
	public void findByIdShouldReturnProductDToWhenIdExists() {
		
		ProductDTo result = service.findById(existingId);
		
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void findAllPagedShouldReturnPage() {
		
		Pageable pageable = PageRequest.of(0, 12);
		String name = "Phone";
		
		Page<ProductDTo> result = service.findAllPaged(1L, name, pageable);
		
		Assertions.assertNotNull(result);
	}
	
	@Test 
	public void deleteShouldThrowDataBaseExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(DataBaseException.class, () -> {
			service.delete(dependentId);
		});
		verify(repository, times(1)).deleteById(dependentId);
	}
	
	@Test 
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
		verify(repository, times(1)).deleteById(nonExistingId);
	}
	
	@Test 
	public void deleteShouldDoNothingWhenIdExists() {
		
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
		verify(repository, times(1)).deleteById(existingId);
	}
}
