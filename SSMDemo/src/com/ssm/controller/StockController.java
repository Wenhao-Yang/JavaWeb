package com.ssm.controller;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
import com.ssm.dao.IStocksMapper;
import com.ssm.dao.IUsersMapper;
import com.ssm.entity.Bill;
import com.ssm.entity.HisStock;
import com.ssm.entity.Stock;
import com.ssm.entity.User;

import mjmtest.Test;

@Controller
@RequestMapping("/stockt")
public class StockController {
	IStocksMapper stocksMapper=null;
	IBillsMapper billsMapper=null;
	
	public IStocksMapper getStockMapper() {
		return stocksMapper;
	}
	@Autowired
	public void setStockMapper(@Qualifier("stocksModel")IStocksMapper stockMapper) {
		this.stocksMapper = stockMapper;
	}
	public IBillsMapper getBillMapper() {
		return billsMapper;
	}
	@Autowired
	public void setBillMapper(@Qualifier("billsModel")IBillsMapper billsMapper) {
		this.billsMapper = billsMapper;
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
		
		System.out.println("请求查看全部股票！");
		
		for (Stock stock : listStock) {
			map=new HashMap<String, Object>();
			map.put("sid", stock.getSid());
			map.put("sname",stock.getSname());
			
			res.add(map);
		}
		return res;
	}
	
	//查看股票信息
	@RequestMapping("/searchStockBySid")
	@ResponseBody
	public Map<String, Object> FindStockBySid(HttpServletRequest request,
			HttpServletResponse response ,@RequestParam("sid")int sid) throws UnsupportedEncodingException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		Stock stock=this.stocksMapper.SelectStockBySid(sid);
		Map<String, Object> map=null;
		
		//System.out.println("search sid stock");
		if (stock!=null){
			Test my=new Test();
			String sr=my.sendPost("http://hq.sinajs.cn/list=sh"+sid, "");
			String[] astr1 = sr.split("\"");
			sr=astr1[1];
			String[] resultStr=sr.split(",");
			System.out.println("查看股票实时数据！");
			
			map=new HashMap<String, Object>();
			map.put("sid", stock.getSid());
			map.put("sname",stock.getSname());
			map.put("openPrice", resultStr[1]);
			map.put("preClosePrice", resultStr[2]);
			map.put("currentPrice", resultStr[3]);
			map.put("todayHighest", resultStr[4]);
			map.put("todayLowest", resultStr[5]);
			map.put("buyFirst", resultStr[6]);
			map.put("sellFirst", resultStr[7]);
			map.put("numOfDeal", resultStr[8]);
			map.put("monOfDeal", resultStr[9]);
			map.put("numOfBuyFir", resultStr[10]);
			map.put("priOfBuyFir", resultStr[11]);
			map.put("numOfBuySec", resultStr[12]);
			map.put("priOfBuySec", resultStr[13]);
			map.put("numOfBuyThr", resultStr[14]);
			map.put("priOfBuyThr", resultStr[15]);
			map.put("numOfBuyFou", resultStr[16]);
			map.put("priOfBuyFou", resultStr[17]);
			map.put("numOfBuyFiv", resultStr[18]);
			map.put("priOfBuyFiv", resultStr[19]);
			
			map.put("numOfSellFir", resultStr[20]);
			map.put("priOfSellFir", resultStr[21]);
			map.put("numOfSellSec", resultStr[22]);
			map.put("priOfSellSec", resultStr[23]);
			map.put("numOfSellThr", resultStr[24]);
			map.put("priOfSellThr", resultStr[25]);
			map.put("numOfSellFou", resultStr[26]);
			map.put("priOfSellFou", resultStr[27]);
			map.put("numOfSellFiv", resultStr[28]);
			map.put("priOfSellFiv", resultStr[29]);
						
			map.put("date", resultStr[30]);
			map.put("time", resultStr[31]);
		
			//其他信息
		}

