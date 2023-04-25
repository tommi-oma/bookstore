package com.crowdcollective.labs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TrivialCalculatorTest {

	@Parameters
	public static Collection<Object[]> datat() {
		return Arrays.asList(new Object [][] { {1, 3, 4, 0}, {3, 1, 4, 3}, {6, -2, 4, -3},
			{0, 2, 2, 0}, {2, 0, 2, 0} });
	}

	private TrivialCalculator laskin;
	private int yksi;
	private int kaksi;
	private int summa;
	private int jako;
	
	public TrivialCalculatorTest(int yksi, int kaksi, int summa, int jako) {
		super();
		this.yksi = yksi;
		this.kaksi = kaksi;
		this.summa = summa;
		this.jako = jako;
	}
	
	@Before
	public void setup() {
		laskin = new TrivialCalculator();
	}
	
	@Test
	public void laskinSummaaOikein() {
		assertEquals(summa, laskin.sum(yksi, kaksi));
	}
	
	@Test
	public void laskinJakaaOikein() {
		assumeFalse(kaksi == 0);
		assertEquals(jako, laskin.divide(yksi, kaksi));
	}
	
	@Test
	public void laskinJakaaNollallaPoikkeuksen() {
		assumeTrue(kaksi == 0);
		assertThrows(ArithmeticException.class, () -> laskin.divide(yksi, kaksi));
	}
	
}
