package com.crowdcollective.ejb;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.crowdcollective.dao.BookRepository;
import com.crowdcollective.dao.PublisherRepository;
import com.crowdcollective.model.Book;
import com.crowdcollective.model.Publisher;

@Stateless
public class BookService {
    private static final Logger LOGGER = Logger.getLogger(BookService.class.getCanonicalName());
    //@Inject
    //Logger LOGGER;

    @Inject
    private BookRepository bookRepository;
    @Inject
    private PublisherRepository publisherRepository;
    @Inject
    Event<Book> bookEvent;

    public List<Book> findBooks() {
        return bookRepository.findAllOrderedByName();
    }
    public Book saveBook(Book book) {
        Book created = bookRepository.createBook(book);
        bookEvent.fire(book);
        LOGGER.info("Triggered event..");
        return created;
    }
    public Book removeBook(long id) {
        Book removed = bookRepository.deleteBook(id);
        if (removed!=null) {
            bookEvent.fire(removed);
        }
        return removed;
    }
    public void savePublisher(Publisher publisher) {
        publisherRepository.createPublisher(publisher);
    }
}
