package com.bookshopfront.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.bookshopfront.BackendClient;
import com.bookshopfront.model.Book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class BookController {

	private BackendClient backendClient;

	private Logger logger = LoggerFactory.getLogger(BookController.class);

	@Autowired
	public BookController(BackendClient backendClient) {
		this.backendClient = backendClient;
	}

	private static ArrayList<Book> books = new ArrayList<>();

	private ArrayList<Book> manageBooks(Book book) {
		init();
		if (book != null)
			books.add(book);
		return books;
	}

	private ArrayList<Book> init() {
		books = (ArrayList<Book>) backendClient.getBooks();
		return books;
	}

	@GetMapping("/books")
	public String books(Model model) {
		logger.info("GET /books");
		model.addAttribute("books", manageBooks(null));
		model.addAttribute("book", new Book());
		return "books_page";
	}

	@PostMapping("createBook")
	public String createBook(@ModelAttribute Book book, BindingResult result) {
		logger.info("POST /createBook");
		if (result.hasErrors()) {
			return "redirect:/books";
		}
		backendClient.createBook(book);
		return "redirect:/books";

	}
	@GetMapping("/deleteBook")
	public String handleDeleteBook(@RequestParam(name = "bookId") String bookId) {
		logger.info("GET /deleteBook?bookId="+bookId);
		backendClient.deleteBook(bookId);
		return "redirect:/books";
	}

	@GetMapping("/edit/{id}")
	public String editBook(@PathVariable("id") int id, Model model) {
		logger.info("GET /edit/"+id);
		Book editingBook = backendClient.getBook(id);
		editingBook.setId(id);
		model.addAttribute("book", editingBook);
		return "edit_book_page";
	}

	@PostMapping("/update/{id}")
	public String updateBook(@PathVariable("id") int id, Book book, BindingResult result, Model model) {
		logger.info("POST /update/"+id);
		if (result.hasErrors()) {
			book.setId(id);
			return "edit_book_page";
		}
		Book editedBook = (Book) model.getAttribute("book");
		backendClient.editBook(editedBook);
		return "redirect:/books";
	}

}
