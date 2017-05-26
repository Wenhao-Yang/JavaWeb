package com.ssm.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mjmtest.Test;

import org.apache.commons.httpclient.HttpState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ssm.dao.IUsersMapper;
import com.ssm.entity.User;
import com.sun.corba.se.impl.ior.GenericIdentifiable;



@Controller
@RequestMapping("/usert")
public class UserController {
	//璋冪敤UserMapp鎺ュ彛鎿嶄綔鐨勫姛鑳�
	IUsersMapper userMapper=null;
	public IUsersMapper getUserMapper() {
		return userMapper;
	}
	@Autowired
	public void setUserMapper(@Qualifier("usersModel")IUsersMapper userMapper) {
		this.userMapper = userMapper;
	}
		
	@RequestMapping("/login")
	@ResponseBody  
	public Map<String, Object> UsersLogin(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("username")String usn,
			@RequestParam("pwd")String pwd) throws UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		//System.out.println(usn+"  //  "+pwd);		

		Map<String, Object> map=new HashMap<String, Object>();
		User user=userMapper.SelectUserByLogin(usn, pwd);		
		if(user!=null){
			System.out.println(user.getUid()+"  +");
			
			map.put("id", user.getUid());
			map.put("name",user.getUname());
			 
		}
		else{
			map.put("id", 0);
		}
		return map;
	}
	@RequestMapping("/sinfo1")
	@ResponseBody
	public List<Map<String, Object>> TestMethod(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("sid")String sid,
			@RequestParam("stype")String stype) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		
		List<Map<String, Object>> res=new ArrayList<Map<String,Object>>();
		Map<String, Object> map=new HashMap<String, Object>();
		
		map.put("id", "100");
		map.put("name","100");
		map.put("pwd", "100");
		
		res.add(map);
		System.out.println("put");
		
		return res;
	}

	@RequestMapping("/sinfo")
	@ResponseBody 
	public String Sinfo(HttpServletRequest request, HttpServletResponse response,@RequestParam("sid")String sid,
			@RequestParam("stype")String stype) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");//html页面请求的编码格式，国际标准中文编码格式
		response.setCharacterEncoding("utf-8");//往往异步机制返回的页面编码格式，中国标准的中文编码格式
	
		Test my=new Test();
		
		
		String sr=my.sendPost("http://jieone.com/demo/stock/data/stock.php?callback=jQuery17206472256606980518_1495438225734&Action=minute&stockID="+sid+"&stockType="+stype, "");
		String[] astr1 = sr.split(",");
		int ii=0;
		String strr ="";
		String[] r_str= new String [1000];
		int count =0;
        for (int i = 0; i < astr1.length-1; i++) {
        	if(ii==6)
        	{
        		String[] astr2 = astr1[i].split(":");
        		String str1 = astr2[0].substring(0,astr2[0].length()-2);
        		//System.out.println();
        		String str2 = astr2[0].substring(astr2[0].length()-2,astr2[0].length())+":"+astr2[1];

        		//String str2 = astr2[0].substring(astr2[0].length()-2,astr2[0].length())+":"+astr2[1];
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
        String strc="";
        for (int i = 0; i < count; i++) {
        	strc = strc + r_str[i];
        	strc = strc + "\n";
        }
        //System.out.println(strc);
        //System.out.println("strc");
        return strc;
		//return r_str[0];
		//return "aaaa";
	}
	@RequestMapping("/loginAndroid")
	@ResponseBody  
	public String AndroidLogin(HttpServletRequest request, HttpServletResponse response,@RequestParam("username")String usn,
			@RequestParam("pwd")String pwd) throws UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		
		Map<String, Object> map=new HashMap<String, Object>();
		User user=userMapper.SelectUserByLogin(usn, pwd);
		
		if(user!=null){
			System.out.println(user.getUid()+"  +");
			
			map.put("id", user.getUid());
			map.put("name",user.getUname());
		}
		else{
			map.put("id", 0);
		}
				
		return map.toString();
	}
	
	//璇ユ柟娉曡姹傜殑url: http://127..../SSMdemo/usert/searchUser.action
	@RequestMapping("/searchUser")
	@ResponseBody
	public List<Map<String, Object>> FindUsersAll(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		List<Map<String, Object>> res=new ArrayList<Map<String,Object>>();
		List<User> listUser=this.userMapper.SelectUserAll();
		Map<String, Object> map=null;
		
//<div class='btn btn-primary' data-toggle='modal' data-target='#addModal'>Edit</div>
		
		for (User user : listUser) {
			map=new HashMap<String, Object>();
			map.put("id", user.getUid());
			map.put("name",user.getUname());
			map.put("pwd", user.getUpwd());


			res.add(map);
		}
		//json  [{},{},{}]
		return res;
	}
	@RequestMapping("/download")
	@ResponseBody
	public ResponseEntity<byte []> DownloadImag(@RequestParam("logo") String logo) throws IOException{
		System.out.println("imag"+logo);
		String path="C:/Users/WILLIAM/Desktop" + logo;
		File file = new File(path);
		
		HttpHeaders http=new HttpHeaders();
		InputStream is;
		try {
			is = new FileInputStream(file);
			byte[] by=new byte[is.available()];
			
			is.read(by);
			
			http.add("Context-Dispostion", "attchementheaderValue);filename="+file.getName());
			HttpStatus status=HttpStatus.OK;
			http.add("Context-Dispostion", "attchementheaderValue);filename="+file.getName());
			HttpStatus statu=HttpStatus.OK;
			ResponseEntity<byte []> entity = new ResponseEntity<byte[]>(by,http,statu);
			return entity;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	@RequestMapping("/uploadFile")
	@ResponseBody
	public String uploadFile(@RequestParam("file")MultipartFile file, 
			@ModelAttribute("users")User users, 
			HttpServletRequest request) throws IllegalStateException, IOException {
		String filePath = request.getSession().getServletContext().getRealPath("/") + "/css/"
				+ file.getOriginalFilename();
		// 杞瓨鏂囦欢
		file.transferTo(new File(filePath));
		// 涓婁紶鐨勬枃浠跺悕
		String filename = file.getOriginalFilename();
		System.out.println("fff " + filename + "/" +users.getUname());

		String contextpath = request.getScheme() +"://" + 
		              request.getServerName()  + ":" +
				      request.getServerPort() +request.getContextPath();
//		System.out.println("path:"+request.getRequestURL());
//		System.out.println("path1:"+request.getRequestURI());
//		String pa=request.getServletPath();
//		System.out.println("path2:"+pa);
//		System.out.println("path2: "+pa.substring(pa.lastIndexOf("/")+1,pa.indexOf(".action")));
//		System.out.println("path3:"+request.getQueryString());

		return "success";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
