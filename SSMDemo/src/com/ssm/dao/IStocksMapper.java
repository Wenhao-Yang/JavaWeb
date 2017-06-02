package com.ssm.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ssm.entity.Stock;


@Component("stocksModel")
public interface IStocksMapper {
	
	public List<Stock> SelectStockAll();
}
