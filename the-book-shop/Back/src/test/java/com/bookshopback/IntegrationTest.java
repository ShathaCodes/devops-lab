package com.bookshopback;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.bookshopback.model.Book;
import com.bookshopback.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

	private static PostgreSQLContainer  postgreSQLContainer ;

	@BeforeAll
	static void init() {
		postgreSQLContainer  = (PostgreSQLContainer) new PostgreSQLContainer("postgres:12.10").withDatabaseName("test")
				.withUsername("shatha").withPassword("pwd");
		postgreSQLContainer .start();
	}

	@DynamicPropertySource
	public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
		registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
		registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
	}

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper mapper;

	@Test
	public void getBooks_success() throws Exception {
		// given a list of Books
		Book book1 = new Book(1, "Pride and Prejeduce", "Jane Austen", 4, "Romance", 10.99);
		Book book2 = new Book(2, "Divergent", "Veronica Roth", 1, "Science fiction", 15.05);
		Book book3 = new Book(3, "Crazy Rich Asians", "Kevin Kwan", 7, "Humor", 30.00);
		List<Book> books = List.of(book1, book2, book3);
		bookRepository.saveAll(books);

		// when we perform GET request /books
		ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/books"));

		// then list all books
		response.andExpect(MockMvcResultMatchers.status().isOk());
		response.andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(books.size())));
	}

	@Test
	public void getBook_success() throws Exception {
		// given a book
		Book book = new Book(1, "Pride and Prejeduce", "Jane Austen", 4, "Romance", 10.99);
		bookRepository.save(book);

		// when we perform GET request /book/1
		ResultActions response = mockMvc
				.perform(MockMvcRequestBuilders.get("/books/1").contentType(MediaType.APPLICATION_JSON));

		// then show book with id=1
		response.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.title", is("Pride and Prejeduce"))).andExpect(jsonPath("$.quantity", is(4)));
	}

	@Test
	public void create_success() throws Exception {
		// given a book
		Book book = new Book(1, "Pride and Prejeduce", "Jane Austen", 4, "Romance", 10.99);

		// when we perform a POST request /books/create
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/books/create")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(book));
		ResultActions response = mockMvc.perform(mockRequest);

		// then create the book object
		response.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.title", is("Pride and Prejeduce")));
	}

	@Test
	public void update_success() throws Exception {
		// given an update book
		Book updated_book = new Book(1, "Emma", "Jane Austen", 4, "Romance", 10.99);

		// when we perform a PUT request /books with the body containing the updated
		// book
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/books")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(updated_book));
		ResultActions response = mockMvc.perform(mockRequest);

		// then update the book object
		response.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.title", is("Emma")));
	}

	// test what to do in case the book is not found
	@Test
	public void update_bookNotFound() throws Exception {
		// given a non existing book
		Book updated_book = new Book(8, "A Dance with Dragons", "George R. R. Martin", 5, "Fantasy", 12.55);

		// when we perform a PUT request /books with the body containing the non
		// existing book
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/books")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(updated_book));
		ResultActions response = mockMvc.perform(mockRequest);

		// then return a bad request response with message
		response.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof Exception))
				.andExpect(result -> assertEquals("Book with ID 8 does not exist.",
						result.getResolvedException().getMessage()));
	}

	@Test
	public void delete_success() throws Exception {

		// given a book
		Book book = new Book(2, "Divergent", "Veronica Roth", 1, "Science fiction", 15.05);

		// when we perform a DELETE request /books/id
		ResultActions response = mockMvc.perform(
				MockMvcRequestBuilders.delete("/books/" + book.getId()).contentType(MediaType.APPLICATION_JSON));
		// then return no content response
		// response.andExpect(status().isNoContent());
	}

	@Test
	public void delete_notFound() throws Exception {

		// given a non existing book id
		int book_id = 2222;

		// when we perform a DELETE request /books/id
		ResultActions response = mockMvc
				.perform(MockMvcRequestBuilders.delete("/books/" + book_id).contentType(MediaType.APPLICATION_JSON));

		// then return bad request with error message
		response.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof Exception))
				.andExpect(result -> assertEquals("Book with ID " + book_id + " does not exist.",
						result.getResolvedException().getMessage()));
	}

}
