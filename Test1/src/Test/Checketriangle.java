package Test;

import java.util.Scanner;

public class Checketriangle {
	private static String result;

	public void Checke(int a, int b, int c) {
		if(a>0&&b>0&&c>0)
		{
			if ((a + b <= c) || (a + c <= b) || (b + c <= a)) {
				result = "这不是三角形！";
			}else if (a == b && b == c) {
				result="这是正三角形！";
			}else if (a == b || b == c || a == c) {
				result="这是等腰三角形！";
			}else if(a != b && b != c && a != c){
				result="这是斜角三角形！";
			}

		}else
		{
			result = "三角形的边应>0!";
		}
		
	}

	public String getResult() {
		return result;
	}
	public static void main(String[] args) {
//		Scanner sc = new Scanner(System.in);
//		System.out.println("请输入三角形的三个边：");
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
