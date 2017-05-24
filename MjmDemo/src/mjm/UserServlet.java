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
 * 1.Servlet��һ���࣬���ĵĸ���HttpServlet������ĺ��ĵ�ְ�ܣ�ת��jdk httpЭ��
 * 2.@WebServlet("/usert") htmlҳ�������url��ַ������ "/"��������ʲô��
 * 3.doPost doGet�������ĵĲ�����������������
 *   <form method="post|get">  ��ʽ�ύ|�����ύ      ����ʽ�ύΪ����������doPost��������Ϊ��
 *   ��������������ִ�е���һ�������������post��ʽ�ύ���õ�doPost����֮���õ�doGet����
 *   Ϊ�˱�֤�ܹ���ȷ��ִ��һ��������
 * 4.HttpServletRequest request, HttpServletResponse response
 *   HttpServlet�����httpЭ��ģ�Ҳ���ǽ����http���������Ӧ���������
 *   Ҳ���Ǹ����������Ǵ�httpЭ����������Ӧ�����ġ�
 *   
 *   <form action="usert" method="post">-->@WebServlet("/usert")��Ӧ��servlet�࣬�е�
 *   doPost����
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
		//���£������û��Ƿ���get�ύ������ִ��doPost����
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * ��½��֤������
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");//htmlҳ������ı����ʽ�����ʱ�׼���ı����ʽ
		response.setCharacterEncoding("utf-8");//�����첽���Ʒ��ص�ҳ������ʽ���й���׼�����ı����ʽ
	
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
