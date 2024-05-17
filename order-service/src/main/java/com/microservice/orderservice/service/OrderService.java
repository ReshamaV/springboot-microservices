package com.microservice.orderservice.service;

import java.sql.Array;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.microservice.orderservice.dto.InventoryResponse;
import com.microservice.orderservice.dto.OrderLineItemsDto;
import com.microservice.orderservice.dto.OrderRequest;
import com.microservice.orderservice.model.Order;
import com.microservice.orderservice.model.OrderLineItems;
import com.microservice.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
	
	private final OrderRepository orderRepository;
	
	private final WebClient.Builder webClientBuilder;
	
	public void placeOrder(OrderRequest orderRequst) {
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		List<OrderLineItems> orderLineItems = orderRequst.getOrderLineItemsDtoList()
				.stream()
				.map(orderLineItemsDto ->mapToDto(orderLineItemsDto)).collect(Collectors.toList());
		order.setOrderLineItems(orderLineItems);
		
		List<String> skuCodes = order.getOrderLineItems().stream()
				.map(orderLineItem -> orderLineItem.getSkuCode())
				.collect(Collectors.toList());
		
		//Call Inventory service and place order if product is in stock
		InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
		.uri("http://localhost:8082/api/inventory", uriBuilder ->uriBuilder.queryParam("skuCode", skuCodes).build())
		.retrieve()
		.bodyToMono(InventoryResponse[].class)
		.block();
		
		
		boolean allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(inventoryResponse ->inventoryResponse.isInStock());
		
		if(allProductsInStock) {
			orderRepository.save(order);
		}
		else {
			throw new IllegalArgumentException("Product is not in stock, please try again later");
		}
		
		
				
	}

	private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
		OrderLineItems orderLineItems = new OrderLineItems();
		orderLineItems.setPrice(orderLineItemsDto.getPrice());
		orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
		orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
		return orderLineItems;
	}

}
