package com.microservice.inventoryservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.inventoryservice.dto.InventoryResponse;
import com.microservice.inventoryservice.service.InventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class InventoryController {
	
	private final InventoryService inventoryService;
	
	@GetMapping("/api/inventory")
	@ResponseStatus(HttpStatus.OK)
	public List<InventoryResponse> isInstock(@RequestParam List<String> skuCode) {
		return inventoryService.isInStock(skuCode);
		
	}

}
