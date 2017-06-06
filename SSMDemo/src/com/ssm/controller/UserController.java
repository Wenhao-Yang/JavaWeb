package com.ssm.controller;
import mjmtest.Test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.HashedMap;
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

import com.ssm.dao.IStocksMapper;
import com.ssm.dao.IUsersMapper;
import com.ssm.entity.User;
import com.sun.corba.se.impl.ior.GenericIdentifiable;

import jdk.internal.org.xml.sax.InputSource;
//Springmvc核心调用的类对象，而且springIOC核心@注入的类对象，核心的调用mybaits操作数据库 类对象
//ajax异步返回json的类对象，IOC mybatis@注解注入的对象
//@Controller:SpringMVC的注解注入标识，唯一的id 是usert

@Controller
@RequestMapping("/usert")
public class UserController {
	//调用Mapper接口操作的功能
	IUsersMapper userMapper=null;
	
	public IUsersMapper getUserMapper() {
		return userMapper;
	}
	@Autowired
	public void setUserMapper(@Qualifier("usersModel")IUsersMapper userMapper) {
		this.userMapper = userMapper;
	}
	//@ResponseBody  -->  jackson jar
	
	//用户登录
	@RequestMapping("/login")
	@ResponseBody  
	public Map<String, Object> UsersLogin(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("username")String usn,
			@RequestParam("pwd")String pwd) throws UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		System.out.println(usn+"  //  "+pwd);		

