package com.ssm.entity;
/**
 * User实体类对象，映射的users表
 * 字段对应属性
 * 属性和字段名一致  
 * @author Administrator
 *
 */
public class User {
   private int uid,age;
   private double money;
   private String uname,gender,upwd,udate,context,degree,online,work,sign;
   
   public User(){}
   public User(int uid,int age,double money,String uname,String upwd,String udate,String context,String degree,String online, String sign){
	   this.uid=uid;
	   this.age=age;
	   this.money=money;
	   this.uname=uname;
	   this.upwd=upwd;
	   this.udate=udate;
	   this.context=context;
	   this.degree = degree;
	   this.online = online;
	   this.work = work;
	   this.sign=sign;
   }
   
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
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

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}
	public String getOnline() {
		return online;
	}

	public void setOnline(String online) {
		this.online = online;
	}
	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}
	/**
	 * @return the work
	 */
	public String getWork() {
		return work;
	}
	/**
	 * @param work the work to set
	 */
	public void setWork(String work) {
		this.work = work;
	}
	/**
	 * @return the sign
	 */
	public String getSign() {
		return sign;
	}
	/**
	 * @param sign the sign to set
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}
   
}
