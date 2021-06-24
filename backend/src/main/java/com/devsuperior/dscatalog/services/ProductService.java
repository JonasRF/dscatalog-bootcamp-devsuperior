package com.devsuperior.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.DTO.ProductDTo;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

	@Transactional(readOnly = true)
	public Page<ProductDTo> findAllPaged(PageRequest pageResquest) {
		Page<Product> list = repository.findAll(pageResquest);
		return list.map(x -> new ProductDTo(x));

	}

	@Transactional(readOnly = true)
	public ProductDTo findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new ProductDTo(entity, entity.getCategories());

	}

	@Transactional(readOnly = true)
	public ProductDTo insert(ProductDTo dto) {
		Product entity = new Product();
	//	entity.setName(dto.getName());
		entity = repository.save(entity);
		return new ProductDTo(entity);
	}

	@Transactional
	public ProductDTo update(Long id, ProductDTo dto) {
		try {
			Product entity = repository.getOne(id);
		//	entity.setName(dto.getName());
			entity = repository.save(entity);
			return new ProductDTo(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found" + id);
		}
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new DataBaseException("id not found " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity violation");
		}
	}
}
