package Test;

import static org.junit.Assert.*;

import org.junit.Test;

public class ChecketriangleTest {
	private static Checketriangle triangle = new Checketriangle();
	@Test
	public void testChecketriangle() {
		triangle.Checke(0, 5, 4);
		assertEquals("�����εı�Ӧ>0!",triangle.getResult());
		triangle.Checke(10, 5, 4);
		assertEquals("�ⲻ�������Σ�",triangle.getResult());
		triangle.Checke(10, 10, 4);
		assertEquals("���ǵ��������Σ�",triangle.getResult());
		triangle.Checke(5, 5, 5);
		assertEquals("�����������Σ�",triangle.getResult());
		triangle.Checke(5, 3, 4);
		assertEquals("����б�������Σ�",triangle.getResult());
	}


}
