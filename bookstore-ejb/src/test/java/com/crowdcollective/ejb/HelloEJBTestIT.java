package com.crowdcollective.ejb;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class HelloEJBTestIT {
	@Inject
	private HelloEJB ejb;
	
	@Deployment
	public static WebArchive initialize() {
	return ShrinkWrap.create(WebArchive.class, "mytest.war")
			.addClasses(HelloEJB.class)
			.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}
	@Test
	public void joo() {
		assertEquals("Hello", ejb.hello());
	}
}
