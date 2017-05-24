package mjm;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSON;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import mjmtest.Test;

/**
 * 1.Servlet是一个类，核心的父类HttpServlet，父类的核心的职能：转换jdk http协议
 * 2.@WebServlet("/usert") html页面请求的url地址参数。 "/"：代表了什么？
 * 3.doPost doGet两个核心的操作方法，交互的是
 *   <form method="post|get">  隐式提交|明码提交      以隐式提交为主，所以以doPost方法调用为主
 *   该两个方法真正执行的是一个方法，如果是post方式提交调用的doPost，反之调用的doGet方法
 *   为了保证能够正确的执行一个方法，
 * 4.HttpServletRequest request, HttpServletResponse response
 *   HttpServlet紧黏贴http协议的，也就是紧黏贴http的请求和响应两个对象的
 *   也就是该两个参数是从http协议的请求和响应由来的。
 *   
 *   <form action="usert" method="post">-->@WebServlet("/usert")对应的servlet类，中的
 *   doPost方法
 *   
 *   @WebServlet("http://localhost:8081/MjmDemo/usert")
 *   @WebServlet("/usert")
 */
@WebServlet("/usert")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//导致，无论用户是否是get提交，都会执行doPost方法
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * 登陆验证方法：
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");//html页面请求的编码格式，国际标准中文编码格式
		response.setCharacterEncoding("utf-8");//往往异步机制返回的页面编码格式，中国标准的中文编码格式
	
		Test my=new Test();
		PrintWriter pw=response.getWriter();
		//System.out.println("aaaaa");
		String sr=my.sendPost("http://hq.sinajs.cn/list=sz002024", "");
		//System.out.println(sr);
		
		String[]  sr1=sr.split("\"");
		//System.out.println(sr1[1]);
		String[]  sr2=sr1[1].split(",");
//		for(int i =0;i<31;i++)
//		{
//			//System.out.println(sr2[i]); 
//		}
		//System.out.println(sr2[30]); 
		//System.out.println(sr2[31]);
		//System.out.println(sr2[3]);
		String str1 = "{\"name\":"+sr2[30]+" "+sr2[31]+",\"value\":["+sr2[30]+" "+sr2[31]+","+sr2[3]+"]}";
//		//System.out.println(str1);
		Map<String, String[]> map = new HashMap<String, String[]>();
		String[] str2 = {sr2[0]};
		String[] str3 = {sr2[30]+" "+sr2[31],sr2[3]};
        map.put("name", str2);
        map.put("value", sr2);
        JSONArray array = new JSONArray();
        array.add(map);
//        //System.out.print("array:"+array);
//        
		pw.print(array);
	}

}
