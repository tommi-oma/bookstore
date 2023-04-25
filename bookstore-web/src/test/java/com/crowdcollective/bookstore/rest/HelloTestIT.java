package com.crowdcollective.bookstore.rest;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class HelloTestIT extends JerseyTest {
	@Override
	protected ResourceConfig configure() {
		return new ResourceConfig(HelloRESTService.class);
	}	
	@Test
	public void itemTest() {
		Response response = target("/hello").request().get();
		assertEquals("Status should be 200: ", Status.OK.getStatusCode(),
		response.getStatus());
		String content = response.readEntity(String.class);
		assertEquals("Greeting ok ", "Hello Test", content);
	}
}
