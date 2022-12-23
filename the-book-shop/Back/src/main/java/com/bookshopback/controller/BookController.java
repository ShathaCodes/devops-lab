package com.bookshopback.controller;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.http.HttpStatus;

import com.bookshopback.MyMetricsCustomBean;
import com.bookshopback.model.Book;
import com.bookshopback.repository.BookRepository;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@RestController
@RequestMapping("books")
public class BookController {

	@Autowired
	BookRepository repository;

	@Autowired
	MyMetricsCustomBean myMetricsCustomBean;

	private Logger logger = LoggerFactory.getLogger(BookController.class);

	@ExceptionHandler({ IllegalArgumentException.class, IllegalStateException.class, NoHandlerFoundException.class })
	public void handleException() {
		logger.error("Path not found");
	}

	@GetMapping("")
	public Iterable<Book> getBooks() {
		logger.info("GET /books");
		return repository.findAll();
	}

	@GetMapping("/{id}")
	public Book getBook(@PathVariable int id) {
		logger.info("GET /books/" + id);
		Optional<Book> book = repository.findById(id);
		if (book.isPresent())
			return book.get();
		return null;
	}

	@PostMapping("/create")
	public Book create(@RequestBody Book book) {
		logger.info("POST /books/create ");
		myMetricsCustomBean.updateLowInventoryGauges();
		return repository.save(book);

	}

	@PostMapping("/total")
	public double calculateTotal(@RequestBody Iterable<Book> books) {
		double total = 0;
		for (Book book : books) {
			total += book.getPrice();
		}
		return total;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public class InvalidRequestException extends RuntimeException {
		public InvalidRequestException(String s) {
			super(s);
			logger.error(s);
		}
	}

	@PutMapping()
	public Book update(@RequestBody Book book) throws InvalidRequestException {
		logger.info("PUT /books");
		if (book == null) {
			throw new InvalidRequestException("Book or ID must not be null!");
		}
		Optional<Book> optionalBook = repository.findById(book.getId());
		if (optionalBook.isEmpty()) {
			throw new InvalidRequestException("Book with ID " + book.getId() + " does not exist.");
		}
		Book existingBook = optionalBook.get();

		existingBook.setTitle(book.getTitle());
		existingBook.setAuthor(book.getAuthor());
		existingBook.setQuantity(book.getQuantity());
		existingBook.setGenre(book.getGenre());
		existingBook.setPrice(book.getPrice());
		myMetricsCustomBean.updateLowInventoryGauges();
		return repository.save(existingBook);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable int id) throws InvalidRequestException {
		logger.info("DELETE /books/" + id);
		Optional<Book> optionalBook = repository.findById(id);
		if (optionalBook.isEmpty()) {
			throw new InvalidRequestException("Book with ID " + id + " does not exist.");
		}
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

}
