package com.crowdcollective.bookstore.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.crowdcollective.dao.PublisherRepository;
import com.crowdcollective.ejb.BookService;
import com.crowdcollective.model.Book;
import com.crowdcollective.model.Publisher;

@Path("/publishers")
public class PublisherRESTService {
    @Inject
    private Logger log;

    @Inject
    private Validator validator;

    @Inject
    private PublisherRepository repo;

    @Inject
    private BookService ejb;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Publisher> findPublishers() {
        return repo.findAllEagerly();
    }

    @GET
    @Path("/{id:[0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Publisher lookupMemberById(@PathParam("id") long id) {
        Publisher publisher = repo.findById(id);
        if (publisher == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return publisher;
    }

    @GET
    @Path("/{id:[0-9]+}/books")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> booksForPublisher(@PathParam("id") long id) {
        List<Book> books = repo.findBooksByPublisherId(id);
        return books;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uusiKustantaja(Publisher publisher) {
        Response.ResponseBuilder builder = null;
        try {
            validatePublisher(publisher);
            ejb.savePublisher(publisher);
//            repo.createPublisher(publisher);
            builder = Response.ok(publisher); // location header missing
        } catch (ConstraintViolationException e) {
            builder = rakennaViolationResponse(e.getConstraintViolations());
        } catch (ValidationException e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("url", "Bad URL");
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        } catch (Exception e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }
        log.info("Publisher created: " + publisher);
        return builder.build();
    }

    private void validatePublisher(Publisher publisher) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Publisher>> violations = validator.validate(publisher);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }
        // Custom validation, very simple
        String url = publisher.getWebsite();
        if (url == null || url.isEmpty()) return;
        try {
        	@SuppressWarnings("unused")
            URI uri = new URI(publisher.getWebsite());
        } catch (URISyntaxException e) {
            throw new ValidationException("Bad URI violation");
        }
    }

    private Response.ResponseBuilder rakennaViolationResponse(Set<ConstraintViolation<?>> violations) {
        log.fine("Validation completed. violations found: " + violations.size());

        Map<String, String> responseObj = new HashMap<>();

        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
    }

}