package com.crowdcollective.dao;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.crowdcollective.model.Book;
import com.crowdcollective.model.Publisher;

@ApplicationScoped
public class PublisherRepository {
    @Inject
    private EntityManager em;
    @Inject
    Logger log;

    public Publisher findById(Long id) {
        return em.find(Publisher.class, id);
    }
    public List<Publisher> findAllEagerly() {
        TypedQuery<Publisher> q = em.createQuery("SELECT p FROM Publisher p", Publisher.class);
        return q.getResultList();
    }

    public List<Book> findBooksByPublisherId(Long id) {
        Publisher p = em.find(Publisher.class, id);
        if (p == null) {
            log.info("No Publisher with id " + id);
        }
        TypedQuery<Book> q = em.createQuery("SELECT b FROM Book b WHERE b.publisher = :p", Book.class);
        q.setParameter("p", p);
        List<Book> res = q.getResultList();
        return res;
    }

    public void createPublisher(Publisher publisher) {
        em.persist(publisher);
    }
}
