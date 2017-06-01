package mjmtest;
<<<<<<< HEAD
=======
import mjmtest.Test;
>>>>>>> ywh
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Test {
	String driver="com.mysql.jdbc.Driver";
	public Test()
	{
		
	}
	public String find(String uname,String psw)
	{
		String Result = "";
		try {
			Class.forName(driver);
			String url = "jdbc:mysql://127.0.0.1:3306/ob_db";
			try {
				Connection conn = DriverManager.getConnection(url, "root", "");
				String sql="select * from user where username=? and password=? and stage=0";
				PreparedStatement prep=conn.prepareStatement(sql);
				prep.setString(1, uname);
				prep.setString(2, psw);
				System.out.println("uname:"+uname+"psw:"+psw);
				ResultSet rs = prep.executeQuery();
				if(rs.next()) {
					Result = "ok";
					//System.out.println(rs.getInt("uid")+" / "+rs.getString("username")+" / "+rs.getInt("usage"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Result;
	}
	public String sendPost(String url,String param)
	{
		PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
        	URL realUrl = new URL(url);
<<<<<<< HEAD
            // ´ò¿ªºÍURLÖ®¼äµÄÁ¬½Ó
            URLConnection conn = realUrl.openConnection();
            // ÉèÖÃÍ¨ÓÃµÄÇëÇóÊôÐÔ
=======
            // ï¿½ò¿ªºï¿½URLÖ®ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
            URLConnection conn = realUrl.openConnection();
            // ï¿½ï¿½ï¿½ï¿½Í¨ï¿½Ãµï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
>>>>>>> ywh
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("contentType", "utf-8");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
<<<<<<< HEAD
            // ·¢ËÍPOSTÇëÇó±ØÐëÉèÖÃÈçÏÂÁ½ÐÐ
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // »ñÈ¡URLConnection¶ÔÏó¶ÔÓ¦µÄÊä³öÁ÷
            out = new PrintWriter(conn.getOutputStream());
            // ·¢ËÍÇëÇó²ÎÊý
            out.print(param);
            // flushÊä³öÁ÷µÄ»º³å
            out.flush();
            // ¶¨ÒåBufferedReaderÊäÈëÁ÷À´¶ÁÈ¡URLµÄÏìÓ¦
=======
            // ï¿½ï¿½ï¿½ï¿½POSTï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // ï¿½ï¿½È¡URLConnectionï¿½ï¿½ï¿½ï¿½ï¿½Ó¦ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
            out = new PrintWriter(conn.getOutputStream());
            // ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
            out.print(param);
            // flushï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Ä»ï¿½ï¿½ï¿½
            out.flush();
            // ï¿½ï¿½ï¿½ï¿½BufferedReaderï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½È¡URLï¿½ï¿½ï¿½ï¿½Ó¦
>>>>>>> ywh
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
<<<<<<< HEAD
            System.out.println("·¢ËÍ POST ÇëÇó³öÏÖÒì³££¡"+e);
            e.printStackTrace();
        }
        //Ê¹ÓÃfinally¿éÀ´¹Ø±ÕÊä³öÁ÷¡¢ÊäÈëÁ÷
=======
            System.out.println("ï¿½ï¿½ï¿½ï¿½ POST ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ì³£ï¿½ï¿½"+e);
            e.printStackTrace();
        }
        //Ê¹ï¿½ï¿½finallyï¿½ï¿½ï¿½ï¿½ï¿½Ø±ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
>>>>>>> ywh
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }    
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        //new Test();
		//http://web.juhe.cn:8080/finance/stock/hs?gid=sh601009&key=94ddb5b6e08bdc544e2f683a0d4d2040
		 //String sr=sendPost("http://jieone.com/demo/stock/data/stock.php?callback=jQuery17206472256606980518_1495438225734&Action=minute&stockID=002024&stockType=sz", "");
	     
		 ///System.out.println(sr);
		 //System.out.println("11111");
	}
}
