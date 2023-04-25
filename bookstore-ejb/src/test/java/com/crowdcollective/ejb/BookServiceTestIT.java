package com.crowdcollective.ejb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.crowdcollective.dao.BookRepository;
import com.crowdcollective.model.Book;
import com.crowdcollective.util.Resources;

@RunWith(Arquillian.class)
public class BookServiceTestIT {
	@Inject
	private BookService service;
	
	@Deployment
	public static WebArchive initialize() {
	return ShrinkWrap.create(WebArchive.class, "jpatest.war")
			.addClasses(BookService.class, Resources.class)
			.addPackages(false, Book.class.getPackage(), BookRepository.class.getPackage())
			.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
			.addAsWebInfResource("test-ds.xml")
			.addAsResource(
					"META-INF/test-persistence.xml",
					"META-INF/persistence.xml")
			;
			
	}
	
	@Before
	public void setup() {
		Book kirja = new Book();
		kirja.setTitle("Testikirja 1");
		kirja.setPrice(3.14);
		service.saveBook(kirja);
	}
	
	@After
	public void teardown() {
		service.findBooks().stream().forEach(book -> service.removeBook(book.getId()));
	}
	
	@Test
	public void smokeTest() {
		assertNotNull(service);
	}
	
	@Test
	public void fetchBooks() {
		List<Book> books = service.findBooks();
		assertEquals("Kirjojen määrä", 1, books.size());
	}

}
