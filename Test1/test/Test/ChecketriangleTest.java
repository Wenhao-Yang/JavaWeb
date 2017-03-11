package Test;

import static org.junit.Assert.*;

import org.junit.Test;

public class ChecketriangleTest {
	private static Checketriangle triangle = new Checketriangle();
	@Test
	public void testChecketriangle() {
		triangle.Checke(0, 5, 4);
		assertEquals("三角形的边应>0!",triangle.getResult());
		triangle.Checke(10, 5, 4);
		assertEquals("这不是三角形！",triangle.getResult());
		triangle.Checke(10, 10, 4);
		assertEquals("这是等腰三角形！",triangle.getResult());
		triangle.Checke(5, 5, 5);
		assertEquals("这是正三角形！",triangle.getResult());
		triangle.Checke(5, 3, 4);
		assertEquals("这是斜角三角形！",triangle.getResult());
	}


}
