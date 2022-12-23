package com.bookshopfront;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.client.RestTemplate;

import com.bookshopfront.model.Book;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class BackendClient {
	@Autowired
    RestTemplate restTemplate;
	private String baseUrl;

	public BackendClient(@Value("${backend.prefix.url}") String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public List<Book> getBooks() {
		String url = String.format("%s/books", baseUrl);
		ResponseEntity<Object[]> responseEntity = restTemplate.getForEntity(url, Object[].class);
		ArrayList<Book> books = new ArrayList<>();
		Object[] objects = responseEntity.getBody();
		ObjectMapper mapper = new ObjectMapper();
		for (Object o : objects) {
			Book book = mapper.convertValue(o, Book.class);
			books.add(book);
		}
		return books;
	}
	public Book getBook(int bookId) {
		String url = String.format("%s/books/%d", baseUrl,bookId);
		return restTemplate.getForObject(url, Book.class);
	}

	public Book createBook(@ModelAttribute Book book) {
		String url = String.format("%s/books/create", baseUrl);
		return restTemplate.postForObject(url, book, Book.class);
	}
	
	public void deleteBook(String bookId) {
		String url = String.format("%s/books/%s", baseUrl,bookId);
		restTemplate.delete(url);
	}
	
	public Book editBook(Book editedBook) {
		String url = String.format("%s/books", baseUrl);
		restTemplate.put(url, editedBook, Book.class);
		return editedBook;
	}

}
