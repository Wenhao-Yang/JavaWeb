package Test;

import java.util.Scanner;

public class Checketriangle {
	private static String result;

	public void Checke(int a, int b, int c) {
		if(a>0&&b>0&&c>0)
		{
			if ((a + b <= c) || (a + c <= b) || (b + c <= a)) {
				result = "�ⲻ�������Σ�";
			}else if (a == b && b == c) {
				result="�����������Σ�";
			}else if (a == b || b == c || a == c) {
				result="���ǵ��������Σ�";
			}else if(a != b && b != c && a != c){
				result="����б�������Σ�";
			}

		}else
		{
			result = "�����εı�Ӧ>0!";
		}
		
	}

	public String getResult() {
		return result;
	}
	public static void main(String[] args) {
//		Scanner sc = new Scanner(System.in);
//		System.out.println("�����������ε������ߣ�");
//		int a = sc.nextInt();
//		int b = sc.nextInt();
//		int c = sc.nextInt();
//		
//		Checketriangle triangle = new Checketriangle();
//		triangle.Checke(a,b,c);
//		
//		System.out.println(triangle.getResult());
	}
	
	
	

}
