package com.armani.pricealert.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.armani.pricealert.entity.PriceAlert;

public interface PriceAlertRepository extends JpaRepository<PriceAlert, Long> {
}
