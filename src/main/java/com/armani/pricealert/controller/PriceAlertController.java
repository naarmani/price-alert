package com.armani.pricealert.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.armani.pricealert.entity.PriceAlert;
import com.armani.pricealert.service.PriceAlertService;

@RestController
@RequestMapping("/api/alerts")
public class PriceAlertController {
    private final PriceAlertService service;

    public PriceAlertController(PriceAlertService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> createAlert(@RequestBody PriceAlert alert) {
        service.createAlert(alert);
        return ResponseEntity.ok("Alert has been set");
    }

    @GetMapping
    public List<PriceAlert> getAllAlerts() {
        return service.getAllAlerts();
    }
}
