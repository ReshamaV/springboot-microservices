package com.microservice.productservice.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.microservice.productservice.dto.ProductRequest;
import com.microservice.productservice.dto.ProductResponse;
import com.microservice.productservice.model.Product;
import com.microservice.productservice.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
	
	private final ProductRepository productRepository;
	
	public void createProduct(ProductRequest productRequest) {
		Product product = Product.builder().name(productRequest.getName())
				                           .description(productRequest.getDescription())
				                           .price(productRequest.getPrice())
				                           .build();
		
		productRepository.save(product);
		log.info("product {} is saved", product.getId());
	}

	public List<ProductResponse> getAllProducts() {
		List<Product> products = productRepository.findAll();
		//return products.stream().map(this::mapToProductResponse).toList();
		
		return products.stream().map(product ->mapToProductResponse(product)).collect(Collectors.toList());
		
	}

	private ProductResponse mapToProductResponse(Product product) {
		// TODO Auto-generated method stub
		return ProductResponse.builder()
				.id(product.getId())
				.name(product.getName())
				.description(product.getDescription())
				.price(product.getPrice())
				.build(); 
	}

	/*private ProductResponse mapToProductResponse(Product product) {
		
		return ProductResponse.builder()
				.Id(product.getId())
				.name(product.getName())
				.description(product.getDescription())
				.price(product.getPrice())
				.build();
	}*/
	

}
