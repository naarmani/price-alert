
package com.armani.pricealert.service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import com.armani.pricealert.entity.PriceAlert;
import com.armani.pricealert.repository.PriceAlertRepository;
import com.armani.pricealert.util.PriceTracker;

@Service
public class PriceAlertService {

	@Autowired
	private PriceAlertRepository repository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private ThreadPoolTaskScheduler taskScheduler;

	public PriceAlert createAlert(PriceAlert alert) {
		PriceAlert savedAlert = repository.save(alert);
		schedulePriceCheck(savedAlert);
		return savedAlert;
	}

	private void schedulePriceCheck(PriceAlert alert) {
		LocalDateTime scheduleTime = alert.getFrequency() != null
				? LocalDateTime.now().plusMinutes(Long.valueOf(alert.getFrequency()))
				: LocalDateTime.now();
		if (scheduleTime != null) {
			long delay = java.time.Duration.between(LocalDateTime.now(), scheduleTime).toMillis();
			taskScheduler.scheduleAtFixedRate(() -> checkPrice(alert),
					new java.util.Date(System.currentTimeMillis()).toInstant(), Duration.ofMillis(delay));

		}
	}

	private void checkPrice(PriceAlert alert) {
		double currentPrice = simulatePriceFetch(alert.getProductUrl());
		if (currentPrice <= alert.getTargetPrice()) {
			String subject = "Price Drop Alert!";
			String body = "The price for the product at " + alert.getProductUrl() + " has dropped to " + currentPrice
					+ ". Your target price was " + alert.getTargetPrice() + ".";
			// TODO: Send email notification to the user
			emailService.sendEmail("user@domain.com", subject, body);
		}
	}

	private double simulatePriceFetch(String productUrl) {
		try {
			return PriceTracker.fetchPrice(productUrl);
		} catch (IOException e) {
			throw new RuntimeException("Error reading JSON file", e);
		}
	}

	public List<PriceAlert> getAllAlerts() {
		return repository.findAll();
	}
}
