package com.crowdcollective.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;

public class BookTest extends TestBase {

	private Book kirja;
	
	@Before
	public void setup() {
		kirja = new Book();
		kirja.setTitle("Kirja");		
	}
	
	@Test
	public void testi() {
		assertNotNull(kirja);
	}
	
	@Test
	public void testaaOtsikonVaihto() {
		String nimi = "Kirja III";
		kirja.setTitle(nimi);
		assertEquals(nimi, kirja.getTitle());
	}
	
	@Test
	public void testaaNullOtsikko() {
		assertThrows(IllegalArgumentException.class, () -> kirja.setTitle(null));
	}
	
	
}
