package com.ssm.controller;

import java.io.UnsupportedEncodingException;
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
import com.ssm.entity.Bill;
import com.ssm.entity.Stock;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

@Controller
@RequestMapping("/billt")
public class BillController {
	IBillsMapper billsMapper=null;
	
	public IBillsMapper getBillsMapper() {
		return billsMapper;
	}
	@Autowired
	public void setStockMapper(@Qualifier("billsModel")IBillsMapper billMapper) {
		this.billsMapper = billMapper;
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
		
		System.out.println("请求查看全部交易！");
		
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
		System.out.println(bill.getCost());
		System.out.println("买入股票！");
		Bill resultBill=this.billsMapper.SelectBillByBill(bill);
		
		if (resultBill==null){
			return this.billsMapper.BuyStocks(bill)>0?"success":"error";
		}
		else{
			return this.billsMapper.UpdateStocks(bill)>0?"success":"error";
		}
		
	}
	
	//卖出股票股票
	@RequestMapping("/sellStock")
	@ResponseBody
	public String sellStocks(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute("bill")Bill bill) throws UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		System.out.println(bill.getCost()+"--"+bill.getSid()+"--"+bill.getUid());
		
		//查看是否有能够卖的该股票
		Bill resultBill=this.billsMapper.SelectBillByBill(bill);
		System.out.println("卖出股票！");
		if(resultBill!=null && resultBill.getAbalance()>=bill.getBalance()){
			//若是，卖出该股票,添加卖出记录
			this.billsMapper.SellStocks(bill);
			bill.setBalance(-bill.getBalance());
			//更新用户持有股票表
			return this.billsMapper.UpdateStocks(bill)>0?"success":"error";
		}
		return "error";
	}

	//查看当前记录
	@RequestMapping("/userBill")
	@ResponseBody
	public List<Bill> UsreBalance(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("uid")int uid) throws UnsupportedEncodingException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		
		return this.billsMapper.SearchBillByUid(uid);
	}	
		
		
		
		
}
