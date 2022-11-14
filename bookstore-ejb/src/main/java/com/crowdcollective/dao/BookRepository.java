package com.crowdcollective.dao;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.crowdcollective.model.Book;

@Stateless
public class BookRepository {
	@Inject
    private EntityManager em;
    private static final Logger log = Logger.getLogger(BookRepository.class.getCanonicalName());

    public Book findById(Long id) {
        return em.find(Book.class, id);
    }

    public List<Book> findAllOrderedByName() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> criteria = cb.createQuery(Book.class);
        Root<Book> book = criteria.from(Book.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
        criteria.select(book).orderBy(cb.asc(book.get("title")));
        return em.createQuery(criteria).getResultList();
    }
    public List<Book> findAllEagerly() {
        Query q = em.createQuery("SELECT DISTINCT b FROM Book b JOIN FETCH b.authors");
        List<Book> result = q.getResultList();
        return result;
    }

    public Book createBook(Book book) {
        em.persist(book);
        log.info("Persisted a book: "+book);
        return book;
    }

    public Book deleteBook(long id) {
        Book remove = em.find(Book.class, id);
        if (remove!=null) {
            em.remove(remove);
        }
        return remove;
    }
}