		return map;
	}
	
	//查找全部股票订单
	@RequestMapping("/searchAllbill")
	@ResponseBody
	public List<Map<String, Object>> FindAllBills(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		List<Map<String, Object>> res=new ArrayList<Map<String,Object>>();
		System.out.println("search stock history");
		List<Bill> listStock=this.billsMapper.SelectAllBill();
		Map<String, Object> map=null;
		
		//System.out.println("search all stock");
		
		for (Bill stock : listStock) {
			map=new HashMap<String, Object>();
			map.put("uid", stock.getUid());
			map.put("sid", stock.getSid());
			map.put("cost", stock.getCost());
			map.put("date", stock.getDate());
			res.add(map);
		}
		return res;
	}
	//查找股票历史交易记录
	@RequestMapping("/searchHisstock")
	@ResponseBody
	public List<Map<String, Object>> FindHisStocks(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		List<Map<String, Object>> res=new ArrayList<Map<String,Object>>();
		System.out.println("search stock history");
		List<HisStock> listStock=this.billsMapper.SelectHisStock();
		Map<String, Object> map=null;
		
		//System.out.println("search all stock");
		
		for (HisStock stock : listStock) {
			map=new HashMap<String, Object>();
			map.put("uid", stock.getUid());
			map.put("sid", stock.getSid());
			map.put("state", stock.getState());
			map.put("num", stock.getNum());
			map.put("bdate", stock.getBdate());
			map.put("sdate", stock.getSdate());
			res.add(map);
		}
		return res;
	}
	//查找本周股票交易数
	@RequestMapping("/searchWeekstock")
	@ResponseBody
	public int[] FindWeekBills(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		List<Map<String, Object>> res=new ArrayList<Map<String,Object>>();
		System.out.println("search stock history");
		List<HisStock> listStock=this.billsMapper.SelectHisStock();//全部的已出售订单
		List<Bill> listStock1=this.billsMapper.SelectAllBill();//全部的已购买订单
		Map<String, Object> map=null;
		
		int[] weeknum = {0,0,0,0,0,0,0};
		
		//System.out.println("search all stock");
		
		for (HisStock stock : listStock) {
			//map=new HashMap<String, Object>();
			String monday = getCurrentMonday();
			String date=stock.getSdate();
			//System.out.println(isSameDate(monday,date));
			int dyear = Integer.parseInt(date.substring(0, 4));
		    int dmonth = Integer.parseInt(date.substring(5, 7));
		    int dday = Integer.parseInt(date.substring(8, 10));
		    
		    int myear = Integer.parseInt(monday.substring(0, 4));
		    int mmonth ;
		    int mday ;
		    if(monday.substring(6, 7).equals("-"))//年份為單數
		    {
		    	mmonth = Integer.parseInt(monday.substring(5, 6));
		    	if(monday.length()==9)
		    	{
		    		mday = Integer.parseInt(monday.substring(7, 9));
		    	}else{
		    		mday = Integer.parseInt(monday.substring(7, 8));
		    	}
			    
		    }else{//年份為雙數
		    	mmonth = Integer.parseInt(monday.substring(5, 7));
		    	if(monday.length()==9)
		    	{
		    		mday = Integer.parseInt(monday.substring(8, 9));
		    	}else{
		    		mday = Integer.parseInt(monday.substring(8, 10));
		    	}
		    }
		    
		    int sub=-1;
		    if(isSameDate(monday,date))
		    {
		    	if(dyear==myear)
		    	{
		    		if(dmonth==mmonth)
		    		{
		    			sub=dday-mday;
		    		}else{
		    			if(mmonth==1||mmonth==3||mmonth==5||mmonth==7||mmonth==8||mmonth==10||mmonth==12)
		    			{
		    				sub = dday+31-mday;
		    			}else if(mmonth==2)
		    			{
		    				sub = dday+28-mday;
		    			}else{
		    				sub = dday+30-mday;
		    			}
		    		}
		    	}else{
		    		sub = dday+31-mday;
		    	}
		    }
		    if(sub!=-1)
		    {
		    	 weeknum[sub]++;
		    }
		   
		    
		    
		}
		for(int i=0;i<7;i++)
		{
			System.out.println(weeknum[i]);
		}
		for (Bill stock : listStock1) {
			//map=new HashMap<String, Object>();
			//System.out.println(stock.getDate());
			String date = stock.getDate();
			String monday = getCurrentMonday();
			int dyear = Integer.parseInt(date.substring(0, 4));
		    int dmonth = Integer.parseInt(date.substring(5, 7));
		    int dday = Integer.parseInt(date.substring(8, 10));
		    
		    int myear = Integer.parseInt(monday.substring(0, 4));
		    int mmonth ;
		    int mday ;
		    if(monday.substring(6, 7).equals("-"))//年份為單數
		    {
		    	mmonth = Integer.parseInt(monday.substring(5, 6));
		    	if(monday.length()==9)
		    	{
		    		mday = Integer.parseInt(monday.substring(7, 9));
		    	}else{
		    		mday = Integer.parseInt(monday.substring(7, 8));
		    	}
			    
		    }else{//年份為雙數
		    	mmonth = Integer.parseInt(monday.substring(5, 7));
		    	if(monday.length()==9)
		    	{
		    		mday = Integer.parseInt(monday.substring(8, 9));
		    	}else{
		    		mday = Integer.parseInt(monday.substring(8, 10));
		    	}
		    }
		    //System.out.println("dmonth:"+dmonth);
		    //System.out.println("dday:"+dday);
		    int sub=-1;
		    if(isSameDate(monday,date))
		    {
		    	if(dyear==myear)
		    	{
		    		if(dmonth==mmonth)
		    		{
		    			sub=dday-mday;
		    		}else{
		    			if(mmonth==1||mmonth==3||mmonth==5||mmonth==7||mmonth==8||mmonth==10||mmonth==12)
		    			{
		    				sub = dday+31-mday;
		    			}else if(mmonth==2)
		    			{
		    				sub = dday+28-mday;
		    			}else{
		    				sub = dday+30-mday;
		    			}
		    		}
		    	}else{
		    		sub = dday+31-mday;
		    	}
		    }
		    if(sub!=-1)
		    {
		    	 weeknum[sub]++;
		    }
			
		}
		
		return weeknum;
	}
	
	private  int getMondayPlus() {
	      Calendar cd = Calendar.getInstance();
	        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
	      int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
	      if (dayOfWeek == 1) {
	          return -6;
	      } else {
	          return 2 - dayOfWeek;
	      }
	} 
	// 获得当前周- 周一的日期
    private  String getCurrentMonday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        java.util.Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }
    private String getPreviousSunday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus +6);
        java.util.Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    } 
    public static boolean isSameDate(String date1, String date2) 
    {
     SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
     java.util.Date d1 = null;
     java.util.Date d2 = null;
     try 
     {
      d1 = format.parse(date1);
      d2 = format.parse(date2);
     }
     catch (Exception e) 
     {
      e.printStackTrace();
     }
     Calendar cal1 = Calendar.getInstance();
     Calendar cal2 = Calendar.getInstance();
     cal1.setFirstDayOfWeek(Calendar.MONDAY);
     cal2.setFirstDayOfWeek(Calendar.MONDAY);
     cal1.setTime(d1);
     cal2.setTime(d2);
     int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
     if (subYear == 0)
     {
      if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
       return true;
     }
     else if (subYear == 1 && cal2.get(Calendar.MONTH) == 11) 
     {
      if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
       return true;
     }
     else if (subYear == -1 && cal1.get(Calendar.MONTH) == 11)
     {
      if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
       return true;
     }
     return false;
    }



	
	
	






















}
