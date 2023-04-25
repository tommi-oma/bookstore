package com.crowdcollective.ejb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.event.Event;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.crowdcollective.dao.BookRepository;
import com.crowdcollective.model.Book;

public class BookServiceTest {

	@InjectMocks
	private BookService service = new BookService();
	
	@Mock
	private BookRepository bookRepository;
	
	@Mock
	Event<Book> bookEvent;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		Book eka = new Book();
		eka.setTitle("Nimi");
		List<Book> mockatut = new ArrayList<>();
		mockatut.add(eka);
		mockatut.add(new Book());
		mockatut.add(new Book());
		mockatut.add(new Book());
		when(bookRepository.findAllOrderedByName()).thenReturn(mockatut);
	}
	
	@Test
	public void haeKirjat() {
		assertNotNull(service);
		
		List<Book> haetut = service.findBooks();
		assertEquals(4, haetut.size());
		assertEquals("Nimi", haetut.get(0).getTitle());
	}
	
	@Test
	public void luoKirja() {
		Book eka = new Book();
		eka.setTitle("Nimi");
		when(bookRepository.createBook(any())).thenReturn(eka);
		
		Book luotu = service.saveBook(eka);
		
	}
	
}
