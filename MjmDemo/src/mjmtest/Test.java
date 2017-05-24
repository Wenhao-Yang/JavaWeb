package mjmtest;
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
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("contentType", "utf-8");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
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
