package com.ssm.entity;
/**
 * User实体类对象，映射的users表
 * 字段对应属性
 * 属性和字段名一致  
 * @author Administrator
 *
 */
public class User {
   private int uid,uage,uifdel;
   private String uname,upwd,udate,utest;
   
   public User(){}
   public User(int uid,int uage,int uifdel,String uname,String upwd,String udate,String utest){
	   this.uid=uid;
	   this.udate=udate;
	   this.uage=uage;
	   this.uifdel=uifdel;
	   this.uname=uname;
	   this.upwd=upwd;
	   this.utest=utest;
   }
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getUage() {
		return uage;
	}
	public void setUage(int uage) {
		this.uage = uage;
	}
	public int getUifdel() {
		return uifdel;
	}
	public void setUifdel(int uifdel) {
		this.uifdel = uifdel;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getUpwd() {
		return upwd;
	}
	public void setUpwd(String upwd) {
		this.upwd = upwd;
	}
	public String getUdate() {
		return udate;
	}
	public void setUdate(String udate) {
		this.udate = udate;
	}
	public String getUtest() {
		return utest;
	}
	public void setUtest(String utest) {
		this.utest = utest;
	}
   
}
