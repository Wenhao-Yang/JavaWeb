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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssm.dao.IStocksMapper;
import com.ssm.dao.IUsersMapper;
import com.ssm.entity.Stock;
import com.ssm.entity.User;

@Controller
@RequestMapping("/stockt")
public class StockController {
	IStocksMapper stocksMapper=null;
	
	public IStocksMapper getStockMapper() {
		return stocksMapper;
	}
	@Autowired
	public void setStockMapper(@Qualifier("stocksModel")IStocksMapper stockMapper) {
		this.stocksMapper = stockMapper;
	}
	
	//查找所有股票
	@RequestMapping("/searchStock")
	@ResponseBody
	public List<Map<String, Object>> FindStocksAll(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		List<Map<String, Object>> res=new ArrayList<Map<String,Object>>();
		List<Stock> listStock=this.stocksMapper.SelectStockAll();
		Map<String, Object> map=null;
		
		System.out.println("search all stock");
		
		for (Stock stock : listStock) {
			map=new HashMap<String, Object>();
			map.put("sid", stock.getSid());
			map.put("sname",stock.getSname());
			
			res.add(map);
		}
		return res;
	}
}
