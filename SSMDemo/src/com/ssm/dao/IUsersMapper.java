package com.ssm.dao;

import java.util.List;
import java.util.Map;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ssm.entity.User;


/**
 * mybatis核心操作的接口
 * 1.IUsersMapper接口操作针对的是user表， * 
 * 2.注解注入到app..xml配置文件中，但是标识一个唯一的bean id 是usersModel
 * 3.该接口是mybatis操作的核心接口所以没有Class实现类
 * @author Administrator
 */
@Component("usersModel")
public interface IUsersMapper {
	//4个方法，1体现出两个表操作一个接口的性能  2为SpringMVC异步json操作，返回json 3 登陆
	//查询单个User对象，适合于登陆,欢迎登陆   院长  jerry
	public User SelectUserByLogin(String uname,String upwd);
//	public List<User> SelectUserByLogin1(String uname,String upwd);
	//返回的集合的size()是1
//	public User SelectUserByObject(User user);
	public User SelectUserByUid(int uid);
//	//return json
	public List<User> SelectUserAll();
//	//根据角色名称查询对应user集合记录
//	public List<User> SelectUserByRname(String rname);
//	//return Roloes
//	public Roles SelectRolesByrid(Integer rid);
//	public List<Roles> SelectRolesAll();
	public int InsertUser(User user);
	public int UpdateUser(User user);
	public int DeleteUser(int uid);
//	public int InsertUsers(List<User> listuser);
//	public List<Roles> SearchRolUser();
}
