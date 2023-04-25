package com.crowdcollective.bookstore.rest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestAssuredBookStoreRAIT {
	@Test
	public void itemTest() {

	}

	@Test
	public void GetItemDetails() {
		RestAssured.baseURI = "http://localhost:8080/bookstore-web/api/books";
		RestAssured.get("/1").then().statusCode(200).assertThat().body("price", equalTo("37.0")).assertThat()
				.body("title", startsWith("Head"));
	}
}
