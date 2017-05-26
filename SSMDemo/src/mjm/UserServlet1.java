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


@WebServlet("/usert1")
public class UserServlet1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet1() {
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
		String sr=my.sendPost("http://jieone.com/demo/stock/data/stock.php?callback=jQuery17206472256606980518_1495438225734&Action=minute&stockID=002024&stockType=sz", "");
		String[] astr1 = sr.split(",");
		int ii=0;
		String strr ="";
		String[] r_str= new String [1000];
		int count =0;
        for (int i = 0; i < astr1.length-1; i++) {
        	if(ii==6)
        	{
        		String[] astr2 = astr1[i].split(":");
        		String str1 = astr2[0].substring(0,5);
        		String str2 = astr2[0].substring(5,7)+":"+astr2[1];
        		//System.out.println(str1);
        		strr = strr+','+str1;
        		r_str[count] = strr;
        		//System.out.println(r_str[count]);
        		count++;
        		strr = str2;
        		//System.out.println(str2);
        		ii=0;
        	}else{
        		if(i==0)
        		{
        			strr = astr1[i];
        		}else{
        			strr = strr+','+astr1[i];
        		}
        	}
            //System.out.println(sourceStrArray[i]);
        	ii++;
        }
        for (int i = 0; i < count; i++) {
        	pw.print(r_str[i]);
        	pw.print("\n");
        }
	}

}
