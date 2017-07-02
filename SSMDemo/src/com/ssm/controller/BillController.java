package com.ssm.controller;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssm.dao.IBillsMapper;
import com.ssm.dao.IUsersMapper;
import com.ssm.entity.Bill;
import com.ssm.entity.Stock;
import com.ssm.entity.User;
import com.sun.javafx.image.ByteToIntPixelConverter;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import mjmtest.Test;

@Controller
@RequestMapping("/billt")
public class BillController {
	IBillsMapper billsMapper=null;
	IUsersMapper usersMapper=null;
	public IBillsMapper getBillsMapper() {
		return billsMapper;
	}
	@Autowired
	public void setStockMapper(@Qualifier("billsModel")IBillsMapper billsMapper) {
		this.billsMapper = billsMapper;
	}
	public IUsersMapper getUsersMapper() {
		return usersMapper;
	}
	@Autowired
	public void setUserMapper(@Qualifier("usersModel")IUsersMapper usersMapper) {
		this.usersMapper = usersMapper;
	}
	
	//查看所有股票
	@RequestMapping("/searchAllStock")
	@ResponseBody
	public List<Map<String, Object>> SearchALLStocks(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		
		List<Map<String, Object>> res=new ArrayList<Map<String,Object>>();
		List<Bill> listBill=this.billsMapper.SelectAllBill();
		Map<String, Object> map=null;
		
		//System.out.println("请求查看全部交易！");
		for (Bill bill : listBill) {
			map=new HashMap<String, Object>();
			map.put("sid", bill.getSid());
			map.put("uid",bill.getUid());
			
			res.add(map);
		}
		return res;
	}
	
	//买入股票股票
	@RequestMapping("/buyStock")
	@ResponseBody
	public String BuyStocks(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute("bill")Bill bill) throws UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		//System.out.println(bill.getCost());
		//System.out.println("买入股票！");
		Bill resultBill=this.billsMapper.SelectBillByBill(bill);

		User thisUser=this.usersMapper.SelectUserByUid(bill.getUid());
		
		//用户余额足够
		if ((thisUser.getMoney()-bill.getBalance()*bill.getCost())>0){
			//System.out.println(thisUser.getMoney()-bill.getBalance()*bill.getCost());
			thisUser.setMoney(thisUser.getMoney()-bill.getBalance()*bill.getCost());
			this.usersMapper.UpdateUser(thisUser);
			if (resultBill==null){
				return this.billsMapper.BuyStocks(bill)>0?"success":"error";
			}
			else{
				bill.setBalance(resultBill.getBalance()+bill.getBalance());
				//更新买入均价
				bill.setCost((resultBill.getCost()*resultBill.getBalance()+bill.getBalance()*bill.getCost())/
						(resultBill.getBalance()+bill.getBalance()));
				return this.billsMapper.UpdateStocks(bill)>0?"success":"error";
			}
		}
		return "error";	
	}
	
	//卖出股票股票
	@RequestMapping("/sellStock")
	@ResponseBody
	public String sellStocks(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute("bill")Bill bill) throws UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		//System.out.println(bill.getCost()+"--"+bill.getSid()+"--"+bill.getUid());
		
		//查看是否有能够卖的该股票
		Bill resultBill=this.billsMapper.SelectBillByBill(bill);
		//System.out.println("卖出股票！");
		if(resultBill!=null && resultBill.getAbalance()>=bill.getBalance()){
			//若是，卖出该股票,添加卖出记录
			this.billsMapper.SellStocks(bill);
			resultBill.setBalance(resultBill.getBalance()-bill.getBalance());
			resultBill.setAbalance(resultBill.getAbalance()-bill.getBalance());
			//添加用户金额
			User thisUser=this.usersMapper.SelectUserByUid(bill.getUid());
			thisUser.setMoney(thisUser.getMoney()+bill.getBalance()*bill.getCost());
			this.usersMapper.UpdateUser(thisUser);
			//更新用户持有股票表
			return this.billsMapper.UpdateStocks(resultBill)>0?"success":"error";
		}
		return "error";
	}

	//查看当前记录
	@RequestMapping("/userBill")
	@ResponseBody
	public List<Map<String, Object>> UsreBalance(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("uid")int uid) throws UnsupportedEncodingException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		
		List<Bill> thisUserBill=this.billsMapper.SearchBillByUid(uid);
		
		List<Map<String, Object>> res=new ArrayList<Map<String,Object>>();
		Map<String, Object> map=null;
		
		//获取股票的实时价格
		Test my=new Test();
		String sr=null;
		String[] astr1 =null; 

		String[] resultStr=null;
		//System.out.println("查看股票实时价格！");
		
		double currentPri;
		DecimalFormat df = new DecimalFormat( "#,##0.00");
		for (Bill bill : thisUserBill) {

			sr=my.sendPost("http://hq.sinajs.cn/list=sh"+bill.getSid(), "");
			astr1=sr.split("\"");
			sr=astr1[1];
			resultStr=sr.split(",");
			currentPri=Double.valueOf(resultStr[3]);
					
			map=new HashMap<String, Object>();
			map.put("uid",bill.getUid());
			map.put("sid", bill.getSid());
			map.put("sname", bill.getStock().getSname());
			map.put("balance", bill.getBalance());
			map.put("abalance", bill.getAbalance());
			map.put("unbalance", bill.getBalance()-bill.getAbalance());
			map.put("cost", bill.getCost());
			map.put("currentPri", currentPri);
			map.put("date", bill.getDate());
			map.put("context", bill.getContext());
			map.put("profits", df.format(bill.getBalance()*(currentPri-bill.getCost())));
			map.put("profitsRate", df.format((currentPri-bill.getCost())/bill.getCost()));
			res.add(map);
		}	
		return res;
	}	
		
		
		
		
}
