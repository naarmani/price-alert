package com.armani.pricealert.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.armani.pricealert.entity.PriceAlert;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PriceTracker {
	public static double fetchPrice(String productUrl) throws IOException {
		// Simulate fetching price from a static JSON file
		ObjectMapper mapper = new ObjectMapper();

		List<PriceAlert> products = mapper.readValue(new File("src/main/resources/static/prices.json"),
				new TypeReference<List<PriceAlert>>() {
				});

		// Find the product by URL and return its price
		return products.stream().filter(p -> p.getProductUrl().equals(productUrl)).map(PriceAlert::getTargetPrice)
				.findFirst().orElseThrow(() -> new RuntimeException("Product not found: " + productUrl));

	}
}
