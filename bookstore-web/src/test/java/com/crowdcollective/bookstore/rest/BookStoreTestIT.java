package com.crowdcollective.bookstore.rest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import javax.validation.Validator;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.crowdcollective.dao.BookRepository;
import com.crowdcollective.ejb.BookService;
import com.crowdcollective.model.Book;

public class BookStoreTestIT extends JerseyTest {
	@Mock
	private BookRepository repository;
	@Mock
	private BookService service;
	@Mock
	private Validator validator;
	
	@Override
	protected ResourceConfig configure() {
		MockitoAnnotations.initMocks(this);
		return new ResourceConfig(BookRESTService.class)
				.register(new AbstractBinder() {
					@Override
					protected void configure() {
						bind(repository).to(BookRepository.class);
						bind(service).to(BookService.class);
						bind(validator).to(Validator.class);
					}
				});
						
				
	}	
	@Test 
	public void helloTest() {
		Response response = target("/books/testi").request().get();
		assertEquals("Status should be 200: ", Status.OK.getStatusCode(),
		response.getStatus());
		String content = response.readEntity(String.class);
		assertEquals("Greeting ok ", "Terve", content);
	}
	
	@Test
	public void findAllBooks() {
		when(repository.findAllOrderedByName()).thenReturn(Collections.emptyList());
		Response response = target("/books").request().get();
		assertEquals("Status should be 200: ", Status.OK.getStatusCode(),
		response.getStatus());
		List<Book> books = response.readEntity(new GenericType<List<Book>>(){});
		assertEquals("Expecting empty list ", 0, books.size());		
	}
	
	@Test
	public void findOneBook() {
		Book book = new Book();
		book.setId(1L);
		book.setTitle("Test title");
		when(repository.findById(1L)).thenReturn(book);
		Response response = target("/books/1").request().get();
		assertEquals("Status should be 200: ", Status.OK.getStatusCode(),
		response.getStatus());
		Book returned = response.readEntity(Book.class);
		assertEquals("Book title", book.getTitle(), returned.getTitle());		
	}
	
	@Test
	public void nonExistingBookReturns() {
		when(repository.findById(1L)).thenReturn(null);
		Response response = target("/books/1").request().get();
		assertEquals("Status should be 404 (Not found): ", Status.NOT_FOUND.getStatusCode(),
				response.getStatus());
	}
}
