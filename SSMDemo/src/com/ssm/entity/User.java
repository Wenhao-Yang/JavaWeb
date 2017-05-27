package com.ssm.entity;
/**
 * User实体类对象，映射的users表
 * 字段对应属性
 * 属性和字段名一致  
 * @author Administrator
 *
 */
public class User {
   private int uid,uage,money;
   private String uname,gender,upwd,udate,context;
   
   public User(){}
   public User(int uid,int uage,int money,String uname,String upwd,String udate,String context){
	   this.uid=uid;
	   this.uage=uage;
	   this.money=money;
	   this.uname=uname;
	   this.upwd=upwd;
	   this.udate=udate;
	   this.context=context;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}
   
}