		Map<String, Object> map=new HashMap<String, Object>();
		User user=userMapper.SelectUserByLogin(usn, pwd);		
		if(user!=null){
			System.out.println("服务器接受成功！");
			
			map.put("id", user.getUid());
			map.put("name",user.getUname()); 
		}
		else{
			map.put("id", 0);
		}
		return map;
	}
	
	//查找所有用户
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
		
		System.out.println("search all user");
		
		for (User user : listUser) {
			map=new HashMap<String, Object>();
			map.put("id", user.getUid());
			map.put("name",user.getUname());
			map.put("pwd", user.getUpwd());
			//超链a 是json数据被异步显示到页面中，添加title属性值是该条记录的users主键，每一条记录的title值是不同的
			map.put("edit", "<a href='javascript:void(0);' class='btn btn-info  btn-sm' title='"
			        +user.getUid()+"' id='editUserBt'>Edit</a>");
			map.put("del", "<a href='javascript:void(0);' class='btn btn-danger btn-sm'  title='"+user.getUid()+"' id='deleteUserBtn'>Delete</a>");
			
			res.add(map);
		}
		
		return res;
	}
	
	//用户注册
	@RequestMapping("/regist")
	@ResponseBody
	public String RegistUsers(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute("users")User user) 
					throws UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		
		System.out.println("注册用户：" + user.getUname()+" / "+user.getUpwd()+
				" / "+user.getGender()+" / "+user.getAge());
		
		return userMapper.InsertUser(user)>0?"success":"error";
	}
	
	//用户信息修改
	@RequestMapping("/userUpdate")
	@ResponseBody
	public String UpdateUsers(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute("users")User users) 
					throws UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		
		System.out.println("修改用户:"+users.getUname()+" / "+users.getUpwd()
							+ " / " + users.getAge()+ " / " +users.getContext()+  " //  "+users.getUid());
		
		return userMapper.UpdateUser(users)>0?"success":"error";
	}
	
	//删除用户
	@RequestMapping("/DeleteUser")
	@ResponseBody
	public String DeleteUsers(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("uid")int uid) 
					throws UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		
		System.out.println("删除用户--"+uid);
		
		return userMapper.DeleteUser(uid)>0?"success":"error";
	}
	
	//下载文件
	@RequestMapping("/download")
	@ResponseBody
	public ResponseEntity<byte []> DownloadImg1(@RequestParam("logo")String logo,
			HttpServletRequest request,	HttpServletResponse response)throws Exception{
		System.out.println("图片:"+logo);
		//创建路径，创建File对象操作
//		String path="E:/Android/images/"+logo;
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		//设置返回的二进制文件流，不是网页
		response.setContentType("application/x-msdownload");
		//ulogo是赋值下载图片的图片名称 包含扩展名
//		String logo=request.getParameter("ulogo");	        
//	    System.out.println("image:"+logo);	       
	    response.setHeader("Content-Disposition", "attachment;filename="+java.net.URLEncoder.encode(logo,"utf-8"));
	        //获取下载文件的真实路径
	    String filename="C:/Users/WILLIAM/Desktop/H.W/img/"+logo;//+filename	          
	          //创建文件输入流
	    FileInputStream fis=new FileInputStream(filename);
	        //创建缓冲输入流
	    BufferedInputStream bis=new BufferedInputStream(fis);
	        //获取响应的输出流
	    OutputStream  os=response.getOutputStream();
	        //创建缓冲输出流
	    BufferedOutputStream bos=new BufferedOutputStream(os);	          
	        //把输入流的数据写入到输出流
	    byte[] b=new byte[1024];
	    int len=0;
	    while((len=bis.read(b))!=-1){
	          bos.write(b, 0, len);
	    }
	    bos.close();
	    bis.close();
		return null;
	}
	
	//查找用户
	@RequestMapping("/searchObject")
	@ResponseBody
	public List<Map<String, Object>> FindObjectToAndroid(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");		
		List<Map<String, Object>> res=new ArrayList<Map<String,Object>>();	
		Map<String, Object> map=null;
		List<User> listUser=this.userMapper.SelectUserAll();
		
		for (User user : listUser) {
			map=new HashMap<String, Object>();
			map.put("id", user.getUid());
			map.put("name",user.getUname());
			map.put("pwd", user.getUpwd());
			map.put("date", user.getUdate());
			map.put("logo", "u"+user.getUid()+".gif");
			map.put("context", user.getContext());
			
			res.add(map);
		}
		System.out.println(res);
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
	}
	
	//用户信息查询
	@RequestMapping("/userEntity")
	@ResponseBody  
	public Map<String, Object> UserEntity(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("uid")int uid)
					throws UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		System.out.println(uid+"  //  ");		

		Map<String, Object> map=new HashMap<String, Object>();
		User user=userMapper.SelectUserByUid(uid);	
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if(user!=null){
			System.out.println(user.getUid()+"  +");
			
			map.put("id", user.getUid());
			map.put("name",user.getUname());
			map.put("pwd",user.getUpwd());
			map.put("age",user.getAge());
			map.put("gender",user.getGender());
			map.put("context", user.getContext());
		}
		return map;
	}
	
	//上传
	@RequestMapping("/uploadFile")
	@ResponseBody
	public String uploadFile(@RequestParam("file")MultipartFile file, 
			@ModelAttribute("users")User users, 
			HttpServletRequest request) throws IllegalStateException, IOException {
		String filePath = request.getSession().getServletContext().getRealPath("/") + "/css/"
				+ file.getOriginalFilename();
		// 转存文件
		file.transferTo(new File(filePath));
		// 上传的文件名
		String filename = file.getOriginalFilename();
		System.out.println("fff " + filename + "/" +users.getUname());

		String contextpath = request.getScheme() +"://" + 
							 request.getServerName()  + ":" +
				             request.getServerPort() +
				             request.getContextPath();

		return "success";
	}
	
	//买卖股票
	@RequestMapping("/buyStocks")
	@ResponseBody
	public String buyStocks(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("uid")int uid) {
		return "success";
	}
	
	@RequestMapping("/kinfo")
	@ResponseBody  
	public List<List<String>> Kinfo(HttpServletRequest request, HttpServletResponse response,@RequestParam("code")int code,
			@RequestParam("start")int start,@RequestParam("end")int end) throws UnsupportedEncodingException{
		int iStockCode = code,iCurStart=start, iCurEnd=end;
		String strUrl = null;
		String str;
		if (iStockCode>=600000)
		{
			str = String.format("http://quotes.money.163.com/service/chddata.html?code=0%06d&start=%d&end=%d&fields=TCLOSE;HIGH;LOW;TOPEN;LCLOSE;VOTURNOVER;VATURNOVER", iStockCode, iCurStart, iCurEnd);

		}
		else
		{
			str = String.format("http://quotes.money.163.com/service/chddata.html?code=1%06d&start=%d&end=%d&fields=TCLOSE;HIGH;LOW;TOPEN;LCLOSE;VOTURNOVER;VATURNOVER", iStockCode, iCurStart, iCurEnd);
		}
		//System.out.println(str);
		Test my=new Test();
		String sr=my.sendPost(str, "");
		String[] k_str = sr.split(",");
		
		String[] k_str1 = new String[10000];;
		int j=0;
		//System.out.println(sr);
		for(int i =0,o=0;i<k_str.length;i++)
		{
			j++;
			if(j==10)
			{
				if(i==k_str.length-1)
				{	
					k_str1[o] =k_str[i];
					
					o++;
					j=1;
				}else{
					String s1 = k_str[i].substring(k_str[i].length()-10,k_str[i].length());
					String s2 = k_str[i].substring(0,k_str[i].length()-10);
					//System.out.println(i);
					//System.out.println(s1);
					k_str1[o] = s2;
					o++;
					//System.out.println(s2);
					k_str1[o] = s1;
					o++;
					j=1;
				}
				
			}else{
				if(j==2)
				{
					k_str1[o] =k_str[i];
				}else{
					k_str1[o] = k_str[i];
				}
				
				o++;
			}
			//System.out.println(k_str[i]);
		}
		System.out.println(k_str1.length);
		int l=k_str.length/9;
		List<List<String>> arr_str = new ArrayList<List<String>>();
		
		for(int i =1,x=0,y=0;i<k_str.length/9;i++)
		{
			List<String> arr_str1 = new ArrayList<String>();
			for(int o=0;o<10;o++)
			{
				arr_str1.add(k_str1[i*10+o]);
			}
			arr_str.add(arr_str1);
			//System.out.println(k_str1[i]);
			//arr_str[o] = arr_str[o]+
			//System.out.println(i);
		}
		//System.out.println(k_str.length/9*10);
//		for(int i =0;i<1;i++)
//		{
//			for(int o =0;o<10;o++)
//			{
//				System.out.println(arr_str[i][o]);
//			}
//		}
		 //System.out.println(Arrays.deepToString(arr_str));
		return arr_str;
	}

	

	
	
	
	
	
	
}
