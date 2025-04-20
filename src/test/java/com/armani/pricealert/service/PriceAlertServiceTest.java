package com.armani.pricealert.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.armani.pricealert.entity.PriceAlert;
import com.armani.pricealert.repository.PriceAlertRepository;

import jakarta.mail.internet.MimeMessage;

class PriceAlertServiceTest {

	@Mock
	private PriceAlertRepository repository;

	@Mock
	private ThreadPoolTaskScheduler taskScheduler;

	@InjectMocks
	private PriceAlertService priceAlertService;

	@Mock
	private JavaMailSender mailSender;

	@Mock
	private MimeMessage mimeMessage;

	@Mock
	private MimeMessageHelper mimeMessageHelper;

	@InjectMocks
	private EmailService emailService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
	}

	@Test
	void testCreateAlert() {

		PriceAlert alert = new PriceAlert();
		alert.setProductUrl("http://example.com/product");
		alert.setTargetPrice(50.0);

		when(repository.save(any(PriceAlert.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// Act
		PriceAlert createdAlert = priceAlertService.createAlert(alert);

		// Assert
		assertNotNull(createdAlert);
		assertEquals("http://example.com/product", createdAlert.getProductUrl());
		assertEquals(50.0, createdAlert.getTargetPrice());
		verify(repository, times(1)).save(alert);

	}

	@Test
	void testGetAllAlerts() {
		PriceAlert alert1 = new PriceAlert();
		alert1.setProductUrl("http://example.com/product1");
		alert1.setTargetPrice(100.0);

		PriceAlert alert2 = new PriceAlert();
		alert2.setProductUrl("http://example.com/product2");
		alert2.setTargetPrice(200.0);

		when(repository.findAll()).thenReturn(Arrays.asList(alert1, alert2));

		List<PriceAlert> alerts = priceAlertService.getAllAlerts();

		assertNotNull(alerts);
		assertEquals(2, alerts.size());
	}

	@Test
	void testSendEmail() throws Exception {
		// Arrange
		String to = "test@example.com";
		String subject = "Test Subject";
		String body = "Test Body";

		// Act
		emailService.sendEmail(to, subject, body);

		// Assert
		verify(mailSender, times(1)).createMimeMessage();
		verify(mailSender, times(1)).send(mimeMessage);
	}
}
