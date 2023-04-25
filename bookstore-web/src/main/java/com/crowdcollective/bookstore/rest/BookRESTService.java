package com.crowdcollective.bookstore.rest;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.crowdcollective.dao.BookRepository;
import com.crowdcollective.ejb.BookService;
import com.crowdcollective.model.Book;

@Path("/books")
public class BookRESTService {
	private static final Logger log = Logger.getGlobal();
    @Inject
    private Validator validator;

    @Inject
    private BookRepository repo;

    @Inject
    private BookService ejb;

    @GET
    @Path("/testi")
    public String testi() {
    	return "Terve";
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> findBooks() {
        return repo.findAllEagerly();
    }
    @GET
    @Path("/{id:[0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Book lookupBookById(@PathParam("id") long id) {
        Book book = repo.findById(id);
        if (book == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return book;
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBook(Book book) {
        Response.ResponseBuilder builder = null;
        Set<ConstraintViolation<Book>> violations = null;
        try {
            violations = validator.validate(book);

            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
            }

            ejb.saveBook(book);
            URI location = new URI("/books/");
            location.resolve(Long.toString(book.getId()));
            builder = Response.created(location).entity(book);
            log.info("New book location: " + location.toString());
        } catch (ConstraintViolationException e) {

            Map<String, String> responseObj = new HashMap<>();

            for (ConstraintViolation<?> violation : violations) {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }

            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        } catch (Exception e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }
        log.info("New book created: " + book);
        return builder.build();

    }

    @DELETE
    @Path("/{id:[0-9]+}")
    public Response deleteBook(@PathParam("id") long id) {
        Book removed = ejb.removeBook(id);
        if (removed == null) {
            return Response.status(404).build();
        }
        return Response.noContent().build();
    }

}
