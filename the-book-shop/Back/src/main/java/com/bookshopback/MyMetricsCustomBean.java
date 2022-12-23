package com.bookshopback;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.MultiGauge;
import io.micrometer.core.instrument.Tags;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.bookshopback.controller.BookController;
import com.bookshopback.model.Book;
import com.bookshopback.repository.BookRepository;

@Component
public class MyMetricsCustomBean {

	Logger logger = LoggerFactory.getLogger(MyMetricsCustomBean.class);

	// REST controller for metrics
	@Lazy
	@Autowired
	protected BookController bookController;

	// Database for metrics
	@Lazy
	@Autowired
	protected BookRepository bookRepository;

	// multigauge for low inventory (dimensions on id and name)
	MultiGauge lowInventoryCounts = null;

	public void updateLowInventoryGauges() {
		boolean overWrite = true;

		// create MultiGauge.Row for each product with low inventory count
		lowInventoryCounts.register(bookRepository.findBookWithLowInventoryCount().stream().map(
				(Book b) -> MultiGauge.Row.of(Tags.of("id", "" + b.getId(), "name", b.getTitle()), b.getQuantity()))
				.collect(Collectors.toList()), overWrite);

	}

	public MyMetricsCustomBean(MeterRegistry registry) {

		// dynamically sized dimensions from database
		// rely on updateLowInventoryGauges() to populate because data is not available
		// here
		lowInventoryCounts = MultiGauge.builder("low.inventory.count").tag("id", "name").register(registry);
	}

}